package com.ln.tms.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ln.tms.bean.InfoWhere;
import com.ln.tms.bean.Trace;
import com.ln.tms.enums.StateType;
import com.ln.tms.mapper.MonitoMapper;
import com.ln.tms.pojo.ExportFieldVo;
import com.ln.tms.pojo.ExportFields;
import com.ln.tms.pojo.InfoMonito;
import com.ln.tms.util.CsvWriterUtils;
import com.ln.tms.util.DateUtils;
import com.ln.tms.util.JsonUtils;
import com.ln.tms.util.TmsUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * MonitoService - 后台管理监测Service
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Service
public class MonitoService extends BaseService<InfoMonito> {

    /**
     * 监测管理Mapper
     */
    @Autowired
    private MonitoMapper monitoMapper;

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
     * 文件存放磁盘
     */
    @Value("${file.disk.path}")
    private String fileDiskPath;


    /**
     * 保存导出字段
     *
     * @param exportFields 导出字段Vo
     */
    @Transactional
    public void saveExportFields(ExportFields exportFields) {
        monitoMapper.saveExportFields(exportFields);
    }

    /**
     * 检查所属模块查询是否存在
     *
     * @param exportFields 导出字段Vo
     * @return true：存在 false：不存在
     */
    @Transactional
    public boolean checkExortFields(ExportFields exportFields) {
        return monitoMapper.checkExortFields(exportFields) > 0 ;
    }

    /**
     * 修改导出字段
     *
     * @param exportFields 导出字段Vo
     */
    @Transactional
    public void updExportFields(ExportFields exportFields) {
        monitoMapper.updExportFields(exportFields);
    }

    /**
     * 根据所属模块查询导出字段
     *
     * @param exportFields 导出字段Vo
     * @return ExportFields 导出字段Vo
     */
    @Transactional
    public ExportFields getExportFieldsByBelongTo(ExportFields exportFields) {
        return monitoMapper.getExportFieldsByBelongTo(exportFields);
    }

    /**
     * 根据导出字段设置表设置已勾选导出字段（导出：1；未导出：0）；供第二次选择已勾选的之用
     *
     * @param exportFields 导出字段Vo
     * @return Map<String, String> 导出字段设置表Map
     */
    @Transactional
    public Map<String, String> getReturnMap(ExportFields exportFields) {
        Map<String, String> returnMap = null;
        if (exportFields != null) {
            returnMap = new HashMap<String, String>();
            if (StringUtils.isNotBlank(exportFields.getExpFieldName())) {
                String exp_field_name[] = exportFields.getExpFieldName().split(
                        "#"); // 导出字段名称（字段名）数组
                for (String str : exp_field_name) {
                    returnMap.put(str, "1");
                }
            }
            if (StringUtils.isNotBlank(exportFields.getNoexpFieldName())) {
                String noexp_field_name[] = exportFields.getNoexpFieldName()
                        .split("#"); // 未导出字段名称（字段名）数组
                for (String str : noexp_field_name) {
                    returnMap.put(str, "0");
                }
            }
        }
        return returnMap;
    }

    /**
     * 根据速递id查询详情信息
     *
     * @param infoId 速递id
     * @return InfoMonito  速递信息Vo
     */
    @Transactional
    public InfoMonito queryInfoByInfoId(String infoId) {
        return monitoMapper.queryInfoByInfoId(infoId);
    }

    /**
     * excel导出
     *
     * @param type         took 揽收 sign 签收  all 全部
     * @param infoWhere    查询条件
     * @param exportFields 导出字段vo
     * @return String      导出文件的文件名
     */
    @Transactional
    @Cacheable(value = "info", key = "#infoWhere.logisticCode+'_'+#infoWhere.signToday+'_'"
            + "+#infoWhere.shipperCode+'_'+#infoWhere.storageCode+'_'+#infoWhere.orderState+'_'"
            + "+#infoWhere.tookReason+'_'+#infoWhere.signReason+'_'+#infoWhere.state+'_'"
            + "+#infoWhere.shipmentsTimeStart+'_'+#infoWhere.shipmentsTimeEnd+'_'+#infoWhere.planTimeStart+'_'"
            + "+#infoWhere.planTimeEnd+'_'+#infoWhere.tookTimeStart+'_'+#infoWhere.tookTimeEnd+'_'"
            + "+#infoWhere.signTimeStart+'_'+#infoWhere.signTimeEnd+'_'+#infoWhere.payTimeStart+'_'+#infoWhere.payTimeEnd+'_'+#infoWhere.lsOver+'_'"
            + "+#infoWhere.psOver+'_'+#infoWhere.qsOver+'_'+#infoWhere.qsOver+'_'"
            + "+#infoWhere.signFor+'_'+#infoWhere.dqOver+'_'+#infoWhere.dlsOver+'_'"
            + "+#infoWhere.rgxtFlag+'_'+#infoWhere.fhTookFlag+'_'+#infoWhere.fkzlsFlag+'_'+#infoWhere.orderField+'_'"
            + "+#infoWhere.orderDirection+'_'+#infoWhere.tookReasonNull+'_'+#infoWhere.signReasonNull+'_'"
            + "+#type+'_'+#exportFields.expNameList+'_'+#exportFields.expFieldName")
    public String exportExcle(String type, InfoWhere infoWhere,
                              ExportFields exportFields, HttpServletResponse response) {

        StringBuffer buf = new StringBuffer();
        // csv标题行处理
        buf.append(this.appendExcleTitle(exportFields.getExpNameList()));

        // csv数据输出处理
        List<InfoMonito> infoList = monitoMapper.queryInfoListNoPage(infoWhere);
        buf.append(this.appendExcleDate(infoList,
                exportFields.getExpFieldName()));

        // csv文件名处理
        TmsUtils.createDir(fileDiskPath);
        String fn = "监测管理导出".concat(DateUtils.getDateformat(new Date(),
                "yyyyMMddHHmmss") + CSV_SF);// 文件名
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
     * @param expNameList 导出excel对应的标题字符串(用"#"隔开)如：发货日期#快递号#
     * @return StringBuffer  追加excel的标题行
     */
    @Transactional
    private StringBuffer appendExcleTitle(String expNameList) {
        String[] expNameListArr;// 标题数组(导出名称列表（中文）)
        expNameListArr = expNameList.substring(0, expNameList.length() - 1)
                .split("#");
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
     * @param infoList     数据list
     * @param expFieldName 导出excel对应的字段名(用"#"隔开),如:fhdh#kddh#
     * @return StringBuffer 追加excel的数据行
     */
    @Transactional
    private StringBuffer appendExcleDate(List<InfoMonito> infoList,
                                         String expFieldName) {
        String[] expFieldNamesArr;// 导出字段名数组(导出字段名称（字段名）)
        expFieldNamesArr = expFieldName.substring(0, expFieldName.length() - 1)
                .split("#");
        StringBuffer buf = new StringBuffer();
        for (InfoMonito info : infoList) {
            for (int i = 0; i < expFieldNamesArr.length; i++) {
                Field[] fields = info.getClass().getDeclaredFields();
                for (int j = 0; j < fields.length; j++) {
                    if (fields[j].getName().equals(expFieldNamesArr[i])) { // 匹配字段名
                        try {
                            String expFieldValue = "";
                            // 处理个别判断字段
                            // 揽件是否超时
                            if ("tookOut".equals(expFieldNamesArr[i])) {
                                expFieldValue = "是";
                                if (info.getShipmentsTime() != null
                                        && info.getXtTookTime() != null) {
                                    int hour = Integer
                                            .parseInt(new SimpleDateFormat("HH").format(info
                                                    .getShipmentsTime()));
                                    if (hour <= 20) {
                                        String shipRq = new SimpleDateFormat(
                                                "yyyy-MM-dd").format(info
                                                .getShipmentsTime());
                                        String xtTookRq = new SimpleDateFormat(
                                                "yyyy-MM-dd").format(info
                                                .getXtTookTime());
                                        if (shipRq.equals(xtTookRq)) {
                                            expFieldValue = "否";
                                        }
                                    } else {
                                        long hours = DateUtils.getDateHours(
                                                info.getXtTookTime(),
                                                info.getShipmentsTime());
                                        if (hours <= 24) {
                                            expFieldValue = "否";
                                        }
                                    }
                                }
                            }
                            // 人工导入揽收时间标记
                            if ("tookFlag".equals(expFieldNamesArr[i])) {
                                if (info.getRgTookTime() != null) {
                                    expFieldValue = new SimpleDateFormat(
                                            "yyyy-MM-dd HH:mm:ss").format(info
                                            .getRgTookTime());
                                }
                            }
                            // 判别列
                            if ("pbLine".equals(expFieldNamesArr[i])) {
                                if (info.getXtSignTime() != null
                                        && info.getRgSignTime() != null) {
                                    long hours = DateUtils.getDateHours(
                                            info.getXtSignTime(),
                                            info.getRgSignTime());
                                    if (hours > 24) {
                                        expFieldValue = new SimpleDateFormat(
                                                "yyyy-MM-dd HH:mm:ss")
                                                .format(info.getXtSignTime());
                                    }
                                } else if (info.getXtSignTime() == null
                                        && info.getRgSignTime() != null) {
                                    expFieldValue = new SimpleDateFormat(
                                            "yyyy-MM-dd HH:mm:ss").format(info
                                            .getRgSignTime());
                                }
                            }
                            // 人工导入早于系统对接24h 标记
                            if ("rgxtFlag".equals(expFieldNamesArr[i])) {
                                expFieldValue = "否";
                                if (info.getXtSignTime() != null
                                        && info.getRgSignTime() != null) {
                                    long hours = DateUtils.getDateHours(
                                            info.getXtSignTime(),
                                            info.getRgSignTime());
                                    if (hours > 24) {
                                        expFieldValue = "是";
                                    }
                                }
                            }
                            // 20点后发货当天揽收 标记
                            if ("fhTookFlag".equals(expFieldNamesArr[i])) {
                                expFieldValue = "否";
                                if (info.getShipmentsTime() != null) {
                                    int hour = Integer
                                            .parseInt(new SimpleDateFormat("HH").format(info
                                                    .getShipmentsTime()));
                                    if (hour > 20 && info.getXtTookTime() != null) {
                                        String shipRq = new SimpleDateFormat(
                                                "yyyy-MM-dd").format(info
                                                .getShipmentsTime());
                                        String xtTookRq = new SimpleDateFormat(
                                                "yyyy-MM-dd").format(info
                                                .getXtTookTime());
                                        if (shipRq.equals(xtTookRq)) {
                                            expFieldValue = "是";
                                        }
                                    }
                                }
                            }
                            //20170418addstart
                            //配送状态
                            if ("orderState".equals(expFieldNamesArr[i])) {
                                if (StringUtils.isNotBlank(info.getOrderState())) {
                                    expFieldValue = info.getOrderState();
                                }
                                buf.append(expFieldValue).append(CSV_COLUMN_SEPARATOR).append(NUM_TAB);
                                break;
                            }
                            //揽收超时原因
                            if ("tookOutReason".equals(expFieldNamesArr[i])) {
                                if (StringUtils.isNotBlank(info.getTookOutReason())) {
                                    expFieldValue = info.getTookOutReason();
                                }
                                buf.append(expFieldValue).append(CSV_COLUMN_SEPARATOR).append(NUM_TAB);
                                break;
                            }
                            //签收超时原因
                            if ("reason".equals(expFieldNamesArr[i])) {
                                if (StringUtils.isNotBlank(info.getReason())) {
                                    expFieldValue = info.getReason();
                                }
                                buf.append(expFieldValue).append(CSV_COLUMN_SEPARATOR).append(NUM_TAB);
                                break;
                            }
                            //20170418addend
                            // 快递备注运作状态
                            if ("beiuzhuState".equals(expFieldNamesArr[i])) {
                                if (StringUtils.isNotBlank(info.getState())) {
                                    expFieldValue = (StateType.codeOf(Integer.parseInt(info.getState()))).getDescribe();
                                }
                                buf.append(expFieldValue).append(CSV_COLUMN_SEPARATOR).append(NUM_TAB);
                                break;
                            }
                            // 当前快递流转信息
                            if ("kdlzxx".equals(expFieldNamesArr[i])) {
                                if (StringUtils.isNotBlank(info.getInfoTrace())) {
                                    List<Trace> list = JsonUtils.toObject(
                                            info.getInfoTrace(),
                                            new TypeReference<List<Trace>>() {
                                            });
                                    expFieldValue = list.get(list.size() - 1)
                                            .getAcceptStation();
                                }
                                expFieldValue = expFieldValue.replace(",", "，").replace("\n", "");
                                buf.append(expFieldValue).append(CSV_COLUMN_SEPARATOR).append(NUM_TAB);
                                break;
                            }
                            // 延迟第一天快递流转信息
                            if ("yckdlzxxOne".equals(expFieldNamesArr[i])) {
                                if (null != info.getOverDay()
                                        && info.getOverDay() == 1 && StringUtils.isNotBlank(info.getInfoTrace())) {
                                    List<Trace> list = JsonUtils.toObject(
                                            info.getInfoTrace(),
                                            new TypeReference<List<Trace>>() {
                                            });
                                    expFieldValue = list.get(list.size() - 1)
                                            .getAcceptStation();
                                }
                                expFieldValue = expFieldValue.replace(",", "，").replace("\n", "");
                                buf.append(expFieldValue).append(CSV_COLUMN_SEPARATOR).append(NUM_TAB);
                                break;
                            }
                            // 延迟第二天快递流转信息
                            if ("yckdlzxxTwo".equals(expFieldNamesArr[i])) {
                                if (null != info.getOverDay()
                                        && info.getOverDay() == 2 && StringUtils.isNotBlank(info.getInfoTrace())) {
                                    List<Trace> list = JsonUtils.toObject(
                                            info.getInfoTrace(),
                                            new TypeReference<List<Trace>>() {
                                            });
                                    expFieldValue = list.get(list.size() - 1)
                                            .getAcceptStation();
                                }
                                expFieldValue = expFieldValue.replace(",", "，").replace("\n", "");
                                buf.append(expFieldValue).append(CSV_COLUMN_SEPARATOR).append(NUM_TAB);
                                break;
                            }
                            // 延迟第三天快递流转信息
                            if ("yckdlzxxThree".equals(expFieldNamesArr[i])) {
                                if (null != info.getOverDay()
                                        && info.getOverDay() == 3 && StringUtils.isNotBlank(info.getInfoTrace())) {
                                    List<Trace> list = JsonUtils.toObject(
                                            info.getInfoTrace(),
                                            new TypeReference<List<Trace>>() {
                                            });
                                    expFieldValue = list.get(list.size() - 1)
                                            .getAcceptStation();
                                }
                                expFieldValue = expFieldValue.replace(",", "，").replace("\n", "");
                                buf.append(expFieldValue).append(CSV_COLUMN_SEPARATOR).append(NUM_TAB);
                                break;
                            } else {
                                if (null != fields[j].get(info)) {
                                    if (fields[j]
                                            .getType()
                                            .getName()
                                            .equals(java.lang.String.class
                                                    .getName())) {
                                        expFieldValue = fields[j].get(info)
                                                + "";
                                    } else if (fields[j]
                                            .getType()
                                            .getName()
                                            .equals(java.lang.Integer.class
                                                    .getName())) {
                                        expFieldValue = fields[j].get(info)
                                                + "";
                                    } else if (fields[j]
                                            .getType()
                                            .getName()
                                            .equals(java.util.Date.class
                                                    .getName())) {
                                        SimpleDateFormat sdf = new SimpleDateFormat(
                                                "yyyy-MM-dd HH:mm:ss");
                                        expFieldValue = sdf.format(fields[j]
                                                .get(info));
                                    }
                                }
                                if (i == expFieldNamesArr.length - 1) {
                                    buf.append(expFieldValue);
                                } else {
                                    //英文逗号转中文逗号，防导出字段中存在英文逗号和CSV的分隔符起冲突.201705151436bug优化
                                    expFieldValue = expFieldValue.replace(",", "，").replace("\n", "");
                                    buf.append(expFieldValue).append(
                                            CSV_COLUMN_SEPARATOR).append(NUM_TAB);
                                }
                                break;
                            }
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
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

    /**
     * 处理显示可导出的字段and是否选择过
     *
     * @param retExportFields ExportFields
     * @param seeExportFields ExportFields
     * @return List<ExportFieldVo>
     */
    public List<ExportFieldVo> getSeeExportFieldList(ExportFields retExportFields, ExportFields seeExportFields) {
        List<ExportFieldVo> retList = new ArrayList<ExportFieldVo>();
        String[] expNameListArr = seeExportFields.getExpNameList().split("#");  //显示的字段（中文）
        String[] expFieldNameArr = seeExportFields.getExpFieldName().split("#"); //显示的字段（字段名）
        for (int i = 0; i < expNameListArr.length; i++) {
            ExportFieldVo efVo = new ExportFieldVo();
            efVo.setExpName(expNameListArr[i]);
            efVo.setExpFieldName(expFieldNameArr[i]);
            //判断是否包含（1：选择；0：未选择过）
            if (retExportFields != null && StringUtils.contains(retExportFields.getExpFieldName(), expFieldNameArr[i])) {
                efVo.setIsSelect("1");
            } else {
                efVo.setIsSelect("0");
            }
            retList.add(efVo);
        }
        return retList;
    }

}