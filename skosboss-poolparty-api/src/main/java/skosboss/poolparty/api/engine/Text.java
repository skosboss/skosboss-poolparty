package skosboss.poolparty.api.engine;

class Text implements Segment {
	
	private String text;

	Text(String text) {
		this.text = text;
	}

	
	String getText() {
		return text;
	}
}