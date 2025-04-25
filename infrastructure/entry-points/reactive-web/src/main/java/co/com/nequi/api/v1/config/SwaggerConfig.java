package co.com.nequi.api.v1.config;

import io.swagger.v3.core.converter.ModelConverters;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import io.swagger.v3.oas.models.Operation;
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenApi(){
        return new OpenAPI()
                .info(new Info()
                        .title("Nequi challenge API")
                        .description("API form mapping franchise")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Pragma Team")
                                .email("support@pragma.com.co")
                                .url("https://pragma.com.co")
                        )
                        .license( new License()
                                .name("Apache 2.0")
                                .url("https://pragma.com.co\"")
                        )
                );
    }

    @Bean
    public OperationCustomizer operationCustomizer() {
        return (Operation operation, HandlerMethod handlerMethod) ->
        {
            SecurityRequirement securityRequirement = new SecurityRequirement();
            operation.addSecurityItem(securityRequirement.addList("BearerAuth"));
            return operation;
        };
    }
    @Bean
    public GroupedOpenApi docketV1() {
        return GroupedOpenApi.builder()
                .group("public-api-v1")
                .pathsToMatch("/api/v1/**")
                .packagesToScan("co.com.nequi.api.v1.usecases")
                .displayName("Challenge pragma API V1")
                .addOpenApiCustomizer(openApiCustomizer())
                .addOperationCustomizer(operationCustomizer())
                .build();
    }

    @Bean
    GlobalOpenApiCustomizer openApiCustomizer() {
        return openApi -> {
            var resolvedErrorSchema = ModelConverters.getInstance().readAllAsResolvedSchema(co.com.nequi.api.v1.commons.ApiResponse.class);
            resolvedErrorSchema.referencedSchemas.forEach((k, v) -> openApi.getComponents().addSchemas(k, v));
            openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation ->
                    addErrorResponses(operation, resolvedErrorSchema.schema)
            ));
        };
    }
    private void addErrorResponse(Operation operation, String code, String description, Schema<?> schema) {
        if (operation.getResponses() == null) {
            operation.setResponses(new io.swagger.v3.oas.models.responses.ApiResponses());
        }
        operation.getResponses().addApiResponse(code,
                new ApiResponse()
                        .description(description)
                        .content(new Content().addMediaType("application/json", new MediaType().schema(schema)))
        );
    }


    private void addErrorResponses(Operation operation, Schema<?> schema) {
        addErrorResponse(operation, "204", "No content", schema);
        addErrorResponse(operation, "400", "Bad Request", schema);
        addErrorResponse(operation, "401", "Unauthorized", schema);
        addErrorResponse(operation, "403", "Forbidden", schema);
        addErrorResponse(operation, "404", "Not Found", schema);
        addErrorResponse(operation, "415", "Unsupported media type", schema);
        addErrorResponse(operation, "500", "Internal Server Error", schema);
        addErrorResponse(operation, "503", "Service Unavailable", schema);
        addErrorResponse(operation, "504", "Gateway Timeout", schema);
    }
}
