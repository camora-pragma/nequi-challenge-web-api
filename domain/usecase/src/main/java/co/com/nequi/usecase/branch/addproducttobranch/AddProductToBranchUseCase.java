package co.com.nequi.usecase.branch.addproducttobranch;

import co.com.nequi.model.branch.BranchErrorType;
import co.com.nequi.model.branch.gateways.BranchRepository;
import co.com.nequi.model.exceptions.BusinessException;
import co.com.nequi.model.product.Product;
import co.com.nequi.model.product.gateways.ProductRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AddProductToBranchUseCase {

    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;

    public Mono<Boolean> apply(Long id, Product product) {
        return branchRepository.getBranchById(id)
                .switchIfEmpty(Mono.error(new BusinessException(
                        BranchErrorType.BRANCH_NOT_FOUND)))
                .flatMap(franchise -> {
                    product.setBranchId(id);
                    return productRepository.createProduct(product)
                            .thenReturn(true);
                });
    }
}
