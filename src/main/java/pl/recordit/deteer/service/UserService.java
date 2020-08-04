package pl.recordit.deteer.service;
import org.springframework.stereotype.Service;
import pl.recordit.deteer.controll.Feedback;
import pl.recordit.deteer.dto.LoginUserDto;
import pl.recordit.deteer.dto.UnregisteredUserDto;
import pl.recordit.deteer.entity.User;
import java.util.Optional;
import java.util.function.BiFunction;

@Service
public interface UserService {
    Feedback register(UnregisteredUserDto user, BiFunction<Long, String, String> linkGenerator);
    VerificationStatus verifyUser(String token, long userId);
    Optional<User> findById(long id);
    boolean isEmailExists(String email);
}
