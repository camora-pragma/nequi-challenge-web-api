package co.com.nequi.usecase.franchise.productswithmaxstockbyfranchise;

import co.com.nequi.model.branch.gateways.BranchRepository;
import co.com.nequi.model.exceptions.BusinessException;
import co.com.nequi.model.franchise.Franchise;
import co.com.nequi.model.franchise.FranchiseErrorType;
import co.com.nequi.model.franchise.gateways.FranchiseRepository;
import co.com.nequi.model.product.gateways.ProductRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class GetFranchiseWithProductsMaxStockUseCase {

    private final FranchiseRepository franchiseRepository;
    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;


    public Mono<Franchise> apply(Long id) {
        return franchiseRepository.getFranchiseById(id)
                .switchIfEmpty(Mono.error(new BusinessException(FranchiseErrorType.FRANCHISE_NOT_FOUND)))
                .flatMap(franchise ->
                        branchRepository.getAllByFranchise(id)
                                .flatMap(branch ->
                                        productRepository.getTopByBranchIdOrderByStockDesc(branch.getId())
                                                .collectList()  // Collect products into a list
                                                .map(products -> {
                                                    branch.setProducts(products);
                                                    return branch;
                                                })
                                )
                                .collectList()
                                .map(branches -> {
                                    franchise.setBranches(branches);
                                    return franchise;
                                })
                )
                .onErrorResume(ex -> {
                    if (ex instanceof BusinessException) return Mono.error(ex);
                    return Mono.error(new BusinessException(FranchiseErrorType.FRANCHISE_ERROR_SERVER));
                });
    }
}

