package ru.prusakova.linkshortener.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

public class ValidLocalDateTimeValidator implements ConstraintValidator<ValidLocalDateTime, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (!StringUtils.hasText(value)) {
            return true;
        }

        try {
            return LocalDateTime.parse(value).isAfter(LocalDateTime.now());
        } catch(Exception e) {
            return false;
        }
    }
}
