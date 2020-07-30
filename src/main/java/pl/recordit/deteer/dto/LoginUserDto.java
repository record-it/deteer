package pl.recordit.deteer.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginUserDto {
    private final String login;
    private final String password;
}
