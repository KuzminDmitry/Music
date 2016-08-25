package com.gehtsoft;

import com.gehtsoft.core.*;
import com.gehtsoft.factory.ServiceFactory;
import com.gehtsoft.iDAO.IBasicService;
import com.gehtsoft.iDAO.IUserService;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.test.DeploymentContext;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.ServletDeploymentContext;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by dkuzmin on 7/22/2016.
 */
public class UserServiceTest extends JerseyTest {

    @Override
    protected DeploymentContext configureDeployment() {
        return ServletDeploymentContext.builder(new ResourceConfig())
                .initParam(ServerProperties.PROVIDER_PACKAGES, this.getClass().getPackage().getName())
                .build();
    }

    @Override
    protected TestContainerFactory getTestContainerFactory() {
        return new GrizzlyWebTestContainerFactory();
    }

    IUserService userService = ServiceFactory.getUserService();

    @Test
    public void serviceUserTest() throws Exception {
        
        String username = "Имя пользователя";
        String password = "Пароль пользователя";

        List<Integer> roleIds = new ArrayList<>();

        roleIds.add(Role.USER);

        List<User> users = userService.getAll();

        Integer startSize = users.size();

        User userForInsert = new User();
        userForInsert.setUserName(username);
        userForInsert.setPassword(password);
        userForInsert.setRoleIds(roleIds);

        Collections.sort(userForInsert.getRoleIds());

        userForInsert = (User)userService.add(userForInsert);

        users = userService.getAll();

        Integer afterInsertSize = users.size();

        Assert.assertEquals(Integer.valueOf(afterInsertSize), Integer.valueOf(startSize + 1));

        User userById = (User)userService.getById(userForInsert.getId());

        Collections.sort(userById.getRoleIds());

        Assert.assertEquals(userForInsert.getId(), userById.getId());
        Assert.assertEquals(userForInsert.getUserName(), userById.getUserName());
        Assert.assertEquals(userForInsert.getPassword(), userById.getPassword());

        Assert.assertEquals(userForInsert.getRoleIds().size(), userById.getRoleIds().size());

        if (userForInsert.getRoleIds().size() == userById.getRoleIds().size()) {
            for (int i = 0; i < userForInsert.getRoleIds().size(); i++) {
                Assert.assertEquals(userForInsert.getRoleIds().get(i), userById.getRoleIds().get(i));
            }
        }

        User beforeUpdateUser = userById.copyTo();

        beforeUpdateUser.setUserName("Новое имя пользователя");

        User afterUpdateUser = (User)userService.update(beforeUpdateUser);

        Assert.assertEquals(userById.getId(), beforeUpdateUser.getId());
        Assert.assertNotEquals(userById.getUserName(), beforeUpdateUser.getUserName());
        Assert.assertEquals(userById.getPassword(), beforeUpdateUser.getPassword());

        userService.deleteById(afterUpdateUser.getId());

        users = userService.getAll();

        Integer endSize = users.size();

        Assert.assertEquals(startSize, endSize);
    }
}
