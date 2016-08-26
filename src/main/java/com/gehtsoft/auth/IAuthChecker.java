package com.gehtsoft.auth;

import com.gehtsoft.token.Token;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by dkuzmin on 8/26/2016.
 */
public interface IAuthChecker {
    Token validate(HttpServletRequest httpServletRequest) throws Exception;
}
