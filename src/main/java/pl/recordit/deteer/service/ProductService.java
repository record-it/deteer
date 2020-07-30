package pl.recordit.deteer.service;

import pl.recordit.deteer.dto.FileDocumentDto;
import pl.recordit.deteer.dto.NewProductDto;
import pl.recordit.deteer.dto.ProductDto;
import pl.recordit.deteer.entity.FileDocument;
import pl.recordit.deteer.entity.Product;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public interface ProductService {
    Optional<Product> findBy(long id);
    List<Product> findAll();
    Optional<Product> create(ProductDto dto);
    Optional<Product> create(NewProductDto dto);
    Optional<Product> update(ProductDto dto, long id);
    List<Product> findByName(String name);
    List<Product> findByProperty(String property);
    Optional<Product> updateOperatingManual(long id, FileDocument fileDocument);
    Stream<FileDocument> findDocuments(Function<Product, FileDocument> selector);
}
