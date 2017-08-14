package skosboss.poolparty.api.operations;

import skosboss.poolparty.api.annotations.Operation;
import skosboss.poolparty.api.annotations.UrlParam;

@Operation(
	url = "/PoolParty/api/thesaurus/{project}/schemes",
	method = "GET"
)
public class GetConceptSchemes implements Op<String> {

	private String project;

	public GetConceptSchemes(String project) {
		this.project = project;
	}

	@UrlParam
	public String getProject() {
		return project;
	}

}
