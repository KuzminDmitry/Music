package com.gehtsoft.mySQL.DAO;

import com.gehtsoft.core.Role;
import com.gehtsoft.iDAO.IBasicService;
import com.gehtsoft.mySQL.databaseConnection.DBConnection;
import com.gehtsoft.mySQL.databaseConnection.DBConnectionPoolSingleton;
import org.apache.log4j.Logger;

import javax.naming.NamingException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dkuzmin on 8/24/2016.
 */
public class RoleService implements IBasicService {

    final static Logger logger = Logger.getLogger("DAO");

    public Role getFromResultSet(ResultSet resultSet) throws SQLException {
        if(logger.isDebugEnabled()){
            logger.debug("Result set: " + resultSet);
        }
        Role role = new Role();
        role.setId(resultSet.getInt("id"));
        role.setName(resultSet.getString("fldName"));
        role.setDescription(resultSet.getString("fldDescription"));
        if(logger.isDebugEnabled()){
            logger.debug("Role from result set: " + role);
        }
        return role;
    }

    @Override
    public List<Role> getAll() throws SQLException, NamingException {
        if (logger.isDebugEnabled()) {
            logger.debug("Started.");
        }
        DBConnection dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        while (dbConnection == null) {
            dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        }
        List<Role> roles = new ArrayList<>();
        String query = "select * from tbMUSRole";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            roles.add(getFromResultSet(resultSet));
        }
        DBConnectionPoolSingleton.getInstance().retrieveDBConnection(dbConnection);
        if (logger.isDebugEnabled()) {
            logger.debug("Result of function roles: " + roles);
        }
        return roles;
    }

    @Override
    public Role getById(Integer id) throws SQLException, NamingException {
        if (logger.isDebugEnabled()) {
            logger.debug("Started with param id="+id);
        }
        DBConnection dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        while (dbConnection == null) {
            dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        }
        String query = "select * from tbMUSRole where id = ?";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        Role role = null;
        if(resultSet.next()) {
            role = getFromResultSet(resultSet);
        }
        DBConnectionPoolSingleton.getInstance().retrieveDBConnection(dbConnection);
        if (logger.isDebugEnabled()) {
            logger.debug("Result of function role: " + role);
        }
        return role;
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

        String query = "delete from tbMUSRole where id = ?";
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
    public Role add(Object object) throws SQLException, NamingException {
        Role role = (Role) object;
        if (logger.isDebugEnabled()) {
            logger.debug("Started with param role="+role);
        }
        DBConnection dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        while (dbConnection == null) {
            dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        }
        dbConnection.setAutoCommit(false);

        String query = "insert into tbMUSRole (fldName, fldDescription) values(?, ?)";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, role.getName());
        preparedStatement.setString(2, role.getDescription());
        preparedStatement.executeUpdate();
        try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                role.setId(generatedKeys.getInt(1));
            }
            else {
                throw new SQLException("Creating role failed, no id obtained.");
            }
        }

        dbConnection.commit();
        DBConnectionPoolSingleton.getInstance().retrieveDBConnection(dbConnection);
        if (logger.isDebugEnabled()) {
            logger.debug("Result of function role: " + role);
        }
        return role;
    }

    @Override
    public Role update(Object object) throws SQLException, NamingException {
        Role role = (Role) object;
        if (logger.isDebugEnabled()) {
            logger.debug("Started with param role="+role);
        }
        DBConnection dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        while (dbConnection == null) {
            dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        }
        dbConnection.setAutoCommit(false);

        String query = "update tbMUSRole set fldName = ?, fldDescription = ? where id = ?";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
        preparedStatement.setString(1, role.getName());
        preparedStatement.setString(2, role.getDescription());
        preparedStatement.setInt(3, role.getId());
        Integer result = preparedStatement.executeUpdate();

        dbConnection.commit();
        DBConnectionPoolSingleton.getInstance().retrieveDBConnection(dbConnection);
        if (logger.isDebugEnabled()) {
            logger.debug("Result of function role: " + role);
        }
        return role;
    }
}
