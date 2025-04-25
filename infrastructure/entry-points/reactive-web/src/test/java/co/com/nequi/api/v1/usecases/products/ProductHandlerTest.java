package co.com.nequi.api.v1.usecases.products;


import co.com.nequi.api.v1.commons.RequestValidator;
import co.com.nequi.model.product.Product;
import co.com.nequi.usecase.product.UpdateStockProductUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductHandlerTest {

    @Mock
    private UpdateStockProductUseCase updateStockProductUseCase;

    @Mock
    private RequestValidator requestValidator;

    @InjectMocks
    private ProductHandler productHandler;

    private ServerRequest mockServerRequest;

    @BeforeEach
    void setUp() {
        mockServerRequest = mock(ServerRequest.class);
    }

    @Test
    void updateStockProduct_ShouldReturnOkResponse() {
        // Arrange
        Long productId = 99L;
        UpdateStockRequest request = UpdateStockRequest.builder().stock(5).build();


        when(mockServerRequest.pathVariable("productId")).thenReturn(productId.toString());
        when(mockServerRequest.bodyToMono(UpdateStockRequest.class)).thenReturn(Mono.just(request));
        when(requestValidator.validate(any(UpdateStockRequest.class))).thenReturn(Mono.just(request));
        when(updateStockProductUseCase.apply(eq(productId), eq(5))).thenReturn(Mono.just(Product.builder().stock(5).build()));

        // Act
        Mono<ServerResponse> response = productHandler.updateStockProduct(mockServerRequest);

        // Assert
        StepVerifier.create(response)
                .consumeNextWith(serverResponse ->
                        assertThat(serverResponse.statusCode().value()).isEqualTo(200))
                .verifyComplete();

        verify(updateStockProductUseCase).apply(eq(productId), eq(5));
    }

    @Test
    void updateStockProduct_WhenValidationFails_ShouldPropagateError() {
        // Arrange
        Long productId = 99L;
        UpdateStockRequest request = UpdateStockRequest.builder().stock(5).build();
        RuntimeException validationError = new RuntimeException("Invalid stock");

        when(mockServerRequest.pathVariable("productId")).thenReturn(productId.toString());
        when(mockServerRequest.bodyToMono(UpdateStockRequest.class)).thenReturn(Mono.just(request));
        when(requestValidator.validate(any(UpdateStockRequest.class))).thenReturn(Mono.error(validationError));

        // Act
        Mono<ServerResponse> response = productHandler.updateStockProduct(mockServerRequest);

        // Assert
        StepVerifier.create(response)
                .expectErrorMatches(e -> e.getMessage().equals("Invalid stock"))
                .verify();
    }

    @Test
    void updateStockProduct_WhenUseCaseFails_ShouldPropagateError() {
        // Arrange
        Long productId = 99L;
        UpdateStockRequest request = UpdateStockRequest.builder().stock(5).build();
        RuntimeException useCaseError = new RuntimeException("Stock update failed");

        when(mockServerRequest.pathVariable("productId")).thenReturn(productId.toString());
        when(mockServerRequest.bodyToMono(UpdateStockRequest.class)).thenReturn(Mono.just(request));
        when(requestValidator.validate(any(UpdateStockRequest.class))).thenReturn(Mono.just(request));
        when(updateStockProductUseCase.apply(eq(productId), eq(5))).thenReturn(Mono.error(useCaseError));

        // Act
        Mono<ServerResponse> response = productHandler.updateStockProduct(mockServerRequest);

        // Assert
        StepVerifier.create(response)
                .expectErrorMatches(e -> e.getMessage().equals("Stock update failed"))
                .verify();
    }
}
