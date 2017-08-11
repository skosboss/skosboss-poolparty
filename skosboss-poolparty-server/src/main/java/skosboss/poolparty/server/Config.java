package skosboss.poolparty.server;

import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

@ApplicationScoped
public class Config {

	@Inject
	private Properties config;
	
	@Produces @Named("baseUrl")
	public String getBaseUrl() {
		return config.getProperty("api.baseUrl");
	}

}
