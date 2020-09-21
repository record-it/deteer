package pl.recordit.deteer.rest.entity;

import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRest {
    @Id
    private long productId;

    private String name;

    @JsonRawValue
    private String properties;

    @UpdateTimestamp
    private Timestamp updated;

    private boolean isValid;

}
