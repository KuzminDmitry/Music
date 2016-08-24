package com.gehtsoft.mySQL.DAO;

import com.gehtsoft.iDAO.IBasicService;
import com.gehtsoft.mySQL.databaseConnection.DBConnectionPoolSingleton;
import com.gehtsoft.core.Album;
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
public class AlbumService implements IBasicService {

    final static Logger logger = Logger.getLogger("resource");

    public Album getFromResultSet(ResultSet resultSet) throws SQLException, ParseException {
        Album album = new Album();
        album.setId(resultSet.getInt("id"));
        album.setName(resultSet.getString("fldName"));
        album.setDescription(resultSet.getString("fldDescription"));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        album.setReleaseDate(simpleDateFormat.parse(resultSet.getString("fldReleaseDate")));
        if(logger.isDebugEnabled()){
            logger.debug("Album from result set: " + album);
        }
        return album;
    }

    @Override
    public List<Album> getAll() throws SQLException, NamingException, ParseException {
        if (logger.isDebugEnabled()) {
            logger.debug("Started.");
        }
        DBConnection dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        while (dbConnection == null) {
            dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        }
        List<Album> albums = new ArrayList<>();
        String query = "select * from tbMUSAlbum";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            albums.add(getFromResultSet(resultSet));
        }
        DBConnectionPoolSingleton.getInstance().retrieveDBConnection(dbConnection);
        if (logger.isDebugEnabled()) {
            logger.debug("Result of function albums: " + albums);
        }
        return albums;
    }

    @Override
    public Album getById(Integer id) throws SQLException, NamingException, ParseException {
        if (logger.isDebugEnabled()) {
            logger.debug("Started with param id="+id);
        }
        DBConnection dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        while (dbConnection == null) {
            dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        }
        String query = "select * from tbMUSAlbum where id = ?";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        Album album = null;
        if(resultSet.next()) {
            album = getFromResultSet(resultSet);
        }
        DBConnectionPoolSingleton.getInstance().retrieveDBConnection(dbConnection);
        if (logger.isDebugEnabled()) {
            logger.debug("Result of function album: " + album);
        }
        return album;
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
        String query = "delete from tbMUSAlbum where id = ?";
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
    public Album add(Object object) throws SQLException, NamingException {
        Album album = (Album) object;
        if (logger.isDebugEnabled()) {
            logger.debug("Started with param album="+album);
        }
        DBConnection dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        while (dbConnection == null) {
            dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        }
        dbConnection.setAutoCommit(false);
        String query = "insert into tbMUSAlbum (fldName, fldDescription, fldReleaseDate) values(?, ?, ?)";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, album.getName());
        preparedStatement.setString(2, album.getDescription());
        java.sql.Date sqlDate = new java.sql.Date(album.getReleaseDate().getTime());
        preparedStatement.setDate(3, sqlDate);
        preparedStatement.executeUpdate();
        try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                album.setId(generatedKeys.getInt(1));
            }
            else {
                throw new SQLException("Creating album failed, no id obtained.");
            }
        }
        dbConnection.commit();
        DBConnectionPoolSingleton.getInstance().retrieveDBConnection(dbConnection);
        if (logger.isDebugEnabled()) {
            logger.debug("Result of function album: " + album);
        }
        return album;
    }

    @Override
    public Album update(Object object) throws SQLException, NamingException {
        Album album = (Album) object;
        if (logger.isDebugEnabled()) {
            logger.debug("Started with param album="+album);
        }
        DBConnection dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        while (dbConnection == null) {
            dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        }
        dbConnection.setAutoCommit(false);

        String query = "update tbMUSAlbum set fldName = ?, fldDescription = ?, fldReleaseDate = ? where id = ?";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
        preparedStatement.setString(1, album.getName());
        preparedStatement.setString(2, album.getDescription());
        java.sql.Date sqlDate = new java.sql.Date(album.getReleaseDate().getTime());
        preparedStatement.setDate(3, sqlDate);
        preparedStatement.setInt(4, album.getId());
        preparedStatement.executeUpdate();
        dbConnection.commit();
        DBConnectionPoolSingleton.getInstance().retrieveDBConnection(dbConnection);
        if (logger.isDebugEnabled()) {
            logger.debug("Result of function album: " + album);
        }
        return album;
    }
}
