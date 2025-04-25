package co.com.nequi.api.v1.usecases.franchise;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FranchiseRouterRestTest {

    private WebTestClient webTestClient;

    @Mock
    private FranchiseHandler franchiseHandler;

    @BeforeEach
    void setUp() {
        FranchiseRouterRest routerRest = new FranchiseRouterRest();


        webTestClient = WebTestClient
                .bindToRouterFunction(routerRest.franchiseRoutes(franchiseHandler))
                .build();
    }

    @Test
    void createFranchiseRoute_ShouldExist() {
        when(franchiseHandler.createFranchise(any())).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/api/v1/franchise")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void addBranchRoute_ShouldExist() {

        when(franchiseHandler.addBranchToFranchise(any())).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/api/v1/franchise/1/branch")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void getProductsWithMaxStockByBranch_ShouldExist() {

        when(franchiseHandler.getProductsWithMaxStockByBranch(any())).thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/api/v1/franchise/1/branches/products/max-stock")
                .exchange()
                .expectStatus().isOk();
    }
}