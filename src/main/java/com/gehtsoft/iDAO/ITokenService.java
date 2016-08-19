package com.gehtsoft.iDAO;

/**
 * Created by dkuzmin on 8/17/2016.
 */
public interface ITokenService extends IBasicService {
    void deleteExpired() throws Exception;
}
