package pl.recordit.deteer.validation;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PasswordValidatorTest {

    @Test
    void shouldReturnErrorForEmptyPassword(){
        Optional<String> error = PasswordValidator.DEFAULT.validateAndGetError("");
        assertTrue(error.isPresent());
        assertEquals("Hasło nie może być puste", error.get());
    }

    @Test
    void shouldReturnErrorForTooShortPassword(){
        Optional<String> error = PasswordValidator.DEFAULT.validateAndGetError("ABc%1");
        assertTrue(error.isPresent());
        assertEquals("Hasło musi się składać z co najmniej ośmiu znaków!", error.get());
    }

    @Test
    void shouldReturnErrorForPasswordWithoutLowerCaseLetter(){
        Optional<String> error = PasswordValidator.DEFAULT.validateAndGetError("1BCDEEEEASDF");
        assertTrue(error.isPresent());
        assertEquals("Hasło musi zawierać małą literę!", error.get());
    }

    @Test
    void shouldReturnErrorForPasswordWithoutDigit(){
        Optional<String> error = PasswordValidator.DEFAULT.validateAndGetError("ABCDEEEEASDA");
        assertTrue(error.isPresent());
        assertEquals("Hasło musi zawierać cyfrę!", error.get());
    }

    @Test
    void shouldReturnErrorForPasswordWithoutNonAlphabetic(){
        Optional<String> error = PasswordValidator.DEFAULT.validateAndGetError("ABCDEEEEAaa12");
        assertTrue(error.isPresent());
        error.ifPresent(e -> assertEquals("Hasło musi zawierać znak nie będący literą ani cyfrą!", e));
    }

    @Test
    void shouldReturnErrorForPasswordWithWhitespace(){
        Optional<String> error = PasswordValidator.DEFAULT.validateAndGetError("ABCDE   EEEAaa*1");
        assertTrue(error.isPresent());
        error.ifPresent(e -> assertEquals("Hasło nie może zawierać spacji!", error.get()));
    }

    @Test
    void shouldReturnEmptyForValidPassword(){
        Optional<String> error = PasswordValidator.DEFAULT.validateAndGetError("ABCDEEEEAaa*1");
        assertFalse(error.isPresent());
    }


}