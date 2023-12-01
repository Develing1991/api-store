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
        // java에서 Result.Companion.OK로 사용해야 하니
        @JvmStatic // java에서 사용할 때 static메서드 처럼 사용하게 하는 어노테이션
        fun OK():Result{
            return Result(
                resultCode = ErrorCode.OK.getErrorCode(),
                resultMessage = ErrorCode.OK.getMessage(),
                resultDescription = "성공"
            )
        }

        @JvmStatic
        fun ERROR(errorCodeIfs: ErrorCodeIfs):Result{
            return Result(
                resultCode = errorCodeIfs.getErrorCode(),
                resultMessage = errorCodeIfs.getMessage(),
                resultDescription = "실패"
            )
        }

        @JvmStatic
        fun ERROR(errorCodeIfs: ErrorCodeIfs, throwable: Throwable):Result{
            return Result(
                resultCode = errorCodeIfs.getErrorCode(),
                resultMessage = errorCodeIfs.getMessage(),
                resultDescription = throwable.localizedMessage
            )
        }

        @JvmStatic
        fun ERROR(errorCodeIfs: ErrorCodeIfs, description:String):Result{
            return Result(
                resultCode = errorCodeIfs.getErrorCode(),
                resultMessage = errorCodeIfs.getMessage(),
                resultDescription = description
            )
        }
        
    }
}