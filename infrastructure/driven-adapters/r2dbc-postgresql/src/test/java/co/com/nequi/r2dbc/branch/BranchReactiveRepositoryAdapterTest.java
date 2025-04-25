package co.com.nequi.r2dbc.branch;

import co.com.nequi.model.branch.Branch;
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
class BranchReactiveRepositoryAdapterTest {

    @Mock
    private BranchDataRepository repository;

    @Mock
    private ObjectMapper mapper;

    private BranchReactiveRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new BranchReactiveRepositoryAdapter(repository, mapper);
    }

    @Test
    void shouldCreateBranch() {
        Branch branch = Branch.builder().id(1L).name("Sucursal Test").build();
        BranchData branchData = new BranchData();

        when(mapper.map(branch, BranchData.class)).thenReturn(branchData);
        when(repository.save(branchData)).thenReturn(Mono.just(branchData));
        when(mapper.mapBuilder(branchData, Branch.BranchBuilder.class)).thenReturn(Branch.builder().id(1L).name("Sucursal Test"));

        StepVerifier.create(adapter.createBranch(branch))
                .expectNextMatches(b -> b.getId().equals(1L) && b.getName().equals("Sucursal Test"))
                .verifyComplete();
    }

    @Test
    void shouldGetBranchById() {
        Long id = 1L;
        BranchData data = new BranchData();
        data.setId(id);
        data.setName("Sucursal 1");

        when(repository.findById(id)).thenReturn(Mono.just(data));
        when(mapper.mapBuilder(data, Branch.BranchBuilder.class)).thenReturn(Branch.builder().id(id).name("Sucursal 1"));

        StepVerifier.create(adapter.getBranchById(id))
                .expectNextMatches(branch -> branch.getId().equals(id) && branch.getName().equals("Sucursal 1"))
                .verifyComplete();
    }

    @Test
    void shouldGetAllByFranchise() {
        Long franchiseId = 10L;
        BranchData data = new BranchData();
        data.setId(1L);
        data.setFranchiseId(franchiseId);
        data.setName("Sucursal Franquicia");

        when(repository.findByFranchiseId(franchiseId)).thenReturn(Flux.just(data));
        when(mapper.mapBuilder(data, Branch.BranchBuilder.class)).thenReturn(Branch.builder().id(1L).name("Sucursal Franquicia"));

        StepVerifier.create(adapter.getAllByFranchise(franchiseId))
                .expectNextMatches(branch -> branch.getId().equals(1L) && branch.getName().equals("Sucursal Franquicia"))
                .verifyComplete();
    }
}
