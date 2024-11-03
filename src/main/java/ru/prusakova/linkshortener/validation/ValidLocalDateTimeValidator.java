package ru.prusakova.linkshortener.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

public class ValidLocalDateTimeValidator implements ConstraintValidator<ValidLocalDateTime, String> {

    public static final String DATETIME_PATTERN = "^[0-9]{4}-(0[1-9]|1[012])-(0[1-9]|1[0-9]|2[0-9]|3[01])T([0-1]\\d|2[0-3])(:[0-5]\\d){2}";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (!StringUtils.hasText(value)) {
            return false;
        }

        if(value.matches(DATETIME_PATTERN)) {
            return LocalDateTime.parse(value).isAfter(LocalDateTime.now());
        }

        return false;
    }
}
