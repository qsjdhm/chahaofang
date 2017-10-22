package com.spider.service.impl.system;


import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by zhangyan on 2017/9/16.
 * socket业务
 */
public class SocketServiceImpl {

    private static Socket socket = null;
    private static DataOutputStream out = null;



    // 将此次线程的客户端连接socket，赋值给自己
    public static void initSocketServer(Socket threadSocket) {
        socket = threadSocket;
    }


    // 向此socket发送数据
    public static void sendMsg(String info) {
        try {
            out = new DataOutputStream(socket.getOutputStream());

            out.writeUTF(info);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 关闭此socket
    public static void closeSocketServer() {
        try {
            out.writeUTF("close");
            out.close();
            socket.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }



}
