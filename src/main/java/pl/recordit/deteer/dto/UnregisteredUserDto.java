package pl.recordit.deteer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import pl.recordit.deteer.model.UserRole;

@Data
@AllArgsConstructor
@Builder
public class UnregisteredUserDto {
    private String email;
    private String password;
    private UserRole role;
    private final boolean active = false;
    private final boolean verifying = false;
}
