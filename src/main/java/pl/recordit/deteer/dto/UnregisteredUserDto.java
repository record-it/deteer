package pl.recordit.deteer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UnregisteredUserDto {
    private final String email;
    private final String password;
    private final boolean enabled = false;
    private final boolean verified = false;
}
