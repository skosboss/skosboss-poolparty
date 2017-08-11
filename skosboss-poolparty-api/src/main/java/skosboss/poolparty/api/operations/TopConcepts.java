package skosboss.poolparty.api.operations;

import java.util.List;

import skosboss.poolparty.api.annotations.Operation;
import skosboss.poolparty.api.annotations.QueryParam;
import skosboss.poolparty.api.annotations.UrlParam;

@Operation(
	url = "/PoolParty/api/thesaurus/{project}/topconcepts",
	method = "GET"
)
public class TopConcepts implements Op<List<PoolPartyConcept>> {

	private String project;
	private String scheme;
	
	public TopConcepts(String project, String scheme) {
		this.project = project;
		this.scheme = scheme;
	}

	@UrlParam
	public String getProject() {
		return project;
	}

	@QueryParam
	public String getScheme() {
		return scheme;
	}
	
}
