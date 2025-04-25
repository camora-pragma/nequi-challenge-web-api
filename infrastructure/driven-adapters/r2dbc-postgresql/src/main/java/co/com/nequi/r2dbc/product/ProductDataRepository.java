package co.com.nequi.r2dbc.product;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface ProductDataRepository extends ReactiveCrudRepository<ProductData, Long>, ReactiveQueryByExampleExecutor<ProductData> {
    Mono<Void> deleteByIdAndBranchId(Long id, Long branchId);
    Flux<ProductData> findTopByBranchIdOrderByStockDesc(Long branchId);
}
