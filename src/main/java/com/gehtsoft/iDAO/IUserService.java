package com.gehtsoft.iDAO;

import com.gehtsoft.core.User;

/**
 * Created by dkuzmin on 7/11/2016.
 */
public interface IUserService extends IBasicService{

    User getByFilter(User user) throws Exception;
}
