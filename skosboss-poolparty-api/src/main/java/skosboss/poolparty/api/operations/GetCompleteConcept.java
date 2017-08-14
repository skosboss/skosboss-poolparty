package skosboss.poolparty.api.operations;

import skosboss.poolparty.api.annotations.Operation;
import skosboss.poolparty.api.annotations.QueryParam;
import skosboss.poolparty.api.annotations.UrlParam;

@Operation(
	url = "/PoolParty/api/thesaurus/{project}/concept",
	method = "GET"
)
public class GetCompleteConcept implements Op<String> {

	private String project;
	private String concept;
	private String properties;

	public GetCompleteConcept(String project, String concept, String properties) {
		this.project = project;
		this.concept = concept;
		this.properties = properties;
	}

	@UrlParam
	public String getProject() {
		return project;
	}

	@QueryParam
	public String getConcept() {
		return concept;
	}

	@QueryParam
	public String getProperties() {
		return properties;
	}
}
