package skosboss.poolparty.server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Iterables;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Namespace;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.model.impl.SimpleNamespace;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.model.vocabulary.SKOS;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.UnsupportedRDFormatException;
import org.eclipse.rdf4j.rio.WriterConfig;
import org.eclipse.rdf4j.rio.helpers.BasicWriterSettings;
import skosboss.poolparty.api.engine.Api;
import skosboss.poolparty.api.operations.JsonLdContextifier;

@Path("/thesaurus")
public class ThesaurusService {

	private static final ValueFactory f = SimpleValueFactory.getInstance();
	
	private static final ObjectMapper OM = new ObjectMapper();
	
	private static final String CONCEPT_PROPERTIES =
			"skos:altLabel,skos:hiddenLabel,skos:definition,"
					+ "skos:notation,skos:example,skos:scopeNote,skos:broader,skos:narrower,"
					+ "skos:related,skos:ConceptScheme,skos:exactMatch,skos:closeMatch,skos:broadMatch,"
					+ "skos:narrowMatch,skos:relatedMatch,skos:topConceptOf";

	// TODO move this elsewhere
	private static class SkosApi {
		
		static IRI iri(String value) {
			return f.createIRI(NAMESPACE + value);
		}
		
		static final String PREFIX = "skos-api";
		
		static final String NAMESPACE = "http://skos-api.org/metamodel#";
		
		static final Namespace NS =  new SimpleNamespace(PREFIX, NAMESPACE);
		
		static IRI uri = iri("uri");
		
		static IRI parent = iri("parent");
		
	}
	
	// TODO move this elsewhere
	private static class Hydra {

		static IRI iri(String value) {
			return f.createIRI(NAMESPACE + value);
		}

		static final String PREFIX = "hydra";

		static final String NAMESPACE = "http://www.w3.org/ns/hydra/core#";
		
		static final Namespace NS =  new SimpleNamespace(PREFIX, NAMESPACE);

		static IRI member = iri("member");

		static IRI collection = iri("Collection");

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
		model.setNamespace(SkosApi.PREFIX, SkosApi.NAMESPACE);
		
		return asTurtle(model);
	}
	
	@GET
	@Path("{projectId}/schemes")
	@Produces("text/turtle")
	public String getConceptSchemes(
		@PathParam("projectId") String projectId,
		@HeaderParam("Authorization") String basicAuth
	) {
		String response =
			api(projectId, basicAuth)
				.getConceptSchemes();
		
		return makeJsonLd(response)
				.map(jsonLd -> processJsonLd(SKOS.CONCEPT_SCHEME, jsonLd)).orElse("");
	}
	
	@GET
	@Path("{projectId}/topconcepts")
	@Produces("text/turtle")
	public String getTopConcepts(
		@PathParam("projectId") String projectId,
		@QueryParam("scheme") String scheme,
		@HeaderParam("Authorization") String basicAuth
	) {
		String response =
			api(projectId, basicAuth)
				.getTopConcepts(scheme, CONCEPT_PROPERTIES); //should be provided by client

		return makeJsonLd(response)
				.map(jsonLd -> processJsonLd(SKOS.CONCEPT, jsonLd)).orElse("");
	}
	
	@GET
	@Path("{projectId}/childconcepts")
	@Produces("text/turtle")
	public String getChildConcepts(
		@PathParam("projectId") String projectId,
		@QueryParam("parent") String parent,
		@HeaderParam("Authorization") String basicAuth
	) {
		String response =
			api(projectId, basicAuth)
				.getChildConcepts(parent, CONCEPT_PROPERTIES); //should be provided by client
		
		return makeJsonLd(response)
			.map(jsonLd -> processJsonLd(SKOS.CONCEPT, jsonLd)).orElse("");
	}
	
	@GET
	@Path("{projectId}/concept")
	@Produces("text/turtle")
	public String getCompleteConcept(
		@PathParam("projectId") String projectId,
		@QueryParam("concept") String concept,
		@HeaderParam("Authorization") String basicAuth
	) {
		String response =
			api(projectId, basicAuth)
				.getCompleteConcept(concept, CONCEPT_PROPERTIES); //should be provided by client

		return makeJsonLd(response).map(jsonLd -> processJsonLd(SKOS.CONCEPT, jsonLd)).orElse("");
	}
	
	private Optional<JsonNode> makeJsonLd(String response) {
		Optional<JsonNode> jsonLdConcepts = Optional.empty();

		try {
			JsonNode concepts = OM.readTree(response);
			JsonNode conceptContext =
					OM.readTree(ThesaurusService.class.getResourceAsStream("pp-context.ld.json"));
			return Optional.of(JsonLdContextifier.contextify(concepts, conceptContext));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return jsonLdConcepts;
	}
	
	private String processJsonLd(IRI resourceClass, JsonNode jsonLd) {
		if (jsonLd.size() < 2) { // either empty, or only context, so no triples.
			return "";
		}

		Model model = new LinkedHashModel();
		try {
			model = Rio.parse(new StringReader(jsonLd.toString()), "http://example.com",
					RDFFormat.JSONLD);
		} catch (RDFParseException | UnsupportedRDFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (model.isEmpty()) {
			throw new IllegalStateException("JsonLD response shouldn't be empty");
		}

		// TODO: a bit nasty
		if (jsonLd.has("member")) {
			return processCollection(resourceClass, jsonLd, model);
		} else {
			return processResource(resourceClass, jsonLd, model);
		}
	}
	
	private String processCollection(IRI resourceClass, JsonNode jsonLd, Model model) {
		Model memberStatements = model.filter(null, Hydra.member, null);
		Resource collectionResource = Iterables.getOnlyElement(memberStatements.subjects());
		model.add(collectionResource, RDF.TYPE, Hydra.collection);
		model.add(collectionResource, RDF.TYPE, RDFS.RESOURCE);

		for (Value cs : memberStatements.objects()) {
			model.add((Resource) cs, RDF.TYPE, resourceClass);
			model.add((Resource) cs, RDF.TYPE, RDFS.RESOURCE);
		}

		return asTurtle(model);
	}
	
	private String processResource(IRI resourceClass, JsonNode jsonLd, Model model) {
		Resource subjectResource = Iterables.getOnlyElement(model.subjects());
		model.add(subjectResource, RDF.TYPE, resourceClass);
		model.add(subjectResource, RDF.TYPE, RDFS.RESOURCE);

		return asTurtle(model);
	}
	
	private Api api(String projectId, String basicAuth) {
		return apiProducer.produce(projectId, basicAuth);
	}

	private String asTurtle(Model model) {
		skosApiInference(model);
		
		Model sorted = 
			model.stream()
				.collect(Collectors.toCollection(TreeModel::new));
		sorted.setNamespace(RDFS.NS);
		sorted.setNamespace(SKOS.NS);
		sorted.setNamespace(Hydra.NS);
		sorted.setNamespace(SkosApi.NS);
		
		StringWriter writer = new StringWriter();
		WriterConfig config = new WriterConfig();
		config.set(BasicWriterSettings.PRETTY_PRINT, true);
		Rio.write(sorted, writer, RDFFormat.TURTLE, config);
		return writer.toString();
	}
	
	private void skosApiInference(Model model) {
		Set<Statement> targetTriples = 
			model.stream()
				.filter(s -> s.getPredicate().equals(SKOS.BROADER) || 
						s.getPredicate().equals(SKOS.TOP_CONCEPT_OF))
				.collect(Collectors.toSet());
		targetTriples.forEach(s -> 
			model.add(s.getSubject(), SkosApi.parent, s.getObject(), s.getContext()));
		
		targetTriples = 
				model.stream()
					.filter(s -> s.getPredicate().equals(RDF.TYPE) && 
							s.getObject().equals(SKOS.CONCEPT) || 
							s.getObject().equals(SKOS.CONCEPT_SCHEME))
					.collect(Collectors.toSet());
		targetTriples.forEach(s -> 
			model.add(
				s.getSubject(), 
				SkosApi.uri, 
				f.createLiteral(s.getSubject().stringValue()), 
				s.getContext()
			)
		);

	}
}
