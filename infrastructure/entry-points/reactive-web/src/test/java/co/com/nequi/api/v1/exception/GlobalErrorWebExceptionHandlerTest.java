package co.com.nequi.api.v1.exception;


import co.com.nequi.model.exceptions.BusinessException;
import co.com.nequi.model.franchise.FranchiseErrorType;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GlobalErrorWebExceptionHandlerTest {

    private GlobalErrorWebExceptionHandler handler;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private WebProperties.Resources resources;

    @Mock
    private ServerCodecConfigurer serverCodecConfigurer;

    @Mock
    private ErrorAttributes errorAttributes;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(applicationContext.getClassLoader()).thenReturn(getClass().getClassLoader());

        handler = new GlobalErrorWebExceptionHandler(errorAttributes, resources, applicationContext, serverCodecConfigurer);
    }

    @Test
    void testHandleBusinessException() {
        // Arrange
        BusinessException businessException = new BusinessException(FranchiseErrorType.FRANCHISE_ERROR_SERVER);

        ServerRequest serverRequest = mock(ServerRequest.class);
        when(serverRequest.path()).thenReturn("/test-path");

        when(errorAttributes.getError(any())).thenReturn(businessException);

        // Act
        Mono<ServerResponse> response = handler.renderErrorResponse(serverRequest);

        // Assert
        assertTrue(response.block() instanceof ServerResponse);
        ServerResponse serverResponse = response.block();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), serverResponse.statusCode().value());
        assertEquals(MediaType.APPLICATION_JSON, serverResponse.headers().getContentType());
    }

    @Test
    void testHandleWebExchangeBindException() {
        WebExchangeBindException bindException = mock(WebExchangeBindException.class);
        when(bindException.getBindingResult()).thenReturn(new BeanPropertyBindingResult(new Object(), "object"));

        ServerRequest serverRequest = mock(ServerRequest.class);
        when(serverRequest.path()).thenReturn("/test-validation");
        when(errorAttributes.getError(any())).thenReturn(bindException);

        Mono<ServerResponse> response = handler.renderErrorResponse(serverRequest);

        ServerResponse serverResponse = response.block();
        assertEquals(HttpStatus.BAD_REQUEST.value(), serverResponse.statusCode().value());
        assertEquals(MediaType.APPLICATION_JSON, serverResponse.headers().getContentType());
    }

    @Test
    void testHandleServerWebInputException() {
        ServerWebInputException inputException = new ServerWebInputException("Invalid input");

        ServerRequest serverRequest = mock(ServerRequest.class);
        when(serverRequest.path()).thenReturn("/test-web-input");
        when(errorAttributes.getError(any())).thenReturn(inputException);

        Mono<ServerResponse> response = handler.renderErrorResponse(serverRequest);

        ServerResponse serverResponse = response.block();
        assertEquals(HttpStatus.BAD_REQUEST.value(), serverResponse.statusCode().value());
        assertEquals(MediaType.APPLICATION_JSON, serverResponse.headers().getContentType());
    }

    @Test
    void testHandleValidationException() {
        ValidationException validationException = new ValidationException("violación de restricción");

        ServerRequest serverRequest = mock(ServerRequest.class);
        when(serverRequest.path()).thenReturn("/test-validation-ex");
        when(errorAttributes.getError(any())).thenReturn(validationException);

        Mono<ServerResponse> response = handler.renderErrorResponse(serverRequest);

        ServerResponse serverResponse = response.block();
        assertEquals(HttpStatus.BAD_REQUEST.value(), serverResponse.statusCode().value());
        assertEquals(MediaType.APPLICATION_JSON, serverResponse.headers().getContentType());
    }

    @Test
    void testHandleResponseStatusException() {
        ResponseStatusException statusException = new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");

        ServerRequest serverRequest = mock(ServerRequest.class);
        when(serverRequest.path()).thenReturn("/test-status-ex");
        when(errorAttributes.getError(any())).thenReturn(statusException);

        Mono<ServerResponse> response = handler.renderErrorResponse(serverRequest);

        ServerResponse serverResponse = response.block();
        assertEquals(HttpStatus.NOT_FOUND.value(), serverResponse.statusCode().value());
        assertEquals(MediaType.APPLICATION_JSON, serverResponse.headers().getContentType());
    }

    @Test
    void testHandleUnknownError() {
        RuntimeException unknown = new RuntimeException("Unexpected error");

        ServerRequest serverRequest = mock(ServerRequest.class);
        when(serverRequest.path()).thenReturn("/test-unknown");
        when(errorAttributes.getError(any())).thenReturn(unknown);

        Mono<ServerResponse> response = handler.renderErrorResponse(serverRequest);

        ServerResponse serverResponse = response.block();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), serverResponse.statusCode().value());
        assertEquals(MediaType.APPLICATION_JSON, serverResponse.headers().getContentType());
    }

}
