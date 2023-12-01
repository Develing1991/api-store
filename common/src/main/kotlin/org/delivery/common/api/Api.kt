package org.delivery.common.api

import org.delivery.common.error.ErrorCodeIfs
import javax.validation.Valid

data class Api<T>(
    var result: Result? = null,
    @field: Valid // jakarta.validation-api 의존성만 들고옴
    var body: T? = null
) {

    companion object {
        fun <T> OK(data: T): Api<T>{
            return Api(
                result = Result.OK(),
                body = data
            )
        }

        fun <T> ERROR(result: Result): Api<T>{
            return Api(
                result = result,
            )
        }
        /*fun ERROR(result: Result): Api<Any>{
            return Api(
                result = result,
            )
        }*/

        fun <T> ERROR(errorCodeIfs: ErrorCodeIfs): Api<T>{
            return Api(
                result = Result.ERROR(errorCodeIfs),
            )
        }
        fun <T> ERROR(errorCodeIfs: ErrorCodeIfs, throwable: Throwable): Api<T>{
            return Api(
                result = Result.ERROR(errorCodeIfs, throwable),
            )
        }

        fun <T> ERROR(errorCodeIfs: ErrorCodeIfs, description: String): Api<T>{
            return Api(
                result = Result.ERROR(errorCodeIfs, description),
            )
        }
    }
}