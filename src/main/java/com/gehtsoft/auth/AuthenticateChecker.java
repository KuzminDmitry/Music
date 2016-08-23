package com.gehtsoft.auth;

import com.gehtsoft.token.Token;
import com.gehtsoft.token.TokenMemorySingleton;
import org.apache.http.auth.AuthenticationException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by dkuzmin on 8/23/2016.
 */
public class AuthenticateChecker {

    public static HttpServletResponse validate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        String jwt = null;
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("authdata")) {
                    jwt = cookie.getValue();
                    //logger.info("JWT from cookies: " + jwt);
                }
            }
            if (jwt == null) {
                //logger.error("JWT is null. User is not authorized. Access denied!");
                httpServletResponse.sendError(403);
            } else {
                Token token = TokenMemorySingleton.getInstance().getToken(jwt);
                if (token == null) {
                    //logger.error("Token is null. User is not authorized. Access denied!");
                    httpServletResponse.sendError(403);
                } else {
                    //logger.info("User is authorized. Access is allowed!");
                }
            }
        } else {
            //logger.error("Cookies is null!");
            httpServletResponse.sendError(403);
        }
        return httpServletResponse;
    }
}
