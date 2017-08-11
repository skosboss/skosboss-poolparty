package skosboss.poolparty.api.engine;

import java.util.List;

interface Url {

	Url set(String variable, String value);
	
	List<String> getVariables();
	
	String render();
	
}
