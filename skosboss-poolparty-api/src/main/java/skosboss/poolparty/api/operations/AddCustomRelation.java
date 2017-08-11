package skosboss.poolparty.api.operations;

import skosboss.poolparty.api.annotations.Operation;
import skosboss.poolparty.api.annotations.QueryParam;
import skosboss.poolparty.api.annotations.UrlParam;

@Operation(
	url = "/PoolParty/api/thesaurus/{project}/addCustomRelation",
	method = "POST",
	contentType = "application/x-www-form-urlencoded"
)
public class AddCustomRelation implements Op<Void> {

	private String project;
	private String source;
	private String target;
	private String property;
	
	public AddCustomRelation(String project, String source, String target, String property) {
		this.project = project;
		this.source = source;
		this.target = target;
		this.property = property;
	}

	/**
	 * @return UUID or textual identifier of project
	 */
	@UrlParam
	public String getProject() {
		return project;
	}

	/**
	 * @return URI of the source concept
	 */
	@QueryParam
	public String getSource() {
		return source;
	}

	/**
	 * @return URI of the target concept
	 */
	@QueryParam
	public String getTarget() {
		return target;
	}

	/**
	 * @return URI of the relation to add between the source and target concepts
	 */
	@QueryParam
	public String getProperty() {
		return property;
	}

	@Override
	public String toString() {
		return "AddCustomRelation [project=" + project + ", source=" + source + ", target=" + target + ", property="
			+ property + "]";
	}
	
}
