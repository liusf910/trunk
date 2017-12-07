package com.ln.tms.mapper;

import java.util.List;

import com.ln.tms.mymapper.MyMapper;
import com.ln.tms.pojo.Carrier;

/**
 *  carrierMapper
 *  
 * @author zhengyu
 */
public interface CarrierMapper extends MyMapper<Carrier>{

	/**
	 * 查询承运商信息
	 * @param carrier
	 * @return
	 */
	List<Carrier> queryAllCarrier();
}
