package co.com.nequi.api.v1.usecases.products;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductRouterRestTest {

    private WebTestClient webTestClient;

    @Mock
    private ProductHandler handler;

    @BeforeEach
    void setUp() {
        ProductRouterRest routerRest = new ProductRouterRest();

        webTestClient = WebTestClient
                .bindToRouterFunction(routerRest.productRoutes(handler))
                .build();
    }

    @Test
    void updateStockProduct_ShouldReturnCreatedResponse() {
        // Arrange
        when(handler.updateStockProduct(any())).thenReturn(ServerResponse.created(null).build());

        // Act & Assert
        webTestClient.put()
                .uri("/api/v1/product/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(UpdateStockRequest.builder().stock(10).build())
                .exchange()
                .expectStatus().isCreated();
    }
}
