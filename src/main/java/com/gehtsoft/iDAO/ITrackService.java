package com.gehtsoft.iDAO;

import com.gehtsoft.core.Track;

import java.util.List;

/**
 * Created by dkuzmin on 8/17/2016.
 */
public interface ITrackService extends IBasicService {
    List<Track> getAllByFilter(Track track) throws Exception;
}
