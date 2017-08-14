package skosboss.poolparty.api.operations;

import skosboss.poolparty.api.annotations.Operation;
import skosboss.poolparty.api.annotations.QueryParam;
import skosboss.poolparty.api.annotations.UrlParam;

@Operation(
	url = "/PoolParty/api/thesaurus/{project}/topconcepts",
	method = "GET"
)
public class GetTopConcepts implements Op<String> {

	private String project;
	private String scheme;
	private String properties;

	public GetTopConcepts(String project, String scheme, String properties) {
		this.project = project;
		this.scheme = scheme;
		this.properties = properties;
	}

	@UrlParam
	public String getProject() {
		return project;
	}

	@QueryParam
	public String getScheme() {
		return scheme;
	}

	@QueryParam
	public String getProperties() {
		return properties;
	}
}
