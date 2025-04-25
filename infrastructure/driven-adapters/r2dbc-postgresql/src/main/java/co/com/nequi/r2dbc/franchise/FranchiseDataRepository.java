package co.com.nequi.r2dbc.franchise;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;


public interface FranchiseDataRepository extends ReactiveCrudRepository<FranchiseData, Long>, ReactiveQueryByExampleExecutor<FranchiseData> {

}
