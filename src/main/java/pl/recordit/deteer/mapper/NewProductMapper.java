package pl.recordit.deteer.mapper;

import lombok.Builder;
import pl.recordit.deteer.dto.FileDocumentDto;
import pl.recordit.deteer.dto.NewProductDto;
import pl.recordit.deteer.dto.ProductDto;
import pl.recordit.deteer.entity.FileDocument;
import pl.recordit.deteer.entity.Product;
import pl.recordit.deteer.entity.ProductProperties;
import pl.recordit.deteer.entity.User;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

@Builder
public class NewProductMapper {

    private final Function<Long, Product> parentMap;

    private final Function<FileDocumentDto, FileDocument> manualMap;

    private final Function<FileDocumentDto, FileDocument> labelMap;

    private final Function<FileDocumentDto, FileDocument> sheetMap;

    private final Supplier<User> userSupplier;

    public Optional<Product> fromDto(NewProductDto dto) {
        return dto != null && dto.getProperties() != null
                ? Optional.of(Product.builder()
                .properties(ProductProperties.builder().json(dto.getProperties()).build())
                .name(dto.getName())
                .parent(parentMap != null && dto.getParentId() != null ? parentMap.apply(dto.getParentId()) : null)
                .energyLabel(labelMap != null && dto.getEnergyLabel() != null ? labelMap.apply(dto.getEnergyLabel()) : null)
                .operatingManual(manualMap != null && dto.getOperatingManual() != null ? manualMap.apply(dto.getOperatingManual()) : null)
                .productSheet(sheetMap != null && dto.getProductSheet() != null ? sheetMap.apply(dto.getProductSheet()) : null)
                .publisher(userSupplier != null ? userSupplier.get() : null)
                .build())
                : Optional.empty();
    }
}
