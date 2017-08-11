package skosboss.poolparty.api.operations;

import skosboss.poolparty.api.annotations.Operation;
import skosboss.poolparty.api.annotations.QueryParam;
import skosboss.poolparty.api.annotations.UrlParam;

@Operation(
	url = "/PoolParty/api/thesaurus/{project}/addCustomAttribute",
	method = "POST",
	contentType = "application/x-www-form-urlencoded"
)
public class AddCustomAttribute implements Op<Void> {

	private String project;
	private String resource;
	private String property;
	private String value;
	// TODO language attribute; OPTIONAL.
	
	public AddCustomAttribute(String project, String resource, String property, String value) {
		this.project = project;
		this.resource = resource;
		this.property = property;
		this.value = value;
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
	public String getProperty() {
		return property;
	}

	@QueryParam
	public String getValue() {
		return value;
	}
}
