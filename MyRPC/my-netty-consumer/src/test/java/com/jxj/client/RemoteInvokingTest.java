package com.jxj.client;

import com.alibaba.fastjson.JSONObject;
import com.jxj.client.annotation.RemoteInvoke;
import com.jxj.client.param.Response;
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

    @RemoteInvoke
    private UserRemote userRemote;

    @Test
    public void testUserController() {
        User user = new User();
        user.setId(1);
        user.setName("张三");
        Response response = userRemote.saveUser(user);
        System.out.println(JSONObject.toJSONString(response));
    }

    @Test
    public void testUserssssss() {
        User user = new User();
        user.setId(1);
        user.setName("张三");
        Long start = System.currentTimeMillis();
		for(int i=1;i<1000000;i++){
            userRemote.saveUser(user);
		}
		Long end = System.currentTimeMillis();
		Long count = end-start;
		System.out.println("总计时:"+count/1000+"秒");
//        Response response = userRemote.saveUser(user);
//        System.out.println(JSONObject.toJSONString(response));
    }

    @Test
    public void testUsersController() {
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setId(1);
        user.setName("张三");
        users.add(user);
        Response response = userRemote.saveUsers(users);
        System.out.println(JSONObject.toJSONString(response));
    }
}
