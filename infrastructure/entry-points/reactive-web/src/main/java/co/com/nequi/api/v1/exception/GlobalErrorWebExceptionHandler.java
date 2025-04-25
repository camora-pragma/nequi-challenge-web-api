package co.com.nequi.api.v1.exception;


import co.com.nequi.api.v1.commons.ApiResponse;
import co.com.nequi.model.exceptions.BusinessException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Component
@Order(-2)
@Slf4j
public class GlobalErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {

    public GlobalErrorWebExceptionHandler(ErrorAttributes errorAttributes,
                                          WebProperties.Resources resources,
                                          ApplicationContext applicationContext,
                                          ServerCodecConfigurer configurer) {
        super(errorAttributes, resources, applicationContext);
        this.setMessageWriters(configurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Throwable error = getError(request);
        log.error("Error handling request: {}", request.path(), error);

        return switch (error) {
            case BusinessException businessException -> handleBusinessException(businessException);
            case WebExchangeBindException webExchangeBindException -> handleValidationErrors(webExchangeBindException);
            case ServerWebInputException serverWebInputException ->
                    handleServerWebInputException(serverWebInputException);
            case ValidationException validationException -> handleConstraintViolation(validationException);
            case ResponseStatusException responseStatusException ->
                    handleResponseStatusException(responseStatusException);

            case null, default -> handleUnknownError(error);
        };
    }

    private Mono<ServerResponse> handleBusinessException(BusinessException ex) {
        return ServerResponse.status(ex.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(
                        ApiResponse.error(
                                ex.getStatus(),
                                ex.getMessage(),
                                List.of("code: " + ex.getCode())
                        )
                ));
    }

    private Mono<ServerResponse> handleValidationErrors(WebExchangeBindException ex) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.add(error.getField()+ ": " +  error.getDefaultMessage())
        );

        return ServerResponse.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(
                        ApiResponse.error(
                                HttpStatus.BAD_REQUEST.value(),
                                "Validation Failed",
                                errors
                        )
                ));
    }

    private Mono<ServerResponse> handleConstraintViolation(ValidationException ex) {

        return ServerResponse.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(
                        ApiResponse.error(
                                HttpStatus.BAD_REQUEST.value(),
                                "Validation Failed",
                                List.of(ex.getMessage())
                        )
                ));
    }

    private Mono<ServerResponse> handleServerWebInputException(ServerWebInputException ex) {
        return ServerResponse.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(
                        ApiResponse.error(
                                HttpStatus.BAD_REQUEST.value(),
                                "Invalid Request",
                                List.of(ex.getReason() != null ? ex.getReason() : null)
                        )
                ));
    }

    private Mono<ServerResponse> handleResponseStatusException(ResponseStatusException ex) {
        return ServerResponse.status(ex.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(
                        ApiResponse.error(
                                ex.getStatusCode().value(),
                                ex.getReason(),
                                null
                        )
                ));
    }

    private Mono<ServerResponse> handleUnknownError(Throwable error) {
        log.error("Unhandled exception", error);
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(
                        ApiResponse.error(
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                "Internal Server Error",
                                List.of(error.getMessage())
                        )
                ));
    }
}

