package pl.recordit.deteer.mapper;
import lombok.Builder;
import pl.recordit.deteer.dto.FileDocumentDto;
import pl.recordit.deteer.entity.FileDocument;
import pl.recordit.deteer.entity.Product;

import java.util.function.Function;

@Builder
public class FileDocumentMapper {
    private final Function<Long, Product> ownerMap;

    public FileDocument fromDto(FileDocumentDto dto){
        return FileDocument.builder()
                .originalFilename(dto.getFile().getOriginalFilename())
                .visibleName(dto.getVisibleName())
                .product(ownerMap != null && dto.getProductId() != null ? ownerMap.apply(dto.getProductId()):null)
                .build();
    }
}
