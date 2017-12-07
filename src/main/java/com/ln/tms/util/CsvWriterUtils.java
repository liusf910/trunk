package com.ln.tms.util;

import com.ln.tms.pojo.InfoTsTemp;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CsvWriterUtils - 写出
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@SuppressWarnings("Duplicates")
public class CsvWriterUtils {

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
     * 空值
     */
    private static final String NONE = "无数据";

    /**
     * 创建文件并写入标题
     *
     * @param title        标题
     * @param type         文件类型名称
     * @param fileDiskPath 磁盘路径
     * @return 返回文件磁盘路径
     */
    public static String initCsvData(String title, String type, String fileDiskPath) {
        StringBuffer buf = new StringBuffer();
        String[] displayColNamesArr;
        displayColNamesArr = title.split(CSV_COLUMN_SEPARATOR);
        String fn = type.concat(DateUtils.getDateformat(new Date(), "yyyyMMddHHmmss") + CSV_SF);
        for (int i = 0; i < displayColNamesArr.length; i++) {
            if (i == displayColNamesArr.length - 1) {
                buf.append(displayColNamesArr[i]);
            } else {
                buf.append(displayColNamesArr[i]).append(CSV_COLUMN_SEPARATOR);
            }

        }
        TmsUtils.createDir(fileDiskPath);
        String fileName = fileDiskPath + fn;
        buf.append(CSV_RN);
        exportCsv(fileName, buf.toString());
        return fileName;
    }

    /**
     * 格式数据(揽件，签收)
     *
     * @param infoTsTemps 详细信息
     * @return 处理后的字符串
     */
    public static Map<String, Object> listToTookCsvData(List<InfoTsTemp> infoTsTemps, String type) {
        Map<String, Object> result = new HashMap<String, Object>();
        StringBuffer buf = new StringBuffer();
        if (type.equals("1-1")) {
            for (InfoTsTemp tsTemp : infoTsTemps) {
                buf.append(tsTemp.getShipmentsDate());
                buf.append(CSV_COLUMN_SEPARATOR);
                buf.append(tsTemp.getOrderCode() + NUM_TAB);
                buf.append(NUM_TAB);
                buf.append(CSV_COLUMN_SEPARATOR);
                buf.append(tsTemp.getCustomName());
                buf.append(CSV_COLUMN_SEPARATOR);
                buf.append(tsTemp.getLogisticCode());
                buf.append(NUM_TAB);
                buf.append(CSV_COLUMN_SEPARATOR);
                buf.append(tsTemp.getCourierName());
                buf.append(CSV_COLUMN_SEPARATOR);
                buf.append(tsTemp.getFileTookTime());
                buf.append(CSV_COLUMN_SEPARATOR);
                buf.append(tsTemp.getTookReason().getDesc() != null ? tsTemp.getTookReason().getDesc() : null);
                buf.append(CSV_COLUMN_SEPARATOR);
                buf.append(tsTemp.getShipmentsTime());
                buf.append(CSV_COLUMN_SEPARATOR);
                buf.append(tsTemp.getOverDay() != null && tsTemp.getOverDay() > 0 ? "是" : "否");
                buf.append(CSV_COLUMN_SEPARATOR);
                buf.append(getFormatFlag(tsTemp.getFlag()));
                buf.append(CSV_RN);
            }
        } else if (type.equals("2-1")) {
            for (InfoTsTemp tsTemp : infoTsTemps) {
                buf.append(tsTemp.getShipmentsDate());
                buf.append(CSV_COLUMN_SEPARATOR);
                buf.append(tsTemp.getOrderCode());
                buf.append(NUM_TAB);
                buf.append(CSV_COLUMN_SEPARATOR);
                buf.append(tsTemp.getCustomName());
                buf.append(CSV_COLUMN_SEPARATOR);
                buf.append(tsTemp.getLogisticCode());
                buf.append(NUM_TAB);
                buf.append(CSV_COLUMN_SEPARATOR);
                buf.append(tsTemp.getCourierName());
                buf.append(CSV_COLUMN_SEPARATOR);
                buf.append(tsTemp.getFileSignTime());
                buf.append(CSV_COLUMN_SEPARATOR);
                buf.append(tsTemp.getSignReason() != null ? tsTemp.getSignReason().getDesc() : null);
                buf.append(CSV_COLUMN_SEPARATOR);
                buf.append(tsTemp.getOrderStatus() != null ? tsTemp.getOrderStatus().getDesc() : null);
                buf.append(CSV_COLUMN_SEPARATOR);
                buf.append(tsTemp.getShipmentsTime());
                buf.append(CSV_COLUMN_SEPARATOR);
                buf.append(tsTemp.getPlanTime());
                buf.append(CSV_COLUMN_SEPARATOR);
                buf.append(tsTemp.getOverDay() != null && tsTemp.getOverDay() > 0 ? "是" : "否");
                buf.append(CSV_COLUMN_SEPARATOR);
                buf.append(getFormatFlag(tsTemp.getFlag()));
                buf.append(CSV_RN);
            }
        }
        result.put("data", buf.toString().replaceAll("\"", ""));
        return result;
    }

    private static String getFormatFlag(String flag) {
        if (StringUtils.isBlank(flag)) {
            return flag;
        }
        String result = "";
        switch (flag) {
            case "2":
                result = "忽略(数据重复，已导过)";
                break;
            case "3":
                result = "忽略(已对接系统揽收时间)";
                break;
            default:
                result = "成功";
        }
        return result;
    }

    /**
     * 写入文件
     *
     * @param fileName 文件名称
     * @param content  写入内容
     */
    public static void exportCsv(String fileName, String content) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(fileName, true), "GB2312"));
            out.write(content);
        } catch (Exception e) {
            File file = new File(fileName);
            if (!file.delete()) {
                throw new NullPointerException("Folder Delete failed");
            }
            throw new RuntimeException(e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
