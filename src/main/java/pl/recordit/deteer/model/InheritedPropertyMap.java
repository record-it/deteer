package pl.recordit.deteer.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;
import java.util.stream.Collectors;

public class InheritedPropertyMap {
  public static final InheritedPropertyMap EMPTY = new InheritedPropertyMap(Collections.emptyMap());
  private final Map<String, InheritedProperty> properties;

  public InheritedPropertyMap(Map<String, InheritedProperty> properties) {
    this.properties = properties;
  }

  public InheritedPropertyMap mergeWithInheritance(InheritedPropertyMap parentMap) {
    Map<String, InheritedProperty> current = new LinkedHashMap<>(properties);
    parentMap.properties.forEach((key, value) -> current.merge(key, value, (v1, v2) -> v1));
    return new InheritedPropertyMap(current);
  }

  public static InheritedPropertyMap fromJson(String json, Long id, String name) {
    if (json == null) {
      return EMPTY;
    }
    ObjectMapper mapper = new ObjectMapper();
    try {
      Map<String, Object> map = mapper.readValue(json, new TypeReference<Map<String, Object>>() {
      });
      Map<String, InheritedProperty> result = map.entrySet()
              .stream()
              .map(entry
                      -> new AbstractMap.SimpleEntry<>(entry.getKey(), InheritedProperty.builder()
                      .value(entry.getValue())
                      .inheritanceSourceId(id)
                      .inheritanceSourceName(name)
                      .build()))
              .collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));
      return new InheritedPropertyMap(result);
    } catch (JsonProcessingException e) {
      return EMPTY;
    }
  }
  public Map<String, InheritedProperty> getProperties(){
    return Collections.unmodifiableMap(properties);
  }

  public Set<Map.Entry<String, InheritedProperty>> getPropertiesAsSet() {
    return properties != null ? Collections.unmodifiableSet(properties.entrySet()) : Collections.emptySet();
  }

}
