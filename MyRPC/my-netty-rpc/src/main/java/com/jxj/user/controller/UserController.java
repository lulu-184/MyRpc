package com.jxj.user.controller;

import com.jxj.netty.util.Response;
import com.jxj.netty.util.ResponseUtil;
import com.jxj.user.bean.User;
import com.jxj.user.service.UserService;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description
 */
@Controller
public class UserController {

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
