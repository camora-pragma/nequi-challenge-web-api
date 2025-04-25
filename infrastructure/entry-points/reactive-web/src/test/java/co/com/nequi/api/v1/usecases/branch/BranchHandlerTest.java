package co.com.nequi.api.v1.usecases.branch;

import co.com.nequi.api.v1.commons.RequestValidator;
import co.com.nequi.model.product.Product;
import co.com.nequi.usecase.branch.addproducttobranch.AddProductToBranchUseCase;
import co.com.nequi.usecase.branch.deleteproductfrombranch.DeleteProductFromBranchUseCase;
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
class BranchHandlerTest {

    @Mock
    private AddProductToBranchUseCase addProductToBranchUseCase;

    @Mock
    private DeleteProductFromBranchUseCase deleteProductFromBranchUseCase;

    @Mock
    private RequestValidator requestValidator;

    @InjectMocks
    private BranchHandler branchHandler;

    private ServerRequest mockServerRequest;

    @BeforeEach
    void setUp() {
        mockServerRequest = mock(ServerRequest.class);
    }

    @Test
    void addProductToBranch_ShouldReturnCreatedResponse() {
        // Arrange
        Long branchId = 123L;
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("Test Product");
        productRequest.setStock(10);


        when(mockServerRequest.pathVariable("branchId")).thenReturn(branchId.toString());
        when(mockServerRequest.bodyToMono(ProductRequest.class)).thenReturn(Mono.just(productRequest));
        when(requestValidator.validate(any(ProductRequest.class))).thenReturn(Mono.just(productRequest));
        when(addProductToBranchUseCase.apply(eq(branchId), any(Product.class))).thenReturn(Mono.just(true));

        // Act
        Mono<ServerResponse> response = branchHandler.addProductToBranch(mockServerRequest);

        // Assert
        StepVerifier.create(response)
                .consumeNextWith(serverResponse -> {
                    assertThat(serverResponse.statusCode().value()).isEqualTo(201);
                })
                .verifyComplete();

        verify(addProductToBranchUseCase).apply(eq(branchId), any(Product.class));
    }

    @Test
    void deleteProductFromBranch_ShouldReturnOkResponse() {
        // Arrange
        Long branchId = 123L;
        Long productId = 456L;

        when(mockServerRequest.pathVariable("branchId")).thenReturn(branchId.toString());
        when(mockServerRequest.pathVariable("productId")).thenReturn(productId.toString());
        when(deleteProductFromBranchUseCase.apply(eq(productId), eq(branchId))).thenReturn(Mono.just(Boolean.TRUE));

        // Act
        Mono<ServerResponse> response = branchHandler.deleteProductFromBranch(mockServerRequest);

        // Assert
        StepVerifier.create(response)
                .consumeNextWith(serverResponse -> {
                    assertThat(serverResponse.statusCode().value()).isEqualTo(200);

                })
                .verifyComplete();

        verify(deleteProductFromBranchUseCase).apply(eq(productId), eq(branchId));
    }

    @Test
    void addProductToBranch_WhenValidationFails_ShouldPropagateError() {
        // Arrange
        Long branchId = 123L;
        ProductRequest productRequest = new ProductRequest();

        RuntimeException validationException = new RuntimeException("Validation failed");

        when(mockServerRequest.pathVariable("branchId")).thenReturn(branchId.toString());
        when(mockServerRequest.bodyToMono(ProductRequest.class)).thenReturn(Mono.just(productRequest));
        when(requestValidator.validate(any(ProductRequest.class))).thenReturn(Mono.error(validationException));

        // Act
        Mono<ServerResponse> response = branchHandler.addProductToBranch(mockServerRequest);

        // Assert
        StepVerifier.create(response)
                .expectErrorMatches(ex -> ex instanceof RuntimeException && ex.getMessage().equals("Validation failed"))
                .verify();
    }

    @Test
    void deleteProductFromBranch_WhenUseCaseFails_ShouldPropagateError() {
        // Arrange
        Long branchId = 123L;
        Long productId = 456L;

        RuntimeException useCaseException = new RuntimeException("Product not found");

        when(mockServerRequest.pathVariable("branchId")).thenReturn(branchId.toString());
        when(mockServerRequest.pathVariable("productId")).thenReturn(productId.toString());
        when(deleteProductFromBranchUseCase.apply(eq(productId), eq(branchId))).thenReturn(Mono.error(useCaseException));

        // Act
        Mono<ServerResponse> response = branchHandler.deleteProductFromBranch(mockServerRequest);

        // Assert
        StepVerifier.create(response)
                .expectErrorMatches(ex -> ex instanceof RuntimeException && ex.getMessage().equals("Product not found"))
                .verify();
    }
}