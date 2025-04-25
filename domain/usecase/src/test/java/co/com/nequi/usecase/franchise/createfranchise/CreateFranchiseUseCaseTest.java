package co.com.nequi.usecase.franchise.createfranchise;

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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateFranchiseUseCaseTest {

    @Mock
    private FranchiseRepository franchiseRepository;

    @InjectMocks
    private CreateFranchiseUseCase useCase;

    private Franchise franchise;

    @BeforeEach
    void setUp(){
        franchise = Franchise.builder()
                .name("Test #1")
                .build();
    }

    @Test
    void shouldCreateFranchiseSuccessfully() {
        // Arrange
        when(franchiseRepository.createFranchise(any(Franchise.class))).thenReturn(Mono.just(franchise));

        // Act
        Mono<Franchise> result = useCase.apply(franchise);

        // Assert
        StepVerifier.create(result)
                .expectNext(franchise)
                .verifyComplete();

        verify(franchiseRepository).createFranchise(franchise);
    }

    @Test
    void shouldPropagateErrorIfRepositoryFails() {
        // Arrange
        RuntimeException exception = new RuntimeException("DB error");
        when(franchiseRepository.createFranchise(any(Franchise.class))).thenReturn(Mono.error(exception));

        // Act
        Mono<Franchise> result = useCase.apply(franchise);

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(err -> err instanceof RuntimeException &&
                        err.getMessage().equals("DB error"))
                .verify();

        verify(franchiseRepository).createFranchise(franchise);
    }
}
