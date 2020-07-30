package pl.recordit.deteer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewProductDto {
    private String properties;
    private String name;
    private Long  parentId;
    private FileDocumentDto operatingManual;
    private FileDocumentDto energyLabel;
    private FileDocumentDto productSheet;
}
