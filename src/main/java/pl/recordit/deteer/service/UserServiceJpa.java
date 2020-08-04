package pl.recordit.deteer.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.recordit.deteer.controll.Error;
import pl.recordit.deteer.controll.Feedback;
import pl.recordit.deteer.dto.LoginUserDto;
import pl.recordit.deteer.dto.UnregisteredUserDto;
import pl.recordit.deteer.mapper.UserMapper;
import pl.recordit.deteer.entity.User;
import pl.recordit.deteer.repository.UserRepository;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

@Service
public class UserServiceJpa implements UserService {
  private final UserRepository repository;
  private final MailService mailService;
  private final VerifyingTokenService verifyingTokenService;
  private final PasswordEncoder passwordEncoder;
  private final Logger logger = Logger.getLogger(UserServiceJpa.class.getName());

  public UserServiceJpa(UserRepository repository, MailService mailService, VerifyingTokenService verifyingTokenService, PasswordEncoder passwordEncoder) throws IOException {
    this.repository = repository;
    this.mailService = mailService;
    this.verifyingTokenService = verifyingTokenService;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public Feedback register(UnregisteredUserDto unregisteredUserAppDto, BiFunction<Long, String, String> linkGenerator) {
    return Optional.of(repository.save(UserMapper.INSTANCE.setPasswordEncoder(passwordEncoder).fromDto(unregisteredUserAppDto)))
            .flatMap(user -> {
              logger.info("Saved registered user: " + user);
              String verifyingLink = verifyingTokenService.generateVerifyingLinkForUser(user, linkGenerator);
              Feedback feedback = mailService.sendVerifyingMail(user.getEmail(), verifyingLink);
              logger.info("Send verifying mail and got feedback: " + feedback);
              if (feedback.isError()) {
                logger.info("Deleting registered user due failed mail delivery with activation link ");
                repository.deleteById(user.getId());
              }
              return Optional.of(feedback);
            })
            .orElse(Feedback.ofError("Nieznany błąd dostarczenia wiadomości z linkiem weryfikacyjnym"));
  }

  @Override
  @Transactional
  public VerificationStatus verifyUser(String token, long userId) {
    return repository.findById(userId)
            .flatMap(user -> {
              if (user.isVerified()) {
                return Optional.of(VerificationStatus.ALREADY_VERIFIED);
              }
              if (verifyingTokenService.isTokenValid(token, user)) {
                user.setEnabled(true);
                user.setVerified(true);
                return Optional.of(VerificationStatus.VERIFIED);
              } else {
                return Optional.of(VerificationStatus.NOT_VERIFIED);
              }
            }).orElse(VerificationStatus.UNKNOWN_ERROR);
  }

  @Override
  public Optional<User> findById(long id) {
    return Optional.empty();
  }

  @Override
  public boolean isEmailExists(String email) {
    return repository.findByEmail(email) != null;
  }
}
