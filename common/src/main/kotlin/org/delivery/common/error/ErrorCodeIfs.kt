package org.delivery.common.error

interface ErrorCodeIfs {
    fun getHttpStatusCode(): Int
    fun getErrorCode(): Int
    fun getMessage(): String
}