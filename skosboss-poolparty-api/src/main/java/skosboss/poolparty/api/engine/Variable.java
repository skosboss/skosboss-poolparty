package skosboss.poolparty.api.engine;

class Variable implements Segment {
	
	private String name;

	Variable(String name) {
		this.name = name;
	}
	
	String getName() {
		return name;
	}
}