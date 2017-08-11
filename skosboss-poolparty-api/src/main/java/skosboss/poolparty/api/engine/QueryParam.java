package skosboss.poolparty.api.engine;

import java.lang.reflect.Method;

class QueryParam extends Param {

	QueryParam(String name, Method accessor) {
		super(name, accessor);
	}
}
