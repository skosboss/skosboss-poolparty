package skosboss.poolparty.api.engine;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import skosboss.poolparty.api.annotations.Operation;
import skosboss.poolparty.api.operations.Op;

class OperationExecutor {

	private OkHttpClient client;
	private OperationRequestCreatorBuilder builder;
	private Gson gson;
	
	OperationExecutor(OkHttpClient client, OperationRequestCreatorBuilder builder, Gson gson) {
		this.client = client;
		this.builder = builder;
		this.gson = gson;
	}
	
	<T> T execute(Op<T> operation) {
		
		Class<?> t = operation.getClass();
		Function<Op<T>, Request> requestCreator = (Function<Op<T>, Request>) builder.buildRequestCreator(t);
		// TODO cache requestCreator per operation type
		Type responseType = getOperationResponseType(t);
		// TODO cache responseType per operation type
		
		Request request = requestCreator.apply(operation);
		
		try {
			Response response = client.newCall(request).execute();
			if (!response.isSuccessful())
				throw new RuntimeException(
					"error executing api call; response = " + response + "\n" +
					response.body().string()
				);
			
			// if the operation response type is non-void,
			// parse body according to contentType.charset, contentType.mimeType
			// into an instance of responseType
			if (responseType.equals(Void.class))
				return null;
			ContentType contentType = determineContentType(response);
			return parseResponse(response.body(), contentType, responseType);
		}
		catch (IOException e) {
			throw new RuntimeException("error executing request", e);
		}
	}
	
	private <T> T parseResponse(ResponseBody body, ContentType contentType, Type responseType) {
		
		if (!contentType.getMimeType().equals("application/json"))
			throw new RuntimeException("only application/json responses are supported for now");
		
		Charset charset = contentType.getCharset();
		try (Reader reader =
			new InputStreamReader(
				new BufferedInputStream(body.byteStream(), 10240),
				charset
			)
		) {
			String str = IOUtils.toString(reader);
//			System.out.println("raw string: " + str);
			
			if (responseType.equals(String.class))
				return (T) str;
			
			// TODO catch errors
			return gson.fromJson(str, responseType);
			
//			throw new RuntimeException("unsupported response type " + responseType);
		}
		catch (IOException e) {
			throw new RuntimeException("error reading response body", e);
		}
	}

	private ContentType determineContentType(Response response) {
		String contentType = response.header("Content-Type");
		if (contentType == null) {
			throw new RuntimeException("no Content-Type header in response");
			// TODO make a guess based on the body contents, or return default assumptions?
		}
		List<String> parts = Arrays.asList( contentType.split(";"));
		String mimeType = parts.get(0).trim();
		Charset charset;
		if (parts.size() > 1)
			charset = determineCharset(parts.get(1));
		else
			charset = StandardCharsets.UTF_8;

		return new ContentType(mimeType, charset);
	}
	
	private Charset determineCharset(String str) {
		if (!str.startsWith("charset="))
			throw new RuntimeException("charset part of Content-Type header value does not begin with charset=");
		str = str.substring("charset=".length()).trim();
		return Charset.forName(str); // TODO handle exceptions
	}
	
	// determine <T> in Op<T>
	private Type getOperationResponseType(Class<?> type) {
		
		Operation op = type.getAnnotation(Operation.class);
		if (op == null)
			throw new IllegalArgumentException("type " + type.getCanonicalName() + " does NOT have an @Operation annotation");
		
		try {
			return
			Arrays.asList(type.getGenericInterfaces()).stream()
				.filter(t -> t instanceof ParameterizedType)
				.map(t -> (ParameterizedType) t)
				.filter(t -> t.getRawType().equals(Op.class))
				.map(t -> t.getActualTypeArguments()[0])
				.findFirst().get();
		}
		catch (NoSuchElementException e) {
			throw new RuntimeException("could not determine type parm of Op<T> as implemented by "
				+ type.getCanonicalName(), e);
		}
	}
	
	static OperationExecutor build(String baseUrl, String basicAuth) {
		return new OperationExecutor(
			new OkHttpClient(),
			OperationRequestCreatorBuilder.build(baseUrl, basicAuth),
			new Gson()
		);
	}
}
