package com.ln.tms.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TmsUtils - 工具类
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
public final class TmsUtils {

    public static final String PROVINCE = "省";
    public static final String CITY = "市";
    public static final String DISTRICT = "区";
    public static final String COUNTY = "县";
    public static final String YT_SPLIT = "已发出 下一站";
    public static final String YT_L = "【";
    public static final String YT_R = "】";
    public static final String YT_YSJ = "已收件";
    public static final String YT_YFC = "已发出";
    public static final String YT_YCB = "已拆包";
    public static final String YT_YDB = "已打包";
    public static final String YT_YSR = "已收入";
    public static final String YT_HFY = "海外发运";
    public static final String YT_HSR = "海外收入";
    public static final String YT_YQF = "航班已起飞";
    public static final String YT_PJZ = "派件中";
    public static final String YT_YZ = "驿站";
    public static final String YT_HZ = "合作点";
    public static final String YT_ZT = "自提";
    public static final String YT_YQS = "已签收";
    public static final String ST_SJ = "收件";
    public static final String ST_PJ = "派件";
    public static final String ST_QS = "签收";
    public static final String ST_FJ = "发件";
    public static final String ST_DJ = "到件";

    /**
     * 拆分list
     *
     * @param list     List
     * @param capacity size
     * @param <T>      T
     * @return List
     */
    public static <T> List<List<T>> getSubList(List<T> list, Integer capacity) {
        Assert.notEmpty(list);
        Integer total = list.size();
        Integer groupNum = total % capacity == 0 ? total / capacity : total / capacity + 1;
        List<List<T>> result = new ArrayList<List<T>>();
        for (int i = 0; i < groupNum; i++) {
            Integer start = i * capacity;
            Integer end = start + capacity > total ? total : start + capacity;
            List<T> subList = list.subList(start, end);
            result.add(subList);
        }
        return result;
    }

    /**
     * 获取客户端IP
     *
     * @param request request
     * @return IP
     */
    public static String getRemoteHost(javax.servlet.http.HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
    }

    /**
     * 获取UUID
     *
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 创建文件夹
     *
     * @param destDirName
     * @return
     */
    public static void createDir(String destDirName) {
        File dir = new File(destDirName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * 文件上传到指定文件夹
     *
     * @param fileDiskPath
     * @param multipartFile
     * @return
     * @throws IOException
     */
    public static File uploadFile(String fileDiskPath, MultipartFile multipartFile) throws IOException {
        TmsUtils.createDir(fileDiskPath);
        String fileName = StringUtils.substringBeforeLast(multipartFile.getOriginalFilename(), ".csv");
        File targetFile = new File(StringUtils.join(fileDiskPath, fileName, "_", DateUtils.getDateformat(new Date(), "yyyyMMddHHmmss"), ".csv"));
        FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), targetFile);
        return targetFile;
    }


    /**
     * 驼峰法转下划线
     *
     * @param line 源字符串
     * @return 转换后的字符串
     */
    public static String cameUnderline(String line) {
        if (line == null || "".equals(line)) {
            return "";
        }
        line = String.valueOf(line.charAt(0)).toUpperCase().concat(line.substring(1));
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile("[A-Z]([a-z\\d]+)?");
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String word = matcher.group();
            sb.append(word.toUpperCase());
            sb.append(matcher.end() == line.length() ? "" : "_");
        }
        return sb.toString();
    }
}