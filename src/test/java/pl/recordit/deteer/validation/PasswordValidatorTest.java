package pl.recordit.deteer.validation;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PasswordValidatorTest {

    @Test
    void shouldReturnErrorForEmptyPassword(){
        Optional<String> error = PasswordValidator.DEFAULT.validateAndGetError("");
        assertTrue(error.isPresent());
        assertEquals(PasswordValidator.Errors.EMPTY.getMessage(), error.get());
    }

    @Test
    void shouldReturnErrorForTooShortPassword(){
        Optional<String> error = PasswordValidator.DEFAULT.validateAndGetError("ABc%1");
        assertTrue(error.isPresent());
        assertEquals(PasswordValidator.Errors.NO_VALID_LENGTH.getMessage(), error.get());
    }

    @Test
    void shouldReturnErrorForPasswordWithoutLowerCaseLetter(){
        Optional<String> error = PasswordValidator.DEFAULT.validateAndGetError("1BCDEEEEASDF");
        assertTrue(error.isPresent());
        assertEquals(PasswordValidator.Errors.NO_LOWER_CASE_CHAR.getMessage(), error.get());
    }

    @Test
    void shouldReturnErrorForPasswordWithoutUpperCaseLetter(){
        Optional<String> error = PasswordValidator.DEFAULT.validateAndGetError("asd1&8765");
        assertTrue(error.isPresent());
        assertEquals(PasswordValidator.Errors.NO_UPPER_CASE_CHAR.getMessage(), error.get());
    }

    @Test
    void shouldReturnErrorForPasswordWithoutDigit(){
        Optional<String> error = PasswordValidator.DEFAULT.validateAndGetError("ABCDEEEEASDA");
        assertTrue(error.isPresent());
        assertEquals(PasswordValidator.Errors.NO_DIGIT.getMessage(), error.get());
    }

    @Test
    void shouldReturnErrorForPasswordWithoutNonAlphabetic(){
        Optional<String> error = PasswordValidator.DEFAULT.validateAndGetError("ABCDEEEEAaa12");
        assertTrue(error.isPresent());
        error.ifPresent(e -> assertEquals(PasswordValidator.Errors.NO_SPECIAL_CHAR.getMessage(), e));
    }

    @Test
    void shouldReturnErrorForPasswordWithWhitespace(){
        Optional<String> error = PasswordValidator.DEFAULT.validateAndGetError("ABCDE   EEEAaa*1");
        assertTrue(error.isPresent());
        error.ifPresent(e -> assertEquals(PasswordValidator.Errors.WHITESPACE.getMessage(), error.get()));
    }

    @Test
    void shouldReturnEmptyForValidPassword(){
        Optional<String> error = PasswordValidator.DEFAULT.validateAndGetError("ABCDEEEEAaa*1");
        assertFalse(error.isPresent());
    }


}