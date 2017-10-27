
package com.web.controller;

import com.web.model.User;
import com.web.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Zhangxq on 2016/7/15.
 */

@Controller
@RequestMapping("/user")
public class UserController {

    private Logger log = Logger.getLogger(UserController.class);
    @Resource
    private UserService userService;


    @RequestMapping("/showUser")
    public void showUser(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
        log.info("查询所有用户信息");
//        List<User> userList = userService.getAllUser();
//        model.addAttribute("userList",userList);
//        return "showUser";
        response.getWriter().print("asdasd");
    }
}