package com.gehtsoft.threadPool;

import com.gehtsoft.factory.EntityFactory;
import com.gehtsoft.iDAO.ITokenService;
import org.apache.log4j.Logger;

import java.util.concurrent.Callable;

/**
 * Created by dkuzmin on 8/15/2016.
 */
public class TokenCallable implements Callable<Object> {

    final static Logger logger = Logger.getLogger("resource");

    private ITokenService authService;

    private Object param;
    private String method;

    public TokenCallable(Class c, String method, Object param) {
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
        authService = EntityFactory.getTokenService(c);
    }

    @Override
    public Object call() throws Exception {
        if(logger.isDebugEnabled()) {
            logger.debug(Thread.currentThread().getName() + " started.");
        }
        Object result = null;
        if (method.equals("getAll")) {
            result = authService.getAll();
        } else if (method.equals("getById")) {
            result = authService.getById((Integer) param);
        } else if (method.equals("add")) {
            result = authService.add(param);
        } else if (method.equals("deleteById")) {
            authService.deleteById((Integer) param);
        } else if (method.equals("update")) {
            result = authService.update(param);
        } else if (method.equals("deleteExpired")) {
            authService.deleteExpired();
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
