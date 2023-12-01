package org.delivery.api.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


/**
 * User의 경우 1000번대 에러코드 사용
 */
@AllArgsConstructor
@Getter
public enum UserErrorCode implements ErrorCodeIfs{

    // 외부, 내부, description
    //USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), 1484, "사용자를 찾을 수 없습니다"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), 1484, "일치 하는 회원 정보가 없습니다."),

    ;
    
    private final Integer httpStatusCode; // http status
    private final Integer errorCode; // internal(내부)코드
    private final String message;

}
