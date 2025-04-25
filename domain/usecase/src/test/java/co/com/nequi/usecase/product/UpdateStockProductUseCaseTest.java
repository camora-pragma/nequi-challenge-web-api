package co.com.nequi.usecase.product;

import co.com.nequi.model.exceptions.BusinessException;
import co.com.nequi.model.product.Product;
import co.com.nequi.model.product.gateways.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateStockProductUseCaseTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private UpdateStockProductUseCase useCase;

    private Product existingProduct;
    private final Long PRODUCT_ID = 1L;
    private final Integer NEW_STOCK = 10;

    @BeforeEach
    void setUp() {
        existingProduct = Product.builder()
                .id(PRODUCT_ID)
                .name("Sample Product")
                .stock(5)
                .build();
    }

    @Test
    void shouldUpdateStockSuccessfully() {
        // Arrange
        when(productRepository.getProductById(PRODUCT_ID)).thenReturn(Mono.just(existingProduct));
        when(productRepository.updateProductStock(any(Product.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        // Act
        Mono<Product> result = useCase.apply(PRODUCT_ID, NEW_STOCK);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(product -> product.getStock().equals(NEW_STOCK))
                .verifyComplete();

        verify(productRepository).getProductById(PRODUCT_ID);
        verify(productRepository).updateProductStock(existingProduct);
    }

    @Test
    void shouldReturnErrorWhenProductNotFound() {
        // Arrange
        when(productRepository.getProductById(PRODUCT_ID)).thenReturn(Mono.empty());

        // Act
        Mono<Product> result = useCase.apply(PRODUCT_ID, NEW_STOCK);

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(error ->
                        error instanceof BusinessException &&
                                ((BusinessException) error).getStatus() == 404
                )
                .verify();
        verify(productRepository).getProductById(PRODUCT_ID);
        verify(productRepository, never()).updateProductStock(any());
    }
}
