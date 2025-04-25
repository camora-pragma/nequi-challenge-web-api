package co.com.nequi.model.franchise;

import co.com.nequi.model.branch.Branch;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class FranchiseTest {

    @Test
    void testFranchiseCreation() {
        Franchise franchise = Franchise.builder()
                .id(1L)
                .name("Franchise 1")
                .branches(Collections.emptyList())
                .build();

        assertNotNull(franchise);
        assertEquals(1L, franchise.getId());
        assertEquals("Franchise 1", franchise.getName());
        assertTrue(franchise.getBranches().isEmpty());
    }

    @Test
    void testFranchiseWithBranches() {
        Branch branch = Branch.builder()
                .id(1L)
                .name("Branch 1")
                .franchiseId(1L)
                .products(Collections.emptyList())
                .build();

        Franchise franchise = Franchise.builder()
                .id(1L)
                .name("Franchise 1")
                .branches(Collections.singletonList(branch))
                .build();

        assertNotNull(franchise);
        assertEquals(1, franchise.getBranches().size());
        assertEquals("Branch 1", franchise.getBranches().get(0).getName());
    }

    @Test
    void testFranchiseEdgeCases() {
        Franchise franchise = new Franchise();
        assertNull(franchise.getId());
        assertNull(franchise.getName());
        assertNull(franchise.getBranches());
    }
}
