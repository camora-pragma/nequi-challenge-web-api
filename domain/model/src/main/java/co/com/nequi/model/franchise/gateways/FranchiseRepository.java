package co.com.nequi.model.franchise.gateways;

import co.com.nequi.model.franchise.Franchise;
import reactor.core.publisher.Mono;

public interface FranchiseRepository {

    Mono<Franchise> createFranchise(Franchise franchise);

    Mono<Franchise> getFranchiseById(Long id);
}
