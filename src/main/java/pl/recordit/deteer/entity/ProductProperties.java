package pl.recordit.deteer.entity;

import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import pl.recordit.deteer.model.InheritedPropertyMap;
import pl.recordit.deteer.model.JsonMap;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductProperties {
    @JsonRawValue
    private String json;
    @Transient
    private JsonMap jsonMap = JsonMap.EMPTY;
    @Transient
    private InheritedPropertyMap properties = InheritedPropertyMap.EMPTY;

    public JsonMap getJsonMap() {
        if (jsonMap == JsonMap.EMPTY && json != null) {
            jsonMap = JsonMap.fromJson(json);
        }
        return jsonMap;
    }

    public InheritedPropertyMap getDetailedPropertiesMap(Long originId, String originName){
        if (properties == InheritedPropertyMap.EMPTY || json != null) {
            properties = InheritedPropertyMap.fromJson(json, originId, originName);
        }
        return properties;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        jsonMap = JsonMap.EMPTY;
        this.json = json;
    }

    public void addProperty(String key, Object value){
        jsonMap = jsonMap.addAttribute(key, value);
        json = jsonMap.toJson().orElse(json);
    }

    public void removeProperty(String key){
        jsonMap = jsonMap.removeAttribute(key);
        json = jsonMap.toJson().orElse(json);
    }

    public String asString(){
        return json;
    }
}

