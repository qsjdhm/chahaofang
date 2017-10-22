package com.spider.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by zhangyan on 17/7/16.
 * 读取Properties文件工具类
 */
public class PropertiesUtil {

    private Properties pro = null;
    private InputStream ins = null;

    // 初始化并打开读取通道写到内存
    public PropertiesUtil (String filePath) {
        // 打开
        pro = new Properties();
        ins = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        try {
            pro.load(ins);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 获取数据
    public String getValue (String key) {
        return pro.getProperty(key);
    }

    // 关闭文件读取管道
    public void closeProperties () {
        try {
            ins.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
