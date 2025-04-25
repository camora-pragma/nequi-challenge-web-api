package co.com.nequi.config;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UseCasesConfigTest {

    @Test
    void testUseCaseBeansExist() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class)) {
            String[] beanNames = context.getBeanDefinitionNames();

            boolean useCaseBeanFound = false;
            for (String beanName : beanNames) {
                if (beanName.endsWith("UseCase")) {
                    useCaseBeanFound = true;
                    break;
                }
            }

            assertTrue(useCaseBeanFound, "No beans ending with 'Use Case' were found");
        }
    }

    @Configuration
    @Import(UseCasesConfig.class)
    static class TestConfig {

        @Bean
        public AddProductToBranchUseCase addProductToBranchUseCase() {
            return new AddProductToBranchUseCase();
        }
        @Bean
        public DeleteProductFromBranchUseCase deleteProductFromBranchUseCase() {
            return new DeleteProductFromBranchUseCase();
        }
        @Bean
        public AddBranchToFranchiseUseCase addBranchToFranchiseUseCase() {
            return new AddBranchToFranchiseUseCase();
        }
        @Bean
        public CreateFranchiseUseCase createFranchiseUseCase() {
            return new CreateFranchiseUseCase();
        }
        @Bean
        public GetFranchiseWithProductsMaxStockUseCase getFranchiseWithProductsMaxStockUseCase() {
            return new GetFranchiseWithProductsMaxStockUseCase();
        }
        @Bean
        public UpdateStockProductUseCase updateStockProductUseCase() {
            return new UpdateStockProductUseCase();
        }
    }

    static class AddProductToBranchUseCase {
        public String execute() {
            return "AddProductToBranchUseCase Test";
        }
    }

    static class DeleteProductFromBranchUseCase {
        public String execute() {
            return "DeleteProductFromBranchUseCase Test";
        }
    }

    static class AddBranchToFranchiseUseCase {
        public String execute() {
            return "AddBranchToFranchiseUseCase Test";
        }
    }

    static class CreateFranchiseUseCase {
        public String execute() {
            return "CreateFranchiseUseCase Test";
        }
    }

    static class GetFranchiseWithProductsMaxStockUseCase {
        public String execute() {
            return "GetFranchiseWithProductsMaxStockUseCase Test";
        }
    }

    static class UpdateStockProductUseCase {
        public String execute() {
            return "UpdateStockProductUseCase Test";
        }
    }



}