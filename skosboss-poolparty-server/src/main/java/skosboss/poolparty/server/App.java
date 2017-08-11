package skosboss.poolparty.server;

public class App {

	public static void main(String... args) {
		new AppModule().buildApp()
			.run();
	}
}
