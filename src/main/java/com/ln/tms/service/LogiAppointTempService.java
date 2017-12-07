package com.ln.tms.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ln.tms.exception.FailRuntimeException;
import com.ln.tms.mapper.LogisticsMapper;
import com.ln.tms.mapper.StorageAppointMapper;
import com.ln.tms.pojo.LogisticsAppointment;
import com.ln.tms.pojo.LogisticsAppointmentTemp;
import com.ln.tms.pojo.StorageAppoint;
import com.ln.tms.util.DateUtils;

@Service
public class LogiAppointTempService {

	@Autowired
	private LogisticsMapper logisticsMapper;

	@Autowired
	private StorageAppointMapper storageAppointMapper;

	private Map<String, Integer> map = new HashMap<String, Integer>();

	private Map<String, String> flowMap = new HashMap<String, String>();

	private List<String> oddNumList = new ArrayList<String>();

	public List<LogisticsAppointment> checkLogistics(List<LogisticsAppointmentTemp> lists) {
		if (CollectionUtils.isEmpty(lists)) {
			return null;
		}
		oddNumList.clear();
		List<LogisticsAppointment> logisticsAppointment = new ArrayList<LogisticsAppointment>();
		try {
			for (LogisticsAppointmentTemp logAppoint : lists) {
				LogisticsAppointment lAppo = new LogisticsAppointment();

				String oddNum = logAppoint.getOddNum();

				LogisticsAppointment byOddNum = logisticsMapper.queryByOddNum(oddNum);
				if (StringUtils.isBlank(oddNum)) {
					throw new FailRuntimeException("单号不能为空,上传失败");
				} else if (null != byOddNum) {
					throw new FailRuntimeException("单号已存在,上传失败");
				}
				oddNumList.add(oddNum);
				String dateAppoint = logAppoint.getDateAppoint();
				SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
				Date date;
				try {
					date = format.parse(dateAppoint);
				} catch (Exception e) {
					throw new FailRuntimeException("预约送达日期格式不正确,上传失败");
				}
				if (StringUtils.isBlank(dateAppoint)) {
					throw new FailRuntimeException("预约送达日期不能为空,上传失败");
				} else if (dateAppoint.equals(date.toString())) {
					throw new FailRuntimeException("预约送达日期格式不正确,上传失败");
				}

				Date d = new Date();
				if (DateUtils.compareToDate(d, date)) {
					throw new FailRuntimeException("预约日期导入错误，预约日期必须从明天开始");
				}
				String warehouse = logAppoint.getWarehouse();
				if (StringUtils.isBlank(warehouse)) {
					throw new FailRuntimeException("选择仓库不能为空,上传失败");
				}
				if (!"经销仓".equals(warehouse) && !"直营仓".equals(warehouse)) {
					throw new FailRuntimeException("仓库只能填写经销仓库或直营仓库");
				}

				String item = logAppoint.getItem();
				if (StringUtils.isBlank(item)) {
					throw new FailRuntimeException("品项不能为空,上传失败");
				}
				Integer number = logAppoint.getNumber();
				String numStr = String.valueOf(number);
				if ("null".equals(numStr)) {
					throw new FailRuntimeException("数量不能为空,上传失败");
				}
				Integer numberPackage = logAppoint.getNumberPackage();
				String packStr = String.valueOf(numberPackage);
				if ("null".equals(packStr)) {
					throw new FailRuntimeException("件数不能为空,上传失败");
				}
				String shipper = logAppoint.getShipper();
				if (StringUtils.isBlank(shipper)) {
					throw new FailRuntimeException("发货方不能为空,上传失败");
				}
				String shipperCity = logAppoint.getShipperCity();
				if (StringUtils.isBlank(shipperCity)) {
					throw new FailRuntimeException("发货城市不能为空,上传失败");
				}
				String carrier = logAppoint.getCarrier();
				if (StringUtils.isBlank(carrier)) {
					throw new FailRuntimeException("承运商不能为空,上传失败");
				}
				String reservatePerson = logAppoint.getReservatePerson();
				if (StringUtils.isBlank(reservatePerson)) {
					throw new FailRuntimeException("预约人不能为空,上传失败");
				}
				String appointCompany = logAppoint.getAppointCompany();
				if (StringUtils.isBlank(appointCompany)) {
					throw new FailRuntimeException("预约人所属公司不能为空,上传失败");
				}
				String phone = logAppoint.getPhone();
				if (StringUtils.isBlank(phone)) {
					throw new FailRuntimeException("预约人联系方式不能为空,上传失败");
				}
				Date lnecAcceptanceDate;
				// 先获取当前时间
				String oldDate = DateUtils.getDaysFormat(d);
				oldDate = oldDate + " 16:30:00";
				Date toDate = DateUtils.stringToDate(oldDate);
				boolean compareToDate = DateUtils.compareToDate(d, toDate);
				if (!compareToDate) {
					// 说明现在的时间小于下午4：30
					// 判断预约送达日期是否大于等于导入日期加1
					boolean compareToImportDate = DateUtils.compareToDate(
							DateUtils.stringToDate(DateUtils.getDaysFormat(date) + " 10:00:00"),
							DateUtils.stringToDate(DateUtils.getAfterDays() + " 10:00:00"));
					if (!compareToImportDate) {
						lnecAcceptanceDate = getLnecAcceptanceDate(
								DateUtils.stringToDate(DateUtils.getAfterDays() + " 10:00:00"), warehouse,
								numberPackage);
					} else {
						lnecAcceptanceDate = getLnecAcceptanceDate(
								DateUtils.stringToDate(DateUtils.getDaysFormat(date) + " 10:00:00"), warehouse,
								numberPackage);
					}
				} else {
					// 判断预约送达是否大于等于导入日期加2
					boolean compareToImportDate = DateUtils.compareToDate(
							DateUtils.stringToDate(DateUtils.getDaysFormat(date) + " 10:00:00"),
							DateUtils.stringToDate(DateUtils.getAfterTowDays() + " 10:00:00"));
					if (!compareToImportDate) {
						throw new FailRuntimeException("当前时间已经晚于16:30，预约日期必须从后天开始");
					} else {
						lnecAcceptanceDate = getLnecAcceptanceDate(
								DateUtils.stringToDate(DateUtils.getDaysFormat(date) + " 10:00:00"), warehouse,
								numberPackage);
					}
				}
				lAppo.setOddNum(oddNum);
				lAppo.setItem(item);
				lAppo.setNumber(number);
				lAppo.setNumberPackage(numberPackage);
				date = DateUtils.strToDate(DateUtils.getNewDaysFormat(date) + " 10:00:00");
				lAppo.setDateAppoint(date);
				lAppo.setShipper(shipper);
				lAppo.setShipperCity(shipperCity);
				lAppo.setCarrier(carrier);
				lAppo.setReservatePerson(reservatePerson);
				lAppo.setAppointCompany(appointCompany);
				lAppo.setPhone(phone);
				lAppo.setWarehouse(warehouse);
				lAppo.setLnecAcceptanceDate(lnecAcceptanceDate);
				lAppo.setSerialNumber(flowMap.get(lnecAcceptanceDate + warehouse));
				StorageAppoint storageAppoint = storageAppointMapper.queryStorageAppointByName(warehouse);
				String lnecStorageReservatnum = DateUtils.getDaysFormat(lnecAcceptanceDate).replaceAll("-", "")
						+ storageAppoint.getWarehouseCode() + flowMap.get(lnecAcceptanceDate + warehouse);
				lAppo.setLnecStorageReservatnum(lnecStorageReservatnum);
				logisticsAppointment.add(lAppo);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		map.clear();
		flowMap.clear();
		return logisticsAppointment;
	}

	/**
	 * 获取受理送达日期
	 * 
	 * @param date
	 * @return
	 */
	Date getLnecAcceptanceDate(Date lnecAcceptanceDate, String warehouse, Integer numberPackage) {
		StorageAppoint storageAppoint = storageAppointMapper.queryStorageAppointByName(warehouse);
		Integer appointLimit = storageAppoint.getAppointLimit();
		Boolean flag = true;
		while (flag) {
			// 根据受理送达日期进行查询
			Integer totalNumberPackage = 0;
			if (null == map.get(lnecAcceptanceDate + warehouse))
				totalNumberPackage = logisticsMapper.queryByLnecAcceptanceDateAndWareHouse(lnecAcceptanceDate,
						warehouse);
			map.put(lnecAcceptanceDate + warehouse,
					null == totalNumberPackage
							? (null == map.get(lnecAcceptanceDate + warehouse) ? numberPackage
									: map.get(lnecAcceptanceDate + warehouse) + numberPackage)
							: (null == map.get(lnecAcceptanceDate + warehouse) ? 0
									: map.get(lnecAcceptanceDate + warehouse)) + totalNumberPackage + numberPackage);
			if (appointLimit >= (null == totalNumberPackage
					? (null == map.get(lnecAcceptanceDate + warehouse) ? 0 : map.get(lnecAcceptanceDate + warehouse))
					: ((null == map.get(lnecAcceptanceDate + warehouse) ? 0
							: map.get(lnecAcceptanceDate + warehouse))))) {
				// 仓库预约限量大于总量
				flag = false;
				String serialNumber = logisticsMapper.queryMaxNum(lnecAcceptanceDate, warehouse);
				if (null == serialNumber) {
					String num = flowMap.get(lnecAcceptanceDate + warehouse);
					if (null == num) {
						flowMap.put(lnecAcceptanceDate + warehouse, "01");
					} else {
						int parseInt = Integer.parseInt(num);
						parseInt = parseInt + 1;
						if (String.valueOf(parseInt).length() == 1) {
							flowMap.put(lnecAcceptanceDate + warehouse, "0" + parseInt);
						} else {
							flowMap.put(lnecAcceptanceDate + warehouse, parseInt + "");
						}
					}
				} else {
					String num = flowMap.get(lnecAcceptanceDate + warehouse);
					if (null == num) {
						int parseInt = Integer.parseInt(serialNumber);
						parseInt = parseInt + 1;
						if (String.valueOf(parseInt).length() == 1) {
							flowMap.put(lnecAcceptanceDate + warehouse, "0" + parseInt);
						} else {
							flowMap.put(lnecAcceptanceDate + warehouse, parseInt + "");
						}
					} else {
						int parseInt = Integer.parseInt(num);
						parseInt = parseInt + 1;
						if (String.valueOf(parseInt).length() == 1) {
							flowMap.put(lnecAcceptanceDate + warehouse, "0" + parseInt);
						} else {
							flowMap.put(lnecAcceptanceDate + warehouse, parseInt + "");
						}
					}
				}
			} else {
				map.put(lnecAcceptanceDate + warehouse, map.get(lnecAcceptanceDate + warehouse) - numberPackage);
				// 仓库预约限量小于总量
				lnecAcceptanceDate = DateUtils.getDateAddDays(lnecAcceptanceDate, 1);
				flag = true;

			}
		}
		return lnecAcceptanceDate;
	}

}
