package com.jxj.netty;

import com.jxj.user.bean.User;
import com.jxj.user.remote.UserRemote;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RemoteInvokingTest.class)
@ComponentScan("com.jxj")
public class RemoteInvokingTest {

    private UserRemote userRemote;

    @Test
    public void testUserController() {
        User user = new User();
        user.setId(1);
        user.setName("张三");
        userRemote.saveUser(user);
    }

    @Test
    public void testUsersController() {
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setId(1);
        user.setName("张三");
        users.add(user);
        userRemote.saveUsers(users);
    }
}
