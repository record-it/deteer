package pl.recordit.deteer.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.File;
import java.time.LocalDate;
import java.util.function.Supplier;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class FileDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private long id;

    @Getter
    @Setter
    private String visibleName;

    @Getter
    @Setter
    @EqualsAndHashCode.Include
    @Column(unique = true)
    private String originalFilename;

    @Getter
    @Setter
    @CreationTimestamp
    private LocalDate published;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Getter
    @Setter
    @ManyToOne
    private User publisher;

    @Transient
    @Getter
    private String link = "";

    @Getter
    @Setter
    private boolean isPublic;

    public FileDocument generateLink(String path){
        link = String.format("%s/%s",path,originalFilename);
        return this;
    }
    //TODO implement proper equals and hashCode
}
