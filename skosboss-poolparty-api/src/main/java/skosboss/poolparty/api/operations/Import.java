package skosboss.poolparty.api.operations;

import skosboss.poolparty.api.annotations.Body;
import skosboss.poolparty.api.annotations.ContentType;
import skosboss.poolparty.api.annotations.Operation;
import skosboss.poolparty.api.annotations.QueryParam;
import skosboss.poolparty.api.annotations.UrlParam;

@Operation(
	url = "/PoolParty/api/projects/{project}/import",
	method = "POST"
)
public class Import implements Op<Void> {

	private String project;
	private boolean overwrite;
	private String importModule;
	private String contentType;
	private String body;
	
	public Import(String project, boolean overwrite, String importModule, String contentType, String body) {
		this.project = project;
		this.overwrite = overwrite;
		this.importModule = importModule;
		this.contentType = contentType;
		this.body = body;
	}

	@UrlParam
	public String getProject() {
		return project;
	}

	@QueryParam
	public boolean isOverwrite() {
		return overwrite;
	}

	@QueryParam
	public String getImportModule() {
		return importModule;
	}

	@ContentType
	public String getContentType() {
		return contentType;
	}

	@Body
	public String getBody() {
		return body;
	}

}
