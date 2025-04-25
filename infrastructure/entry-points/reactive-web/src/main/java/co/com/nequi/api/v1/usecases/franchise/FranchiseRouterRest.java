package co.com.nequi.api.v1.usecases.franchise;

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

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
@Configuration
public class FranchiseRouterRest {

    @Bean
    public RouterFunction<ServerResponse> franchiseRoutes(FranchiseHandler handler) {
        return createFranchiseRoute(handler)
                .and(addBranchRoute(handler))
                .and(getProductsWithMaxStockByBranch(handler));
    }

    @RouterOperation(
            path = "/api/v1/franchise/{franchiseId}/branches/products/max-stock",
            produces = MediaType.APPLICATION_JSON_VALUE,
            beanClass = FranchiseHandler.class,
            beanMethod = "getProductsWithMaxStockByBranch",
            operation = @Operation(
                    operationId = "getProductsWithMaxStockByBranch",
                    summary = "get a specific franchise, the product with the highest stock for each branch",
                    tags = {"Franchises"},
                    parameters = {
                            @Parameter(
                                    name = "franchiseId",
                                    description = "ID of the franchise",
                                    required = true,
                                    in = ParameterIn.PATH,
                                    schema = @Schema(type = "integer", format = "int64")
                            )
                    },
                    responses = {
                            @ApiResponse(
                                    responseCode = "200",
                                    description = "Branch successfully added to franchise",
                                    content = @Content(schema = @Schema(implementation = co.com.nequi.api.v1.commons.ApiResponse.class))
                            ),
                            @ApiResponse(responseCode = "400", description = "Invalid branch data provided"),
                            @ApiResponse(responseCode = "404", description = "Franchise not found"),
                            @ApiResponse(responseCode = "500", description = "Internal server error")
                    }
            )
    )
    @Bean
    public RouterFunction<ServerResponse> getProductsWithMaxStockByBranch(FranchiseHandler handler) {
        return route(GET("/api/v1/franchise/{franchiseId}/branches/products/max-stock").and(accept(MediaType.APPLICATION_JSON)),
                handler::getProductsWithMaxStockByBranch);
    }

    @RouterOperation(
            path = "/api/v1/franchise",
            produces = MediaType.APPLICATION_JSON_VALUE,
            beanClass = FranchiseHandler.class,
            beanMethod = "createFranchise",
            operation = @Operation(
                    operationId = "createFranchise",
                    summary = "Create a new franchise",
                    tags = {"Franchises"},
                    requestBody = @RequestBody(
                            required = true,
                            content = @Content(schema = @Schema(implementation = FranchiseRequest.class))
                    ),
                    responses = {
                            @ApiResponse(
                                    responseCode = "201",
                                    description = "Franchise successfully created",
                                    content = @Content(schema = @Schema(implementation = co.com.nequi.api.v1.commons.ApiResponse.class))
                            ),
                            @ApiResponse(responseCode = "400", description = "Invalid franchise data provided"),
                            @ApiResponse(responseCode = "500", description = "Internal server error")
                    }
            )
    )
    @Bean
    public RouterFunction<ServerResponse> createFranchiseRoute(FranchiseHandler handler) {
        return route(POST("/api/v1/franchise").and(accept(MediaType.APPLICATION_JSON)),
                handler::createFranchise);
    }

    @RouterOperation(
            path = "/api/v1/franchise/{franchiseId}/branch",
            produces = MediaType.APPLICATION_JSON_VALUE,
            beanClass = FranchiseHandler.class,
            beanMethod = "addBranchToFranchise",
            operation = @Operation(
                    operationId = "addBranchToFranchise",
                    summary = "Add a new branch to an existing franchise",
                    tags = {"Franchises"},
                    parameters = {
                            @Parameter(
                                    name = "franchiseId",
                                    description = "ID of the franchise to add the branch to",
                                    required = true,
                                    in = ParameterIn.PATH,
                                    schema = @Schema(type = "integer", format = "int64")
                            )
                    },
                    requestBody = @RequestBody(
                            required = true,
                            content = @Content(schema = @Schema(implementation = BranchRequest.class))
                    ),
                    responses = {
                            @ApiResponse(
                                    responseCode = "201",
                                    description = "Branch successfully added to franchise",
                                    content = @Content(schema = @Schema(implementation = co.com.nequi.api.v1.commons.ApiResponse.class))
                            ),
                            @ApiResponse(responseCode = "400", description = "Invalid branch data provided"),
                            @ApiResponse(responseCode = "404", description = "Franchise not found"),
                            @ApiResponse(responseCode = "500", description = "Internal server error")
                    }
            )
    )
    @Bean
    public RouterFunction<ServerResponse> addBranchRoute(FranchiseHandler handler) {
        return route(POST("/api/v1/franchise/{franchiseId}/branch").and(accept(MediaType.APPLICATION_JSON)),
                handler::addBranchToFranchise);
    }

}