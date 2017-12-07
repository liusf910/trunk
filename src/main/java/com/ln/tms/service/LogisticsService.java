package com.ln.tms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ln.tms.mapper.LogisticsMapper;
import com.ln.tms.pojo.Fileup;
import com.ln.tms.pojo.LogisticsAppointment;

/**
 * LogisticsService
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Service
public class LogisticsService extends BaseService<LogisticsAppointment>{

	@Autowired
	private LogisticsMapper logisticsMapper;
	
	/**
     * 根据条件查询附件表
     *
     * @param fileup 查询条件
     * @return List
     */
    @Transactional(readOnly = true)
	public List<LogisticsAppointment> queryLogisticsList(LogisticsAppointment logisticsAppointment) {
    	 return logisticsMapper.queryLogisticsList(logisticsAppointment);
	}

    /**
     * 根据条件查询附件表条数
     *
     * @param fileup 查询条件
     * @return Integer
     */
    @Transactional(readOnly = true)
	public Integer queryTotal(LogisticsAppointment logisticsAppointment) {
    	 return logisticsMapper.queryTotal(logisticsAppointment);
	}

	/**
	 * 根据单号查询物流预约详细信息
	 * @param oddNum
	 * @return
	 */
	public Object queryByOddNum(String oddNum) {
		return logisticsMapper.queryByOddNum(oddNum);
	}

	/**批量插入物流预约信息
	 * @param list
	 */
	public void insertLogisticsAppointmentList(List<LogisticsAppointment> list) {
		logisticsMapper.insertLogisticsAppointmentList(list);
	}
	
	/**
	 * 批量修改受理日期
	 * @param list
	 */
	public void updateLogisticsDateList(List<LogisticsAppointment> list){
		logisticsMapper.updateLogisticsDateList(list);
	}
	
}
