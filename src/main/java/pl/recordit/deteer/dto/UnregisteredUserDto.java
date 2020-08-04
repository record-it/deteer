package pl.recordit.deteer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import pl.recordit.deteer.model.UserRole;

@Data
@AllArgsConstructor
@Builder
public class UnregisteredUserDto {
    private final String email;
    private final String password;
    private final boolean enabled = false;
    private final boolean verified = false;
}
