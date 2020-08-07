package pl.recordit.deteer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class InheritedProperty {
  private final Object value;
  private final Long inheritanceSourceId;
  private final String inheritanceSourceName;

  public boolean isInherited(Long currentId){
    return !inheritanceSourceId.equals(currentId);
  }

  public boolean isNative(Long currentId){
    return inheritanceSourceId.equals(currentId);
  }
}
