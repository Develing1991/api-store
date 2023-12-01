/*
package org.delivery.api.common.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.delivery.common.error.ErrorCodeIfs;

import javax.validation.Valid;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Api<T> {

    private Result result;

    @Valid
    private T body;

    public static <T> Api<T> OK(T data){
        var api = new Api<T>();
        api.body = data;
        api.result = Result.OK();
        return api;
    }

    // 에러시 body에 담아줄 내용이 없어서 굳이 Generic으로 받지 않고 Object 타입으로 리턴
    public static Api<Object> ERROR(Result result){
        var api = new Api<Object>();
        api.result = result;
        return api;
    }

    public static Api<Object> ERROR(ErrorCodeIfs errorCodeIfs){
        var api = new Api<Object>();
        api.result = Result.ERROR(errorCodeIfs);
        return api;
    }

    */
/*public static <T> Api<T> ERROR(T data, ErrorCodeIfs errorCodeIfs){
        var api = new Api<T>();
        api.result = Result.ERROR(errorCodeIfs);
        return api;
    }*//*



    public static Api<Object> ERROR(ErrorCodeIfs errorCodeIfs, Throwable throwable){
        var api = new Api<Object>();
        api.result = Result.ERROR(errorCodeIfs, throwable);
        return api;
    }

    public static Api<Object> ERROR(ErrorCodeIfs errorCodeIfs, String description){
        var api = new Api<Object>();
        api.result = Result.ERROR(errorCodeIfs, description);
        return api;
    }
}
*/
