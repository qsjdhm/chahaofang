package com.chahaofang;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 引入spider模块的功能类
import service.Core;

/**
 * Created by zhangyan on 17/10/22.
 */
public class HelloServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("开始说2web call spider function begin：");
        Core core = new Core();
        core.sayHello();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("开始说2：");
    }
}
