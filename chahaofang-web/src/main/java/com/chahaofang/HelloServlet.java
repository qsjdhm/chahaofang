package com.chahaofang;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 引入spider模块的功能类
import com.spider.action.RebAction;
import service.Core;
import com.spider.*;

import java.io.IOException;

/**
 * Created by zhangyan on 17/10/22.
 */
public class HelloServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("开始说2web call spider function begin：");
        Core core = new Core();
        core.sayHello();

        RebAction rebAction = new RebAction();

        rebAction.syncDetailsByUrl("济南建邦置业有限公司", "http://www.jnfdc.gov.cn/kfqy/show/915c802f-f227-4cec-853d-e5161a90b0c4.shtml");

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("开始说2：");
    }
}
