package com.gehtsoft.mySQL.DAO;

import com.gehtsoft.iDAO.IBasicService;
import com.gehtsoft.mySQL.databaseConnection.DBConnectionPoolSingleton;
import com.gehtsoft.core.Singer;
import com.gehtsoft.mySQL.databaseConnection.DBConnection;
import org.apache.log4j.Logger;

import javax.naming.NamingException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dkuzmin on 7/6/2016.
 */
public class SingerService implements IBasicService {

    final static Logger logger = Logger.getLogger("resource");

    public Singer getFromResultSet(ResultSet resultSet) throws SQLException, ParseException {
        Singer singer = new Singer();
        singer.setId(resultSet.getInt("id"));
        singer.setName(resultSet.getString("fldName"));
        singer.setDescription(resultSet.getString("fldDescription"));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        singer.setBirthDate(simpleDateFormat.parse(resultSet.getString("fldBirthDate")));

        if(logger.isDebugEnabled()){
            logger.debug("Singer from result set: " + singer);
        }
        return singer;
    }

    @Override
    public List<Singer> getAll() throws SQLException, NamingException, ParseException {
        if (logger.isDebugEnabled()) {
            logger.debug("Started.");
        }
        DBConnection dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        while (dbConnection == null) {
            dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        }
        List<Singer> singers = new ArrayList<>();
        String query = "select * from tbMUSSinger";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            singers.add(getFromResultSet(resultSet));
        }

        DBConnectionPoolSingleton.getInstance().retrieveDBConnection(dbConnection);
        if (logger.isDebugEnabled()) {
            logger.debug("Result of function singers: " + singers);
        }
        return singers;
    }

    @Override
    public Singer getById(Integer id) throws SQLException, NamingException, ParseException {
        if (logger.isDebugEnabled()) {
            logger.debug("Started with param id="+id);
        }
        DBConnection dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        while (dbConnection == null) {
            dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        }
        String query = "select * from tbMUSSinger where id = ?";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        Singer singer = null;
        if(resultSet.next()) {
            singer = getFromResultSet(resultSet);
        }
        DBConnectionPoolSingleton.getInstance().retrieveDBConnection(dbConnection);
        if (logger.isDebugEnabled()) {
            logger.debug("Result of function singer: " + singer);
        }
        return singer;
    }

    @Override
    public void deleteById(Integer id) throws SQLException, NamingException {
        if (logger.isDebugEnabled()) {
            logger.debug("Started with param id="+id);
        }
        DBConnection dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        while (dbConnection == null) {
            dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        }
        dbConnection.setAutoCommit(false);
        String query = "delete from tbMUSSinger where id = ?";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
        dbConnection.commit();
        DBConnectionPoolSingleton.getInstance().retrieveDBConnection(dbConnection);
        if (logger.isDebugEnabled()) {
            logger.debug("Result of function deleted album with id=" + id);
        }
    }

    @Override
    public Singer add(Object object) throws SQLException, NamingException {
        Singer singer = (Singer) object;
        if (logger.isDebugEnabled()) {
            logger.debug("Started with param singer="+singer);
        }
        DBConnection dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        while (dbConnection == null) {
            dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        }
        dbConnection.setAutoCommit(false);

        String query = "insert into tbMUSSinger (fldName, fldDescription, fldBirthDate) values(?, ?, ?)";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, singer.getName());
        preparedStatement.setString(2, singer.getDescription());
        java.sql.Date sqlDate = new java.sql.Date(singer.getBirthDate().getTime());
        preparedStatement.setDate(3, sqlDate);
        preparedStatement.executeUpdate();
        try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                singer.setId(generatedKeys.getInt(1));
            }
            else {
                throw new SQLException("Creating singer failed, no id obtained.");
            }
        }
        dbConnection.commit();
        DBConnectionPoolSingleton.getInstance().retrieveDBConnection(dbConnection);
        if (logger.isDebugEnabled()) {
            logger.debug("Result of function singer: " + singer);
        }
        return singer;
    }

    @Override
    public Singer update(Object object) throws SQLException, NamingException {
        Singer singer = (Singer) object;
        if (logger.isDebugEnabled()) {
            logger.debug("Started with param singer="+singer);
        }
        DBConnection dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        while (dbConnection == null) {
            dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        }
        dbConnection.setAutoCommit(false);
        String query = "update tbMUSSinger set fldName = ?, fldDescription = ?, fldBirthDate = ? where id = ?";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
        preparedStatement.setString(1, singer.getName());
        preparedStatement.setString(2, singer.getDescription());
        java.sql.Date sqlDate = new java.sql.Date(singer.getBirthDate().getTime());
        preparedStatement.setDate(3, sqlDate);
        preparedStatement.setInt(4, singer.getId());
        preparedStatement.executeUpdate();
        dbConnection.commit();
        DBConnectionPoolSingleton.getInstance().retrieveDBConnection(dbConnection);
        if (logger.isDebugEnabled()) {
            logger.debug("Result of function singer: " + singer);
        }
        return singer;
    }
}
