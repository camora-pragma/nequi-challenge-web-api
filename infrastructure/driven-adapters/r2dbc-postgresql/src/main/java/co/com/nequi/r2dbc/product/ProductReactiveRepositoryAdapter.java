package co.com.nequi.r2dbc.product;

import co.com.nequi.model.exceptions.BusinessException;
import co.com.nequi.model.product.Product;
import co.com.nequi.model.product.ProductErrorType;
import co.com.nequi.model.product.gateways.ProductRepository;
import co.com.nequi.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class ProductReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Product,
        ProductData,
        Long,
        ProductDataRepository
        > implements ProductRepository {
    public ProductReactiveRepositoryAdapter(ProductDataRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.mapBuilder(d, Product.ProductBuilder.class).build());
    }


    @Override
    public Mono<Product> createProduct(Product product) {
        return save(product).
                onErrorResume(DuplicateKeyException.class, ex -> Mono.error(new BusinessException(ProductErrorType.PROPERTY_DUPLICATE)));
    }

    @Override
    public Mono<Product> updateProductStock(Product product) {
        return save(product);
    }

    @Override
    public Mono<Boolean> deleteProductByBranch(Long id, Long branchId) {
        return this.repository.deleteByIdAndBranchId(id,branchId).thenReturn(true);
    }

    @Override
    public Mono<Product> getProductById(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Product> getTopByBranchIdOrderByStockDesc(Long id) {
        return this.repository.findTopByBranchIdOrderByStockDesc(id).map(this::toEntity);
    }
}
