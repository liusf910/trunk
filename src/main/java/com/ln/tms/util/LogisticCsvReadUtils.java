package com.ln.tms.util;

import com.csvreader.CsvReader;
import com.ln.tms.exception.FailRuntimeException;
import com.ln.tms.pojo.LogisticsAppointmentTemp;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * CsvReadUtils - csv文件读取
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
public class LogisticCsvReadUtils {
	/**
	 * csv文件导入、解析
	 *
	 * @param file
	 *            文件
	 * @param csvHead
	 *            csv文件标题，用来判断头部信息是否正确
	 * @return 解析完成之后时效数据集合
	 */
	public static List<LogisticsAppointmentTemp> resolveCsvTL(File file, String csvHead) {
		List<LogisticsAppointmentTemp> date = new ArrayList<>();
		CsvReader r = null;
		try {
			r = new CsvReader(new FileInputStream(file), ',', Charset.forName("GB2312"));
			// 读取表头
			r.readHeaders();
			String[] heads = r.getHeaders();
			String head = StringUtils.join(heads, ",");
			if (!csvHead.equals(head)) {
				throw new FailRuntimeException("The title is wrong");
			}
			// 逐条读取记录，直至读完
			while (r.readRecord()) {
				LogisticsAppointmentTemp limit = new LogisticsAppointmentTemp();
				// 读取一条记录
				if (StringUtils.isNotBlank(r.get("80或180单号")) && StringUtils.isNotBlank(r.get("预约送达日期"))
						&& StringUtils.isNotBlank(r.get("仓库"))) {
					limit.setWarehouse(r.get("仓库"));
					limit.setOddNum(r.get("80或180单号"));
					limit.setDateAppoint(r.get("预约送达日期"));
					limit.setItem(r.get("品项"));
					limit.setNumber(StringUtils.isNotBlank(r.get("数量")) ? Integer.parseInt(r.get("数量")) : null);
					limit.setNumberPackage(StringUtils.isNotBlank(r.get("件数")) ? Integer.parseInt(r.get("件数")) : null);
					limit.setShipper(r.get("发货方"));
					limit.setShipperCity(r.get("发货城市"));
					limit.setCarrier(r.get("承运商"));
					limit.setReservatePerson(r.get("预约人"));
					limit.setAppointCompany(r.get("预约人所属公司"));
					limit.setPhone(r.get("预约人联系方式"));
					date.add(limit);
				} else {
					throw new FailRuntimeException("数据不能为空");
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			r.close();
		}
		return date;
	}
}
