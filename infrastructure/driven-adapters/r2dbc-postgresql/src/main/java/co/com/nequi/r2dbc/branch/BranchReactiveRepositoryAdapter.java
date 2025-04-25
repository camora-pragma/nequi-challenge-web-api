package co.com.nequi.r2dbc.branch;

import co.com.nequi.model.branch.Branch;
import co.com.nequi.model.branch.BranchErrorType;
import co.com.nequi.model.branch.gateways.BranchRepository;
import co.com.nequi.model.exceptions.BusinessException;
import co.com.nequi.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class BranchReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Branch,
        BranchData,
        Long,
        BranchDataRepository
        > implements BranchRepository {
    public BranchReactiveRepositoryAdapter(BranchDataRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.mapBuilder(d, Branch.BranchBuilder.class).build());
    }

    @Override
    public Mono<Branch> createBranch(Branch branch) {
        return save(branch).onErrorResume(DuplicateKeyException.class, ex -> Mono.error(new BusinessException(BranchErrorType.PROPERTY_DUPLICATE)));
    }

    @Override
    public Mono<Branch> getBranchById(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Branch> getAllByFranchise(Long franchiseId) {
        return this.repository.findByFranchiseId(franchiseId).map(this::toEntity);
    }
}
