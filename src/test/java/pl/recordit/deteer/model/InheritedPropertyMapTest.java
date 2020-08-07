package pl.recordit.deteer.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InheritedPropertyMapTest {

  @Test
  public void testPropertyInheritance(){
    //WHEN
    String jsonParent = "{\"length\":10, \"name\": \"name\", \"isRed\": true}";
    Long parentId = 1L;
    //GIVEN
    InheritedPropertyMap parent = InheritedPropertyMap.fromJson(jsonParent, parentId, "parent");
    //THAT
    assertEquals(10, parent.getProperties().get("length").getValue());
    assertFalse(parent.getProperties().get("name").isInherited(1L));
    assertTrue(parent.getProperties().get("length").isNative(1L));

    //WHEN
    String jsonChild = "{\"length\":110, \"color\": \"black\"}";
    Long childId = 2L;
    //GIVEN
    InheritedPropertyMap child = InheritedPropertyMap.fromJson(jsonChild, childId, "child");
    child = child.mergeWithInheritance(parent);
    //THAT
    assertEquals(110, child.getProperties().get("length").getValue());
    assertEquals(childId, child.getProperties().get("length").getInheritanceSourceId());
    assertEquals("name", child.getProperties().get("name").getValue());
    assertEquals(parentId, child.getProperties().get("name").getInheritanceSourceId());
  }

}