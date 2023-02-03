package com.jxj.user.remote;

import com.jxj.netty.annotation.Remote;
import com.jxj.netty.util.Response;
import com.jxj.netty.util.ResponseUtil;
import com.jxj.user.bean.User;
import com.jxj.user.service.UserService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description
 */
@Remote
public class UserRemoteImpl implements UserRemote{
    @Resource
    private UserService userService;

    public Response saveUser(User user){

        userService.save(user);
        return ResponseUtil.createSuccessResult(user);
    }

    public Response saveUsers(List<User> users){

        userService.saveList(users);
        return ResponseUtil.createSuccessResult(users);
    }
}
