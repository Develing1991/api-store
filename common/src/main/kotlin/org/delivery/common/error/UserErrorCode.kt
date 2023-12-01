package org.delivery.common.error

/**
 * User의 경우 1000번대 에러코드 사용
 */
enum class UserErrorCode(
    private val httpStatusCode: Int,
    private val errorCode: Int,
    private val message: String
): ErrorCodeIfs {
    // 외부, 내부, description
    //USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), 1484, "사용자를 찾을 수 없습니다"),
    USER_NOT_FOUND(404, 1484, "일치 하는 회원 정보가 없습니다."),
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