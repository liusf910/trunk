package com.ln.tms.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ln.tms.mymapper.MyMapper;
import com.ln.tms.pojo.LogisticsAppointment;

public interface LogisticsMapper extends MyMapper<LogisticsAppointment>{
	
	 /**
     * 多条件分页查询文件记录列表
     *
     * @param fileup
     * @return List
     */
	List<LogisticsAppointment> queryLogisticsList(LogisticsAppointment logisticsAppointment);

	 /**
     * 多条件查询文件记录的总条数
     *
     * @param fileup
     * @return Integer
     */
	Integer queryTotal(LogisticsAppointment logisticsAppointment);

	/**
	 * 根据单号查询物流预约信息
	 * @param oddNum
	 * @return
	 */
	LogisticsAppointment queryByOddNum(String oddNum);

	/**批量插入物流预约信息
	 * @param list
	 */
	void insertLogisticsAppointmentList(List<LogisticsAppointment> list);

	/**
	 * 根据相同受理日期统计该受理日期内总件
	 * @param date
	 * @return
	 */
	Integer queryByLnecAcceptanceDateAndWareHouse(@Param(value="lnecAcceptanceDate") Date lnecAcceptanceDate,@Param(value="warehouse") String warehouse);

	/**
	 * 根据查询条件不分页查询物流预约信息
	 * @param logisticsAppointment
	 * @return
	 */
	List<LogisticsAppointment> querylogisticListNoPage(LogisticsAppointment logisticsAppointment);

	/**
	 * 批量修改受理日期
	 * @param list
	 */
	void updateLogisticsDateList(List<LogisticsAppointment> list);

	/**根据条件查询最大的流水号
	 * @param lnecAcceptanceDate
	 * @param warehouse
	 * @return
	 */
	String queryMaxNum(@Param(value="lnecAcceptanceDate") Date lnecAcceptanceDate,@Param(value="warehouse") String warehouse);
}
