package skosboss.poolparty.server;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/api/*")
class ApiResource extends ResourceConfig {

	ApiResource() {
		register(CORSFilter.class);
		register(MultiPartFeature.class);
		packages(true, "skosboss.poolparty.server");
	}
	
}
