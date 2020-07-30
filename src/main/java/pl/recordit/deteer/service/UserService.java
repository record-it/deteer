package pl.recordit.deteer.service;
import org.springframework.stereotype.Service;
import pl.recordit.deteer.dto.LoginUserDto;
import pl.recordit.deteer.dto.UnregisteredUserDto;
import pl.recordit.deteer.entity.User;
import java.util.Optional;

@Service
public interface UserService {
    void register(UnregisteredUserDto user);
    boolean verify(String verifyingString);
    Optional<User> login(LoginUserDto loggingUser);
    void logout(User userApp);
    Optional<User> findById(long id);
}
