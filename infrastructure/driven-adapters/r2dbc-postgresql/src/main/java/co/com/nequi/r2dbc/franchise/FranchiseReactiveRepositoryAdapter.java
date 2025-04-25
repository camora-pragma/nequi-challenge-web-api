package co.com.nequi.r2dbc.franchise;

import co.com.nequi.model.exceptions.BusinessException;
import co.com.nequi.model.franchise.Franchise;
import co.com.nequi.model.franchise.FranchiseErrorType;
import co.com.nequi.model.franchise.gateways.FranchiseRepository;
import co.com.nequi.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class FranchiseReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Franchise,
        FranchiseData,
        Long,
        FranchiseDataRepository
        > implements FranchiseRepository {
    public FranchiseReactiveRepositoryAdapter(FranchiseDataRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.mapBuilder(d, Franchise.FranchiseBuilder.class).build());
    }

    @Override
    public Mono<Franchise> createFranchise(Franchise franchise) {
        return save(franchise).onErrorResume(DuplicateKeyException.class, ex -> Mono.error(new BusinessException(FranchiseErrorType.PROPERTY_DUPLICATE)));
    }

    @Override
    public Mono<Franchise> getFranchiseById(Long id) {
        return findById(id);
    }
}
