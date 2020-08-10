package pl.recordit.deteer.validation;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public enum PasswordValidator {
    DEFAULT(Arrays.asList(
            new Rule(Objects::nonNull, "Hasło nie może być null!"),
            new Rule(password -> !password.isBlank(), "Hasło nie może być puste"),
            new Rule(password -> password.length() >= 8, "Hasło musi się składać z co najmniej ośmiu znaków!"),
            new Rule(password -> password.chars().anyMatch(Character::isDigit), "Hasło musi zawierać cyfrę!"),
            new Rule(password -> password.chars().anyMatch(Character::isLowerCase), "Hasło musi zawierać małą literę!"),
            new Rule(password -> password.chars().anyMatch(Character::isUpperCase),"Hasło musi zawierać dużą literę!"),
            new Rule(password -> password.chars().anyMatch(c -> !Character.isLetterOrDigit(c)),"Hasło musi zawierać znak nie będący literą ani cyfrą!"),
            new Rule(password -> password.chars().noneMatch(Character::isWhitespace), "Hasło nie może zawierać spacji!")
    ));

    private static class Rule{

        private final Predicate<String> predicate;

        private final String messageOnFalse;

        Rule(Predicate<String> predicate, String messageOnFalse) {
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
                .flatMap(rule -> Optional.of(rule.messageOnFalse));
    }
}
