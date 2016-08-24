package com.gehtsoft.date;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by dkuzmin on 8/24/2016.
 */
public class UTCService implements IDateService {

    @Override
    public Date getDate(int field, int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(field, amount);
        return calendar.getTime();
    }

    @Override
    public Date getNow() {
        return new Date();
    }
}
