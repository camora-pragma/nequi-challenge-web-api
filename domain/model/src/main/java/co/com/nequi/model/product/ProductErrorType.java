package co.com.nequi.model.product;

import co.com.nequi.model.exceptions.NequiErrorType;
import lombok.Getter;

@Getter
public enum ProductErrorType implements NequiErrorType {

    PRODUCT_NOT_FOUND(404, "PRODUCT_001", "Product not found"),
    STOCK_NEGATIVE(400, "PRODUCT_002", "Stock cannot be less than zero"),
    INSUFFICIENT_STOCK(400, "PRODUCT_003", "Insufficient stock for the operation"),
    NEGATIVE_STOCK_ADDITION(400, "PRODUCT_004", "Cannot add negative quantity to stock"),
    PROPERTY_DUPLICATE(400, "23505", "Product properties must be unique: duplicate value found [name]"),
    ERROR_SERVER(500, "PRODUCT_006", "Unhandled error")

    ;
    ;

    private final Integer status;
    private final String code;
    private final String description;
    ProductErrorType(Integer status, String code, String description) {
        this.code = code;
        this.description = description;
        this.status = status;
    }
}
