package com.jxj.user.remote;

import com.jxj.client.param.Response;
import com.jxj.user.bean.User;

import java.util.List;

/**
 * @Description
 */
public interface UserRemote {
    public Response saveUser(User user);

    public Response saveUsers(List<User> users);


}
