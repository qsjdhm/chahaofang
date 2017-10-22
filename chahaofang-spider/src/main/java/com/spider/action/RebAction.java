package com.spider.action;

import com.spider.dao.RebMapper;
import com.spider.entity.Reb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.spider.service.impl.houses.RebServiceImpl;
import com.spider.service.impl.system.SocketServiceImpl;
import com.spider.service.impl.system.SpiderProgressServiceImpl;
import com.spider.service.impl.system.SqlServiceImpl;
import com.spider.utils.SysConstant;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class RebAction {

    SpiderProgressServiceImpl progressService = new SpiderProgressServiceImpl();
    SqlServiceImpl sqlService = new SqlServiceImpl();
    RebServiceImpl rebService = new RebServiceImpl();

    /**
     * 同步房产商的所有信息
     */
    public void syncAllList() {


        // 1. 循环调用service方法获取数据
        List<Reb> allRebList = new ArrayList<Reb>();
        int number = 1;
        boolean isTimedOut = false;

        do {
            List<Reb> pageRebList = new ArrayList<Reb>();
            try {
                progressService.addProgress(
                        "房产商", "分页", number,
                        "开始", "", new ArrayList(), null
                );

                pageRebList = rebService.getListByPage(number);

                progressService.addProgress(
                        "房产商", "分页", number,
                        "完成", "", new ArrayList(), null
                );
            } catch (IOException e) {
                if (e.toString().indexOf("Read timed out") > -1) {
                    isTimedOut = true;

                    String url = new SysConstant().REB_LIST_URL + "/index_"+number+".shtml";
                    progressService.addProgress(
                            "房产商", "分页", number,
                            "超时异常", url, new ArrayList(), e
                    );
                }
                e.printStackTrace();
            }

            // 如果这页数据为空，并且是由爬虫超时导致的，就继续获取第2页
            // 如果第2页数据为空并超时，继续第3页
            // 如果第3页数据为空，但是不是超时导致的，代表获取数据完成，结束循环
            if (pageRebList.size()==0) {
                if (isTimedOut) {
                    number++;
                } else {
                    number = 0;
                }
            } else {
                number++;
                // 把每页的地块数据添加到全部的地块列表中
                for(Reb reb : pageRebList) {
                    allRebList.add(reb);
                }

                // 更新入数据库
                sqlService.updateRebList(pageRebList);
            }


        } while (number > 0);

        // 每次action结束，关闭socket
        new SocketServiceImpl().closeSocketServer();
    }


    /**
     * 根据页数同步此页房产商的数据列表
     * syncListByPage(2)
     */
    public void syncListByPage(int number) throws IOException {

        // 同步获取数据
        List<Reb> rebList = new ArrayList<Reb>();
        try {
            progressService.addProgress(
                    "房产商", "分页", number,
                    "开始", "", new ArrayList(), null
            );

            rebList = rebService.getListByPage(number);

            // 更新入数据库
            sqlService.updateRebList(rebList);

            progressService.addProgress(
                    "房产商", "分页", number,
                    "完成", "", new ArrayList(), null
            );
        } catch (IOException e) {
            if (e.toString().indexOf("Read timed out") > -1) {

                String url = new SysConstant().REB_LIST_URL + "/index_"+number+".shtml";
                progressService.addProgress(
                        "房产商", "分页", number,
                        "超时异常", url, new ArrayList(), e
                );
            }
            e.printStackTrace();
        } finally {
            // 每次action结束，关闭socket
            new SocketServiceImpl().closeSocketServer();
        }
    }

    /**
     * 根据某一个的url同步此个房产商的所有信息
     * syncDetailsByUrl("济南建邦置业有限公司", "http://www.jnfdc.gov.cn/kfqy/show/915c802f-f227-4cec-853d-e5161a90b0c4.shtml")
     */
    public void syncDetailsByUrl(String name, String url) throws IOException {

        Reb reb = new Reb();
        List locationList = new ArrayList();
        locationList.add(name);

        try {
            progressService.addProgress(
                    "房产商", "详情", 0,
                    "开始", "", locationList, null
            );

            reb = rebService.getDetailsByUrl(url);

            progressService.addProgress(
                    "房产商", "详情", 0,
                    "完成", "", locationList, null
            );

            // 更新入数据库
            sqlService.updateReb(reb);

        } catch (IOException e) {
            if (e.toString().indexOf("Read timed out") > -1) {

                progressService.addProgress(
                        "房产商", "详情", 0,
                        "超时异常", url, locationList, e
                );
            }
            e.printStackTrace();
        } finally {
            // 每次action结束，关闭socket
            new SocketServiceImpl().closeSocketServer();
        }
    }
}
