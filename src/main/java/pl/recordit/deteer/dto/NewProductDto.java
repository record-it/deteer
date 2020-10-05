package pl.recordit.deteer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import pl.recordit.deteer.entity.User;

import javax.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewProductDto {
    @Size(min=10, max = 2000, message = "Niepoprawna długość, powinna być od 5 do 2000")
    private String properties;
    @Size(min=4, max = 50, message = "Zbyt krótka (mniej niż 5 znaków) lub zbyt długa (powyżej 50 znaków) nazwa!")
    private String name;
    private Long  parentId;
    private FileDocumentDto operatingManual;
    private FileDocumentDto energyLabel;
    private FileDocumentDto productSheet;
    private boolean isPublic;
    private User publisher;
}
