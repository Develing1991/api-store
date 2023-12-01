package org.delivery.common.exception

import org.delivery.common.error.ErrorCodeIfs

class ApiException : RuntimeException, ApiExceptionIfs{
    override val errorCodeIfs: ErrorCodeIfs
    override val errorMessage: String

    constructor(errorCodeIfs: ErrorCodeIfs): super(errorCodeIfs.getMessage()){
        this.errorCodeIfs = errorCodeIfs
        this.errorMessage = errorCodeIfs.getMessage()
    }
    constructor(errorCodeIfs: ErrorCodeIfs, errorDescription: String): super(errorDescription){
        this.errorCodeIfs = errorCodeIfs
        this.errorMessage = errorDescription
    }

    constructor(errorCodeIfs: ErrorCodeIfs, throwable: Throwable): super(throwable){
        this.errorCodeIfs = errorCodeIfs
        this.errorMessage = errorCodeIfs.getMessage()
    }

    constructor(errorCodeIfs: ErrorCodeIfs, throwable: Throwable, errorDescription: String): super(throwable){
        this.errorCodeIfs = errorCodeIfs
        this.errorMessage = errorDescription
    }

}