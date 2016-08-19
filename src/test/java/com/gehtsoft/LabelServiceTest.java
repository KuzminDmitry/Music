package com.gehtsoft;

import com.gehtsoft.core.Label;
import com.gehtsoft.factory.ServiceFactory;
import com.gehtsoft.iDAO.IBasicService;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by dkuzmin on 7/13/2016.
 */
public class LabelServiceTest {

    @Test
    public void serviceLabelTest() throws Exception {

        IBasicService labelService = ServiceFactory.getLabelService();

        List<Label> labels = labelService.getAll();

        Integer startSize = labels.size();

        String name = "Наименование лейбла";

        Label labelForInsert = new Label();
        labelForInsert.setName(name);

        labelForInsert = (Label)labelService.add(labelForInsert);

        labels = labelService.getAll();
        Integer afterInsertSize = labels.size();

        Assert.assertEquals(Integer.valueOf(afterInsertSize), Integer.valueOf(startSize + 1));

        Label labelById = (Label)labelService.getById(labelForInsert.getId());

        Assert.assertEquals(labelForInsert.getId(), labelById.getId());
        Assert.assertEquals(labelForInsert.getName(), labelById.getName());

        Label beforeUpdateLabel = new Label();
        beforeUpdateLabel.setId(labelById.getId());
        beforeUpdateLabel.setName(labelById.getName());

        beforeUpdateLabel.setName("Новое название лейбла");

        Label afterUpdateLabel = (Label)labelService.update(beforeUpdateLabel);

        Assert.assertEquals(labelById.getId(), beforeUpdateLabel.getId());
        Assert.assertNotEquals(labelById.getName(), beforeUpdateLabel.getName());

        labelService.deleteById(afterUpdateLabel.getId());

        labels = labelService.getAll();

        Integer endSize = labels.size();

        Assert.assertEquals(startSize, endSize);
    }
}
