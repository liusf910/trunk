package com.ln.tms.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ln.tms.bean.InfoWhere;
import com.ln.tms.mapper.InfoMapper;
import com.ln.tms.pojo.Info;
import com.ln.tms.pojo.UserStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * InfoService - 快递基本信息Service
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@SuppressWarnings("ALL")
@Service
public class InfoService extends BaseService<Info> {

    @Autowired
    private InfoTraceService infoTraceService;

    @Autowired
    private InfoMapper infoMapper;

    /**
     * Example 查询
     *
     * @param example Example
     * @return List<Info>
     */
    @Override
    @Transactional(readOnly = true)
    public List<Info> queryByExample(Example example) {
        return super.queryByExample(example);
    }

    /**
     * 根据条件查询
     *
     * @param t 条件
     * @return List
     */
    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<Info> queryByWhere(Info t) {
        return super.queryByWhere(t);
    }

    /**
     * 保存从ERP中间表中获取的基本数据
     *
     * @param list 基本数据集合
     */
    @Transactional
    public void saveBaseInfo(List<Info> list) {
        if (list != null && list.size() > 0) {
            infoMapper.saveInfoList(list);
            infoTraceService.saveInfoTrace(list);
        }
    }

    /**
     * 批量更新快递信息
     *
     * @param list List
     */
    @Transactional
    public void updateListInfo(List<Info> list) {
        infoMapper.updateListInfo(list);
    }

    /**
     * 根据userId查询对应得仓库和快递公司
     *
     * @param userId 用户ID
     * @return UserStorage
     */
    public UserStorage getUserStorageCourier(String userId) {
        return infoMapper.getUserStorageCourier(userId);
    }

    /**
     * 根据条件查询快递信息
     *
     * @param infoWhere InfoWhere
     * @param page      页
     * @param rows      条
     * @return PageInfo
     */
    @Transactional(readOnly = true)
    public PageInfo<Info> getInfoPageList(InfoWhere infoWhere, Integer page, Integer rows) {
        PageHelper.startPage(page, rows, true);
        return infoMapper.selectInfoByWhere(infoWhere);
    }

    /**
     * 批量删除
     *
     * @param ids ID
     * @return
     */
    @Transactional
    public Integer deleteTryByIds(List<Object> ids) {
        Integer byIds = super.deleteByIds(ids, "infoId", Info.class);
        infoTraceService.deleteByIds(ids);
        return byIds;
    }

    /**
     * 查看Info是否存在
     *
     * @param shipperCode  快递公司编号
     * @param logisticCode 快递单号
     * @param orderCode    订单号
     * @return
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public boolean queryExist(String shipperCode, String logisticCode, String orderCode) {
        Info info = new Info();
        info.setShipperCode(shipperCode);
        info.setLogisticCode(logisticCode);
        info.setOrderCode(orderCode);
        return super.queryOne(info) != null;
    }
}