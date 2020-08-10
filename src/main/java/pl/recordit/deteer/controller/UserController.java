package pl.recordit.deteer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.recordit.deteer.controll.Feedback;
import pl.recordit.deteer.dto.UnregisteredUserDto;
import pl.recordit.deteer.service.UserService;
import pl.recordit.deteer.validation.PasswordValidator;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
public class UserController {
  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/register")
  public String registerForm() {
    return "users/registerForm";
  }

  @PostMapping("/register")
  public String registerUser(HttpServletRequest request, UnregisteredUserDto unregisteredUserDto, Model model) {
    if (userService.isEmailExists(unregisteredUserDto.getEmail())) {
      model.addAttribute("email", unregisteredUserDto.getEmail());
      return "users/errorExistingEmail";
    }
    Optional<String> validationError = PasswordValidator.DEFAULT.validateAndGetError(unregisteredUserDto.getPassword());
    if (validationError.isPresent()){
      model.addAttribute("error", validationError.get());
      return "users/errorInvalidPassword";
    }
    Feedback feedback = userService.register(unregisteredUserDto, (id, token) -> String.format("%s/verify/%d/%s", request.getContextPath(), id, token));
    model.addAttribute("email", unregisteredUserDto.getEmail());
    if (feedback.isError()) {
      feedback.toError().ifPresent(error -> model.addAttribute("error", error.getErrorMessage()));
      return "users/errorRegisterUser";
    }
    return "users/infoVerifyingMail";
  }

  @GetMapping("/verify/{userId}/{token}")
  public String verification(@PathVariable Long userId, @PathVariable String token) {
    switch (userService.verifyUser(token, userId)) {
      case VERIFIED:
        return "redirect:/confirm/" + userId;
      case ALREADY_VERIFIED:
        return "users/errorAlreadyVerified";
      case NOT_VERIFIED:
      default:
        return "error";
    }
  }

  @GetMapping("/confirm/{userId}")
  public String confirmVerification(@PathVariable Long userId) {
    return userId != null ? "users/confirmVerification" : "error";
  }
}
