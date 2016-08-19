package com.gehtsoft.factory;

import com.gehtsoft.configProperties.ConfigProperties;
import com.gehtsoft.iDAO.*;
import com.gehtsoft.mySQL.DAO.*;

/**
 * Created by dkuzmin on 7/11/2016.
 */
public class ServiceFactory {

    private static String database = ConfigProperties.getProperties().getProperty("factory.database");

    public static IUserService getUserService() {
        if (database.equals("mysql")) {
            return new UserService();
        }
        return new UserService();
    }

    public static IBasicService getAlbumService() {
        if (database.equals("mysql")) {
            return new AlbumService();
        }
        return new AlbumService();
    }

    public static ITokenService getAuthService() {
        if (database.equals("mysql")) {
            return new TokenService();
        }
        return new TokenService();
    }

    public static IBasicService getGenreService() {
        if (database.equals("mysql")) {
            return new GenreService();
        }
        return new GenreService();
    }

    public static IBasicService getLabelService() {
        if (database.equals("mysql")) {
            return new LabelService();
        }
        return new LabelService();
    }

    public static IBasicService getSingerService() {
        if (database.equals("mysql")) {
            return new SingerService();
        }
        return new SingerService();
    }

    public static ITrackService getTrackService() {
        if (database.equals("mysql")) {
            return new TrackService();
        }
        return new TrackService();
    }
}
