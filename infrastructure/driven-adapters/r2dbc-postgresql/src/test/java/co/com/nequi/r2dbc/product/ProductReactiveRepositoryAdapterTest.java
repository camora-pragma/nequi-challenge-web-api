package co.com.nequi.r2dbc.product;

import co.com.nequi.model.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductReactiveRepositoryAdapterTest {

    @Mock
    private ProductDataRepository repository;

    @Mock
    private ObjectMapper mapper;

    private ProductReactiveRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new ProductReactiveRepositoryAdapter(repository, mapper);
    }

    @Test
    void shouldCreateProduct() {
        Product product = Product.builder().id(1L).name("Product Test").stock(100).build();
        ProductData productData = new ProductData();

        when(mapper.map(product, ProductData.class)).thenReturn(productData);
        when(repository.save(productData)).thenReturn(Mono.just(productData));
        when(mapper.mapBuilder(productData, Product.ProductBuilder.class)).thenReturn(Product.builder().id(1L).name("Producto Test").stock(100));

        StepVerifier.create(adapter.createProduct(product))
                .expectNextMatches(p -> p.getId().equals(1L) && p.getStock() == 100)
                .verifyComplete();
    }

    @Test
    void shouldUpdateProductStock() {
        Product product = Product.builder().id(1L).name("Product Test").stock(50).build();
        ProductData productData = new ProductData();

        when(mapper.map(product, ProductData.class)).thenReturn(productData);
        when(repository.save(productData)).thenReturn(Mono.just(productData));
        when(mapper.mapBuilder(productData, Product.ProductBuilder.class)).thenReturn(Product.builder().id(1L).name("Producto Test").stock(50));

        StepVerifier.create(adapter.updateProductStock(product))
                .expectNextMatches(p -> p.getId().equals(1L) && p.getStock() == 50)
                .verifyComplete();
    }

    @Test
    void shouldDeleteProductByBranch() {
        Long productId = 1L;
        Long branchId = 10L;

        when(repository.deleteByIdAndBranchId(productId, branchId)).thenReturn(Mono.empty());

        StepVerifier.create(adapter.deleteProductByBranch(productId, branchId))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void shouldGetProductById() {
        Long id = 1L;
        ProductData data = new ProductData();
        data.setId(id);
        data.setName("Product #1");

        when(repository.findById(id)).thenReturn(Mono.just(data));
        when(mapper.mapBuilder(data, Product.ProductBuilder.class)).thenReturn(Product.builder().id(id).name("Product #1"));

        StepVerifier.create(adapter.getProductById(id))
                .expectNextMatches(p -> p.getId().equals(id) && p.getName().equals("Product #1"))
                .verifyComplete();
    }

    @Test
    void shouldGetTopProductsByBranchId() {
        Long branchId = 20L;
        ProductData productData = new ProductData();
        productData.setId(1L);
        productData.setStock(999);
        productData.setName("Top product");

        when(repository.findTopByBranchIdOrderByStockDesc(branchId)).thenReturn(Flux.just(productData));
        when(mapper.mapBuilder(productData, Product.ProductBuilder.class)).thenReturn(Product.builder().id(1L).name("Top product").stock(999));

        StepVerifier.create(adapter.getTopByBranchIdOrderByStockDesc(branchId))
                .expectNextMatches(p -> p.getStock() == 999 && p.getName().equals("Top product"))
                .verifyComplete();
    }
}
