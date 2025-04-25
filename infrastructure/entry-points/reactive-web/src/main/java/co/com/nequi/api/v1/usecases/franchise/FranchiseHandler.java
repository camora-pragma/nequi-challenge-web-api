package co.com.nequi.api.v1.usecases.franchise;

import co.com.nequi.api.v1.commons.ApiResponse;
import co.com.nequi.api.v1.commons.RequestValidator;
import co.com.nequi.model.branch.Branch;
import co.com.nequi.model.franchise.Franchise;

import co.com.nequi.usecase.franchise.addbranch.AddBranchToFranchiseUseCase;
import co.com.nequi.usecase.franchise.createfranchise.CreateFranchiseUseCase;
import co.com.nequi.usecase.franchise.productswithmaxstockbyfranchise.GetFranchiseWithProductsMaxStockUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class FranchiseHandler {

    private final CreateFranchiseUseCase createFranchiseUseCase;
    private final AddBranchToFranchiseUseCase addBranchToFranchiseUseCase;
    private final GetFranchiseWithProductsMaxStockUseCase getFranchiseWithProductsMaxStockUseCase;
    private final RequestValidator requestValidator;

    public Mono<ServerResponse> createFranchise(ServerRequest request) {
        return request.bodyToMono(FranchiseRequest.class)
                .flatMap(requestValidator::validate)
                .map(this::convertToEntity)
                .flatMap(createFranchiseUseCase::apply)
                .map(this::convertToDTO)
                .map(ApiResponse::success)
                .flatMap(response -> {
                    Franchise franchise = (Franchise) response.getData();
                    URI location = URI.create("/api/v1/franchise/" + franchise.getId());
                    return ServerResponse.created(location)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(response);
                });
    }

    public Mono<ServerResponse> addBranchToFranchise(ServerRequest request) {
        Long franchiseId = Long.valueOf(request.pathVariable("franchiseId"));

        return request.bodyToMono(BranchRequest.class)
                .flatMap(requestValidator::validate)
                .map(this::convertToBranchEntity)
                .flatMap(branch -> addBranchToFranchiseUseCase.apply(franchiseId, branch))
                .map(ApiResponse::success)
                .flatMap(response -> {
                    URI location = URI.create("/api/v1/franchise/" + franchiseId + "/branches");
                    return ServerResponse.created(location)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(response);
                });
    }

    private Branch convertToBranchEntity(BranchRequest dto) {
        return Branch.builder()
                .name(dto.getName())
                .build();
    }

    private Franchise convertToEntity(FranchiseRequest dto) {
        return Franchise.builder()
                .name(dto.getName())
                .build();
    }

    private Franchise convertToDTO(Franchise franchise) {
        return Franchise.builder()
                .id(franchise.getId())
                .name(franchise.getName())
                .build();
    }

    public Mono<ServerResponse> getProductsWithMaxStockByBranch(ServerRequest serverRequest) {
        Long franchiseId = Long.valueOf(serverRequest.pathVariable("franchiseId"));

        return getFranchiseWithProductsMaxStockUseCase.apply(franchiseId)
                .flatMap(resp->
                     ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(resp)
                );
    }
}

