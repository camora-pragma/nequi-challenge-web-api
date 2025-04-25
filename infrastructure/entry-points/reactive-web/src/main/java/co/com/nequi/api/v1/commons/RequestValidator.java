package co.com.nequi.api.v1.commons;


import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class RequestValidator {

    private final Validator validator;

    public <T> Mono<T> validate(T request) {
        Errors errors = new BeanPropertyBindingResult(request, request.getClass().getName());
        validator.validate(request, errors);

        if (errors.hasErrors()) {
            String errorMessage = errors.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));

            return Mono.error(new ValidationException("Validation failed: " + errorMessage));
        }

        return Mono.just(request);
    }
}