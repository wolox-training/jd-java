package wolox.training.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Json {

    public static JsonNode stringToJson(String jsonString)
        throws JsonProcessingException {
        return (new ObjectMapper()).readTree(jsonString);
    }

}
