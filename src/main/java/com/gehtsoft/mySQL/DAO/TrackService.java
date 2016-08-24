package com.gehtsoft.mySQL.DAO;

import com.gehtsoft.iDAO.ITrackService;
import com.gehtsoft.mySQL.databaseConnection.DBConnectionPoolSingleton;
import com.gehtsoft.core.Track;
import com.gehtsoft.mySQL.databaseConnection.DBConnection;
import com.mysql.jdbc.Statement;
import org.apache.log4j.Logger;

import javax.naming.NamingException;
import java.sql.*;
import java.sql.PreparedStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by dkuzmin on 7/6/2016.
 */
public class TrackService implements ITrackService {

    final static Logger logger = Logger.getLogger("resource");

    public Track getFromResultSet(ResultSet resultSet) throws SQLException, ParseException {
        Track track = new Track();
        track.setId(resultSet.getInt("trackId"));
        track.setName(resultSet.getString("trackName"));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        track.setReleaseDate(simpleDateFormat.parse(resultSet.getString("trackReleaseDate")));
        //Parse singerIds
        String singerIdsString = resultSet.getString("singerIds");
        List<Integer> singerIds = new ArrayList<>();
        if (singerIdsString != null) {
            List<String> temp = Arrays.asList(singerIdsString.split(","));
            for (String s : temp) singerIds.add(Integer.valueOf(s));
        } else {
            singerIds = new ArrayList<>();
        }
        //
        track.setSingerIds(singerIds);
        //
        //Parse singerIds
        String singerNamesString = resultSet.getString("singerNames");
        List<String> singerNames = new ArrayList<>();
        if (singerNamesString != null) {
            singerNames = Arrays.asList(singerNamesString.split(","));
        } else {
            singerNames = new ArrayList<>();
        }
        //
        track.setSingerNames(singerNames);
        //
        track.setAlbumId(resultSet.getInt("albumId"));
        if(resultSet.wasNull()) {
            track.setAlbumId(null);
        }
        track.setAlbumName(resultSet.getString("albumName"));
        track.setLabelId(resultSet.getInt("labelId"));
        track.setLabelName(resultSet.getString("labelName"));
        track.setGenreId(resultSet.getInt("genreId"));
        track.setGenreName(resultSet.getString("genreName"));
        if(logger.isDebugEnabled()){
            logger.debug("Track from result set: " + track);
        }
        return track;
    }

    @Override
    public List<Track> getAll() throws SQLException, NamingException, ParseException {
        if (logger.isDebugEnabled()) {
            logger.debug("Started.");
        }
        DBConnection dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        while (dbConnection == null) {
            dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        }
        List<Track> tracks = new ArrayList<>();
        String query = "select * from trackInfo";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            tracks.add(getFromResultSet(resultSet));
        }
        DBConnectionPoolSingleton.getInstance().retrieveDBConnection(dbConnection);
        if (logger.isDebugEnabled()) {
            logger.debug("Result of function tracks: " + tracks);
        }
        return tracks;
    }

    @Override
    public List<Track> getAllByFilter(Track track) throws SQLException, NamingException, ParseException {
        if (logger.isDebugEnabled()) {
            logger.debug("Started with param track="+track);
        }
        DBConnection dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        while (dbConnection == null) {
            dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        }
        List<Track> tracks = new ArrayList<>();
        String query = "select * from trackInfo where (genreId = ? or ? is null) and (labelId = ? or ? is null) and (albumId = ? or ? is null) and (? in (singerIds) or ? is null);";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
        //Genre
        if(track.getGenreId() == null) {
            preparedStatement.setNull(1, java.sql.Types.VARCHAR);
            preparedStatement.setNull(2, java.sql.Types.VARCHAR);
        }else {
            preparedStatement.setInt(1, track.getGenreId());
            preparedStatement.setInt(2, track.getGenreId());
        }
        //Label
        if(track.getLabelId() == null) {
            preparedStatement.setNull(3, java.sql.Types.VARCHAR);
            preparedStatement.setNull(4, java.sql.Types.VARCHAR);
        }else {
            preparedStatement.setInt(3, track.getLabelId());
            preparedStatement.setInt(4, track.getLabelId());
        }
        //Album
        if(track.getAlbumId() == null) {
            preparedStatement.setNull(5, java.sql.Types.VARCHAR);
            preparedStatement.setNull(6, java.sql.Types.VARCHAR);
        }else {
            preparedStatement.setInt(5, track.getAlbumId());
            preparedStatement.setInt(6, track.getAlbumId());
        }
        //Singer
        if(track.getSingerIds().get(0) == null) {
            preparedStatement.setNull(7, java.sql.Types.VARCHAR);
            preparedStatement.setNull(8, java.sql.Types.VARCHAR);
        }else {
            preparedStatement.setInt(7, track.getSingerIds().get(0));
            preparedStatement.setInt(8, track.getSingerIds().get(0));
        }
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            tracks.add(getFromResultSet(resultSet));
        }
        DBConnectionPoolSingleton.getInstance().retrieveDBConnection(dbConnection);
        if (logger.isDebugEnabled()) {
            logger.debug("Result of function tracks: " + tracks);
        }
        return tracks;
    }

    @Override
    public Track getById(Integer id) throws SQLException, NamingException, ParseException {
        if (logger.isDebugEnabled()) {
            logger.debug("Started with param id="+id);
        }
        DBConnection dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        while (dbConnection == null) {
            dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
        }
        String query = "select * from trackInfo where trackId = ?";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        Track track = null;
        if (resultSet.next()) {
            track = getFromResultSet(resultSet);
        }
        DBConnectionPoolSingleton.getInstance().retrieveDBConnection(dbConnection);
        if (logger.isDebugEnabled()) {
            logger.debug("Result of function track: " + track);
        }
        return track;
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
        String query = "delete from tbMUSTrackNNSinger where fldLinkTrack = ?";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
        query = "delete from tbMUSTrack where id = ?";
        preparedStatement = dbConnection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
        dbConnection.commit();
        DBConnectionPoolSingleton.getInstance().retrieveDBConnection(dbConnection);
        if (logger.isDebugEnabled()) {
            logger.debug("Result of function deleted track with id=" + id);
        }
    }

    @Override
    public Track add(Object object) throws SQLException, NamingException {
        Track track = (Track) object;
        if(track.getSingerIds().size() != 0) {
            if (logger.isDebugEnabled()) {
                logger.debug("Started with param track="+track);
            }
            DBConnection dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
            while (dbConnection == null) {
                dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
            }
            dbConnection.setAutoCommit(false);
            String query = "insert into tbMUSTrack (fldName, fldReleaseDate, fldLinkLabel, fldLinkGenre, fldLinkAlbum) values(?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = dbConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, track.getName());
            java.sql.Date sqlDate = new java.sql.Date(track.getReleaseDate().getTime());
            preparedStatement.setDate(2, sqlDate);
            preparedStatement.setInt(3, track.getLabelId());
            preparedStatement.setInt(4, track.getGenreId());
            if (track.getAlbumId() != null) {
                preparedStatement.setInt(5, track.getAlbumId());
            } else {
                preparedStatement.setNull(5, java.sql.Types.VARCHAR);
            }
            preparedStatement.executeUpdate();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    track.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating singer failed, no id obtained.");
                }
            }
            query = "insert into tbMUSTrackNNSinger (fldLinkSinger, fldLinkTrack) values (?, ?)";
            for (Integer i : track.getSingerIds()) {
                preparedStatement = dbConnection.prepareStatement(query);
                preparedStatement.setInt(1, i);
                preparedStatement.setInt(2, track.getId());
                preparedStatement.executeUpdate();
            }
            dbConnection.commit();
            DBConnectionPoolSingleton.getInstance().retrieveDBConnection(dbConnection);
            if (logger.isDebugEnabled()) {
                logger.debug("Result of function track: " + track);
            }
            return track;
        }
        else{
            return null;
        }
    }

    @Override
    public Track update(Object object) throws SQLException, NamingException {
        Track track = (Track) object;
        if(track.getSingerIds().size() != 0) {
            if (logger.isDebugEnabled()) {
                logger.debug("Started with param track="+track);
            }
            DBConnection dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
            while (dbConnection == null) {
                dbConnection = DBConnectionPoolSingleton.getInstance().getDBConnection();
            }
            dbConnection.setAutoCommit(false);
            String query = "update tbMUSTrack set fldName = ?, fldReleaseDate = ?, fldLinkLabel = ?, fldLinkGenre = ?, fldLinkAlbum = ? where id = ?";
            PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
            preparedStatement.setString(1, track.getName());
            java.sql.Date sqlDate = new java.sql.Date(track.getReleaseDate().getTime());
            preparedStatement.setDate(2, sqlDate);
            preparedStatement.setInt(3, track.getLabelId());
            preparedStatement.setInt(4, track.getGenreId());
            if (track.getAlbumId() != null) {
                preparedStatement.setInt(5, track.getAlbumId());
            } else {
                preparedStatement.setNull(5, java.sql.Types.VARCHAR);
            }
            preparedStatement.setInt(6, track.getId());
            preparedStatement.executeUpdate();
            //
            query = "delete from tbMUSTrackNNSinger where fldLinkTrack = ?";
            preparedStatement = dbConnection.prepareStatement(query);
            preparedStatement.setInt(1, track.getId());
            preparedStatement.executeUpdate();
            //
            query = "insert into tbMUSTrackNNSinger (fldLinkSinger, fldLinkTrack) values (?, ?)";
            for (Integer i : track.getSingerIds()) {
                preparedStatement = dbConnection.prepareStatement(query);
                preparedStatement.setInt(1, i);
                preparedStatement.setInt(2, track.getId());
                preparedStatement.executeUpdate();
            }
            dbConnection.commit();
            DBConnectionPoolSingleton.getInstance().retrieveDBConnection(dbConnection);
            if (logger.isDebugEnabled()) {
                logger.debug("Result of function track: " + track);
            }
            return track;
        }else {
            return null;
        }
    }
}