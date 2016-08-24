package com.gehtsoft.mySQL.DAO;

import com.gehtsoft.core.Genre;
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
 * Created by dkuzmin on 7/6/2016.
 */
public class GenreService implements IBasicService {

    final static Logger logger = Logger.getLogger("resource");

    public Genre getFromResultSet(ResultSet resultSet) throws SQLException {
        Genre genre = new Genre();
        genre.setId(resultSet.getInt("id"));
        genre.setName(resultSet.getString("fldName"));
        genre.setDescription(resultSet.getString("fldDescription"));
        if(logger.isDebugEnabled()){
            logger.debug("Genre from result set: " + genre);
        }
        return genre;
    }

    @Override
    public List<Genre> getAll() throws SQLException, NamingException {
        if (logger.isDebugEnabled()) {
            logger.debug("Started.");
        }
        DBConnection dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        while (dbConnection == null) {
            dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        }
        List<Genre> genres = new ArrayList<>();
        String query = "select * from tbMUSGenre";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            genres.add(getFromResultSet(resultSet));
        }
        DBConnectionPoolSingleton.getInstance().retrieveDBConnection(dbConnection);
        if (logger.isDebugEnabled()) {
            logger.debug("Result of function genres: " + genres);
        }
        return genres;
    }

    @Override
    public Genre getById(Integer id) throws SQLException, NamingException {
        if (logger.isDebugEnabled()) {
            logger.debug("Started with param id="+id);
        }
        DBConnection dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        while (dbConnection == null) {
            dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        }
        String query = "select * from tbMUSGenre where id = ?";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        Genre genre = null;
        if(resultSet.next()) {
            genre = getFromResultSet(resultSet);
        }
        DBConnectionPoolSingleton.getInstance().retrieveDBConnection(dbConnection);
        if (logger.isDebugEnabled()) {
            logger.debug("Result of function genre: " + genre);
        }
        return genre;
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

        String query = "delete from tbMUSGenre where id = ?";
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
    public Genre add(Object object) throws SQLException, NamingException {
        Genre genre = (Genre) object;
        if (logger.isDebugEnabled()) {
            logger.debug("Started with param genre="+genre);
        }
        DBConnection dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        while (dbConnection == null) {
            dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        }
        dbConnection.setAutoCommit(false);

        String query = "insert into tbMUSGenre (fldName, fldDescription) values(?, ?)";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, genre.getName());
        preparedStatement.setString(2, genre.getDescription());
        preparedStatement.executeUpdate();
        try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                genre.setId(generatedKeys.getInt(1));
            }
            else {
                throw new SQLException("Creating genre failed, no id obtained.");
            }
        }

        dbConnection.commit();
        DBConnectionPoolSingleton.getInstance().retrieveDBConnection(dbConnection);
        if (logger.isDebugEnabled()) {
            logger.debug("Result of function genre: " + genre);
        }
        return genre;
    }

    @Override
    public Genre update(Object object) throws SQLException, NamingException {
        Genre genre = (Genre) object;
        if (logger.isDebugEnabled()) {
            logger.debug("Started with param genre="+genre);
        }
        DBConnection dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        while (dbConnection == null) {
            dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        }
        dbConnection.setAutoCommit(false);

        String query = "update tbMUSGenre set fldName = ?, fldDescription = ? where id = ?";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
        preparedStatement.setString(1, genre.getName());
        preparedStatement.setString(2, genre.getDescription());
        preparedStatement.setInt(3, genre.getId());
        Integer result = preparedStatement.executeUpdate();

        dbConnection.commit();
        DBConnectionPoolSingleton.getInstance().retrieveDBConnection(dbConnection);
        if (logger.isDebugEnabled()) {
            logger.debug("Result of function genre: " + genre);
        }
        return genre;
    }


}
