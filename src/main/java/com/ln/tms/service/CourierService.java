package com.ln.tms.service;

import com.github.pagehelper.PageInfo;
import com.ln.tms.mapper.CourierMapper;
import com.ln.tms.pojo.Courier;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * CourierService
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Service
public class CourierService extends BaseService<Courier> {

    @Autowired
    private CourierMapper courierMapper;

    /**
     * 分页查询快递列表
     *
     * @param courier 查询条件
     * @param page    页
     * @param row     条数
     * @return PageInfo
     */
    @Transactional(readOnly = true)
    public PageInfo<Courier> queryCourierPage(Courier courier, Integer page, Integer row) {
        Example example = new Example(Courier.class);
        example.setOrderByClause("create_date DESC");
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(courier.getShipperCode())) {
            criteria.andEqualTo("shipperCode", courier.getShipperCode());
        }
        if (StringUtils.isNotBlank(courier.getCourierName())) {
            criteria.andLike("courierName", "%" + courier.getCourierName() + "%");
        }
        return super.queryPageListByExample(example, page, row);
    }

    /**
     * 校验快递公司编号
     *
     * @param shipperCode 快递公司编号
     * @return boolean
     */
    @Transactional(readOnly = true)
    public boolean checkShipperCode(String shipperCode) {
        Courier courier = new Courier();
        courier.setShipperCode(shipperCode);
        return super.queryOne(courier) != null ;
    }

    /**
     * 校验快递公司名称
     *
     * @param shipperCode 快递公司编号
     * @param courierName 快递公司名称
     * @return boolean
     */
    @Transactional(readOnly = true)
    public boolean checkCourierName(String shipperCode, String courierName) {
        Courier courier = new Courier();
        courier.setShipperCode(shipperCode);
        courier.setCourierName(courierName);
        return courierMapper.checkCourierName(courier)>0;
    }

    /**
     * 根据Id查询快递公司信息
     *
     * @param shipperCode 快递公司编号
     * @return Courier
     */
    @Transactional(readOnly = true)
    public Courier getCourierById(String shipperCode) {
        return courierMapper.getCourierById(shipperCode);
    }

    /**
     * 判断快递公司是否关联了仓库
     *
     * @param shipperCode 快递公司编号
     * @return boolean
     */
    @Transactional
    public boolean isBindStorageCourier(String shipperCode) {
        if (courierMapper.getBindStorageCourierNum(shipperCode) > 0) {
            return false;
        }
        super.deleteById(shipperCode);
        return true;
    }

    /**
     * 根据条件查询快递
     *
     * @param courier 查询条件
     * @return Courier
     */
    @Override
    @Transactional(readOnly = true)
    public Courier queryOne(Courier courier) {
        return super.queryOne(courier);
    }


    /**
     * 查询所有快递
     *
     * @return List
     */
    @Transactional(readOnly = true)
    public List<Courier> queryAllNew() {
        return courierMapper.queryAllNew();
    }
}
