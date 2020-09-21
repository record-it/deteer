package pl.recordit.deteer.rest.service;

import org.springframework.stereotype.Service;
import pl.recordit.deteer.rest.entity.ProductRest;
import pl.recordit.deteer.rest.repository.ProductRestRepository;
import pl.recordit.deteer.service.ProductService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductRestServiceJpa implements ProductRestService {

    private final ProductRestRepository productRestRepository;
    private final ProductRestUpdateService productRestUpdateService;
    private final ProductService productService;

    public ProductRestServiceJpa(ProductRestRepository productRepository, ProductService productService, ProductRestUpdateService productRestUpdateService, ProductService productService1) {
        this.productRestRepository = productRepository;
        this.productRestUpdateService = productRestUpdateService;
        this.productService = productService1;
    }

    @Override
    public List<ProductRest> findAll() {
        return productRestRepository.findAll()
                .stream()
                .map(productRest -> !productRest.isValid() ? findById(productRest.getProductId()).orElse(null) : productRest)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Optional<ProductRest> findById(long id) {
        productRestRepository.findById(id)
                .filter(productRest -> !productRest.isValid())
                .flatMap(productRest ->
                        productService.findBy(id)
                                .flatMap(productRestUpdateService::update)
                );
        return Optional.ofNullable(productRestRepository.findById(id)
                .orElseGet(() ->
                        productService.findBy(id)
                                .flatMap(productRestUpdateService::create)
                                .orElse(null)
                ));
    }
}
