package co.com.nequi.usecase.branch.addproducttobranch;

import co.com.nequi.model.branch.Branch;
import co.com.nequi.model.branch.gateways.BranchRepository;
import co.com.nequi.model.exceptions.BusinessException;
import co.com.nequi.model.product.Product;
import co.com.nequi.model.product.ProductErrorType;
import co.com.nequi.model.product.gateways.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

class AddProductToBranchUseCaseTest {

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private AddProductToBranchUseCase useCase;

    private Branch branch;
    private Product product;
    private final Long BRANCH_ID = 1L;

    @BeforeEach
    void setUp() {
        branch = Branch.builder()
                .id(BRANCH_ID)
                .name("Test Branch")
                .franchiseId(10L)
                .build();

        product = Product.builder()
                .id(5L)
                .name("Test Product")
                .stock(50)
                .build();
    }

    @Test
    void shouldAddProductToBranchSuccessfully() {
        // Arrange
        when(branchRepository.getBranchById(BRANCH_ID)).thenReturn(Mono.just(branch));
        when(productRepository.createProduct(any(Product.class))).thenReturn(Mono.just(product));

        // Act
        Mono<Boolean> result = useCase.apply(BRANCH_ID, product);

        // Assert
        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();

        verify(branchRepository).getBranchById(BRANCH_ID);
        verify(productRepository).createProduct(any(Product.class));

        verify(productRepository).createProduct(argThat(p -> p.getBranchId().equals(BRANCH_ID)));
    }

    @Test
    void shouldThrowExceptionWhenBranchNotFound() {
        // Arrange
        when(branchRepository.getBranchById(BRANCH_ID)).thenReturn(Mono.empty());

        // Act
        Mono<Boolean> result = useCase.apply(BRANCH_ID, product);

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(error -> {
                    if (!(error instanceof BusinessException businessException)) {
                        return false;
                    }
                    return
                            businessException.getStatus() == 404;
                })
                .verify();

        verify(branchRepository).getBranchById(BRANCH_ID);
        verify(productRepository, never()).createProduct(any(Product.class));
    }

    @Test
    void shouldPropagateErrorWhenProductCreationFails() {
        // Arrange
        when(branchRepository.getBranchById(BRANCH_ID)).thenReturn(Mono.just(branch));
        when(productRepository.createProduct(any(Product.class))).thenReturn(Mono.error(new BusinessException(ProductErrorType.ERROR_SERVER)));

        // Act
        Mono<Boolean> result = useCase.apply(BRANCH_ID, product);

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(error -> error instanceof BusinessException)
                .verify();

        verify(branchRepository).getBranchById(BRANCH_ID);
        verify(productRepository).createProduct(any(Product.class));
    }

    @Test
    void shouldPropagateErrorWhenProductCreationFailsDuplicateKey() {
        // Arrange
        when(branchRepository.getBranchById(BRANCH_ID)).thenReturn(Mono.just(branch));
        when(productRepository.createProduct(any(Product.class))).thenReturn(Mono.error(new BusinessException(ProductErrorType.PROPERTY_DUPLICATE)));

        // Act
        Mono<Boolean> result = useCase.apply(BRANCH_ID, product);

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(error -> error instanceof BusinessException e && Objects.equals(e.getCode(), ProductErrorType.PROPERTY_DUPLICATE.getCode()))
                .verify();

        verify(branchRepository).getBranchById(BRANCH_ID);
        verify(productRepository).createProduct(any(Product.class));
    }

    @Test
    void shouldSetBranchIdToProductBeforeCreation() {
        // Arrange
        when(branchRepository.getBranchById(BRANCH_ID)).thenReturn(Mono.just(branch));
        when(productRepository.createProduct(any(Product.class))).thenReturn(Mono.just(product));

        product.setBranchId(null);

        // Act
        useCase.apply(BRANCH_ID, product).subscribe();

        // Assert
        verify(productRepository).createProduct(argThat(p -> p.getBranchId().equals(BRANCH_ID)));
    }
}