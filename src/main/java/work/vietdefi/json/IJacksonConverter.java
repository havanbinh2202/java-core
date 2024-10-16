package work.vietdefi.json;

import com.fasterxml.jackson.databind.JsonNode;

public interface IJacksonConverter {
    /**
     * Converts a Java object to its JSON string representation.
     *
     * @param object The Java object to convert.
     * @return The JSON string representation of the object.
     */
    String toJsonString(Object object);

    /**
     * Converts a JSON string to a Java object of the specified type.
     *
     * @param jsonString The JSON string to convert.
     * @param clazz      The class of the Java object to create.
     * @param <T>        The type of the Java object.
     * @return The Java object created from the JSON string.
     */
    <T> T fromJsonString(String jsonString, Class<T> clazz);

    /**
     * Converts a JSON string to a JsonNode.
     *
     * @param jsonString The JSON string to convert.
     * @return The JsonNode representation of the JSON string.
     */
    JsonNode toJsonNode(String jsonString);

    /**
     * Converts a Java object to a JsonElement.
     *
     * @param jsonNode The Java object to convert.
     * @return The JSON representation of the Java object.
     */
    String fromJsonNodeToString(JsonNode jsonNode);

    /**
     * Converts a JsonElement to a Java object of the specified type.
     *
     * @param jsonNode The JsonElement to convert.
     * @param clazz       The class of the Java object to create.
     * @param <T>         The type of the Java object.
     * @return The Java object created from the JsonElement.
     */
    <T> T fromJsonNode(JsonNode jsonNode, Class<T> clazz);
}
