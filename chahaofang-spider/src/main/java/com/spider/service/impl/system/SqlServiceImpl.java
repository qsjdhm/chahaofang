package com.spider.service.impl.system;


import com.spider.dao.FloorMapper;
import com.spider.dao.HousesMapper;
import com.spider.dao.PlotsMapper;
import com.spider.dao.RebMapper;
import com.spider.entity.Floor;
import com.spider.entity.Houses;
import com.spider.entity.Plots;
import com.spider.entity.Reb;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangyan on 2017/8/30.
 * 数据库业务
 */
public class SqlServiceImpl {

    static SqlSessionFactory sessionFactory = null;
    static SqlSession sqlSession = null;

    static RebMapper rebMapper = null;
    static HousesMapper housesMapper = null;
    static FloorMapper floorMapper = null;
    static PlotsMapper plotsMapper = null;


    public SqlServiceImpl() {
        // 加个if  判断sessionFactory是不是已经存在，如果存在不进行初始化，不存在初始化
        if (sessionFactory == null) {
            // 打开数据库
            try {
                sessionFactory = new SqlSessionFactoryBuilder()
                        .build(Resources.getResourceAsReader("mybatis-config.xml"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            sqlSession = sessionFactory.openSession();

            // 实例化映射关系
            rebMapper = sqlSession.getMapper(RebMapper.class);
            housesMapper = sqlSession.getMapper(HousesMapper.class);
            floorMapper = sqlSession.getMapper(FloorMapper.class);
            plotsMapper = sqlSession.getMapper(PlotsMapper.class);
        }
    }


    public static RebMapper rebSql() {
        return rebMapper;
    }

    public static HousesMapper housesSql() {
        return housesMapper;
    }

    public static FloorMapper floorSql() {
        return floorMapper;
    }

    public static PlotsMapper plotsSql() {
        return plotsMapper;
    }

    public static void comment() {
        sqlSession.commit();
    }


    // 更新房产商列表的数据
    public static void updateRebList (List<Reb> rebList) {
        for (Reb reb : rebList) {
            updateReb(reb);
        }
    }

    // 更新单个房产商
    public static void updateReb (Reb reb) {

        Map<String, String> rebInfo = new HashMap<String, String>();
        rebInfo.put("name", reb.getName());
        List<Reb> findRebList = rebMapper.select(rebInfo);

        if (findRebList.size() == 0) {
            rebMapper.insert(reb);
        } else {
            for (Reb findReb : findRebList) {
                if (!reb.getHash().equals(findReb.getHash())) {
                    rebMapper.update(reb);
                }
            }
        }

        // 每一页数据就提交到数据库保存起来
        sqlSession.commit();
    }


    // 更新楼盘的数据
    public static void updateHouses (Houses houses) {

        Map<String, String> housesInfo = new HashMap<String, String>();
        housesInfo.put("name", houses.getName());
        List<Houses> findHousesList = housesMapper.select(housesInfo);

        if (findHousesList.size() == 0) {
            housesMapper.insert(houses);
        } else {
            for (Houses findHouses : findHousesList) {
                if (!houses.getHash().equals(findHouses.getHash())) {
                    housesMapper.update(houses);
                }
            }
        }

        // 每一条数据就提交到数据库保存起来
        sqlSession.commit();
    }

    // 更新地块的数据
    public static void updateFloor (Floor floor) {

        Map<String, String> floorInfo = new HashMap<String, String>();
        floorInfo.put("name", floor.getName());
        List<Floor> findFloorList = floorMapper.select(floorInfo);

        if (findFloorList.size() == 0) {
            floorMapper.insert(floor);
        } else {
            for (Floor findFloor : findFloorList) {
                if (!floor.getHash().equals(findFloor.getHash())) {
                    floorMapper.update(floor);
                }
            }
        }

        // 每一条数据就提交到数据库保存起来
        sqlSession.commit();
    }

    // 更新单元楼列表的数据
    public static void updatePlotsList (List<Plots> plotsList) {

        for (Plots plots : plotsList) {
            updatePlots(plots);
        }
    }

    // 更新单个单元楼列表的数据
    public static void updatePlots (Plots plots) {

        Map<String, String> plotsInfo = new HashMap<String, String>();
        plotsInfo.put("name", plots.getName());
        List<Plots> findPlotsList = plotsMapper.select(plotsInfo);

        if (findPlotsList.size() == 0) {
            plotsMapper.insert(plots);
        } else {
            for (Plots findPlots : findPlotsList) {
                if (!plots.getHash().equals(findPlots.getHash())) {
                    plotsMapper.update(plots);
                }
            }
        }

        // 每一页数据就提交到数据库保存起来
        sqlSession.commit();
    }



}
