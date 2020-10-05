package pl.recordit.deteer.service;

import pl.recordit.deteer.dto.FileDocumentDto;
import pl.recordit.deteer.dto.NewProductDto;
import pl.recordit.deteer.dto.ProductDto;
import pl.recordit.deteer.entity.FileDocument;
import pl.recordit.deteer.entity.Product;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public interface ProductService {

    Optional<Product> findBy(long id);

    List<Product> findAll();

    List<Product> findAllPublic();

    Optional<Product> create(NewProductDto dto);

    Optional<Product> update(ProductDto dto, long id);

    List<Product> findByName(String name);

    List<Product> findByProperty(String property);

    Optional<Product> updateOperatingManual(long id, FileDocument fileDocument);

    Stream<FileDocument> findDocuments(Function<Product, FileDocument> selector);

    Stream<FileDocument> findAllPublicDocumentsForProduct(long id);

    Stream<FileDocument> findAllDocumentsForProduct(long id);

    Stream<Product> findChildren(long id);
}
