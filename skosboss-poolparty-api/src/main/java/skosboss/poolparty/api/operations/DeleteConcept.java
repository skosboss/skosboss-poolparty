package skosboss.poolparty.api.operations;

import skosboss.poolparty.api.annotations.Operation;
import skosboss.poolparty.api.annotations.QueryParam;
import skosboss.poolparty.api.annotations.UrlParam;

@Operation(
	url = "/PoolParty/api/thesaurus/{project}/deleteConcept",
	method = "POST",
	contentType = "application/x-www-form-urlencoded"
)
public class DeleteConcept implements Op<Void> {

	private String project;
	private String concept;
	
	public DeleteConcept(String project, String concept) {
		this.project = project;
		this.concept = concept;
	}

	/**
	 * @return UUID or textual identifier of project
	 */
	@UrlParam
	public String getProject() {
		return project;
	}
	
	/**
	 * @return URI of the concept to delete
	 */
	@QueryParam
	public String getConcept() {
		return concept;
	}
	
}
