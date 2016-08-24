package com.gehtsoft.factory;

import com.gehtsoft.configProperties.ConfigProperties;
import com.gehtsoft.iDAO.*;
import com.gehtsoft.mySQL.DAO.*;
import com.gehtsoft.xml.GenreServiceInMemoryFromXML;

/**
 * Created by dkuzmin on 7/11/2016.
 */
public class ServiceFactory {

    private static String database = ConfigProperties.getProperties().getProperty("factory.database");

    public static IBasicService getRoleService() {
        if (database.equals("mysql")) {
            return new RoleService();
        } else {
            throw new ExceptionInInitializerError("There are no interface in factory for factory.database " + database + "!");
        }
    }

    public static IUserService getUserService() {
        if (database.equals("mysql")) {
            return new UserService();
        } else {
            throw new ExceptionInInitializerError("There are no interface in factory for factory.database " + database + "!");
        }
    }

    public static IBasicService getAlbumService() {
        if (database.equals("mysql")) {
            return new AlbumService();
        } else {
            throw new ExceptionInInitializerError("There are no interface in factory for factory.database " + database + "!");
        }
    }

    public static ITokenService getAuthService() {
        if (database.equals("mysql")) {
            return new TokenService();
        } else {
            throw new ExceptionInInitializerError("There are no interface in factory for factory.database " + database + "!");
        }
    }

    public static IBasicService getGenreService() {
        if (database.equals("mysql")) {
            return new GenreService();
        } else if (database.equals("xml")) {
            String inputPath = ConfigProperties.getProperties().getProperty("xml.genre.inputPath");
            String outputPath = ConfigProperties.getProperties().getProperty("xml.genre.outputPath");
            String xsdPath = ConfigProperties.getProperties().getProperty("xml.genre.xsdPath");
            return new GenreServiceInMemoryFromXML(inputPath, outputPath, xsdPath);
        } else {
            throw new ExceptionInInitializerError("There are no interface in factory for factory.database " + database + "!");
        }
    }

    public static IBasicService getLabelService() {
        if (database.equals("mysql")) {
            return new LabelService();
        } else {
            throw new ExceptionInInitializerError("There are no interface in factory for factory.database " + database + "!");
        }
    }

    public static IBasicService getSingerService() {
        if (database.equals("mysql")) {
            return new SingerService();
        } else {
            throw new ExceptionInInitializerError("There are no interface in factory for factory.database " + database + "!");
        }
    }

    public static ITrackService getTrackService() {
        if (database.equals("mysql")) {
            return new TrackService();
        } else {
            throw new ExceptionInInitializerError("There are no interface in factory for factory.database " + database + "!");
        }
    }
}
