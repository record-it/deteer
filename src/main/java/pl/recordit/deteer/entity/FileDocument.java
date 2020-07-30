package pl.recordit.deteer.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class FileDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private long id;

    private String visibleName;

    private String originalFilename;

    @CreationTimestamp
    private LocalDate published;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @ManyToOne
    private User publisher;
}
