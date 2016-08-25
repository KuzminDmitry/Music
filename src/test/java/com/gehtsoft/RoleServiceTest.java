package com.gehtsoft;

import com.gehtsoft.core.Role;
import com.gehtsoft.core.User;
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
public class RoleServiceTest extends JerseyTest {

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

    IBasicService roleService = ServiceFactory.getRoleService();

    @Test
    public void serviceUserTest() throws Exception {
        List<Role> roles = roleService.getAll();
        List<Integer> roleIds = new ArrayList<>();
        for(Role role: roles){
            roleIds.add(role.getId());
        }
        Collections.sort(roleIds);
        List<Integer> realRoles = Role.getRoleIds();
        for(int i=0; i<roleIds.size(); i++){
            Assert.assertEquals(roleIds.get(i), realRoles.get(i));
            Assert.assertEquals(realRoles.get(i), (Integer)Role.class.getField(roles.get(i).getName()).getInt(null));
        }
    }
}
