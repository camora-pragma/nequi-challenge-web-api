package co.com.nequi.model.exceptions;



import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final String code;
    private final Integer status;

    public BusinessException(NequiErrorType errorType) {
        super(errorType.getExceptionMessage());
        this.code = errorType.getCode();
        this.status = errorType.getStatus();
    }

}