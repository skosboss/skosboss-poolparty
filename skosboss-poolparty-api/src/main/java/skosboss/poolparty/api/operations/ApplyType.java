package skosboss.poolparty.api.operations;

import skosboss.poolparty.api.annotations.Operation;
import skosboss.poolparty.api.annotations.QueryParam;
import skosboss.poolparty.api.annotations.UrlParam;

@Operation(
	url = "/PoolParty/api/thesaurus/{project}/applyType",
	method = "POST",
	contentType = "application/x-www-form-urlencoded"
)
public class ApplyType implements Op<Void> {

	private String project;
	private String resource;
	private String type;
	private boolean propagate;
	
	public ApplyType(String project, String resource, String type, boolean propagate) {
		this.project = project;
		this.resource = resource;
		this.type = type;
		this.propagate = propagate; // optional, really
	}

	@UrlParam
	public String getProject() {
		return project;
	}

	@QueryParam
	public String getResource() {
		return resource;
	}

	@QueryParam
	public String getType() {
		return type;
	}

	@QueryParam
	public boolean isPropagate() {
		return propagate;
	}
	
}
