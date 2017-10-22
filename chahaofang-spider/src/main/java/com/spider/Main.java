package com.spider;

import com.spider.action.*;
import com.spider.dao.RebMapper;
import com.spider.entity.Reb;
import com.spider.service.impl.system.SocketServiceImpl;
import com.spider.service.impl.system.SpiderProgressServiceImpl;
import com.spider.service.impl.system.SqlServiceImpl;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSON;




/**
 * Created by zhangyan on 17/7/16.
 */
public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {


        new socketServer().start();
        Thread.sleep(100);  // 调整时间，让服务端准备好
        new Client().start();

//        PlotsAction plotsAction = new PlotsAction();
//        plotsAction.syncListByPage("中海国际社区C-1地块", "http://www.jnfdc.gov.cn/onsaling/show.shtml?prjno=796f5efb-d8a8-490c-9235-13150582bfd8", 1);
//
//
//        System.out.println(":::::::::::::::::::::::::::::");
//        System.out.println(":::::::::::::::::::::::::::::");
//        System.out.println(":::::::::::::::::::::::::::::");
//
//        Thread.sleep(3000);  // 调整时间，让服务端准备好
//        new Client().start();
//        Thread.sleep(1000);  // 调整时间，让服务端准备好
//        plotsAction.syncListByPage("中海国际社区C-1地块", "http://www.jnfdc.gov.cn/onsaling/show.shtml?prjno=796f5efb-d8a8-490c-9235-13150582bfd8", 1);





        /**
         * 自动任务action调用实例
         */
        TaskAction taskAction = new TaskAction();
        taskAction.begin();



        /**
         * 同步异常补偿action调用实例
         */

//        RebAction rebAction = new RebAction();
//        rebAction.syncAllList();
//        rebAction.syncListByPage(2);
//        rebAction.syncDetailsByUrl("济南建邦置业有限公司", "http://www.jnfdc.gov.cn/kfqy/show/915c802f-f227-4cec-853d-e5161a90b0c4.shtml");


//        HousesAction housesAction = new HousesAction();
//        housesAction.syncAllList();
//        housesAction.syncListByPage(1);
//        housesAction.syncDetailsByUrl("中海国际社区", "http://zhonghaiguojishequ0531.fang.com");


//        FloorAction floorAction = new FloorAction();
//        floorAction.syncAllList("中海国际社区");
//        floorAction.syncListByPage("中海国际社区", 1);
//        floorAction.syncDetailsByUrl("中海国际社区", "中海国际社区B-2地块", "http://www.jnfdc.gov.cn/onsaling/show.shtml?prjno=c4d9a76b-b289-42b5-a65f-c99882645ff6");


//        PlotsAction  plotsAction = new PlotsAction();
//        plotsAction.syncAllList("中海国际社区B-2地块", "http://www.jnfdc.gov.cn/onsaling/show.shtml?prjno=c4d9a76b-b289-42b5-a65f-c99882645ff6");
//        plotsAction.syncListByPage("中海国际社区B-2地块", "http://www.jnfdc.gov.cn/onsaling/show.shtml?prjno=c4d9a76b-b289-42b5-a65f-c99882645ff6", 2);
//        plotsAction.syncDetailsByUrl("中海国际社区B-2地块", "67楼", "http://www.jnfdc.gov.cn/onsaling/bshow.shtml?bno=23c5bfad-f26f-4f8f-b1db-cf15b8a9e1ac");




    }



    static class socketServer extends Thread {

        public void run() {
            try {
                System.out.println("等待客户端socket连接...");
                ServerSocket server = new ServerSocket(1888);
                Socket socket = null;
                while(true) {
                    socket = server.accept();
                    System.out.println("socket客户端" + socket.getInetAddress() + "连接成功！！！");

                    // 因为更新进度service无法区分哪个线程的socket，所以赋值给全局的socket
                    new SocketServiceImpl().initSocketServer(socket);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    static class Client extends Thread {
        public void run() {
            try {
                // 连接socket服务器
                Socket socket = new Socket("localhost", 1888);
                InputStream is = socket.getInputStream();
                DataInputStream dis = new DataInputStream(is);

                while (true) {
                    String msg = dis.readUTF();

                    if (msg.equals("close")) {
                        System.out.println("客户端socket即将关闭！！！");
                        break;
                    }
                    System.out.println("server_msg:"+msg);
                    System.out.println("+++++++++++");
                }
                is.close();
                dis.close();
                socket.close();
                System.out.println("客户端socket关闭成功！！！");
            } catch(Exception e) {
                e.printStackTrace();
            }
        }


    }

}



