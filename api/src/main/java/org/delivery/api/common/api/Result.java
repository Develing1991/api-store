/*
package org.delivery.api.common.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.delivery.common.error.ErrorCode;
import org.delivery.common.error.ErrorCodeIfs;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Result {
    private Integer resultCode;
    private String resultMessage;
    private String resultDescription;

    public static Result OK(){
        return Result.builder()
                .resultCode(ErrorCode.OK.getErrorCode())
                .resultMessage(ErrorCode.OK.getMessage())
                .resultDescription("성공")
                .build();
    }
    
    public static Result ERROR(ErrorCodeIfs errorCodeIfs) {
        return Result.builder()
                .resultCode(errorCodeIfs.getErrorCode())
                .resultMessage(errorCodeIfs.getMessage())
                .resultDescription("실패")
                .build();
    }

    public static Result ERROR(ErrorCodeIfs errorCodeIfs, Throwable throwable) {
        return Result.builder()
                .resultCode(errorCodeIfs.getErrorCode())
                .resultMessage(errorCodeIfs.getMessage())
                .resultDescription(throwable.getLocalizedMessage()) // 서버의 모든 stack trace가 노출 되므로 비추
                .build();
    }

    public static Result ERROR(ErrorCodeIfs errorCodeIfs, String description) {
        return Result.builder()
                .resultCode(errorCodeIfs.getErrorCode())
                .resultMessage(errorCodeIfs.getMessage())
                .resultDescription(description)
                .build();
    }
}
*/
