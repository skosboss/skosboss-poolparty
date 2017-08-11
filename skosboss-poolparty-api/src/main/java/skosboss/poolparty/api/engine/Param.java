package skosboss.poolparty.api.engine;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class Param {

	private String name;
	private Method accessor;
	
	Param(String name, Method accessor) {
		this.name = name;
		this.accessor = accessor;
	}

	Object get(Object source) {
		try {
			return accessor.invoke(source);
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException("error invoking accessor " + accessor + " for QueryParam " + name);
		}
	}

	String getName() {
		return name;
	}
}
