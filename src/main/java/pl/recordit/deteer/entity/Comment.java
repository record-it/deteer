package pl.recordit.deteer.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private long id;

    private String content;

    @ManyToOne
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @CreationTimestamp
    private Timestamp published;

    @Enumerated(EnumType.STRING)
    private CommentStatus status;
}
