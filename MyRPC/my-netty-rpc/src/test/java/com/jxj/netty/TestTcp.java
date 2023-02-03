package com.jxj.netty;

import com.jxj.netty.client.ClientRequest;
import com.jxj.netty.util.Response;
import com.jxj.netty.client.TcpClient;
import com.jxj.user.bean.User;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 */
public class TestTcp {
    @Test
    public void testGetResponse() {
        ClientRequest request = new ClientRequest();
        request.setContent("测试Tcp长连接请求");
        Response response = TcpClient.send(request);
        System.out.println(response.getId() + ", " + response.getResult());
    }

    @Test
    public void testUserController() {
        ClientRequest request = new ClientRequest();
        User user = new User();
        user.setId(1);
        user.setName("张三");
        request.setCommand("com.jxj.user.controller.UserController.saveUser");
        request.setContent(user);
        Response response = TcpClient.send(request);
        System.out.println(response.getResult());
    }

    @Test
    public void testUsersController() {
        ClientRequest request = new ClientRequest();
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setId(1);
        user.setName("张三");
        users.add(user);
        request.setCommand("com.jxj.user.controller.UserController.saveUsers");
        request.setContent(users);
        Response response = TcpClient.send(request);
        System.out.println(response.getResult());
    }
}
