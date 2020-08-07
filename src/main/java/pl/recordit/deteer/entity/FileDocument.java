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
    @EqualsAndHashCode.Include
    @Getter
    @Setter
    private long id;

    @Getter
    @Setter
    private String visibleName;

    @Getter
    @Setter
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

    public FileDocument generateLink(String path){
        link = String.format("%s/%s",path,originalFilename);
        return this;
    }
}
