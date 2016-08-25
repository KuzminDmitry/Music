package com.gehtsoft.mySQL.DAO;

import com.gehtsoft.iDAO.ITokenService;
import com.gehtsoft.mySQL.databaseConnection.DBConnectionPoolSingleton;
import com.gehtsoft.token.Token;
import com.gehtsoft.mySQL.databaseConnection.DBConnection;
import org.apache.log4j.Logger;

import javax.naming.NamingException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by dkuzmin on 7/6/2016.
 */
public class TokenService implements ITokenService {

    final static Logger logger = Logger.getLogger("authenticate");

    public Token getFromResultSet(ResultSet resultSet) throws SQLException, ParseException {
        Token token = new Token();
        token.setId(resultSet.getInt("id"));
        token.setUserId(resultSet.getInt("userId"));
        token.setUserName(resultSet.getString("userName"));
        //Parse userRoleIds
        String userRoleIdsString = resultSet.getString("userRoleIds");
        List<Integer> userRoleIds = new ArrayList<>();
        if (userRoleIdsString != null) {
            List<String> temp = Arrays.asList(userRoleIdsString.split(","));
            for (String s : temp) userRoleIds.add(Integer.valueOf(s));
        } else {
            userRoleIds = new ArrayList<>();
        }
        //
        token.setRoleIds(userRoleIds);
        //
        //Parse userRoleNames
        String userRoleNamesString = resultSet.getString("userRoleNames");
        List<String> userRoleNames = new ArrayList<>();
        if (userRoleNamesString != null) {
            userRoleNames = Arrays.asList(userRoleNamesString.split(","));
        } else {
            userRoleNames = new ArrayList<>();
        }
        //
        token.setRoleNames(userRoleNames);
        //
        token.setJwt(resultSet.getString("jwt"));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        token.setExpirationDate(simpleDateFormat.parse(resultSet.getString("expirationDate")));
        if(logger.isDebugEnabled()){
            logger.debug("Token from result set: " + token);
        }
        return token;
    }

    @Override
    public List<Token> getAll() throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("Started.");
        }
        DBConnection dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        while (dbConnection == null) {
            dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        }
        List<Token> tokens = new ArrayList<>();
        String query = "select * from authInfo";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            tokens.add(getFromResultSet(resultSet));
        }
        DBConnectionPoolSingleton.getInstance().retrieveDBConnection(dbConnection);
        if (logger.isDebugEnabled()) {
            logger.debug("Result of function tokens: " + tokens);
        }
        return tokens;
    }

    @Override
    public Token getById(Integer id) throws SQLException, NamingException, ParseException {
        if (logger.isDebugEnabled()) {
            logger.debug("Started with param id=" + id);
        }
        DBConnection dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        while (dbConnection == null) {
            dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        }
        String query = "select * from tbMUSAuth where id = ?";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        Token token = null;
        if(resultSet.next()) {
            token = getFromResultSet(resultSet);
        }
        DBConnectionPoolSingleton.getInstance().retrieveDBConnection(dbConnection);
        if (logger.isDebugEnabled()) {
            logger.debug("Result of function token: " + token);
        }
        return token;
    }

    @Override
    public void deleteById(Integer id) throws SQLException, NamingException {
        if (logger.isDebugEnabled()) {
            logger.debug("Started with param id=" + id);
        }
        DBConnection dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        while (dbConnection == null) {
            dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        }
        dbConnection.setAutoCommit(false);
        String query = "delete from tbMUSAuth where id = ?";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
        dbConnection.commit();
        DBConnectionPoolSingleton.getInstance().retrieveDBConnection(dbConnection);
        if (logger.isDebugEnabled()) {
            logger.debug("Result of function deleted token with id=" + id);
        }
    }

    @Override
    public Token add(Object object) throws SQLException, NamingException {
        Token token = (Token) object;
        if (logger.isDebugEnabled()) {
            logger.debug("Started with param token=" + token);
        }
        DBConnection dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        while (dbConnection == null) {
            dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        }
        dbConnection.setAutoCommit(false);
        String query = "insert into tbMUSAuth (fldJWT, fldExpirationDate, fldLinkUser) values(?, ?, ?)";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, token.getJwt());
        java.sql.Date sqlDate = new java.sql.Date(token.getExpirationDate().getTime());
        preparedStatement.setDate(2, sqlDate);
        preparedStatement.setInt(3, token.getUserId());
        preparedStatement.executeUpdate();
        try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                token.setId(generatedKeys.getInt(1));
            }
            else {
                throw new SQLException("Creating token failed, no id obtained.");
            }
        }
        dbConnection.commit();
        DBConnectionPoolSingleton.getInstance().retrieveDBConnection(dbConnection);
        if (logger.isDebugEnabled()) {
            logger.debug("Result of function token: " + token);
        }
        return token;
    }

    @Override
    public Token update(Object object) throws SQLException, NamingException {
        Token token = (Token) object;
        if (logger.isDebugEnabled()) {
            logger.debug("Started with param token=" + token);
        }
        DBConnection dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        while (dbConnection == null) {
            dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        }
        dbConnection.setAutoCommit(false);
        String query = "update tbMUSAuth set fldJWT = ?, fldExpirationDate = ?, fldLinkUser = ? where id = ?";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
        preparedStatement.setString(1, token.getJwt());
        java.sql.Date sqlDate = new java.sql.Date(token.getExpirationDate().getTime());
        preparedStatement.setDate(2, sqlDate);
        preparedStatement.setInt(3, token.getUserId());
        preparedStatement.setInt(4, token.getId());
        Integer result = preparedStatement.executeUpdate();
        dbConnection.commit();
        DBConnectionPoolSingleton.getInstance().retrieveDBConnection(dbConnection);
        if (logger.isDebugEnabled()) {
            logger.debug("Result of function token: " + token);
        }
        return token;
    }

    @Override
    public void deleteExpired() throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("Started");
        }
        DBConnection dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        while (dbConnection == null) {
            dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        }
        dbConnection.setAutoCommit(false);
        String query = "delete from tbMUSAuth where fldExpirationDate < ?";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
        java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
        preparedStatement.setDate(1, sqlDate);
        preparedStatement.executeUpdate();
        dbConnection.commit();
        DBConnectionPoolSingleton.getInstance().retrieveDBConnection(dbConnection);
        if (logger.isDebugEnabled()) {
            logger.debug("Deleted all expired tokens.");
        }
    }
}
