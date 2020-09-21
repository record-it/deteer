package pl.recordit.deteer.mapper;

import org.springframework.security.crypto.password.PasswordEncoder;
import pl.recordit.deteer.dto.UnregisteredUserDto;
import pl.recordit.deteer.entity.User;

public enum UserMapper {
    INSTANCE;

    private PasswordEncoder passwordEncoder;

    public User fromDto(UnregisteredUserDto unregistered){
        return User.builder()
                .email(unregistered.getEmail())
                .password(passwordEncoder != null ? passwordEncoder.encode(unregistered.getPassword()) : unregistered.getPassword())
                .enabled(unregistered.isEnabled())
                .verified(unregistered.isVerified())
                .build();
    }

    public UserMapper setPasswordEncoder(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
        return this;
    }
}
