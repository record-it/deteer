package pl.recordit.deteer.rest.entity;

import pl.recordit.deteer.entity.Product;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@Entity
public class PropertiesTemplate implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @OneToOne
    private Product product;

    @OneToMany
    private Set<Property> properties;
}
