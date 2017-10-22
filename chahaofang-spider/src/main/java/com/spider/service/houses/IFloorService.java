package com.spider.service.houses;

import com.spider.entity.Floor;
import com.spider.entity.Houses;
import com.spider.entity.Plots;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.List;

/**
 * Created by zhangyan on 17/8/2.
 * 地块业务接口
 */
public interface IFloorService {


    // 获取地块的接口
    public List<Floor> getListByPage(String fdcName, int number) throws IOException;

    public Floor getDetailsByElement(String fdcName, Element tr) throws IOException;

    public Floor getDetailsByUrl(String url) throws IOException;


    // 获取此地块单元楼的接口
    public List<Plots> getPlotsListByFloorDetailsUrl(String floorName, String floorDetailsUrl) throws IOException;
}
