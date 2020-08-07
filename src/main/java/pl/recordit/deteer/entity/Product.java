package pl.recordit.deteer.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import pl.recordit.deteer.model.InheritedPropertyMap;
import pl.recordit.deteer.model.JsonMap;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "products")
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  private long id;

  private String name;

  @ManyToOne(fetch = FetchType.EAGER)
  private Product parent;

  @ManyToOne
  private User publisher;

  @CreationTimestamp
  private LocalDateTime published;

  @Embedded
  ProductProperties properties;

  @Transient
  private JsonMap allProperties;

  @Transient
  private InheritedPropertyMap allDetailedProperties;

  @ManyToOne
  private FileDocument operatingManual;

  @ManyToOne
  private FileDocument energyLabel;

  @ManyToOne
  private FileDocument productSheet;

  @OneToMany(mappedBy = "product")
  private Set<Comment> comments = new HashSet<>();

  public String getPropertiesAsJson() {
    return properties.getJson();
  }

  public void setPropertiesAsJson(String json) {
    properties.setJson(json);
  }

  public JsonMap getProperties() {
    if (allProperties != null) {
      return allProperties;
    }
    JsonMap parentMap = JsonMap.EMPTY;
    parentMap = parents()
            .map(parent -> parent.properties.getJsonMap())
            .reduce(parentMap, (acu, parent) -> parent.mergeWithInheritance(acu));
    allProperties = properties.getJsonMap().mergeWithInheritance(parentMap);
    return allProperties;
  }

  public InheritedPropertyMap getDetailedProperties() {
    if (allDetailedProperties != null) {
      return allDetailedProperties;
    }
    InheritedPropertyMap parentMap = InheritedPropertyMap.EMPTY;
    parentMap = parents()
            .map(parent -> parent.properties.getDetailedPropertiesMap(parent.id, parent.name))
            .reduce(parentMap, (acu, parent) -> parent.mergeWithInheritance(acu));
    allDetailedProperties = properties.getDetailedPropertiesMap(id, name).mergeWithInheritance(parentMap);
    return allDetailedProperties;
  }

  public Optional<FileDocument> getInheritedOperatingManual() {
    if (operatingManual != null) {
      return Optional.of(operatingManual);
    }
    return parents()
            .map(parent -> Optional.ofNullable(parent.getOperatingManual()))
            .reduce(Optional.empty(), (accu, manual) -> manual.isPresent() ? manual : accu);
  }

  public Stream<Product> parents() {
    Product current = parent;
    List<Product> parents = new LinkedList<>();
    while (current != null) {
      parents.add(0, current);
      current = current.parent;
    }
    return parents.stream();
  }

  public List<Comment> getSortedComments() {
    return comments.stream().sorted(Comparator.comparing(Comment::getPublished)).collect(Collectors.toList());
  }

  public boolean hasOperatingManual() {
    return operatingManual != null;
  }

  public boolean hasProductSheet() {
    return productSheet != null;
  }

  public boolean hasEnergyLabel() {
    return energyLabel != null;
  }

  public boolean hasParent() {
    return parent != null;
  }

}
