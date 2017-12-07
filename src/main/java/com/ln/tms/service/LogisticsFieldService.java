package com.ln.tms.service;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ln.tms.mapper.LogisticsMapper;
import com.ln.tms.mapper.MonitoMapper;
import com.ln.tms.pojo.ExportFieldVo;
import com.ln.tms.pojo.ExportFields;
import com.ln.tms.pojo.LogisticsAppointment;
import com.ln.tms.util.CsvWriterUtils;
import com.ln.tms.util.DateUtils;
import com.ln.tms.util.TmsUtils;

@Service
public class LogisticsFieldService {

	@Autowired
	private MonitoMapper monitoMapper;

	@Autowired
	private LogisticsMapper logisticsMapper;

	/**
	 * 文件存放磁盘
	 */
	@Value("${file.disk.path}")
	private String fileDiskPath;

	/**
	 * 文件列分隔符
	 */
	private static final String CSV_COLUMN_SEPARATOR = ",";

	/**
	 * 文件列换行符
	 */
	public static final String CSV_RN = "\r\n";

	/**
	 * 文件制表符
	 */
	private static final String NUM_TAB = "\t";

	/**
	 * 文件后缀
	 */
	private static final String CSV_SF = ".csv";

	/**
	 * 根据所属模块查询导出字段
	 *
	 * @param exportFields
	 *            导出字段Vo
	 * @return ExportFields 导出字段Vo
	 */
	@Transactional
	public ExportFields getExportFieldsByBelongTo(ExportFields exportFields) {
		return monitoMapper.getExportFieldsByBelongTo(exportFields);
	}

	/**
	 * 处理显示可导出的字段and是否选择过
	 *
	 * @param retExportFields
	 *            ExportFields
	 * @param seeExportFields
	 *            ExportFields
	 * @return List<ExportFieldVo>
	 */
	public List<ExportFieldVo> getSeeExportFieldList(ExportFields retExportFields, ExportFields seeExportFields) {
		List<ExportFieldVo> retList = new ArrayList<ExportFieldVo>();
		String[] expNameListArr = seeExportFields.getExpNameList().split("#"); // 显示的字段（中文）
		String[] expFieldNameArr = seeExportFields.getExpFieldName().split("#"); // 显示的字段（字段名）
		for (int i = 0; i < expNameListArr.length; i++) {
			ExportFieldVo efVo = new ExportFieldVo();
			efVo.setExpName(expNameListArr[i]);
			efVo.setExpFieldName(expFieldNameArr[i]);
			// 判断是否包含（1：选择；0：未选择过）
			if (retExportFields != null
					&& StringUtils.contains(retExportFields.getExpFieldName(), expFieldNameArr[i])) {
				efVo.setIsSelect("1");
			} else {
				efVo.setIsSelect("0");
			}
			retList.add(efVo);
		}
		return retList;
	}

	/**
	 * 保存导出字段
	 *
	 * @param exportFields
	 *            导出字段Vo
	 */
	@Transactional
	public void saveExportFields(ExportFields exportFields) {
		monitoMapper.saveExportFields(exportFields);
	}

	/**
	 * 检查所属模块查询是否存在
	 *
	 * @param exportFields
	 *            导出字段Vo
	 * @return true：存在 false：不存在
	 */
	@Transactional
	public boolean checkExortFields(ExportFields exportFields) {
		return monitoMapper.checkExortFields(exportFields) > 0;
	}

	/**
	 * 修改导出字段
	 *
	 * @param exportFields
	 *            导出字段Vo
	 */
	@Transactional
	public void updExportFields(ExportFields exportFields) {
		monitoMapper.updExportFields(exportFields);
	}

	public String exportExcle(String type, LogisticsAppointment logisticsAppointment, ExportFields exportFields,
			HttpServletResponse response) {

		StringBuffer buf = new StringBuffer();
		// csv标题行处理
		buf.append(this.appendExcleTitle(exportFields.getExpNameList()));

		// csv数据输出处理
		List<LogisticsAppointment> logisticsList = logisticsMapper.querylogisticListNoPage(logisticsAppointment);
		buf.append(this.appendExcleDate(logisticsList, exportFields.getExpFieldName()));

		// csv文件名处理
		TmsUtils.createDir(fileDiskPath);
		String fn = "李宁电子商务仓入库预约信息导出".concat(DateUtils.getDateformat(new Date(), "yyyyMMddHHmmss") + CSV_SF);// 文件名
		String fileName = fileDiskPath + fn;

		try {
			CsvWriterUtils.exportCsv(fileName, buf.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fn;
	}

	/**
	 * 导出追加excel的标题行
	 *
	 * @param expNameList
	 *            导出excel对应的标题字符串(用"#"隔开)如：发货日期#快递号#
	 * @return StringBuffer 追加excel的标题行
	 */
	@Transactional
	private StringBuffer appendExcleTitle(String expNameList) {
		String[] expNameListArr;// 标题数组(导出名称列表（中文）)
		expNameListArr = expNameList.substring(0, expNameList.length() - 1).split("#");
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < expNameListArr.length; i++) {
			if (i == expNameListArr.length - 1) {
				buf.append(expNameListArr[i]);
			} else {
				buf.append(expNameListArr[i]).append(CSV_COLUMN_SEPARATOR);
			}
		}
		buf.append(CSV_RN);// 换行
		return buf;
	}

	/**
	 * 导出追加excel的数据行
	 *
	 * @param infoList
	 *            数据list
	 * @param expFieldName
	 *            导出excel对应的字段名(用"#"隔开),如:fhdh#kddh#
	 * @return StringBuffer 追加excel的数据行
	 */
	@Transactional
	private StringBuffer appendExcleDate(List<LogisticsAppointment> logisticsList, String expFieldName) {
		String[] expFieldNamesArr;// 导出字段名数组(导出字段名称（字段名）)
		expFieldNamesArr = expFieldName.substring(0, expFieldName.length() - 1).split("#");
		StringBuffer buf = new StringBuffer();
		for (LogisticsAppointment logistics : logisticsList) {
			for (int i = 0; i < expFieldNamesArr.length; i++) {
				Field[] fields = logistics.getClass().getDeclaredFields();
				for (int j = 0; j < fields.length; j++) {
					if (fields[j].getName().equals(expFieldNamesArr[i])) { // 匹配字段名
						try {
							String expFieldValue = "";
							if ("oddNum".equals(expFieldNamesArr[i])) {
								expFieldValue = logistics.getOddNum();
							} else if ("item".equals(expFieldNamesArr[i])) {
								expFieldValue = logistics.getItem();
							} else if ("number".equals(expFieldNamesArr[i])) {
								expFieldValue = null == logistics.getNumber() ? "0" : logistics.getNumber().toString();
							} else if ("numberPackage".equals(expFieldNamesArr[i])) {
								expFieldValue = null == logistics.getNumberPackage() ? "0"
										: logistics.getNumberPackage().toString();
							} else if ("dateAppoint".equals(expFieldNamesArr[i])) {
								expFieldValue = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
										.format(logistics.getDateAppoint());
							} else if ("shipper".equals(expFieldNamesArr[i])) {
								expFieldValue = logistics.getShipper();
							} else if ("shipperCity".equals(expFieldNamesArr[i])) {
								expFieldValue = logistics.getShipperCity();
							} else if ("carrier".equals(expFieldNamesArr[i])) {
								expFieldValue = logistics.getCarrier();
							} else if ("reservatePerson".equals(expFieldNamesArr[i])) {
								expFieldValue = logistics.getReservatePerson();
							} else if ("appointCompany".equals(expFieldNamesArr[i])) {
								expFieldValue = logistics.getAppointCompany();
							} else if ("phone".equals(expFieldNamesArr[i])) {
								expFieldValue = logistics.getPhone();
							} else if ("warehouse".equals(expFieldNamesArr[i])) {
								expFieldValue = logistics.getWarehouse();
							} else if ("lnecStorageReservatnum".equals(expFieldNamesArr[i])) {
								expFieldValue = logistics.getLnecStorageReservatnum();
							} else if ("lnecAcceptanceDate".equals(expFieldNamesArr[i])) {
								expFieldValue = null == logistics.getLnecAcceptanceDate() ? ""
										: new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
												.format(logistics.getLnecAcceptanceDate());
							}
							buf.append(expFieldValue).append(CSV_COLUMN_SEPARATOR).append(NUM_TAB);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					;
				}
			}
			buf.append(CSV_RN);// 换行
		}
		return buf;
	}

}
