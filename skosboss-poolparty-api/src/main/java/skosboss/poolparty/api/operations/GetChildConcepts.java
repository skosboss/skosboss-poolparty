package skosboss.poolparty.api.operations;

import skosboss.poolparty.api.annotations.Operation;
import skosboss.poolparty.api.annotations.QueryParam;
import skosboss.poolparty.api.annotations.UrlParam;

@Operation(
	url = "/PoolParty/api/thesaurus/{project}/childconcepts",
	method = "GET"
)
public class GetChildConcepts implements Op<String> {

	private String project;
	private String parent;
	private String properties;

	public GetChildConcepts(String project, String parent, String properties) {
		this.project = project;
		this.parent = parent;
		this.properties = properties;
	}

	@UrlParam
	public String getProject() {
		return project;
	}

	@QueryParam
	public String getParent() {
		return parent;
	}

	@QueryParam
	public String getProperties() {
		return properties;
	}
}
