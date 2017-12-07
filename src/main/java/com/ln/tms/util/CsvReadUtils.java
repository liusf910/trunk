package com.ln.tms.util;

import com.csvreader.CsvReader;
import com.ln.tms.exception.FailRuntimeException;
import com.ln.tms.pojo.TimeLimit;
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
public class CsvReadUtils {
    /**
     * csv文件导入、解析
     *
     * @param file    文件
     * @param csvHead csv文件标题，用来判断头部信息是否正确
     * @return 解析完成之后时效数据集合
     */
    public static List<TimeLimit> resolveCsvTL(File file, String csvHead) {
        List<TimeLimit> date = new ArrayList<>();
        CsvReader r = null;
        try {
            r = new CsvReader(new FileInputStream(file), ',', Charset.forName("GB2312"));
            //读取表头
            r.readHeaders();
            String[] heads = r.getHeaders();
            String head = StringUtils.join(heads, ",");
            if (!csvHead.equals(head)) {
                throw new FailRuntimeException("The title is wrong");
            }
            //逐条读取记录，直至读完
            while (r.readRecord()) {
                TimeLimit limit = new TimeLimit();
                //读取一条记录
                if (StringUtils.isNotBlank(r.get("仓库")) && StringUtils.isNotBlank(r.get("目的省")) && StringUtils.isNotBlank(r.get("目的市"))) {
                    limit.setWarehouse(r.get("仓库"));
                    limit.setProvince(r.get("目的省"));
                    limit.setCity(r.get("目的市"));
                    limit.setSte(StringUtils.isNotBlank(r.get("申通E物流")) ? Integer.parseInt(r.get("申通E物流")) : null);
                    limit.setYt(StringUtils.isNotBlank(r.get("圆通速递")) ? Integer.parseInt(r.get("圆通速递")) : null);
                    limit.setHt(StringUtils.isNotBlank(r.get("汇通快运")) ? Integer.parseInt(r.get("汇通快运")) : null);
                    limit.setZt(StringUtils.isNotBlank(r.get("中通速递")) ? Integer.parseInt(r.get("中通速递")) : null);
                    limit.setYd(StringUtils.isNotBlank(r.get("韵达快递")) ? Integer.parseInt(r.get("韵达快递")) : null);
                    limit.setSf(StringUtils.isNotBlank(r.get("顺丰速运")) ? Integer.parseInt(r.get("顺丰速运")) : null);
                    limit.setEms(StringUtils.isNotBlank(r.get("EMS")) ? Integer.parseInt(r.get("EMS")) : null);
                    limit.setJd(StringUtils.isNotBlank(r.get("京东快递")) ? Integer.parseInt(r.get("京东快递")) : null);
                    limit.setJdcod(StringUtils.isNotBlank(r.get("京东COD")) ? Integer.parseInt(r.get("京东COD")) : null);
                    date.add(limit);
                } else {
                    throw new FailRuntimeException("data column is null");
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
