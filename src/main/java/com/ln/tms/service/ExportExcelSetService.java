package com.ln.tms.service;

import com.ln.tms.mapper.ExportExcelSetMapper;
import com.ln.tms.pojo.ExportFields;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * ExportExcelSetService - 导出字段设置Service
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Service
public class ExportExcelSetService extends BaseService<ExportFields> {

    /**
     * 导出字段设置Mapper
     */
    @Autowired
    private ExportExcelSetMapper exportExcelSetMapper;

    /**
     * 获取导出字段
     *
     * @param belongTo：all：后台 courier:前台
     * @return ExportFields  导出字段Vo
     */
    @Transactional
    public ExportFields getExportFieldsByBelongTo(String belongTo) {
        return exportExcelSetMapper.getExportFieldsByBelongTo(belongTo);
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
     * 检查所属模块查询是否存在
     *
     * @param exportFields 导出字段Vo
     * @return true：存在 false：不存在
     */
    @Transactional
    public boolean checkExortFields(ExportFields exportFields) {
        return exportExcelSetMapper.checkExortFields(exportFields) > 0 ;
    }

    /**
     * 修改导出字段
     *
     * @param exportFields 导出字段Vo
     */
    @Transactional
    public void updExportFields(ExportFields exportFields) {
        exportExcelSetMapper.updExportFields(exportFields);
    }

    /**
     * 保存导出字段
     *
     * @param exportFields 导出字段Vo
     */
    @Transactional
    public void saveExportFields(ExportFields exportFields) {
        exportExcelSetMapper.saveExportFields(exportFields);
    }


}