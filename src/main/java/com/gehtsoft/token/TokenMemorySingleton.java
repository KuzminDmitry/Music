package com.gehtsoft.token;

import com.gehtsoft.configProperties.ConfigProperties;
import com.gehtsoft.crypto.signature.ISignature;
import com.gehtsoft.core.User;
import com.gehtsoft.factory.SecurityFactory;
import com.gehtsoft.threadPool.ThreadPoolSingleton;

import java.util.*;

/**
 * Created by dkuzmin on 7/28/2016.
 */
public class TokenMemorySingleton {
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

    private TokenMemorySingleton() throws Exception {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    deleteExpiredTokens();
                    ThreadPoolSingleton.getInstance().authThread(Token.class, "deleteExpired", null);
                }catch (Exception e){
                   e.printStackTrace();
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
                return token;
            }
        }
        return null;
    }

    public Token addToken(User user) throws Exception {
        Token token = tokenExist(user);
        Date now = new Date();
        if(token != null) {
            if (now.after(token.getExpirationDate()) && token.getExpirationDate().before(now)) {
                this.tokens.remove(token);
                ThreadPoolSingleton.getInstance().authThread(Token.class, "deleteById", token.getId());
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
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.MONTH, tokenLifeTimeInMonth);
            token.setExpirationDate(c.getTime());
            //
            token = (Token)ThreadPoolSingleton.getInstance().authThread(Token.class, "add", token);
            if(token.getId() != null) {
                this.tokens.add(token);
            }else{
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
                return;
            }
        }
    }

    public void deleteExpiredTokens()throws Exception {
        for (Token token : this.tokens) {
            Date now = new Date();
            if (now.after(token.getExpirationDate()) && token.getExpirationDate().before(now)) {
                this.tokens.remove(token);
                ThreadPoolSingleton.getInstance().authThread(Token.class, "deleteById", token.getId());
            }
        }
    }

    public Token getToken(String jwt) throws Exception{
        for(Token token : this.tokens){
            if(token.getJwt().equals(jwt)){
                Date now = new Date();
                if(now.after(token.getExpirationDate()) && token.getExpirationDate().before(now)){
                    this.tokens.remove(token);
                    ThreadPoolSingleton.getInstance().authThread(Token.class, "deleteById", token.getId());
                    return null;
                }
                return token;
            }
        }
        return null;
    }
}
