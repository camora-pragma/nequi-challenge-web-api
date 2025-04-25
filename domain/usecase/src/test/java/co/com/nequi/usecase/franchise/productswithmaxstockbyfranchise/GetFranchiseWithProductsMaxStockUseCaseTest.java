package co.com.nequi.usecase.franchise.productswithmaxstockbyfranchise;

import co.com.nequi.model.branch.Branch;
import co.com.nequi.model.branch.gateways.BranchRepository;
import co.com.nequi.model.exceptions.BusinessException;
import co.com.nequi.model.franchise.Franchise;
import co.com.nequi.model.franchise.gateways.FranchiseRepository;
import co.com.nequi.model.product.Product;
import co.com.nequi.model.product.gateways.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetFranchiseWithProductsMaxStockUseCaseTest {

    @Mock
    FranchiseRepository franchiseRepository;

    @Mock
    ProductRepository productRepository;

    @Mock
    BranchRepository branchRepository;

    @InjectMocks
    GetFranchiseWithProductsMaxStockUseCase useCase;

    private Franchise franchise;
    private Branch branch;
    private Product product;

    @BeforeEach
    void setUp() {
        franchise = Franchise.builder().id(1L).name("Franchise 1").build();
        branch = Branch.builder().id(100L).name("Branch 1").build();
        product = Product.builder().id(200L).name("Product 1").stock(50).build();
    }

    @Test
    void shouldReturnFranchiseWithBranchesAndTopProducts() {
        // Arrange
        when(franchiseRepository.getFranchiseById(1L)).thenReturn(Mono.just(franchise));
        when(branchRepository.getAllByFranchise(1L)).thenReturn(Flux.just(branch));
        when(productRepository.getTopByBranchIdOrderByStockDesc(100L)).thenReturn(Mono.just(product));

        // Act
        Mono<Franchise> result = useCase.apply(1L);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(fr ->
                        fr.getId().equals(1L) &&
                                fr.getBranches() != null &&
                                fr.getBranches().getFirst().getProducts().size() == 1 &&
                                fr.getBranches().getFirst().getProducts().getFirst().getId().equals(200L)
                )
                .verifyComplete();

        verify(franchiseRepository).getFranchiseById(1L);
        verify(branchRepository).getAllByFranchise(1L);
        verify(productRepository).getTopByBranchIdOrderByStockDesc(100L);
    }

    @Test
    void shouldReturnErrorWhenFranchiseNotFound() {
        // Arrange
        when(franchiseRepository.getFranchiseById(1L)).thenReturn(Mono.empty());

        // Act
        Mono<Franchise> result = useCase.apply(1L);

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(error ->
                        error instanceof BusinessException &&
                                ((BusinessException) error).getStatus() == 404
                )
                .verify();

        verify(franchiseRepository).getFranchiseById(1L);
        verify(branchRepository, never()).getAllByFranchise(any());
        verify(productRepository, never()).getTopByBranchIdOrderByStockDesc(any());
    }

    @Test
    void shouldReturnInternalErrorOnUnexpectedFailure() {
        // Arrange
        when(franchiseRepository.getFranchiseById(1L)).thenReturn(Mono.error(new RuntimeException("Unexpected")));

        // Act
        Mono<Franchise> result = useCase.apply(1L);

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(err -> err instanceof BusinessException &&
                        ((BusinessException) err).getStatus() == 500 )
                .verify();

        verify(franchiseRepository).getFranchiseById(1L);
    }

}
