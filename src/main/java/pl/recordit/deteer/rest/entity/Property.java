package pl.recordit.deteer.rest.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Property {

    @Id
    private String name;
}
