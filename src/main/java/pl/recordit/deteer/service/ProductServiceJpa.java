package pl.recordit.deteer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.recordit.deteer.dto.NewProductDto;
import pl.recordit.deteer.dto.ProductDto;
import pl.recordit.deteer.entity.FileDocument;
import pl.recordit.deteer.entity.Product;
import pl.recordit.deteer.mapper.NewProductMapper;
import pl.recordit.deteer.mapper.ProductMapper;
import pl.recordit.deteer.repository.ProductRepository;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ProductServiceJpa implements ProductService {
  private final ProductRepository prodRepo;
  private final FileDocumentService fileDocumentService;
  private final ProductMapper dtoProductMapper;
  private final NewProductMapper newProductMapper;

  @Autowired
  public ProductServiceJpa(ProductRepository entityRepository,
                           FileDocumentService fileDocumentService) {
    this.prodRepo = entityRepository;
    this.fileDocumentService = fileDocumentService;

    dtoProductMapper = ProductMapper.builder()
            .labelMap(fileDocumentService::findBy)
            .manualMap(fileDocumentService::findBy)
            .sheetMap(fileDocumentService::findBy)
            .parentMap(id -> prodRepo.findById(id).orElse(null))
            .build();

    newProductMapper = NewProductMapper.builder()
            .labelMap(labelDto -> fileDocumentService.save(labelDto).orElse(null))
            .sheetMap(sheetDto -> fileDocumentService.save(sheetDto).orElse(null))
            .manualMap(manualDto -> fileDocumentService.save(manualDto).orElse(null))
            .parentMap(id -> prodRepo.findById(id).orElse(null))
            .build();
    //TODO implement user supplier
  }

  @Override
  public Optional<Product> findBy(long id) {
    return prodRepo.findById(id);
  }

  @Override
  public List<Product> findAll() {
    return Collections.unmodifiableList(prodRepo.findAll());
  }

  @Override
  @Transactional
  public Optional<Product> create(NewProductDto dto) {
    return newProductMapper.fromDto(dto)
            .flatMap(entity -> Optional.of(prodRepo.save(entity)))
            .flatMap(entity -> {
              Optional.ofNullable(entity.getOperatingManual()).ifPresent(e -> e.setProduct(entity));
              Optional.ofNullable(entity.getEnergyLabel()).ifPresent(e -> e.setProduct(entity));
              Optional.ofNullable(entity.getProductSheet()).ifPresent(e -> e.setProduct(entity));
              return Optional.of(entity);
            });
  }

  @Override
  @Transactional
  public Optional<Product> update(ProductDto dto, long id) {
    return prodRepo.findById(id)
            .flatMap(entity -> {
              entity.setPropertiesAsJson(dto.getProperties());
              entity.setName(dto.getName());
              return Optional.of(entity);
            });
  }

  @Override
  public List<Product> findByName(String name) {
    return prodRepo.findAll().stream()
            .filter(p -> p.getName().toLowerCase().contains(name.toLowerCase()))
            .collect(Collectors.toList());
  }

  @Override
  public List<Product> findByProperty(String property) {
    return prodRepo.findAll()
            .stream()
            .filter(p -> p.getProperties()
                    .stream()
                    .anyMatch(a -> Objects.nonNull(a.getKey()))
            )
            .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public Optional<Product> updateOperatingManual(long id, FileDocument fileDocument) {
    return prodRepo.findById(id)
            .flatMap(entity -> {
              entity.setOperatingManual(fileDocument);
              return Optional.of(entity);
            });
  }

  @Override
  public Stream<FileDocument> findDocuments(Function<Product, FileDocument> selector) {
    return prodRepo.findAll().stream().map(selector).filter(Objects::nonNull);
  }

  @Override
  public Stream<FileDocument> findAllPublicDocumentsForProduct(long productId) {
    Optional<Product> oProduct = prodRepo.findById(productId);
    if (oProduct.isPresent()) {
      return Stream.empty();
    }
    Product product = oProduct.get();
    return fileDocumentService.findByProductId(productId, FileDocument::isPublic)
            .filter(doc -> product.hasOperatingManual() && doc.getId() != product.getOperatingManual().getId())
            .filter(doc -> product.hasEnergyLabel() && doc.getId() != product.getEnergyLabel().getId())
            .filter(doc -> product.hasProductSheet() && doc.getId() != product.getProductSheet().getId());
  }

}
