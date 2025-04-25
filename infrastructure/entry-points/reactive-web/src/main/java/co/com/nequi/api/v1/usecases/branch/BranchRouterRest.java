package co.com.nequi.api.v1.usecases.branch;

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

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class BranchRouterRest {

    @Bean
    public RouterFunction<ServerResponse> branchRoutes(BranchHandler handler) {
        return addProductToBranch(handler)
                .and(deleteProductFromBranch(handler));
    }

    @RouterOperation(
            path = "/api/v1/branch/{branchId}/product",
            produces = MediaType.APPLICATION_JSON_VALUE,
            beanClass = BranchHandler.class,
            beanMethod = "addProductToBranch",
            operation = @Operation(
                    operationId = "addProductToBranch",
                    summary = "Add a new product to an existing branch",
                    tags = {"Branch"},
                    parameters = {
                            @Parameter(
                                    name = "branchId",
                                    description = "ID of the branch to add the product to",
                                    required = true,
                                    in = ParameterIn.PATH,
                                    schema = @Schema(type = "integer", format = "int64")
                            )
                    },
                    requestBody = @RequestBody(
                            required = true,
                            content = @Content(schema = @Schema(implementation = ProductRequest.class))
                    ),
                    responses = {
                            @ApiResponse(
                                    responseCode = "201",
                                    content = @Content(schema = @Schema(implementation = co.com.nequi.api.v1.commons.ApiResponse.class))
                            )}
            )
    )
    @Bean
    public RouterFunction<ServerResponse> addProductToBranch(BranchHandler handler) {
        return route(POST("/api/v1/branch/{branchId}/product").and(accept(MediaType.APPLICATION_JSON)),
                handler::addProductToBranch);
    }


    @RouterOperation(
            path = "/api/v1/branch/{branchId}/product/{productId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            beanClass = BranchHandler.class,
            beanMethod = "deleteProductFromBranch",
            operation = @Operation(
                    operationId = "deleteProductFromBranch",
                    summary = "Delete a product from an existing branch",
                    tags = {"Branch"},
                    parameters = {
                            @Parameter(
                                    name = "branchId",
                                    description = "ID of the branch to delete the product from",
                                    required = true,
                                    in = ParameterIn.PATH,
                                    schema = @Schema(type = "integer", format = "int64")
                            ),
                            @Parameter(
                                    name = "productId",
                                    description = "ID of the product to delete",
                                    required = true,
                                    in = ParameterIn.PATH,
                                    schema = @Schema(type = "integer", format = "int64")
                            )
                    },
                    responses = {
                            @ApiResponse(
                                    responseCode = "201",
                                    content = @Content(schema = @Schema(implementation = co.com.nequi.api.v1.commons.ApiResponse.class))
                            )}
            )
    )
    @Bean
    public RouterFunction<ServerResponse> deleteProductFromBranch(BranchHandler handler) {
        return route(DELETE("/api/v1/branch/{branchId}/product/{productId}").and(accept(MediaType.APPLICATION_JSON)),
                handler::deleteProductFromBranch);
    }

}