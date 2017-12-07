package com.ln.tms.mapper;

import com.ln.tms.bean.CountData;
import com.ln.tms.mymapper.MyMapper;
import com.ln.tms.pojo.CountRate;
import com.ln.tms.pojo.Info;

import java.util.List;

/**
 * CountRateMapper
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
public interface CountRateMapper extends MyMapper<CountRate> {

    /**
     * 查询前一天的待揽数据
     *
     * @param beforDays 前一天日期
     * @return List
     */
    List<Info> queryInfoTookList(String beforDays);

    /**
     * 批量添加统计的快递比率
     *
     * @param countRates 增加的比率集合
     * @return Integer
     */
    Integer saveCountRateBatch(List<CountRate> countRates);


    /**
     * 根据用户仓库权限查询快递揽收率
     *
     * @param countRate 查询条件
     * @return List
     */
    List<CountRate> queryList(CountRate countRate);

    /**
     * 统计发货时间段内揽收及时率
     *
     * @param countData 查询条件
     * @return List
     */
    List<CountData> queryTookRate(CountData countData);

    /**
     * 查询发货时间段内，仓库，快递的集合
     *
     * @param countData 查询条件
     * @return List
     */
    List<Info> queryInfoAll(CountData countData);

    /**
     * 清空某天的统计
     *
     * @param date
     * @return
     */
    Integer clearCountRateByDate(String date);
}
