package co.com.nequi.model.product;

import co.com.nequi.model.exceptions.BusinessException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void testProductCreation() {
        Product product = Product.builder()
                .id(1L)
                .name("Product 1")
                .stock(10)
                .branchId(100L)
                .build();

        assertNotNull(product);
        assertEquals(1L, product.getId());
        assertEquals("Product 1", product.getName());
        assertEquals(10, product.getStock());
        assertEquals(100L, product.getBranchId());
    }

    @Test
    void testUpdateStockValid() {
        Product product = Product.builder()
                .id(1L)
                .name("Product 1")
                .stock(10)
                .branchId(100L)
                .build();

        product.updateStock(20);
        assertEquals(20, product.getStock());
    }

    @Test
    void testUpdateStockInvalid() {
        Product product = Product.builder()
                .id(1L)
                .name("Product 1")
                .stock(10)
                .branchId(100L)
                .build();

        BusinessException exception = assertThrows(BusinessException.class, () -> product.updateStock(-5));
        assertEquals(ProductErrorType.STOCK_NEGATIVE.getCode(), exception.getCode());
    }

    @Test
    void testReduceStockValid() {
        Product product = Product.builder()
                .id(1L)
                .name("Product 1")
                .stock(10)
                .branchId(100L)
                .build();

        product.reduceStock(5);
        assertEquals(5, product.getStock());
    }

    @Test
    void testReduceStockInvalid() {
        Product product = Product.builder()
                .id(1L)
                .name("Product 1")
                .stock(10)
                .branchId(100L)
                .build();

        BusinessException exception = assertThrows(BusinessException.class, () -> product.reduceStock(15));
        assertEquals(ProductErrorType.INSUFFICIENT_STOCK.getCode(), exception.getCode());
    }

    @Test
    void testAddStockValid() {
        Product product = Product.builder()
                .id(1L)
                .name("Product 1")
                .stock(10)
                .branchId(100L)
                .build();

        product.addStock(5);
        assertEquals(15, product.getStock());
    }

    @Test
    void testAddStockInvalid() {
        Product product = Product.builder()
                .id(1L)
                .name("Product 1")
                .stock(10)
                .branchId(100L)
                .build();

        BusinessException exception = assertThrows(BusinessException.class, () -> product.addStock(-5));
        assertEquals(ProductErrorType.NEGATIVE_STOCK_ADDITION.getCode(), exception.getCode());
    }

    @Test
    void testSetStockValid() {
        Product product = Product.builder()
                .id(1L)
                .name("Product 1")
                .stock(10)
                .branchId(100L)
                .build();

        product.setStock(0);
        assertEquals(0, product.getStock());
    }

    @Test
    void testSetStockInvalid() {
        Product product = Product.builder()
                .id(1L)
                .name("Product 1")
                .stock(10)
                .branchId(100L)
                .build();

        BusinessException exception = assertThrows(BusinessException.class, () -> product.setStock(-1));
        assertEquals(ProductErrorType.NEGATIVE_STOCK_ADDITION.getCode(), exception.getCode());
    }

    @Test
    void testProductEdgeCases() {
        Product product = new Product();
        assertNull(product.getId());
        assertNull(product.getName());
        assertNull(product.getBranchId());
        assertNull(product.getStock());
    }
}
