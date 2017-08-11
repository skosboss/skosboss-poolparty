package skosboss.poolparty.server;

import java.io.StringWriter;

import javax.inject.Inject;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.SKOS;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

import skosboss.poolparty.api.engine.Api;

@Path("/thesaurus")
public class ThesaurusService {

	private static final ValueFactory f = SimpleValueFactory.getInstance();

	// TODO move this elsewhere
	private static class SkosApi {
		
		static IRI iri(String value) {
			return f.createIRI(namespace + value);
		}
		
		static final String prefix = "skos-api";
		
		static final String namespace = "http://skos-api.org/metamodel#";
		
		static IRI uri = iri("uri");
		
	}
	
	@Inject
	private ApiProducer apiProducer;
	
	@POST
	@Path("{projectId}/createConceptScheme")
	@Produces("text/turtle")
	public String createConceptScheme(
		@PathParam("projectId") String projectId,
		@QueryParam("title") String title,
		@HeaderParam("Authorization") String basicAuth
	) {
		
		String uriString =
			api(projectId, basicAuth)
				.createConceptScheme(title);
		IRI uri = f.createIRI(uriString);
		
		Model model = new ModelBuilder()
			.subject(uri)
			.add(RDF.TYPE, SKOS.CONCEPT_SCHEME)
			.add(SkosApi.uri, uri)
			.build();
		
		model.setNamespace(SKOS.PREFIX, SKOS.NAMESPACE);
		model.setNamespace(SkosApi.prefix, SkosApi.namespace);
		
		return asTurtle(model);
	}
	
	private Api api(String projectId, String basicAuth) {
		return apiProducer.produce(projectId, basicAuth);
	}

	private String asTurtle(Model model) {
		StringWriter writer = new StringWriter();
		Rio.write(model, writer, RDFFormat.TURTLE);
		return writer.toString();
	}
}
