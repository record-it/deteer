package pl.recordit.deteer.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.recordit.deteer.rest.entity.ProductRest;
import pl.recordit.deteer.rest.service.ProductRestService;
import java.util.List;


@RestController
@RequestMapping("/api")
public class ProductRestController {
    private final ProductRestService service;

    @Autowired
    public ProductRestController(ProductRestService service) {
        this.service = service;
    }

    @GetMapping("/v1/products")
    List<ProductRest> index() {
        return service.findAll();
    }

    @GetMapping("/v1/products/{id}")
    ResponseEntity<ProductRest> one(@PathVariable long id) {
        return ResponseEntity.of(service.findById(id));
    }

}
