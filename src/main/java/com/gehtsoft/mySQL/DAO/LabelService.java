package com.gehtsoft.mySQL.DAO;

import com.gehtsoft.iDAO.IBasicService;
import com.gehtsoft.mySQL.databaseConnection.DBConnectionPoolSingleton;
import com.gehtsoft.core.Label;
import com.gehtsoft.mySQL.databaseConnection.DBConnection;
import org.apache.log4j.Logger;

import javax.naming.NamingException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dkuzmin on 7/6/2016.
 */
public class LabelService implements IBasicService {

    final static Logger logger = Logger.getLogger("DAO");

    public Label getFromResultSet(ResultSet resultSet) throws SQLException {
        if(logger.isDebugEnabled()){
            logger.debug("Result set: " + resultSet);
        }
        Label label = new Label();
        label.setId(resultSet.getInt("id"));
        label.setName(resultSet.getString("fldName"));
        if(logger.isDebugEnabled()){
            logger.debug("Label from result set: " + label);
        }
        return label;
    }

    @Override
    public List<Label> getAll() throws SQLException, NamingException {
        if (logger.isDebugEnabled()) {
            logger.debug("Started.");
        }
        DBConnection dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        while (dbConnection == null) {
            dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        }
        List<Label> labels = new ArrayList<>();
        String query = "select * from tbMUSLabel";
        PreparedStatement prepareStatement = dbConnection.prepareStatement(query);
        ResultSet resultSet = prepareStatement.executeQuery();
        while (resultSet.next()) {
            labels.add(getFromResultSet(resultSet));
        }
        DBConnectionPoolSingleton.getInstance().retrieveDBConnection(dbConnection);
        if (logger.isDebugEnabled()) {
            logger.debug("Result of function labels: " + labels);
        }
        return labels;
    }

    @Override
    public Label getById(Integer id) throws SQLException, NamingException {
        if (logger.isDebugEnabled()) {
            logger.debug("Started.");
        }
        DBConnection dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        while (dbConnection == null) {
            dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        }
        String query = "select * from tbMUSLabel where id = ?";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        Label label = null;
        if(resultSet.next()) {
            label = getFromResultSet(resultSet);
        }
        DBConnectionPoolSingleton.getInstance().retrieveDBConnection(dbConnection);
        if (logger.isDebugEnabled()) {
            logger.debug("Result of function label: " + label);
        }
        return label;
    }

    @Override
    public void deleteById(Integer id) throws SQLException, NamingException {
        if (logger.isDebugEnabled()) {
            logger.debug("Started.");
        }
        DBConnection dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        while (dbConnection == null) {
            dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        }
        dbConnection.setAutoCommit(false);
        String query = "delete from tbMUSLabel where id = ?";
        PreparedStatement st = dbConnection.prepareStatement(query);
        st.setInt(1, id);
        st.executeUpdate();
        dbConnection.commit();
        DBConnectionPoolSingleton.getInstance().retrieveDBConnection(dbConnection);
        if (logger.isDebugEnabled()) {
            logger.debug("Result of function deleted album with id=" + id);
        }
    }

    @Override
    public Label add(Object object) throws SQLException, NamingException {
        Label label = (Label) object;
        if (logger.isDebugEnabled()) {
            logger.debug("Started with param label="+label);
        }
        DBConnection dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        while (dbConnection == null) {
            dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        }
        dbConnection.setAutoCommit(false);

        String query = "insert into tbMUSLabel (fldName) values (?)";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, label.getName());
        preparedStatement.executeUpdate();
        try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                label.setId(generatedKeys.getInt(1));
            }
            else {
                throw new SQLException("Creating label failed, no id obtained.");
            }
        }
        dbConnection.commit();
        DBConnectionPoolSingleton.getInstance().retrieveDBConnection(dbConnection);
        if (logger.isDebugEnabled()) {
            logger.debug("Result of function label: " + label);
        }
        return label;
    }

    @Override
    public Label update(Object object) throws SQLException, NamingException {
        Label label = (Label) object;
        if (logger.isDebugEnabled()) {
            logger.debug("Started with param label="+label);
        }
        DBConnection dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        while (dbConnection == null) {
            dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        }
        dbConnection.setAutoCommit(false);
        String query = "update tbMUSLabel set fldName = ? where id = ?";
        PreparedStatement st = dbConnection.prepareStatement(query);
        st.setString(1, label.getName());
        st.setInt(2, label.getId());
        st.executeUpdate();
        dbConnection.commit();
        DBConnectionPoolSingleton.getInstance().retrieveDBConnection(dbConnection);
        if (logger.isDebugEnabled()) {
            logger.debug("Result of function label: " + label);
        }
        return label;
    }
}
