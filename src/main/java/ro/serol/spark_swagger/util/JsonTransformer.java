package ro.serol.spark_swagger.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.serol.spark_swagger.model.request.CreateUserRequest;
import spark.ResponseTransformer;

import java.io.IOException;

public class JsonTransformer implements ResponseTransformer {

    private ObjectMapper mapper = new ObjectMapper();
    private Logger log = LoggerFactory.getLogger(JsonTransformer.class);

    @Override
    public String render(Object model) {
        try {
            return mapper.writeValueAsString(model);
        } catch (JsonProcessingException e) {
            log.error("Cannot serialize object", e);
            return null;
        }
    }

    public <T> T deserialize(String body, Class<T> clazz) {
        try {
            return mapper.readValue(body, clazz);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}