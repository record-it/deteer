package pl.recordit.deteer.entity;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import pl.recordit.deteer.model.JsonMap;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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

    @NotBlank(message = "Nazwa nie może być pusta!")
    @NotNull(message = "Nazwa nie może być wartością null!")
    @Size(min=5, max = 50, message = "Zbyt krótka (mniej niż 5 znaków), lub zbyt długa (powyżej 50 znaków) nazwa!")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    private Product parent;

    @ManyToOne
    private User publisher;

    @CreationTimestamp
    private LocalDateTime published;

    @NotBlank(message = "Właściwości nie mogą być puste")
    @NotNull
    @Embedded
    ProductProperties properties;

    public String getJson(){
        return properties.getJson();
    }

    public void setJson(String json){
        properties.setJson(json);
    }

    @Transient
    private JsonMap allProperties;

    @ManyToOne
    private FileDocument operatingManual;

    @ManyToOne
    private FileDocument energyLabel;

    @ManyToOne
    private FileDocument productSheet;

    @OneToMany(mappedBy = "product")
    private Set<Comment> comments = new HashSet<>();

    public JsonMap getProperties(){
        if (allProperties != null){
            return allProperties;
        }
        JsonMap parentMap = JsonMap.EMPTY;
        parentMap = parents()
                .map(parent -> parent.properties.getJsonMap())
                .reduce(parentMap, (acu, parent) -> parent.mergeWithInheritance(acu));
        allProperties = properties.getJsonMap().mergeWithInheritance(parentMap);
        return allProperties;
    }

    public Optional<FileDocument> getInheritedOperatingManual(){
         if (operatingManual != null){
             return Optional.of(operatingManual);
         }
         return parents()
                 .map(parent -> Optional.ofNullable(parent.getOperatingManual()))
                 .reduce(Optional.empty(), (accu, manual) -> manual.isPresent() ? manual : accu);
    }

    public Stream<Product> parents(){
        Product current = parent;
        List<Product> parents = new LinkedList<>();
        while(current != null){
            parents.add(0, current);
            current = current.parent;
        }
        return parents.stream();
    }

    public List<Comment> getSortedComments(){
        return comments.stream().sorted(Comparator.comparing(Comment::getPublished)).collect(Collectors.toList());
    }

    public boolean hasOperatingManual(){
        return operatingManual != null;
    }

    public boolean hasProductSheet(){
        return productSheet != null;
    }

    public boolean hasEnergyLabel(){
        return energyLabel != null;
    }

}
