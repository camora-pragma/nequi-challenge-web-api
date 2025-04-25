package co.com.nequi.api.v1.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@ExtendWith(SpringExtension.class)
@Import(SwaggerConfig.class)
class SwaggerConfigTest {

    @Autowired
    SwaggerConfig config;

    @Test
    void shouldReturnCustomOpenApi() {
        OpenAPI openAPI = config.customOpenApi();
        assertEquals("Nequi challenge API", openAPI.getInfo().getTitle());
    }

    @Test
    void shouldAddBearerAuthToOperation() {
        Operation operation = new Operation();
        HandlerMethod dummyHandlerMethod = mock(HandlerMethod.class);
        config.operationCustomizer().customize(operation, dummyHandlerMethod);

        assertTrue(operation.getSecurity().stream()
                .anyMatch(sec -> sec.containsKey("BearerAuth")));
    }

    @Test
    void shouldCreateGroupedOpenApi() {
        GroupedOpenApi openApi = config.docketV1();
        assertEquals("public-api-v1", openApi.getGroup());
    }

    @Test
    void shouldInvokeAddErrorResponse() throws Exception {

        Operation operation = new Operation();
        Schema<?> schema = mock(Schema.class);


        Method addErrorResponseMethod = SwaggerConfig.class.getDeclaredMethod("addErrorResponse",
                Operation.class, String.class, String.class, Schema.class);
        addErrorResponseMethod.setAccessible(true);


        addErrorResponseMethod.invoke(config, operation, "400", "Bad Request", schema);


        ApiResponse apiResponse = operation.getResponses().get("400");
        assertNotNull(apiResponse);
        assertEquals("Bad Request", apiResponse.getDescription());
        assertEquals("application/json", apiResponse.getContent().keySet().iterator().next());
    }

    @Test
    void shouldInvokeAddErrorResponses() throws Exception {

        Operation operation = new Operation();
        Schema<?> schema = mock(Schema.class);


        Method addErrorResponsesMethod = SwaggerConfig.class.getDeclaredMethod("addErrorResponses", Operation.class, Schema.class);
        addErrorResponsesMethod.setAccessible(true);


        addErrorResponsesMethod.invoke(config, operation, schema);

        assertNotNull(operation.getResponses().get("400"));
        assertNotNull(operation.getResponses().get("500"));

    }

}
