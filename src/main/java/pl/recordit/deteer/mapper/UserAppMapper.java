package pl.recordit.deteer.mapper;

import pl.recordit.deteer.dto.UnregisteredUserDto;
import pl.recordit.deteer.entity.User;

public enum UserAppMapper {
    INSTANCE;

    public User fromDto(UnregisteredUserDto unregistered){
        return User.builder()
                .email(unregistered.getEmail())
                .password(unregistered.getPassword())
                .role(unregistered.getRole())
                .build();
    }
}
