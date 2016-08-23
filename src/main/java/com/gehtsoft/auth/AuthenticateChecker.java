package com.gehtsoft.auth;

import com.gehtsoft.token.Token;
import com.gehtsoft.token.TokenMemorySingleton;
import org.apache.log4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by dkuzmin on 8/23/2016.
 */
public class AuthenticateChecker {

    final static Logger logger = Logger.getLogger("authenticate");

    public static boolean validate(HttpServletRequest httpServletRequest) throws Exception {
        String jwt = null;
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("authdata")) {
                    jwt = cookie.getValue();
                    logger.info("JWT from cookies: " + jwt);
                }
            }
            if (jwt == null) {
                logger.error("JWT is null. User is not authorized. Access denied!");
                return false;
            } else {
                Token token = TokenMemorySingleton.getInstance().getToken(jwt);
                if (token == null) {
                    logger.error("Token is null. User is not authorized. Access denied!");
                    return false;
                } else {
                    logger.info("User is authorized. Access is allowed! Token: " +token);
                }
            }
        } else {
            logger.error("Cookies is null!");
            return false;
        }
        return true;
    }
}
