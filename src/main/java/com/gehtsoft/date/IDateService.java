package com.gehtsoft.date;

import java.util.Date;

/**
 * Created by dkuzmin on 8/24/2016.
 */
public interface IDateService {
    Date getDate(int field, int amount);

    Date getNow();
}
