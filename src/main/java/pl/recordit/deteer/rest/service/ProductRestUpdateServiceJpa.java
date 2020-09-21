package pl.recordit.deteer.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.recordit.deteer.entity.Product;
import pl.recordit.deteer.rest.entity.ProductRest;
import pl.recordit.deteer.rest.mapper.ProductRestMapper;
import pl.recordit.deteer.rest.repository.ProductRestRepository;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class ProductRestUpdateServiceJpa implements ProductRestUpdateService{

    private final ProductRestMapper mapper;
    private final ProductRestRepository repository;

    @Autowired
    public ProductRestUpdateServiceJpa(ProductRestRepository repository) {
        this.repository = repository;
        this.mapper = new ProductRestMapper();
    }

    @Override
    @Transactional
    public Optional<ProductRest> create(Product product) {
        return mapper.mapTo(product)
                .flatMap(productRest -> Optional.of(repository.save(productRest)));
    }

    @Override
    @Transactional
    public Optional<ProductRest> update(Product product) {
       return repository.findById(product.getId())
               .flatMap(productRest -> {
                   productRest.setProperties(product.getProperties().toJson().orElse(""));
                   productRest.setValid(true);
                   return Optional.of(productRest);
               });
    }

    @Override
    @Transactional
    public Optional<ProductRest> delete(long productId) {
        return repository.findById(productId)
                .flatMap(entity -> {
                    repository.delete(entity);
                    return Optional.of(entity);
                });
    }

    @Override
    @Transactional
    public void invalidate(long productId) {
        repository.findById(productId)
                .flatMap(productRest -> {
                    productRest.setValid(false);
                    return Optional.of(productRest);
                });
    }
}
