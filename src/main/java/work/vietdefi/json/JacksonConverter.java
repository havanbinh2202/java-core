package work.vietdefi.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class JacksonConverter implements IJacksonConverter{
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String toJsonString(Object object){
        try {
            return objectMapper.writer().writeValueAsString(object);
        } catch (JsonProcessingException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public <T> T fromJsonString(String jsonString, Class<T> clazz){
        try{
            return objectMapper.readValue(jsonString, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public JsonNode toJsonNode(String jsonString){
        try{
            return objectMapper.readTree(jsonString);
        } catch (JsonProcessingException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String fromJsonNodeToString(JsonNode jsonNode) {
        return jsonNode.toString();
    }

    @Override
    public <T> T fromJsonNode(JsonNode jsonNode, Class<T> clazz){
        try {
            return objectMapper.treeToValue(jsonNode, clazz);
        } catch (JsonProcessingException e){
            e.printStackTrace();
            return null;
        }
    }
}
