package skosboss.poolparty.api.engine;

import java.lang.reflect.Method;

class UrlParam extends Param {

	UrlParam(String name, Method accessor) {
		super(name, accessor);
	}
}
