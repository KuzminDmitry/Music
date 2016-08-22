package com.gehtsoft.factory;

import com.gehtsoft.token.Token;
import com.gehtsoft.core.*;
import com.gehtsoft.iDAO.ITokenService;
import com.gehtsoft.iDAO.IBasicService;
import com.gehtsoft.iDAO.ITrackService;
import com.gehtsoft.iDAO.IUserService;

/**
 * Created by dkuzmin on 8/15/2016.
 */
public class EntityFactory {

    public static IBasicService getBasicService(Class c) {
        if (c.equals(Genre.class)) {
            return ServiceFactory.getGenreService();
        } else if (c.equals(Label.class)) {
            return ServiceFactory.getLabelService();
        } else if (c.equals(Album.class)) {
            return ServiceFactory.getAlbumService();
        } else if (c.equals(Singer.class)) {
            return ServiceFactory.getSingerService();
        } else {
            throw new ExceptionInInitializerError("There are no method in factory for " + c + "!");
        }
    }

    public static ITrackService getTrackService(Class c) {
        if (c.equals(Track.class)) {
            return ServiceFactory.getTrackService();
        } else {
            throw new ExceptionInInitializerError("There are no method in factory for " + c + "!");
        }
    }

    public static IUserService getUserService(Class c) {
        if (c.equals(User.class)) {
            return ServiceFactory.getUserService();
        } else {
            throw new ExceptionInInitializerError("There are no method in factory for " + c + "!");
        }
    }

    public static ITokenService getTokenService(Class c) {
        if (c.equals(Token.class)) {
            return ServiceFactory.getAuthService();
        } else {
            throw new ExceptionInInitializerError("There are no method in factory for " + c + "!");
        }
    }
}
