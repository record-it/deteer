package pl.recordit.deteer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import pl.recordit.deteer.entity.User;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileDocumentDto {
    private String visibleName;
    private MultipartFile file;
    private FileCategory category;
    private Long productId;
    private User publisher;
}
