package pl.recordit.deteer.mapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import pl.recordit.deteer.dto.FileDocumentDto;
import pl.recordit.deteer.entity.FileDocument;
import pl.recordit.deteer.entity.Product;

import java.util.function.Function;

@Builder
@AllArgsConstructor
public class FileDocumentMapper {
    private final Function<Long, Product> productMap;

    public FileDocument fromDto(FileDocumentDto dto){
        return FileDocument.builder()
                .originalFilename(dto.getFile().getOriginalFilename())
                .visibleName(dto.getVisibleName())
                .product(productMap != null && dto.getProductId() != null ? productMap.apply(dto.getProductId()):null)
                .publisher(dto.getPublisher())
                .build();
    }
}
