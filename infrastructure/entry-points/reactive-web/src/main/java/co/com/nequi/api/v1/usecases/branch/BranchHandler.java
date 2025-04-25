package co.com.nequi.api.v1.usecases.branch;

import co.com.nequi.api.v1.commons.ApiResponse;
import co.com.nequi.api.v1.commons.RequestValidator;
import co.com.nequi.model.product.Product;
import co.com.nequi.usecase.branch.addproducttobranch.AddProductToBranchUseCase;
import co.com.nequi.usecase.branch.deleteproductfrombranch.DeleteProductFromBranchUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class BranchHandler {

    private final AddProductToBranchUseCase addProductToBranchUseCase;
    private final DeleteProductFromBranchUseCase deleteProductFromBranchUseCase;
    private final RequestValidator requestValidator;



    public Mono<ServerResponse> addProductToBranch(ServerRequest request) {
        Long branchId = Long.valueOf(request.pathVariable("branchId"));

        return request.bodyToMono(ProductRequest.class)
                .flatMap(requestValidator::validate)
                .map(this::convertToBranchEntity)
                .flatMap(product -> addProductToBranchUseCase.apply(branchId, product))
                .map(ApiResponse::success)
                .flatMap(response -> {
                    URI location = URI.create("/api/v1/branch/" + branchId + "/product");
                    return ServerResponse.created(location)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(response);
                });
    }

    public Mono<ServerResponse> deleteProductFromBranch(ServerRequest request) {
        Long branchId = Long.valueOf(request.pathVariable("branchId"));
        Long productId = Long.valueOf(request.pathVariable("productId"));

        return deleteProductFromBranchUseCase.apply(productId, branchId)
                .map(ApiResponse::success)
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response))
                ;
    }

    private Product convertToBranchEntity(ProductRequest dto) {
        return Product.builder()
                .name(dto.getName()).stock(dto.getStock())
                .build();
    }
}

