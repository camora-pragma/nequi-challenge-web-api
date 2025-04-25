package co.com.nequi.api.v1.usecases.products;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ProductRouterRest {

    @Bean
    public RouterFunction<ServerResponse> productRoutes(ProductHandler handler) {
        return updateStockProduct(handler);
    }


    @RouterOperation(
            path = "/api/v1/product/{productId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            beanClass = ProductHandler.class,
            beanMethod = "updateStockProduct",
            operation = @Operation(
                    operationId = "updateStockProduct",
                    summary = "Update stock a product",
                    tags = {"Products"},
                    parameters = {
                            @Parameter(
                                    name = "productId",
                                    description = "ID of the product to update stock",
                                    required = true,
                                    in = ParameterIn.PATH,
                                    schema = @Schema(type = "integer", format = "int64")
                            )
                    },
                    requestBody = @RequestBody(
                            required = true,
                            content = @Content(schema = @Schema(implementation = UpdateStockRequest.class))
                    ), responses = {
                    @ApiResponse(
                            responseCode = "201",
                            content = @Content(schema = @Schema(implementation = co.com.nequi.api.v1.commons.ApiResponse.class))
                    )}
            )
    )
    @Bean
    public RouterFunction<ServerResponse> updateStockProduct(ProductHandler handler) {
        return route(PUT("/api/v1/product/{productId}").and(accept(MediaType.APPLICATION_JSON)),
                handler::updateStockProduct);
    }

}
