package com.gehtsoft;

import com.gehtsoft.core.Singer;
import com.gehtsoft.factory.ServiceFactory;
import com.gehtsoft.iDAO.IBasicService;
import org.junit.Assert;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by dkuzmin on 7/13/2016.
 */
public class SingerServiceTest {

    @Test
    public void serviceSingerTest() throws Exception {

        IBasicService singerService = ServiceFactory.getSingerService();

        List<Singer> singers = singerService.getAll();

        Integer startSize = singers.size();

        String name = "Наименование исполнителя";
        String description = "Описание исполнителя";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String temp = simpleDateFormat.format(new Date());
        Date birthDate = simpleDateFormat.parse(temp);


        Singer singerForInsert = new Singer();
        singerForInsert.setName(name);
        singerForInsert.setDescription(description);

        singerForInsert.setBirthDate(birthDate);

        singerForInsert = (Singer)singerService.add(singerForInsert);

        singers = singerService.getAll();
        Integer afterInsertSize = singers.size();

        Assert.assertEquals(Integer.valueOf(afterInsertSize), Integer.valueOf(startSize + 1));

        Singer singerById = (Singer)singerService.getById(singerForInsert.getId());

        Assert.assertEquals(singerForInsert.getId(), singerById.getId());
        Assert.assertEquals(singerForInsert.getName(), singerById.getName());
        Assert.assertEquals(singerForInsert.getDescription(), singerById.getDescription());
        Assert.assertEquals(singerForInsert.getBirthDate(), singerById.getBirthDate());

        Singer beforeUpdateSinger = new Singer();
        beforeUpdateSinger.setId(singerById.getId());
        beforeUpdateSinger.setName(singerById.getName());
        beforeUpdateSinger.setDescription(singerById.getDescription());
        beforeUpdateSinger.setBirthDate(singerById.getBirthDate());

        beforeUpdateSinger.setName("Новое наимнование исполнителя");

        Singer afterUpdateSinger = (Singer)singerService.update(beforeUpdateSinger);

        Assert.assertEquals(singerById.getId(), beforeUpdateSinger.getId());
        Assert.assertNotEquals(singerById.getName(), beforeUpdateSinger.getName());
        Assert.assertEquals(singerById.getDescription(), beforeUpdateSinger.getDescription());
        Assert.assertEquals(singerById.getBirthDate(), beforeUpdateSinger.getBirthDate());

        singerService.deleteById(afterUpdateSinger.getId());

        singers = singerService.getAll();

        Integer endSize = singers.size();

        Assert.assertEquals(startSize, endSize);
    }
}
