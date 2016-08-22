package com.gehtsoft.threadPool;

import com.gehtsoft.factory.EntityFactory;
import com.gehtsoft.iDAO.ITokenService;
import org.apache.log4j.Logger;

import java.util.concurrent.Callable;

/**
 * Created by dkuzmin on 8/15/2016.
 */
public class AuthCallable implements Callable<Object> {

    final static Logger logger = Logger.getLogger("threadPool");

    private ITokenService tokenService;

    private Object param;
    private String method;

    public AuthCallable(Class c, String method, Object param) {
        if (param != null) {
            this.param = param;
        }
        if (method == null) {
            logger.error("Method is null!");
            throw new ExceptionInInitializerError("Method must be not null!");
        }
        if (c == null) {
            logger.error("Class is null!");
            throw new ExceptionInInitializerError("Entity class must be not null!");
        }
        this.method = method;
        tokenService = EntityFactory.getTokenService(c);
    }

    @Override
    public Object call() throws Exception {
        if(logger.isDebugEnabled()) {
            logger.debug(Thread.currentThread().getName() + " started.");
        }
        Object result = null;
        if (method.equals("getAll")) {
            result = tokenService.getAll();
        } else if (method.equals("getById")) {
            result = tokenService.getById((Integer) param);
        } else if (method.equals("add")) {
            result = tokenService.add(param);
        } else if (method.equals("deleteById")) {
            tokenService.deleteById((Integer) param);
        } else if (method.equals("update")) {
            result = tokenService.update(param);
        } else if (method.equals("deleteExpired")) {
            tokenService.deleteExpired();
        } else {
            logger.error("There are no method in factory for " + method + "!");
            throw new ExceptionInInitializerError("There are no method in factory for " + method + "!");
        }
        if(logger.isDebugEnabled()) {
            logger.debug(Thread.currentThread().getName() + " started.");
        }
        return result;
    }
}
