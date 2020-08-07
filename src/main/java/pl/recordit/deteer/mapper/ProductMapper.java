package pl.recordit.deteer.mapper;

import lombok.Builder;
import pl.recordit.deteer.dto.ProductDto;
import pl.recordit.deteer.entity.FileDocument;
import pl.recordit.deteer.entity.Product;
import pl.recordit.deteer.entity.ProductProperties;
import pl.recordit.deteer.entity.User;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

@Builder
public class ProductMapper {
    private final Function<Long, Product> parentMap;
    private final Function<Long, FileDocument> manualMap;
    private final Function<Long, FileDocument> labelMap;
    private final Function<Long, FileDocument> sheetMap;
    private final Supplier<User> userSupplier;

    public ProductDto toDto(Product entity) {
        return ProductDto.builder()
                .name(entity.getName())
                .parentId(Optional.ofNullable(entity.getParent()).flatMap(p -> Optional.of(p.getId())).orElse(null))
                .energyLabelId(Optional.ofNullable(entity.getEnergyLabel()).flatMap(p -> Optional.of(p.getId())).orElse(null))
                .operatingManualId(Optional.ofNullable(entity.getOperatingManual()).flatMap(p -> Optional.of(p.getId())).orElse(null))
                .productSheetId(Optional.ofNullable(entity.getOperatingManual()).flatMap(p -> Optional.of(p.getId())).orElse(null))
                .properties(entity.getPropertiesAsJson())
                .build();
    }

    public Optional<Product> fromDto(ProductDto dto) {
        Optional<Product> product = dto != null && dto.getProperties() != null
                ? Optional.of(Product.builder()
                .properties(ProductProperties.builder().json(dto.getProperties()).build())
                .name(dto.getName())
                .parent(parentMap != null && dto.getParentId() != null ? parentMap.apply(dto.getParentId()) : null)
                .energyLabel(labelMap != null && dto.getEnergyLabelId() != null ? labelMap.apply(dto.getEnergyLabelId()) : null)
                .operatingManual(manualMap != null && dto.getOperatingManualId() != null ? manualMap.apply(dto.getOperatingManualId()) : null)
                .productSheet(sheetMap != null && dto.getProductSheetId() != null ? sheetMap.apply(dto.getProductSheetId()) : null)
                .publisher(userSupplier != null ? userSupplier.get() : null)
                .build())
                : Optional.empty();
        return product;
    }
}
