package pl.recordit.deteer.rest.service;

import pl.recordit.deteer.entity.Product;
import pl.recordit.deteer.rest.entity.ProductRest;

import java.util.Optional;

public interface ProductRestUpdateService {
    Optional<ProductRest> create(Product product);
    Optional<ProductRest> update(Product product);
    Optional<ProductRest> delete(long productId);
    void invalidate(long productId);
}
