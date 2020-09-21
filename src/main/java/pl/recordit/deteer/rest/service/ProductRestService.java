package pl.recordit.deteer.rest.service;

import pl.recordit.deteer.rest.entity.ProductRest;

import java.util.List;
import java.util.Optional;

public interface ProductRestService {
    List<ProductRest> findAll();
    Optional<ProductRest> findById(long id);
}
