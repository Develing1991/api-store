package org.delivery.common.api

import org.delivery.common.error.ErrorCodeIfs
import javax.validation.Valid

data class Api<T>(
    var result: Result? = null,
    @field: Valid // jakarta.validation-api 의존성만 들고옴
    var body: T? = null
) {

    companion object {
        // java에서 Api.Companion.OK로 사용해야 하니
        @JvmStatic // java에서 사용할 때 static메서드 처럼 사용하게 하는 어노테이션
        fun <T> OK(data: T): Api<T>{
            return Api(
                result = Result.OK(),
                body = data
            )
        }

        @JvmStatic
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

        @JvmStatic
        fun <T> ERROR(errorCodeIfs: ErrorCodeIfs): Api<T>{
            return Api(
                result = Result.ERROR(errorCodeIfs),
            )
        }

        @JvmStatic
        fun <T> ERROR(errorCodeIfs: ErrorCodeIfs, throwable: Throwable): Api<T>{
            return Api(
                result = Result.ERROR(errorCodeIfs, throwable),
            )
        }

        @JvmStatic
        fun <T> ERROR(errorCodeIfs: ErrorCodeIfs, description: String): Api<T>{
            return Api(
                result = Result.ERROR(errorCodeIfs, description),
            )
        }
    }
}