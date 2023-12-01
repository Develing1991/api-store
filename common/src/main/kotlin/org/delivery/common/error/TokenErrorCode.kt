package org.delivery.common.error

/**
 * Token의 경우 2000번대 에러코드 사용
 */
enum class TokenErrorCode(
    private val httpStatusCode: Int,
    private val errorCode: Int,
    private val message: String
): ErrorCodeIfs {
    INVALID_TOKEN(400, 2000, "유효하지 않은 토큰"),
    EXPIRED_TOKEN(400, 2001, "만료된 토큰"),
    TOKEN_EXCEPTION(400, 2002, "토큰 알 수 없는 에러"),
    AUTHORIZATION_TOKEN_NOT_FOUND(400, 2003, "인증 헤더토큰 없음"),
    ;

    override fun getHttpStatusCode(): Int {
        return this.httpStatusCode
    }

    override fun getErrorCode(): Int {
        return this.errorCode
    }

    override fun getMessage(): String {
        return this.message
    }
}