package co.com.nequi.model.product.gateways;

import co.com.nequi.model.product.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository {
    Mono<Product> createProduct(Product product);
    Mono<Product> updateProductStock(Product product);
    Mono<Boolean> deleteProductByBranch(Long id, Long branchId);
    Mono<Product> getProductById(Long id);
    Mono<Product> getTopByBranchIdOrderByStockDesc(Long id);
}
