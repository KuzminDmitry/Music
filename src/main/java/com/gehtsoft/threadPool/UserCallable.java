package com.gehtsoft.threadPool;

import com.gehtsoft.core.User;
import com.gehtsoft.factory.EntityFactory;
import com.gehtsoft.iDAO.IUserService;
import org.apache.log4j.Logger;

import java.util.concurrent.Callable;

/**
 * Created by dkuzmin on 8/15/2016.
 */
public class UserCallable implements Callable<Object> {

    final static Logger logger = Logger.getLogger("resource");

    private IUserService iUserService;

    private Object param;
    private String method;

    public UserCallable(Class c, String method, Object param) {
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
        iUserService = EntityFactory.getUserService(c);
    }

    @Override
    public Object call() throws Exception {
        if(logger.isDebugEnabled()) {
            logger.debug(Thread.currentThread().getName() + " started.");
        }
        Object result = null;
        if (method.equals("getAll")) {
            result = iUserService.getAll();
        } else if (method.equals("getById")) {
            result = iUserService.getById((Integer) param);
        } else if (method.equals("add")) {
            result = iUserService.add(param);
        } else if (method.equals("deleteById")) {
            iUserService.deleteById((Integer) param);
        } else if (method.equals("update")) {
            result = iUserService.update(param);
        } else if (method.equals("getByFilter")) {
            result = iUserService.getByFilter((User)param);
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
