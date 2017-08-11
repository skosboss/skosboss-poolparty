package skosboss.poolparty.server;

import javax.inject.Inject;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import skosboss.poolparty.api.engine.Api;

@Path("/thesaurus")
public class ThesaurusService {

	@Inject
	private ApiProducer apiProducer;
	
	@POST
	@Path("{projectId}/createConceptScheme")
	@Produces(MediaType.TEXT_PLAIN)
	public String createConceptScheme(
		@PathParam("projectId") String projectId,
		@QueryParam("title") String title,
		@HeaderParam("Authorization") String basicAuth
	) {
		String uri = api(projectId, basicAuth).createConceptScheme(title);

		// TODO ...
		
		return uri;
	}
	
	private Api api(String projectId, String basicAuth) {
		return apiProducer.produce(projectId, basicAuth);
	}

}
