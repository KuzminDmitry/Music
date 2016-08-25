package com.gehtsoft.threadPool;

import com.gehtsoft.factory.EntityFactory;
import com.gehtsoft.iDAO.IBasicService;
import org.apache.log4j.Logger;

import java.util.concurrent.Callable;

/**
 * Created by dkuzmin on 8/15/2016.
 */
public class BasicCallable implements Callable<Object> {

    final static Logger logger = Logger.getLogger("resource");

    private IBasicService basicService;

    private Object param;
    private String method;

    public BasicCallable(Class c, String method, Object param) {
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
        basicService = EntityFactory.getBasicService(c);
    }

    @Override
    public Object call() throws Exception {
        if(logger.isDebugEnabled()) {
            logger.debug(Thread.currentThread().getName() + " started.");
        }
        Object result = null;
        if (method.equals("getAll")) {
            result = basicService.getAll();
        } else if (method.equals("getById")) {
            result = basicService.getById((Integer) param);
        } else if (method.equals("add")) {
            result = basicService.add(param);
        } else if (method.equals("deleteById")) {
            basicService.deleteById((Integer) param);
        } else if (method.equals("update")) {
            result = basicService.update(param);
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
