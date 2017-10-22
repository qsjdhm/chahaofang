package com.spider.action;

import com.spider.utils.SysConstant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TaskAction {

    private static Logger logger = LogManager.getLogger(TaskAction.class.getName());

    static int count = 0;

    public void begin() {

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ++count;
                logger.info(System.getProperty("line.separator") + "自动任务开始开始执行第" + count + "次!!!" + System.getProperty("line.separator") );
                // 同步房产商
                RebAction rebAction = new RebAction();
                rebAction.syncAllList();

                // 同步楼盘
//                HousesAction housesAction = new HousesAction();
//                housesAction.syncAllList();
            }
        };

        // 设置执行时间
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);//每天
        //定制每天的16:00:00执行，
        calendar.set(year, month, day, 15, 41, 00);
        Date date = calendar.getTime();
        Timer timer = new Timer();

        int period = new SysConstant().DAY_SECOND * 1000;
        //每天的date时刻执行task，每隔1天重复执行
        timer.schedule(task, date, period);
        //每天的date时刻执行task, 仅执行一次
        //timer.schedule(task, date);
    }
}
