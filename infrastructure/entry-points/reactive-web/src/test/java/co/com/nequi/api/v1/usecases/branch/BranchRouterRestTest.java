package co.com.nequi.api.v1.usecases.branch;

import org.mockito.Mockito;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.mockito.ArgumentMatchers.any;

public class BranchRouterRestTest {

    private BranchHandler branchHandler;
    private WebTestClient webTestClient;

    @BeforeEach
    void setup() {
        branchHandler = Mockito.mock(BranchHandler.class);

        BranchRouterRest router = new BranchRouterRest();
        webTestClient = WebTestClient.bindToRouterFunction(router.branchRoutes(branchHandler))
                .configureClient()
                .baseUrl("/api/v1")
                .build();
    }

    @Test
    void addProductToBranch_shouldReturn201() {
        Mockito.when(branchHandler.addProductToBranch(any()))
                .thenReturn(ServerResponse.created(null).build());

        ProductRequest request = new ProductRequest();

        webTestClient.post()
                .uri("/branch/123/product")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    void deleteProductFromBranch_shouldReturn201() {
        Mockito.when(branchHandler.deleteProductFromBranch(any()))
                .thenReturn(ServerResponse.created(null).build());

        webTestClient.delete()
                .uri("/branch/123/product/456")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated();
    }
}
