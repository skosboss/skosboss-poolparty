package skosboss.poolparty.server;

import org.glassfish.jersey.servlet.ServletContainer;
import org.jboss.weld.environment.servlet.Listener;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;

public class AppModule {

	Runnable buildApp() {

		DeploymentInfo deployment = Servlets.deployment();

		deployment
			.setClassLoader(App.class.getClassLoader())
			.setContextPath("/")
			.setDeploymentName("my deployment")
			.addListeners(Servlets.listener(Listener.class))
			.addServlets(Servlets.servlet("jerseyServlet", ServletContainer.class)
				.setLoadOnStartup(1)
				.addInitParam("javax.ws.rs.Application", ApiResource.class.getName())
				.addMapping("/api/*")
			);

		DeploymentManager manager = Servlets.defaultContainer()
			.addDeployment(deployment);
		manager.deploy();

		return () -> {
			
			PathHandler path;
			try {
				path = Handlers
					.path(Handlers.redirect("/"))
					.addPrefixPath("/", manager.start());
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
			
			Undertow server = Undertow.builder()
				.addHttpListener(5000, "0.0.0.0") // bind to all interfaces
				.setHandler(path)
				.build();
			server.start();
		};
	}
	
}
