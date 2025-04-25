package co.com.nequi.model.exceptions;

public interface NequiErrorType {

    String getCode();

    String getDescription();
    Integer getStatus();

    default String getExceptionMessage(){
        String var10000 = this.getCode();
        return var10000 + ": "  + this.getDescription();
    }
}
