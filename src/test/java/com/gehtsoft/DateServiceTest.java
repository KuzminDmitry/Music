package com.gehtsoft;

import com.gehtsoft.date.IDateService;
import com.gehtsoft.factory.DateFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by dkuzmin on 7/13/2016.
 */
public class DateServiceTest {

    @Test
    public void serviceDateTest() throws Exception {

        IDateService dateService = DateFactory.getDateService();

        Date fakePast = dateService.getNow();
        Date fakeNow = dateService.getDate(Calendar.MINUTE, 3);
        Date fakeFuture = dateService.getDate(Calendar.MINUTE, 5);

        Assert.assertTrue(fakePast.before(fakeNow) && fakeNow.after(fakePast));
        Assert.assertTrue(fakeNow.before(fakeFuture) && fakeFuture.after(fakeNow));
        Assert.assertTrue(fakePast.before(fakeFuture) && fakeFuture.after(fakePast));
    }
}
