package co.com.nequi.usecase.franchise.addbranch;

import co.com.nequi.model.branch.Branch;
import co.com.nequi.model.branch.gateways.BranchRepository;
import co.com.nequi.model.exceptions.BusinessException;
import co.com.nequi.model.franchise.Franchise;
import co.com.nequi.model.franchise.gateways.FranchiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class AddBranchToFranchiseUseCaseTest {

    @Mock
    private FranchiseRepository franchiseRepository;

    @Mock
    private BranchRepository branchRepository;

    @InjectMocks
    private AddBranchToFranchiseUseCase useCase;

    private Branch branch;
    private Franchise franchise;
    private final Long BRANCH_ID = 1L;
    private final Long FRANCHISE_ID = 1L;

    @BeforeEach
    void setUp() {
        branch = Branch.builder()
                .id(BRANCH_ID)
                .name("Test Branch")
                .franchiseId(FRANCHISE_ID)
                .build();

        franchise = Franchise.builder()
                .id(FRANCHISE_ID)
                .name("Test Franchise")
                .build();
    }

    @Test
    void shouldAddBranchToFranchiseSuccessfully() {
        // Arrange
        when(franchiseRepository.getFranchiseById(FRANCHISE_ID)).thenReturn(Mono.just(franchise));
        when(branchRepository.createBranch(any(Branch.class))).thenReturn(Mono.just(branch));

        // Act
        Mono<Boolean> result = useCase.apply(FRANCHISE_ID, branch);

        // Assert
        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();

        verify(franchiseRepository).getFranchiseById(FRANCHISE_ID);
        verify(branchRepository).createBranch(argThat(b ->
                b.getId().equals(BRANCH_ID) && b.getFranchiseId().equals(FRANCHISE_ID)));
    }

    @Test
    void shouldFailWhenFranchiseDoesNotExist() {
        // Arrange
        when(franchiseRepository.getFranchiseById(FRANCHISE_ID)).thenReturn(Mono.empty());

        // Act
        Mono<Boolean> result = useCase.apply(FRANCHISE_ID, branch);

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(error ->
                        error instanceof BusinessException &&
                                ((BusinessException) error).getStatus() == 404
                )
                .verify();

        verify(franchiseRepository).getFranchiseById(FRANCHISE_ID);

    }

    @Test
    void shouldFailIfCreateBranchFails() {
        // Arrange
        when(franchiseRepository.getFranchiseById(FRANCHISE_ID)).thenReturn(Mono.just(franchise));
        when(branchRepository.createBranch(any())).thenReturn(Mono.error(new RuntimeException("DB error")));

        // Act
        Mono<Boolean> result = useCase.apply(FRANCHISE_ID, branch);

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(error -> error instanceof RuntimeException &&
                        error.getMessage().equals("DB error"))
                .verify();

        verify(franchiseRepository).getFranchiseById(FRANCHISE_ID);
        verify(branchRepository).createBranch(any());
    }

}
