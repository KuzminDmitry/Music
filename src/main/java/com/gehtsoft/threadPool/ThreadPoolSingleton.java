package com.gehtsoft.threadPool;
import com.gehtsoft.configProperties.ConfigProperties;
import org.apache.log4j.Logger;

import java.util.Properties;
import java.util.concurrent.*;

/**
 * Created by dkuzmin on 7/28/2016.
 */
public class ThreadPoolSingleton {

    final static Logger logger = Logger.getLogger("threadPool");

    private static volatile ThreadPoolSingleton instance;

    private ExecutorService executorService;

    private int requestTimeoutInSeconds;
    private int amountOfThread;

    private ThreadPoolSingleton() {
        logger.info("Started.");
        Properties properties = ConfigProperties.getProperties();
        requestTimeoutInSeconds = Integer.parseInt(properties.getProperty("database.connection.lifetime.second"));
        amountOfThread = Integer.parseInt(properties.getProperty("thread.pool.amount"));
        logger.info("Properties: " + "requestTimeoutInSeconds=" + requestTimeoutInSeconds + "; " + "amountOfThread=" + amountOfThread);
        executorService = Executors.newFixedThreadPool(amountOfThread);
    }

    public static ThreadPoolSingleton getInstance() {
        if (instance == null) {
            synchronized (ThreadPoolSingleton.class) {
                if (instance == null) {
                    instance = new ThreadPoolSingleton();
                }
            }
        }
        return instance;
    }

    public Object basicThread(Class expectedEntity, String method, Object param) throws Exception {
        restartExecutorServiceIfShutdown();
        BasicCallable basicCallable = new BasicCallable(expectedEntity, method, param);
        Callable<Object> callable = basicCallable;
        Future<Object> future = executorService.submit(callable);
        try {
            return future.get(requestTimeoutInSeconds, TimeUnit.SECONDS);
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            future.cancel(true);
            logger.error("Request timeout!");
            if(logger.isDebugEnabled()) {
                logger.debug(e.getMessage());
            }
            throw new Exception("Request timeout!");
        }
    }

    public Object trackThread(Class expectedEntity, String method, Object param) throws Exception {
        restartExecutorServiceIfShutdown();
        TrackCallable trackCallable = new TrackCallable(expectedEntity, method, param);
        Callable<Object> callable = trackCallable;
        Future<Object> future = executorService.submit(callable);
        try {
            return future.get(requestTimeoutInSeconds, TimeUnit.SECONDS);
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            future.cancel(true);
            logger.error("Request timeout!");
            if(logger.isDebugEnabled()) {
                logger.debug(e.getMessage());
            }
            throw new Exception("Request timeout!");
        }
    }

    public Object userThread(Class expectedEntity, String method, Object param) throws Exception {
        restartExecutorServiceIfShutdown();
        UserCallable userCallable = new UserCallable(expectedEntity, method, param);
        Callable<Object> callable = userCallable;
        Future<Object> future = executorService.submit(callable);
        try {
            return future.get(requestTimeoutInSeconds, TimeUnit.SECONDS);
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            future.cancel(true);
            logger.error("Request timeout!");
            if(logger.isDebugEnabled()) {
                logger.debug(e.getMessage());
            }
            throw new Exception("Request timeout!");
        }
    }

    public Object authThread(Class expectedEntity, String method, Object param) throws Exception {
        restartExecutorServiceIfShutdown();
        TokenCallable tokenCallable = new TokenCallable(expectedEntity, method, param);
        Callable<Object> callable = tokenCallable;
        Future<Object> future = executorService.submit(callable);
        try {
            return future.get(requestTimeoutInSeconds, TimeUnit.SECONDS);
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            future.cancel(true);
            logger.error("Request timeout!");
            if(logger.isDebugEnabled()) {
                logger.debug(e.getMessage());
            }
            throw new Exception("Request timeout!");
        }
    }

    public void shutdownExecuteService() {
        logger.info("ExecutorService shutdown!");
        executorService.shutdown();
    }

    private void restartExecutorServiceIfShutdown(){
        if (executorService.isShutdown()) {
            logger.warn("ExecutorService was shutdown!");
            logger.info("Restarting...");
            executorService = Executors.newFixedThreadPool(amountOfThread);
            logger.info("ExecutorService restarted!");
        }
    }
}
