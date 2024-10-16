package work.vietdefi.json;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;

public class GsonConverter implements IGsonConverter{
    private final Gson gson;

    public GsonConverter() {
        this.gson = new Gson();
    }

    @Override
    public String toJsonString(Object obj) {
        return gson.toJson(obj);
    }

    @Override
    public <T> T fromJsonString (String jsonString, Class<T> clazz){
        try {
            return gson.fromJson(jsonString,clazz);
        } catch (JsonSyntaxException e)  {
            throw new RuntimeException("Failed to deserialize JSON: " + e.getMessage(), e);
        }
    }

    @Override
    public JsonElement toJsonElement(Object obj){
        return gson.toJsonTree(obj);
    }

    @Override

    public JsonElement toJsonElement(String jsonString) {
        return gson.fromJson(jsonString, JsonElement.class);
    }

    @Override
    public String fromJsonElementToString(JsonElement jsonElement){
        return gson.toJson(jsonElement);
    }

    @Override
    public <T> T fromJsonElement(JsonElement jsonElement, Class<T> clazz){
        return gson.fromJson(jsonElement, clazz);
    }
}
