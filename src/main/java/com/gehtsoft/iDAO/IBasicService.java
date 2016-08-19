package com.gehtsoft.iDAO;

import java.util.List;

/**
 * Created by dkuzmin on 8/15/2016.
 */
public interface IBasicService {

    List getAll() throws Exception;

    Object getById(Integer id) throws Exception;

    void deleteById(Integer id) throws Exception;

    Object add(Object object) throws Exception;

    Object update(Object object) throws Exception;
}
