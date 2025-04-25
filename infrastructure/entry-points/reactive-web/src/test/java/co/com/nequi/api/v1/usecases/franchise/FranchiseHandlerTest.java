package co.com.nequi.api.v1.usecases.franchise;

import co.com.nequi.api.v1.commons.RequestValidator;
import co.com.nequi.model.branch.Branch;
import co.com.nequi.model.franchise.Franchise;
import co.com.nequi.usecase.franchise.addbranch.AddBranchToFranchiseUseCase;
import co.com.nequi.usecase.franchise.createfranchise.CreateFranchiseUseCase;
import co.com.nequi.usecase.franchise.productswithmaxstockbyfranchise.GetFranchiseWithProductsMaxStockUseCase;
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
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class FranchiseHandlerTest {

    @Mock
    private CreateFranchiseUseCase createFranchiseUseCase;

    @Mock
    private AddBranchToFranchiseUseCase addBranchToFranchiseUseCase;

    @Mock
    private GetFranchiseWithProductsMaxStockUseCase getFranchiseWithProductsMaxStockUseCase;

    @Mock
    private RequestValidator requestValidator;

    @InjectMocks
    private FranchiseHandler franchiseHandler;

    private ServerRequest mockServerRequest;

    @BeforeEach
    void setUp() {
        mockServerRequest = mock(ServerRequest.class);
    }

    @Test
    void createFranchise_ShouldReturnCreatedResponse() {
        // Arrange
        FranchiseRequest request = new FranchiseRequest();
        request.setName("Nequi Franchise");

        Franchise created = Franchise.builder()
                .id(1L)
                .name("Nequi Franchise")
                .build();

        when(mockServerRequest.bodyToMono(FranchiseRequest.class)).thenReturn(Mono.just(request));
        when(requestValidator.validate(any(FranchiseRequest.class))).thenReturn(Mono.just(request));
        when(createFranchiseUseCase.apply(any(Franchise.class))).thenReturn(Mono.just(created));

        // Act
        Mono<ServerResponse> responseMono = franchiseHandler.createFranchise(mockServerRequest);

        // Assert
        StepVerifier.create(responseMono)
                .consumeNextWith(response ->
                        assertThat(response.statusCode().value()).isEqualTo(201))
                .verifyComplete();

        verify(createFranchiseUseCase).apply(any(Franchise.class));
    }

    @Test
    void createFranchise_WhenValidationFails_ShouldPropagateError() {
        // Arrange
        FranchiseRequest request = new FranchiseRequest();
        RuntimeException validationException = new RuntimeException("Invalid franchise");

        when(mockServerRequest.bodyToMono(FranchiseRequest.class)).thenReturn(Mono.just(request));
        when(requestValidator.validate(any(FranchiseRequest.class))).thenReturn(Mono.error(validationException));

        // Act
        Mono<ServerResponse> response = franchiseHandler.createFranchise(mockServerRequest);

        // Assert
        StepVerifier.create(response)
                .expectErrorMatches(ex -> ex instanceof RuntimeException && ex.getMessage().equals("Invalid franchise"))
                .verify();
    }

    @Test
    void getProductsWithMaxStockByBranch_WhenUseCaseFails_ShouldPropagateError() {
        // Arrange
        Long franchiseId = 42L;
        RuntimeException useCaseException = new RuntimeException("Franchise not found");

        when(mockServerRequest.pathVariable("franchiseId")).thenReturn(franchiseId.toString());
        when(getFranchiseWithProductsMaxStockUseCase.apply(eq(franchiseId)))
                .thenReturn(Mono.error(useCaseException));

        // Act
        Mono<ServerResponse> response = franchiseHandler.getProductsWithMaxStockByBranch(mockServerRequest);

        // Assert
        StepVerifier.create(response)
                .expectErrorMatches(ex -> ex instanceof RuntimeException && ex.getMessage().equals("Franchise not found"))
                .verify();
    }

    @Test
    void addBranchToFranchise_ShouldMapRequestToEntityAndCallUseCase() {
        // Arrange
        Long franchiseId = 100L;
        BranchRequest branchRequest = new BranchRequest();
        branchRequest.setName("Sucursal Medellín");

        when(mockServerRequest.pathVariable("franchiseId")).thenReturn(franchiseId.toString());
        when(mockServerRequest.bodyToMono(BranchRequest.class)).thenReturn(Mono.just(branchRequest));
        when(requestValidator.validate(any(BranchRequest.class))).thenReturn(Mono.just(branchRequest));
        when(addBranchToFranchiseUseCase.apply(eq(franchiseId), any(Branch.class))).thenReturn(Mono.just(Boolean.TRUE));

        // Act
        Mono<ServerResponse> response = franchiseHandler.addBranchToFranchise(mockServerRequest);

        // Assert
        StepVerifier.create(response)
                .consumeNextWith(serverResponse ->
                        assertThat(serverResponse.statusCode().value()).isEqualTo(201))
                .verifyComplete();

        verify(addBranchToFranchiseUseCase).apply(eq(franchiseId), argThat(branch ->
                branch.getName().equals("Sucursal Medellín")
        ));
    }
    @Test
    void getProductsWithMaxStockByBranch_ShouldReturnOkWithResponse() {
        // Arrange
        Long franchiseId = 42L;


        when(mockServerRequest.pathVariable("franchiseId")).thenReturn(franchiseId.toString());
        when(getFranchiseWithProductsMaxStockUseCase.apply(eq(franchiseId)))
                .thenReturn(Mono.just((Franchise.builder().build())));

        // Act
        Mono<ServerResponse> response = franchiseHandler.getProductsWithMaxStockByBranch(mockServerRequest);

        // Assert
        StepVerifier.create(response)
                .consumeNextWith(serverResponse -> {
                    assertThat(serverResponse.statusCode().value()).isEqualTo(200);
                })
                .verifyComplete();

        verify(getFranchiseWithProductsMaxStockUseCase).apply(franchiseId);
    }


}