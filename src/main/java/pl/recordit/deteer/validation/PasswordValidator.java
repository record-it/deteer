package pl.recordit.deteer.validation;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public enum PasswordValidator {
    DEFAULT(Arrays.asList(
            new Rule(Objects::nonNull, Errors.NULL),
            new Rule(password -> !password.isEmpty(), Errors.EMPTY),
            new Rule(password -> password.length() >= 8, Errors.NO_VALID_LENGTH),
            new Rule(password -> password.chars().anyMatch(Character::isDigit), Errors.NO_DIGIT),
            new Rule(password -> password.chars().anyMatch(Character::isLowerCase), Errors.NO_LOWER_CASE_CHAR),
            new Rule(password -> password.chars().anyMatch(Character::isUpperCase), Errors.NO_UPPER_CASE_CHAR),
            new Rule(password -> password.chars().anyMatch(c -> !Character.isLetterOrDigit(c)), Errors.NO_SPECIAL_CHAR),
            new Rule(password -> password.chars().noneMatch(Character::isWhitespace), Errors.WHITESPACE)
    ));

    public enum Errors {
        NULL("Hasło nie może być null!"),
        EMPTY("Hasło nie może być puste!"),
        NO_VALID_LENGTH("Hasło musi się składać z co najmniej ośmiu znaków!"),
        NO_DIGIT("Hasło musi zawierać cyfrę!"),
        NO_LOWER_CASE_CHAR("Hasło musi zawierać małą literę!"),
        NO_UPPER_CASE_CHAR("Hasło musi zawierać dużą literę!"),
        NO_SPECIAL_CHAR("Hasło musi zawierać znak nie będący literą ani cyfrą!"),
        WHITESPACE("Hasło nie może zawierać spacji!");

        private final String  message;

        Errors(String message){
            this.message = message;
        }

        public String getMessage(){
            return message;
        }
    }

    private static class Rule{

        private final Predicate<String> predicate;

        private final Errors messageOnFalse;

        Rule(Predicate<String> predicate, Errors messageOnFalse) {
            this.predicate = predicate;
            this.messageOnFalse = messageOnFalse;
        }
    }

    private final List<Rule> rules;

    PasswordValidator(List<Rule> rules){
        this.rules = rules;
    }

    public Optional<String> validateAndGetError(String password){
        return rules.stream()
                .filter(rule -> rule.predicate.negate().test(password))
                .findAny()
                .flatMap(rule -> Optional.of(rule.messageOnFalse.message));
    }
}
