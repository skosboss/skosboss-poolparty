package skosboss.poolparty.api.operations;

import skosboss.poolparty.api.annotations.Operation;
import skosboss.poolparty.api.annotations.QueryParam;
import skosboss.poolparty.api.annotations.UrlParam;

// response = URL of created concept scheme (status 200)

@Operation(
	url = "/PoolParty/api/thesaurus/{project}/createConceptScheme",
	method = "POST",
	contentType = "application/x-www-form-urlencoded"
)
public class CreateConceptScheme implements Op<String> {
	
	private String project;
	private String title;
	// TODO omitted a bunch of optional parameters
	
	public CreateConceptScheme(String project, String title) {
		this.project = project;
		this.title = title;
	}

	@UrlParam
	public String getProject() {
		return project;
	}

	@QueryParam
	public String getTitle() {
		return title;
	}

	@Override
	public String toString() {
		return "CreateConceptScheme [project=" + project + ", title=" + title + "]";
	}
	
}
