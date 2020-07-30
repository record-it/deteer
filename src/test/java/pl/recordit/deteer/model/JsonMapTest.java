package pl.recordit.deteer.model;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class JsonMapTest {
    @Test
    void valueShouldReturnValidValueTypeForKey(){
        //GIVEN
        Map<String, Object> json = new HashMap<>();
        json.put("length", 10);
        json.put("name", "name");
        json.put("isRed", true);
        //WHEN
        JsonMap desc = new JsonMap(json);
        Optional<Integer> i = desc.value("length", Integer.class);
        Optional<String> s = desc.value("name", String.class);
        Optional<Boolean> b = desc.value("isRed", Boolean.class);
        //THAT
        assertTrue(i.get() instanceof Integer);
        assertTrue(s.get() instanceof String);
        assertTrue(b.get() instanceof Boolean);
    }

    @Test
    void valueTypeShouldReturnValidTypeForKey(){
        //GIVEN
        Map<String, Object> json = new HashMap<>();
        json.put("length", 10);
        json.put("name", "name");
        json.put("isRed", true);
        json.put("null", null);
        //WHEN
        JsonMap desc = new JsonMap(json);
        //THAT
        assertEquals(Integer.class, desc.valueType("length").get());
        assertEquals(String.class, desc.valueType("name").get());
        assertEquals(Boolean.class, desc.valueType("isRed").get());
        assertFalse(desc.valueType("null").isPresent());
    }
    @Test
    void fromJsonShouldReturnValidMap(){
        //WHEN
        String json = "{\"length\":10, \"name\": \"name\", \"isRed\": true}";
        //GIVEN
        JsonMap desc = JsonMap.fromJson(json);
        //THAT
        assertTrue(desc.value("length", Integer.class).isPresent());
        assertTrue(desc.value("name", String.class).isPresent());
        assertTrue(desc.value("isRed", Boolean.class).isPresent());

        assertEquals(10, desc.value("length", Integer.class).get());
        assertEquals("name", desc.value("name", String.class).get());
        assertEquals(true, desc.value("isRed", Boolean.class).get());
    }

    @Test
    void isKeyShouldReturnValidValues(){
        //WHEN
        String json = "{\"length\":10, \"name\": \"name\", \"isRed\": true}";
        //GIVEN
        JsonMap desc = JsonMap.fromJson(json);
        //THAT
        assertTrue(desc.isKey("length"));
        assertTrue(desc.isKey("name"));
        assertTrue(desc.isKey("isRed"));
        assertFalse(desc.isKey("lenght"));
    }

    @Test
    void shouldReturnEmptyForNullOrEmptyString(){
        //WHEN
        String json;
        //GIVEN
        JsonMap desc1 = JsonMap.fromJson(null);
        JsonMap desc2 = JsonMap.fromJson("");
        //THAT
        assertTrue(desc1.isEmpty());
        assertTrue(desc2.isEmpty());
    }

    @Test
    void shouldReturnValidJsonMapForMergeWithInheritance(){
        //WHEN
        String currentJson = "{\"length\":10, \"name\": \"name\", \"isRed\": true}";
        String parentJson = "{\"length\":20, \"height\": 100, \"isRed\": false}";
        String rootJson = "{\"length\":30, \"height\": 1000, \"isRed\": false}";
        //GIVEN
        JsonMap current = JsonMap.fromJson(currentJson);
        JsonMap parent = JsonMap.fromJson(parentJson);
        JsonMap root = JsonMap.fromJson(rootJson);
        parent = parent.mergeWithInheritance(root);
        current = current.mergeWithInheritance(parent);
        //THAT
        assertTrue(current.value("length", Integer.class).isPresent());
        assertTrue(current.value("height", Integer.class).isPresent());
        assertTrue(current.value("name", String.class).isPresent());
        assertTrue(current.value("isRed", Boolean.class).isPresent());

        assertEquals(true, current.value("isRed", Boolean.class).orElse(false));
        assertEquals(100, current.value("height", Integer.class).orElse(0));
        assertEquals(10, current.value("length", Integer.class).orElse(0));
        assertEquals("name", current.value("name", String.class).orElse(""));

        assertEquals(20, parent.value("length", Integer.class).orElse(0));
    }

    @Test
    void shouldReturnValidJsonAfterAddAttribute(){
        //WHEN
        String currentJson = "{\"length\":10, \"name\": \"name\", \"isRed\": true}";
        JsonMap current = JsonMap.fromJson(currentJson);
        //GIVEN
        current = current.addAttribute("size", "XXL");
        String newJson = current.toJson().orElse(currentJson);
        //THAT
        current = JsonMap.fromJson(newJson);
        assertEquals("XXL", current.value("size", String.class).orElse(""));
        assertEquals(true, current.value("isRed", Boolean.class).orElse(false));
        assertEquals(10, current.value("length", Integer.class).orElse(0));
        assertEquals("name", current.value("name", String.class).orElse(""));
    }
}