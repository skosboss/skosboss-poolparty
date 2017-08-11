package skosboss.poolparty.api.engine;

import java.lang.reflect.Method;
import java.util.function.Function;

class UrlParamCreator implements Function<Method, UrlParam> {

	private Function<String, String> deriveName;

	UrlParamCreator(Function<String, String> deriveName) {
		this.deriveName = deriveName;
	}

	@Override
	public UrlParam apply(Method method) {
		
		skosboss.poolparty.api.annotations.UrlParam annotation =
			method.getAnnotation(skosboss.poolparty.api.annotations.UrlParam.class);
		if (annotation == null)
			return null;
		
		String name = annotation.name();
		if (name.equals(""))
			name = deriveName.apply(method.getName());
		
		return new UrlParam(name, method);
	}

}
