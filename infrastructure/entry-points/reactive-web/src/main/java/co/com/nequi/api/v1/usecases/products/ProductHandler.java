package co.com.nequi.api.v1.usecases.products;

import co.com.nequi.api.v1.commons.ApiResponse;
import co.com.nequi.api.v1.commons.RequestValidator;
import co.com.nequi.usecase.product.UpdateStockProductUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
@Component
@RequiredArgsConstructor
public class ProductHandler {

    private final UpdateStockProductUseCase updateStockProductUseCase;
    private final RequestValidator requestValidator;
    public Mono<ServerResponse> updateStockProduct(ServerRequest serverRequest) {

        Long productId = Long.valueOf(serverRequest.pathVariable("productId"));

        return serverRequest.bodyToMono(UpdateStockRequest.class)
                .flatMap(requestValidator::validate)
                .flatMap(stockRequest -> updateStockProductUseCase.apply(productId, stockRequest.getStock()))
                .map(ApiResponse::success)
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response));
    }
}
