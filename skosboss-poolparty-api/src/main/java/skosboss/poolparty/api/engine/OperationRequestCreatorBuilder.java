package skosboss.poolparty.api.engine;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import okhttp3.HttpUrl;
import okhttp3.HttpUrl.Builder;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import skosboss.poolparty.api.annotations.Body;
import skosboss.poolparty.api.annotations.Operation;

class OperationRequestCreatorBuilder {

	private String baseUrl;
	private UrlParser urlParser;
	private Function<Method, QueryParam> createQueryParam;
	private Function<Method, UrlParam> createUrlParam;
	private String basicAuth;

	static OperationRequestCreatorBuilder build(String baseUrl, String basicAuth) {
		DerivePropertyName deriveName = new DerivePropertyName();
		return new OperationRequestCreatorBuilder(
			baseUrl,
			new UrlParser(),
			new QueryParamCreator(deriveName),
			new UrlParamCreator(deriveName),
			basicAuth
		);
	}
	
	OperationRequestCreatorBuilder(
		String baseUrl,
		UrlParser urlParser,
		Function<Method, QueryParam> createQueryParam,
		Function<Method, UrlParam> createUrlParam,
		String basicAuth
	) {
		this.baseUrl = baseUrl;
		this.urlParser = urlParser;
		this.createQueryParam = createQueryParam;
		this.createUrlParam = createUrlParam;
		this.basicAuth = basicAuth;
	}

	public <T> Function<T, Request> buildRequestCreator(Class<T> type) { // TODO Class<? extends Op<T>> ?
		
		Operation op = type.getAnnotation(Operation.class);
		if (op == null)
			throw new IllegalArgumentException("type " + type.getCanonicalName() + " does NOT have an @Operation annotation");
		
		Supplier<Url> urlFactory = urlParser.parseUrl(op.url());
	
		List<Method> methods = Arrays.asList(type.getMethods());
		
		List<QueryParam> queryParams = methods.stream()
			.map(createQueryParam)
			.filter(p -> p != null)
			.collect(Collectors.toList());
		
		List<UrlParam> urlParams = methods.stream()
			.map(createUrlParam)
			.filter(p -> p != null)
			.collect(Collectors.toList());
		
		List<String> urlParamNames = urlParams.stream().map(u -> u.getName()).collect(Collectors.toList());
		if (!new LinkedHashSet<>(urlFactory.get().getVariables()).equals(new LinkedHashSet<>(urlParamNames)))
			throw new RuntimeException("the set of @UrlParam names does NOT match the url parameters in the @Operation url");
		
		// find @ContentType provider, or use @Operation.contentType value
		Function<T, String> contentTypeProvider = methods.stream()
			.map(m -> this.<T>createContentTypeProvider(m))
			.filter(p -> p != null)
			.findFirst().orElseGet(() -> {
				String contentType = op.contentType();
				return o -> contentType;
			});
		
		// find @Body provider, or use empty String
		Function<T, String> bodyProvider = methods.stream()
			.map(m -> this.<T>createBodyProvider(m))
			.filter(p -> p != null)
			.findFirst()
			.orElseGet(() -> o -> "");
		
		Function<T, Request> createRequest = o -> {
			
			Url url = urlFactory.get();
			urlParams.forEach(u -> {
				String value = u.get(o).toString();
				url.set(u.getName(), value);
			});
			
			Builder urlBuilder = HttpUrl.parse(baseUrl + url.render()).newBuilder();
			queryParams.forEach(q -> {
				Object value = q.get(o);
				// if value is an Iterable, concatenate the toString()
				// values of each element, separated by commas
				// TODO or simply repeat the query parameter instead.......
				if (value instanceof Iterable) {
					List<String> values = new ArrayList<>();
					((Iterable<?>) value).forEach(e -> values.add(e.toString()));
					value = values.stream().collect(Collectors.joining(","));
				}
				urlBuilder.addQueryParameter(q.getName(), value.toString());
			});
			
			RequestBody body;
			if (op.method().toUpperCase().equals("GET"))
				body = null;
			else {
				String contentType = contentTypeProvider.apply(o);
				if (contentType.equals(""))
					throw new RuntimeException("no @ContentType or @Operation.contentType "
						+ "specified for non-GET operation");
				body = RequestBody.create(
					MediaType.parse(contentType),
					bodyProvider.apply(o)
				);
			}
			
			return
			new Request.Builder()
			.url(urlBuilder.build())
			.method(op.method(), body)
			.header("Authorization", basicAuth)
			.build();
		};
		return createRequest;
	}
	
	private <T> Function<T, String> createContentTypeProvider(Method method) {
		return buildGetterProvider(method, skosboss.poolparty.api.annotations.ContentType.class);
	}
	
	private <T> Function<T, String> createBodyProvider(Method method) {
		return buildGetterProvider(method, Body.class);
	}
	
	private <A extends Annotation, T, U> Function<T, U> buildGetterProvider(Method method, Class<A> annotationClass) {
		A annotation = method.getAnnotation(annotationClass);
		if (annotation == null) return null;
		return o -> {
			try {
				@SuppressWarnings("unchecked")
				U value = (U) method.invoke(o);
				return value;
			}
			catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new RuntimeException(
					"could not invoke getter annotated with @" + annotationClass.getName(), e);
			}
		};
	}
	
	
	
}
