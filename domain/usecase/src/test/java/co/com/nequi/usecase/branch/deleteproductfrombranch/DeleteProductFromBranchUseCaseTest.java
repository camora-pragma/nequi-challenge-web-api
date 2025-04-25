package co.com.nequi.usecase.branch.deleteproductfrombranch;

import co.com.nequi.model.branch.Branch;
import co.com.nequi.model.branch.gateways.BranchRepository;
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
public class DeleteProductFromBranchUseCaseTest {
    @Mock
    private BranchRepository branchRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private DeleteProductFromBranchUseCase useCase;


    private Branch branch;
    private final Long BRANCH_ID = 1L;
    private final Long PRODUCT_ID = 1L;

    @BeforeEach
    void setUp() {
        branch = Branch.builder()
                .id(BRANCH_ID)
                .name("Test Branch")
                .franchiseId(10L)
                .build();
    }

    @Test
    void shouldDeleteProductSuccessfully() {
        // Arrange
        when(branchRepository.getBranchById(BRANCH_ID)).thenReturn(Mono.just(branch));
        when(productRepository.deleteProductByBranch(BRANCH_ID, PRODUCT_ID))
                .thenReturn(Mono.just(true));


        // Act
        Mono<Boolean> result = useCase.apply(PRODUCT_ID,BRANCH_ID);

        // Assert
        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();

        verify(branchRepository).getBranchById(BRANCH_ID);
        verify(productRepository).deleteProductByBranch(BRANCH_ID, PRODUCT_ID);

    }


    @Test
    void shouldThrowExceptionWhenBranchNotFound() {
        // Arrange
        when(branchRepository.getBranchById(BRANCH_ID)).thenReturn(Mono.empty());

        // Act
        Mono<Boolean> result = useCase.apply(PRODUCT_ID,BRANCH_ID);

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
}
