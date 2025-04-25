package co.com.nequi.api.v1.commons;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private ZonedDateTime timestamp;
    private Integer status;
    private String message;
    private T data;
    private List<String> errors;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .timestamp(ZonedDateTime.now())
                .status(200)
                .message("Success")
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> error(Integer status, String message, List<String> errors) {
        return ApiResponse.<T>builder()
                .timestamp(ZonedDateTime.now())
                .status(status)
                .message(message)
                .errors(errors)
                .build();
    }
}