package ro.serol.spark_swagger;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.staticFiles;

import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileReader;
import java.util.concurrent.TimeUnit;

@SwaggerDefinition(host = "localhost:4567", //
info = @Info(description = "DonateAPP API", //
version = "V1.0", //
title = "Some random api for testing", //
contact = @Contact(name = "Serol", url = "https://serol.ro") ) , //
schemes = { SwaggerDefinition.Scheme.HTTP, SwaggerDefinition.Scheme.HTTPS }, //
consumes = { "application/json" }, //
produces = { "application/json" }, //
tags = { @Tag(name = "swagger") })
public class App {

	public static final String APP_PACKAGE = "ro.serol.spark_swagger";

	public static void main(String[] args) {

		try {

            String projectDir = System.getProperty("user.dir");
            String staticDir = "/src/main/resources";

            staticFiles.externalLocation(projectDir + staticDir + "/public");

            // Quite unsafe!
			before(new CorsFilter());
			new OptionsController();

			// Scan classes with @Api annotation and add as routes
			RouteBuilder.setupRoutes(APP_PACKAGE);

			// Build swagger json description
			final String swaggerJson = SwaggerParser.getSwaggerJson(APP_PACKAGE);
			get("/swagger", (req, res) -> {
				return swaggerJson;
			});

            get("/explorer", (req, res) -> {
                res.type("text/html");
                File file = new File("/Users/jeff/dev/markf/spark-swagger/src/main/resources/public/index.html");
                String out = FileUtils.readFileToString(file, "UTF-8");
                System.out.println(out);
                res.body(out);
                return out;
            });
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}

}
