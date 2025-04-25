package co.com.nequi.usecase.franchise.addbranch;

import co.com.nequi.model.branch.Branch;
import co.com.nequi.model.branch.gateways.BranchRepository;
import co.com.nequi.model.exceptions.BusinessException;
import co.com.nequi.model.franchise.FranchiseErrorType;
import co.com.nequi.model.franchise.gateways.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AddBranchToFranchiseUseCase {
    private final FranchiseRepository franchiseRepository;
    private final BranchRepository branchRepository;

    public Mono<Boolean> apply(Long id, Branch branch) {
        return franchiseRepository.getFranchiseById(id)
                .switchIfEmpty(Mono.error(new BusinessException(FranchiseErrorType.FRANCHISE_NOT_FOUND)))
                .flatMap(franchise -> {
                    branch.setFranchiseId(id);
                    return branchRepository.createBranch(branch).thenReturn(true);
                });
    }
}
