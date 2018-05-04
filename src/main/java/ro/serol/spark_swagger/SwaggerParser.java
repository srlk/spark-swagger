package ro.serol.spark_swagger;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.jaxrs.Reader;
import io.swagger.models.Swagger;

public class SwaggerParser {

	public static String getSwaggerJson() throws JsonProcessingException {
		Swagger swagger = getSwagger();
		String json = swaggerToJson(swagger);
		return json;
	}

	public static Swagger getSwagger() {
		Reader reader = new Reader(new Swagger());
		return reader.read(ro.serol.spark_swagger.route.Api.class);
	}

	public static String swaggerToJson(Swagger swagger) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(Include.NON_EMPTY);
		String json = objectMapper.writeValueAsString(swagger);
		return json;
	}

}