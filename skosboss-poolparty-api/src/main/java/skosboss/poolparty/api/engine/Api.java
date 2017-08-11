package skosboss.poolparty.api.engine;

import java.util.List;

import org.eclipse.rdf4j.model.IRI;

import skosboss.poolparty.api.operations.AddCustomAttribute;
import skosboss.poolparty.api.operations.AddCustomRelation;
import skosboss.poolparty.api.operations.AddLiteral;
import skosboss.poolparty.api.operations.ApplyType;
import skosboss.poolparty.api.operations.CreateConcept;
import skosboss.poolparty.api.operations.DeleteConcept;
import skosboss.poolparty.api.operations.Import;
import skosboss.poolparty.api.operations.Op;
import skosboss.poolparty.api.operations.PoolPartyConcept;
import skosboss.poolparty.api.operations.TopConcepts;

public class Api {

	private OperationExecutor executor;
	private String project;
	
	private Api(OperationExecutor executor, String project) {
		this.executor = executor;
		this.project = project;
	}
	
	private <T> T execute(Op<T> operation) {
		return executor.execute(operation);
	}
	
	public void addCustomRelation(String source, String target, IRI property) {
		addCustomRelation(source, target, property.stringValue());
	}
	
	public void addCustomRelation(String source, String target, String property) {
		new AddCustomRelation(project, source, target, property);
	}
	
	public void addCustomAttribute(String resource, IRI property, String value) {
		addCustomAttribute(resource, property.stringValue(), value);
	}
	
	public void addCustomAttribute(String resource, String property, String value) {
		execute(new AddCustomAttribute(project, resource, property, value));
	}
	
	public void addLiteral(String concept, String label, String property) {
		execute(new AddLiteral(project, concept, label, property));
	}
	
	public void applyType(String resource, IRI type, boolean propagate) {
		applyType(resource, type.stringValue(), propagate);
	}
	
	public void applyType(String resource, String type, boolean propagate) {
		execute(new ApplyType(project, resource, type, propagate));
	}
	
	public String createConcept(String parentUri, String prefLabel) {
		String uri = execute(new CreateConcept(project, parentUri, prefLabel));
		
		// returned uri in the response body is quoted, so unquote
		if (uri.startsWith("\"")) uri = uri.substring(1);
		if (uri.endsWith("\"")) uri = uri.substring(0, uri.length() - 1);
		return uri;
	}
	
	public void deleteConcept(String concept) {
		execute(new DeleteConcept(project, concept));
	}
	
	public List<PoolPartyConcept> topConcepts(String scheme) {
		return execute(new TopConcepts(project, scheme));
	}
	
	public void clearConceptScheme(String scheme) {
		topConcepts(scheme).stream()
			.map(PoolPartyConcept::getUri)
			.forEach(this::deleteConcept);
	}
	
	public void importData(boolean overwrite, String importModule, String contentType, String body) {
		execute(new Import(project, overwrite, importModule, contentType, body));
	}
	
	public static Api build(String baseUrl, String project, String basicAuth) {
		return new Api(
			OperationExecutor.build(baseUrl, basicAuth),
			project
		);
	}

}
