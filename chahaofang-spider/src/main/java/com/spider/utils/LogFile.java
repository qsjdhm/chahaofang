package com.spider.utils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhangyan on 2017/7/21.
 * 操作日志文件的工具类
 */
public class LogFile {

    /**
     * 接收参数，向每一天的日志文件写内容
     */
    public static void writerLogFile(String logPath, String type, String msg) {

        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String detailsDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String path = LogFile.class.getResource("/").getPath() + logPath + date + ".log";

        // 处理文件夹有中文和空格的情况
        try {
            path = java.net.URLDecoder.decode(path, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        File logFile = new File(path);
        if (logFile.exists()) {
            writerContent(path, "["+type+"] " + "[" +detailsDate+"] " + msg);
        } else {
            if(!logFile.getParentFile().exists()) {
                //如果目标文件所在的目录不存在，则创建父目录
                if(!logFile.getParentFile().mkdirs()) {
                    System.out.println("创建目标文件所在目录失败！");
                }
            }
            try {
                logFile.createNewFile();
                writerContent(path, "["+type+"] " + "[" +detailsDate+"] " + msg);
            } catch (IOException e) {
                System.out.println("创建文件"+logPath + date + ".log出错：" + e);
                e.printStackTrace();
            }
        }
    }

    /**
     * 向文件写数据
     */
    public static void writerContent (String filePath, String content) {
        // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
        FileWriter writer = null;
        try {
            writer = new FileWriter(filePath, true);
            writer.write(content += "\r\n");
            writer.close();
        } catch (IOException e) {
            System.out.println(filePath + "写入文件内容出错：" + e);
            e.printStackTrace();
        }
    }

}
