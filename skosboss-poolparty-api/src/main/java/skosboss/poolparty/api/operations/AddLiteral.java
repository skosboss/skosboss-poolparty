package skosboss.poolparty.api.operations;

import skosboss.poolparty.api.annotations.Operation;
import skosboss.poolparty.api.annotations.QueryParam;
import skosboss.poolparty.api.annotations.UrlParam;

@Operation(
	url = "/PoolParty/api/thesaurus/{project}/addLiteral",
	method = "POST",
	contentType = "application/x-www-form-urlencoded"
)
public class AddLiteral implements Op<Void> {

	private String project;
	private String concept;
	private String label;
	// private String language; // TODO - optional
	private String property;
	
	public AddLiteral(String project, String concept, String label, String property) {
		this.project = project;
		this.concept = concept;
		this.label = label;
		this.property = property;
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
	public String getLabel() {
		return label;
	}

	@QueryParam
	public String getProperty() {
		return property;
	}
}
