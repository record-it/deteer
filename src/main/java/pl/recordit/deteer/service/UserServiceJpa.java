package pl.recordit.deteer.service;

import org.springframework.stereotype.Service;
import pl.recordit.deteer.dto.LoginUserDto;
import pl.recordit.deteer.dto.UnregisteredUserDto;
import pl.recordit.deteer.mapper.UserAppMapper;
import pl.recordit.deteer.entity.User;
import pl.recordit.deteer.repository.UserRepository;

import java.util.Optional;

@Service
public class UserServiceJpa implements UserService{
    private final UserRepository repository;

    public UserServiceJpa(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public void register(UnregisteredUserDto unregisteredUserAppDto) {
        repository.save(UserAppMapper.INSTANCE.fromDto(unregisteredUserAppDto));
    }

    @Override
    public boolean verify(String verifyingString) {
        return false;
    }

    @Override
    public Optional<User> login(LoginUserDto loggingUser) {
        return Optional.empty();
    }

    @Override
    public void logout(User userApp) {

    }

    @Override
    public Optional<User> findById(long id) {
        return Optional.empty();
    }
}
