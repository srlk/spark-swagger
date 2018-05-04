package ro.serol.spark_swagger;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.staticFiles;

import ro.serol.spark_swagger.route.Api;

public class App {

	public static void main(String[] args) {

		try {

            String projectDir = System.getProperty("user.dir");
            String staticDir = "/src/main/resources";

            staticFiles.externalLocation(projectDir + staticDir + "/public");

            // Quite unsafe!
			before(new CorsFilter());
			new OptionsController();

			// Scan classes with @Api annotation and add as routes
            Api.setupRoutes();

			// Build swagger json description
			final String swaggerJson = SwaggerParser.getSwaggerJson();
			get("/swagger", (req, res) -> {
				return swaggerJson;
			});

		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}

}
