package pl.recordit.deteer.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;
import java.util.stream.Stream;

public class JsonMap {
    private final Map<String, Object> properties;

    public final static JsonMap EMPTY = new JsonMap(Collections.emptyMap());

    public JsonMap(Map<String, Object> properties) {
        this.properties = properties;
    }

    public <T> Optional<T> value(String key, Class<T> clazz){
        Object obj = properties.get(key);
        return clazz.isInstance(obj) ? Optional.of(clazz.cast(obj)) : Optional.empty();
    }
    public Optional<Class> valueType(String key){
        Object obj = properties.get(key);
        if (obj != null) {
            Class clazz = obj.getClass();
            return Optional.of(obj.getClass());
        }
        return Optional.empty();
    }
    public boolean isKey(String key){
        return properties.containsKey(key);
    }

    public static JsonMap fromJson(String json){
        if (json == null){
            return EMPTY;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> map = mapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });
            return new JsonMap(Collections.unmodifiableMap(map));
        } catch (JsonProcessingException e) {
            return EMPTY;
        }
    }

    public Optional<String> toJson(){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return Optional.of(mapper.writeValueAsString(properties));
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }
    }

    public int size(){
        return properties.size();
    }

    public Set<String> keys(){
        return properties.keySet();
    }

    public Stream<Map.Entry<String, Object>> stream(){
        return properties.entrySet().stream();
    }

    public Set<Map.Entry<String, Object>> getPropertiesAsSet(){
        return properties != null ? properties.entrySet() : Collections.emptySet();
    }
    public JsonMap mergeWithInheritance(JsonMap parentMap){
        Map<String, Object> current = new LinkedHashMap<>(properties);
        parentMap.properties.forEach((key, value) -> current.merge(key, value, (v1, v2) -> v1));
        return new JsonMap(current);
    }


    public JsonMap addAttribute(String key, Object value){
        Map<String, Object> newMap = new HashMap<>(properties);
        newMap.put(key, value);
        return new JsonMap(Collections.unmodifiableMap(newMap));
    }

    public JsonMap removeAttribute(String key){
        Map<String, Object> newMap = new HashMap<>(properties);
        newMap.remove(key);
        return new JsonMap(Collections.unmodifiableMap(newMap));
    }

    public boolean isEmpty(){
        return this == EMPTY;
    }
}
