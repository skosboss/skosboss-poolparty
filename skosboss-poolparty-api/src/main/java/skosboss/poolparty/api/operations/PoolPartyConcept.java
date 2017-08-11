package skosboss.poolparty.api.operations;

public class PoolPartyConcept {

	private String uri;
	private String prefLabel;
	
	public PoolPartyConcept(String uri, String prefLabel) {
		this.uri = uri;
		this.prefLabel = prefLabel;
	}

	public String getUri() {
		return uri;
	}

	public String getPrefLabel() {
		return prefLabel;
	}

	@Override
	public String toString() {
		return "PoolPartyConcept [uri=" + uri + ", prefLabel=" + prefLabel + "]";
	}
}
