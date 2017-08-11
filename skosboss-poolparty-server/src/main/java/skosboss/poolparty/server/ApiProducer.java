package skosboss.poolparty.server;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import skosboss.poolparty.api.engine.Api;

@ApplicationScoped
public class ApiProducer {

	@Inject @Named("baseUrl")
	private String baseUrl;
	
	public Api produce(String projectId, String basicAuth) {
		return Api.build(baseUrl, projectId, basicAuth);
	}
	
}
