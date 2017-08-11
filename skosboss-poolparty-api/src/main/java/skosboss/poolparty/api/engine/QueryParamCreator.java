package skosboss.poolparty.api.engine;

import java.lang.reflect.Method;
import java.util.function.Function;

class QueryParamCreator implements Function<Method, QueryParam> {

	private Function<String, String> deriveName;

	QueryParamCreator(Function<String, String> deriveName) {
		this.deriveName = deriveName;
	}

	@Override
	public QueryParam apply(Method method) {
		
		skosboss.poolparty.api.annotations.QueryParam annotation =
			method.getAnnotation(skosboss.poolparty.api.annotations.QueryParam.class);
		if (annotation == null)
			return null;
		
		String name = annotation.name();
		if (name.equals(""))
			name = deriveName.apply(method.getName());
		
		return new QueryParam(name, method);
	}
}
