package com.gehtsoft.token;

import com.gehtsoft.configProperties.ConfigProperties;
import com.gehtsoft.crypto.signature.ISignature;
import com.gehtsoft.core.User;
import com.gehtsoft.date.IDateService;
import com.gehtsoft.factory.DateFactory;
import com.gehtsoft.factory.SecurityFactory;
import com.gehtsoft.threadPool.ThreadPoolSingleton;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by dkuzmin on 7/28/2016.
 */
public class TokenMemorySingleton {

    final static Logger logger = Logger.getLogger("authenticate");

    private static volatile TokenMemorySingleton instance;

    Properties properties = ConfigProperties.getProperties();
    Integer tokenExpireTimer = Integer.parseInt(properties.getProperty("database.timer.token.expire.second"));
    Integer tokenLifeTimeInMonth = Integer.parseInt(properties.getProperty("database.token.lifetime.month"));

    private Timer timer = new Timer();

    public List<Token> getTokens() {
        return tokens;
    }

    private List<Token> tokens = new ArrayList<>();

    private ISignature signature = SecurityFactory.getTokenSignature();

    private IDateService dateService = DateFactory.getDateService();

    private TokenMemorySingleton() throws Exception {
        logger.info("Started.");
        logger.info("Properties: " + "tokenExpireTimer=" + tokenExpireTimer + "; " + "tokenLifeTimeInMonth=" + tokenLifeTimeInMonth);
        logger.info("Schedule: delete expired tokens every " + tokenExpireTimer + " seconds.");
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    deleteExpiredTokens();
                    ThreadPoolSingleton.getInstance().authThread(Token.class, "deleteExpired", null);
                }catch (Exception e){
                  if(logger.isDebugEnabled()){
                      logger.debug(e.getStackTrace());
                  }
                }
            }
        }, tokenExpireTimer * 1000, tokenExpireTimer * 1000);
        this.tokens = (List)ThreadPoolSingleton.getInstance().authThread(Token.class, "getAll", null);
    }

    public static TokenMemorySingleton getInstance() throws Exception{
        if (instance == null) {
            synchronized (TokenMemorySingleton.class) {
                if (instance == null) {
                    instance = new TokenMemorySingleton();
                }
            }
        }
        return instance;
    }
    private Token tokenExist(User user){
        for(Token token : this.tokens){
            if(token.getUserName().equals(user.getUserName())){
                logger.info("Token exist: " + token);
                return token;
            }
        }
        logger.info("Token does not exist.");
        return null;
    }

    public Token addToken(User user) throws Exception {
        Token token = tokenExist(user);
        Date now = dateService.getNow();
        if(token != null) {
            if (now.after(token.getExpirationDate()) && token.getExpirationDate().before(now)) {
                this.tokens.remove(token);
                ThreadPoolSingleton.getInstance().authThread(Token.class, "deleteById", token.getId());
                logger.info("Expired user's token was deleted: " + token);
                token = null;
            }
        }
        if(token == null) {
            token = new Token();
            token.setUserId(user.getId());
            token.setUserName(user.getUserName());
            token.setRoleIds(user.getRoleIds());
            token.setRoleNames(user.getRoleNames());
            token.setJwt(signature.sign(user.getUserName() + user.getPassword()));
            //Date
            token.setExpirationDate(dateService.getDate(Calendar.MONTH, tokenLifeTimeInMonth));
            token = (Token) ThreadPoolSingleton.getInstance().authThread(Token.class, "add", token);
            if (token.getId() != null) {
                this.tokens.add(token);
                logger.info("User's token was created: " + token);
            } else {
                logger.info("User's token was not created because id is null: " + token);
                return null;
            }
        }
        return token;
    }

    public void deleteToken(Token tokenForRemove) throws Exception{
        for(Token token : this.tokens){
            if(token.getJwt().equals(tokenForRemove.getJwt())){
                this.tokens.remove(token);
                ThreadPoolSingleton.getInstance().authThread(Token.class, "deleteById", token.getId());
                logger.info("User's token was deleted: " + token);
                return;
            }
        }
    }

    public void deleteExpiredTokens()throws Exception {
        logger.info("Start deleting expired tokens.");
        for (Token token : this.tokens) {
            Date now = dateService.getNow();
            if (now.after(token.getExpirationDate()) && token.getExpirationDate().before(now)) {
                this.tokens.remove(token);
                ThreadPoolSingleton.getInstance().authThread(Token.class, "deleteById", token.getId());
                logger.info("Expired user's token was deleted: " + token);
            }
        }
    }

    public Token getToken(String jwt) throws Exception{
        for(Token token : this.tokens){
            if(token.getJwt().equals(jwt)){
                Date now = dateService.getNow();
                if(now.after(token.getExpirationDate()) && token.getExpirationDate().before(now)){
                    this.tokens.remove(token);
                    ThreadPoolSingleton.getInstance().authThread(Token.class, "deleteById", token.getId());
                    logger.info("Expired user's token was deleted: " + token);
                    return null;
                }
                logger.info("User's token was found: " + token);
                return token;
            }
        }
        logger.info("User's token was not found.");
        return null;
    }
}
