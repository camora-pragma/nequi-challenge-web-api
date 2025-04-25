package co.com.nequi.usecase.product;

import co.com.nequi.model.exceptions.BusinessException;
import co.com.nequi.model.product.Product;
import co.com.nequi.model.product.ProductErrorType;
import co.com.nequi.model.product.gateways.ProductRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UpdateStockProductUseCase {
    private final ProductRepository productRepository;

    public Mono<Product> apply(Long id, Integer stock){
        return productRepository.getProductById(id)
                .switchIfEmpty(Mono.error(new BusinessException(ProductErrorType.PRODUCT_NOT_FOUND)))
                .flatMap(product ->{
                    product.updateStock(stock);
                    return productRepository.updateProductStock(product);
                });
    }
}
