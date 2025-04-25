package co.com.nequi.model.branch;

import co.com.nequi.model.product.Product;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class BranchTest {

    @Test
    void testBranchCreation() {
        Branch branch = Branch.builder()
                .id(1L)
                .name("Branch 1")
                .franchiseId(100L)
                .products(Collections.emptyList())
                .build();

        assertNotNull(branch);
        assertEquals(1L, branch.getId());
        assertEquals("Branch 1", branch.getName());
        assertEquals(100L, branch.getFranchiseId());
        assertTrue(branch.getProducts().isEmpty());
    }

    @Test
    void testBranchWithProducts() {
        Product product = Product.builder()
                .id(1L)
                .name("Product 1")
                .stock(10)
                .branchId(1L)
                .build();

        Branch branch = Branch.builder()
                .id(1L)
                .name("Branch 1")
                .franchiseId(100L)
                .products(Collections.singletonList(product))
                .build();

        assertNotNull(branch);
        assertEquals(1, branch.getProducts().size());
        assertEquals("Product 1", branch.getProducts().get(0).getName());
    }

    @Test
    void testBranchEdgeCases() {
        Branch branch = new Branch();
        assertNull(branch.getId());
        assertNull(branch.getName());
        assertNull(branch.getFranchiseId());
        assertNull(branch.getProducts());
    }
}
