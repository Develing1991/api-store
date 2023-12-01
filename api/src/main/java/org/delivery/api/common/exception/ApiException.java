package org.delivery.api.common.exception;


import lombok.Getter;
import org.delivery.api.common.error.ErrorCodeIfs;

@Getter
public class ApiException extends RuntimeException implements ApiExceptionIfs{
    private final ErrorCodeIfs errorCodeIfs;
    private final String errorMessage;

    public ApiException(ErrorCodeIfs errorCodeIfs){
        super(errorCodeIfs.getMessage());
        this.errorCodeIfs = errorCodeIfs;
        this.errorMessage = errorCodeIfs.getMessage();
    }

    public ApiException(ErrorCodeIfs errorCodeIfs, String errorDescription){
        super(errorDescription);
        this.errorCodeIfs = errorCodeIfs;
        this.errorMessage = errorDescription;
    }

    public ApiException(ErrorCodeIfs errorCodeIfs, Throwable throwable){
        super(throwable);
        this.errorCodeIfs = errorCodeIfs;
        this.errorMessage = errorCodeIfs.getMessage();
    }

    public ApiException(ErrorCodeIfs errorCodeIfs, Throwable throwable, String errorDescription){
        super(throwable);
        this.errorCodeIfs = errorCodeIfs;
        this.errorMessage = errorDescription;
    }
}
