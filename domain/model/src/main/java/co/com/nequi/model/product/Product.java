package co.com.nequi.model.product;

import co.com.nequi.model.exceptions.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Product {
    private Long id;
    private String name;
    private Integer stock;
    private Long branchId;

    public void updateStock(Integer newStock) {
        if (newStock < 0) {
            throw new BusinessException(ProductErrorType.STOCK_NEGATIVE);
        }
        this.stock = newStock;
    }

    public void reduceStock(Integer quantity) {
        if (this.stock - quantity < 0) {
            throw new BusinessException(ProductErrorType.INSUFFICIENT_STOCK);
        }
        this.stock -= quantity;
    }

    public void addStock(Integer quantity) {
        if (quantity < 0) {
            throw new BusinessException(ProductErrorType.NEGATIVE_STOCK_ADDITION);
        }
        this.stock += quantity;
    }

    public void setStock(Integer stock) {
        if (stock < 0) {
            throw new BusinessException(ProductErrorType.NEGATIVE_STOCK_ADDITION);
        }
        this.stock = stock;
    }
}