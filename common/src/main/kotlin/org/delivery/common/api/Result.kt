package org.delivery.common.api

import org.delivery.common.error.ErrorCode
import org.delivery.common.error.ErrorCodeIfs

data class Result(
    private val resultCode: Int?=null,
    private val resultMessage: String?=null,
    private val resultDescription: String?=null
) {
    // like static -> companion object
    companion object {
        fun OK():Result{
            return Result(
                resultCode = ErrorCode.OK.getErrorCode(),
                resultMessage = ErrorCode.OK.getMessage(),
                resultDescription = "성공"
            )
        }

        fun ERROR(errorCodeIfs: ErrorCodeIfs):Result{
            return Result(
                resultCode = errorCodeIfs.getErrorCode(),
                resultMessage = errorCodeIfs.getMessage(),
                resultDescription = "실패"
            )
        }

        fun ERROR(errorCodeIfs: ErrorCodeIfs, throwable: Throwable):Result{
            return Result(
                resultCode = errorCodeIfs.getErrorCode(),
                resultMessage = errorCodeIfs.getMessage(),
                resultDescription = throwable.localizedMessage
            )
        }

        fun ERROR(errorCodeIfs: ErrorCodeIfs, description:String):Result{
            return Result(
                resultCode = errorCodeIfs.getErrorCode(),
                resultMessage = errorCodeIfs.getMessage(),
                resultDescription = description
            )
        }
        
    }
}