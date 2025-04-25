package co.com.nequi.r2dbc.franchise;

import co.com.nequi.model.franchise.Franchise;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FranchiseReactiveRepositoryAdapterTest {

    @Mock
    private FranchiseDataRepository repository;

    @Mock
    private ObjectMapper mapper;

    private FranchiseReactiveRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new FranchiseReactiveRepositoryAdapter(repository, mapper);
    }

    @Test
    void shouldCreateFranchise() {
        Franchise franchise = Franchise.builder().id(1L).name("Franquicia Test").build();
        FranchiseData franchiseData = new FranchiseData(); // Ponerle datos si es necesario

        when(mapper.map(franchise, FranchiseData.class)).thenReturn(franchiseData);
        when(repository.save(franchiseData)).thenReturn(Mono.just(franchiseData));
        when(mapper.mapBuilder(franchiseData, Franchise.FranchiseBuilder.class))
                .thenReturn(Franchise.builder().id(1L).name("Franquicia Test"));

        StepVerifier.create(adapter.createFranchise(franchise))
                .expectNextMatches(f -> f.getId().equals(1L) && f.getName().equals("Franquicia Test"))
                .verifyComplete();
    }

    @Test
    void shouldGetFranchiseById() {
        Long id = 1L;
        FranchiseData data = new FranchiseData();
        data.setId(id);
        data.setName("Franquicia 1");

        when(repository.findById(id)).thenReturn(Mono.just(data));
        when(mapper.mapBuilder(data, Franchise.FranchiseBuilder.class))
                .thenReturn(Franchise.builder().id(id).name("Franquicia 1"));

        StepVerifier.create(adapter.getFranchiseById(id))
                .expectNextMatches(f -> f.getId().equals(id) && f.getName().equals("Franquicia 1"))
                .verifyComplete();
    }
}
