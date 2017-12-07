package com.ln.tms.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ctc.wstx.util.StringUtil;
import com.github.pagehelper.PageInfo;
import com.ln.tms.mapper.CarrierMapper;
import com.ln.tms.pojo.Carrier;
import com.ln.tms.pojo.Courier;

import tk.mybatis.mapper.entity.Example;

/**
 * carrierService
 * 
 * @author zhengyu
 *
 */
@Service
public class CarrierService extends BaseService<Carrier>{

	@Autowired
	private CarrierMapper carrierMapper;
	
	/**
	 * 分页查询承运商信息
	 * 
	 * @param carrier
	 * @param page
	 * @param rows
	 * @return
	 */
	@Transactional(readOnly = true)
	public PageInfo<Carrier> queryCarrierPage(Carrier carrier,Integer page,Integer rows){
		Example example = new Example(Carrier.class);
		example.setOrderByClause("create_date DESC");
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(carrier.getCarrierCode())) {
            criteria.andEqualTo("carrierCode", carrier.getCarrierCode());
        }
        if (StringUtils.isNotBlank(carrier.getCarrierName())) {
            criteria.andLike("carrierName", "%" + carrier.getCarrierName()+ "%");
        }
		return super.queryPageListByExample(example, page, rows);
	}
	
    @Transactional(readOnly = true)
    public boolean checkCarrierCode(String carrierCode) {
        Carrier courier = new Carrier();
        courier.setCarrierCode(carrierCode);
        return super.queryOne(courier) != null ;
    }
	
}
