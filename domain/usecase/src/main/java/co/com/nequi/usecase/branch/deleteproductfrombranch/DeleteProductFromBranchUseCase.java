package co.com.nequi.usecase.branch.deleteproductfrombranch;

import co.com.nequi.model.branch.BranchErrorType;
import co.com.nequi.model.branch.gateways.BranchRepository;
import co.com.nequi.model.exceptions.BusinessException;
import co.com.nequi.model.product.gateways.ProductRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class DeleteProductFromBranchUseCase {

    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;

    public Mono<Boolean> apply(Long productId, Long branchId){
        return branchRepository.getBranchById(branchId)
                .switchIfEmpty(Mono.error( new BusinessException(BranchErrorType.BRANCH_NOT_FOUND)))
                .flatMap(item-> productRepository.deleteProductByBranch(productId,branchId));
    }
}
