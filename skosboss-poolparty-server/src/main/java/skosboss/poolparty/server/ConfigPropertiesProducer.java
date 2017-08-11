package skosboss.poolparty.server;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import javax.enterprise.inject.Produces;

public class ConfigPropertiesProducer {

	@Produces
	public Properties produce() {
		Path workingDir = Paths.get(System.getProperty("user.dir"));
		Path propertiesFile = workingDir.resolve("config.properties");
		try (Reader reader = Files.newBufferedReader(propertiesFile, StandardCharsets.UTF_8)) {
			Properties properties = new Properties();
			properties.load(reader);
			return properties;
		}
		catch (IOException e) {
			throw new RuntimeException(
				"error reading config file from " + propertiesFile.toAbsolutePath(),
				e
			);
		}
	}
	
}
