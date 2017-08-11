package skosboss.poolparty.api.operations;

import skosboss.poolparty.api.annotations.Operation;
import skosboss.poolparty.api.annotations.QueryParam;
import skosboss.poolparty.api.annotations.UrlParam;

// response = URL of created concept (status 200)

@Operation(
	url = "/PoolParty/api/thesaurus/{project}/createConcept",
	method = "POST",
	contentType = "application/x-www-form-urlencoded"
)
public class CreateConcept implements Op<String> {

	private String project;
	private String parent;
	private String prefLabel;
	
	public CreateConcept(String project, String parent, String prefLabel) {
		this.project = project;
		this.parent = parent;
		this.prefLabel = prefLabel;
	}

	/**
	 * @return UUID or textual identifier of project
	 */
	@UrlParam
	public String getProject() {
		return project;
	}
	
	/**
	 * @return URI of the parent concept or concept scheme
	 */
	@QueryParam
	public String getParent() {
		return parent;
	}
	
	/**
	 * @return prefLabel in default language
	 */
	@QueryParam
	public String getPrefLabel() {
		return prefLabel;
	}

	@Override
	public String toString() {
		return "CreateConcept [project=" + project + ", parent=" + parent + ", prefLabel=" + prefLabel + "]";
	}
	
}
