package co.com.nequi.model.exceptions;

import co.com.nequi.model.product.ProductErrorType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BusinessExceptionTest {

    @Test
    void testBusinessExceptionWithErrorType() {
        BusinessException exception = new BusinessException(ProductErrorType.PRODUCT_NOT_FOUND);

        assertNotNull(exception);
        assertEquals(ProductErrorType.PRODUCT_NOT_FOUND.getExceptionMessage(), exception.getMessage());
        assertEquals(ProductErrorType.PRODUCT_NOT_FOUND.getCode(), exception.getCode());
        assertEquals(ProductErrorType.PRODUCT_NOT_FOUND.getStatus(), exception.getStatus());
    }

    @Test
    void testBusinessExceptionWithAnotherErrorType() {
        BusinessException exception = new BusinessException(ProductErrorType.STOCK_NEGATIVE);

        assertNotNull(exception);
        assertEquals(ProductErrorType.STOCK_NEGATIVE.getExceptionMessage(), exception.getMessage());
        assertEquals("PRODUCT_002", exception.getCode());
        assertEquals(400, exception.getStatus());
    }
}