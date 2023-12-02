package org.delivery.api.domain.token.service;

import lombok.RequiredArgsConstructor;
import org.delivery.common.error.ErrorCode;
import org.delivery.common.exception.ApiException;
import org.delivery.api.domain.token.ifs.TokenHelperIfs;
import org.delivery.api.domain.token.model.TokenDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;

/**
 * token 에 대한 도메인 로직 담당
 */
@Service
//@RequiredArgsConstructor
public class TokenService {


    private final TokenHelperIfs tokenHelperIfs;

    public TokenService(@Qualifier("testTokenHelper") TokenHelperIfs tokenHelperIfs){
        this.tokenHelperIfs = tokenHelperIfs;
    }

    public TokenDto issueAccessToken(Long id){
        var data = new HashMap<String, Object>();
        data.put("userId", id);
        return tokenHelperIfs.issueAccessToken(data);
    }

    public TokenDto issueRefreshToken(Long id){
        var data = new HashMap<String, Object>();
        data.put("userId", id);
        return tokenHelperIfs.issueRefreshToken(data);
    }

    public Long validationToken(String token){
        var map = tokenHelperIfs.validationTokenWithThrow(token);
        var userId = map.get("userId");
        Objects.requireNonNull(userId, () -> {throw new ApiException(ErrorCode.NULL_POINT);});
        return Long.parseLong(userId.toString());
    }
}
