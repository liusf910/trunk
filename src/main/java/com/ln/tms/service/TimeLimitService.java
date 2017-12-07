package com.ln.tms.service;

import com.ln.tms.exception.FailRuntimeException;
import com.ln.tms.mapper.TimeLimitMapper;
import com.ln.tms.pojo.Fileup;
import com.ln.tms.pojo.TimeLimit;
import com.ln.tms.pojo.User;
import com.ln.tms.util.CsvReadUtils;
import com.ln.tms.util.TmsUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * TimeValidityService - 快递公司时限Service
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Service
public class TimeLimitService extends BaseService<TimeLimit> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeLimitService.class);

    @Value("${file.server.url}")
    private String fileServerUrl;

    @Value("${file.disk.path}")
    private String fileDiskPath;

    @Value("${import.time.limit.title}")
    private String csvHead;

    @Autowired
    private TimeLimitMapper timeLimitMapper;

    @Autowired
    private FileupService fileupService;

    /**
     * 获取时限
     *
     * @param warehouse 发货仓库
     * @param province  收件省
     * @return List
     */
    @Transactional(readOnly = true)
    public List<TimeLimit> getTimeLimit(String warehouse, String province) {
        List<TimeLimit> timeLimit = null;
        if (StringUtils.isNotBlank(warehouse) && StringUtils.isNotBlank(province)) {
            TimeLimit tvy = new TimeLimit();
            tvy.setWarehouse(warehouse);
            tvy.setProvince(province);
            timeLimit = super.queryByWhere(tvy);
        }
        return timeLimit;

    }

    /**
     * 获取所有的省份
     *
     * @return
     */
    @Transactional(readOnly = true)
    public List<String> queryProvince() {
        return timeLimitMapper.queryProvince();
    }

    /**
     * 文件解析，批量导入时效表
     *
     * @param multipartFile MultipartFile
     * @return String
     */
    @Transactional
    public String insertTimeLimit(MultipartFile multipartFile, User currentUser) {
        String result = "";
        File targetFile = null;
        try {

            String uploadFilename = multipartFile.getOriginalFilename();
            if (!(StringUtils.substringAfterLast(uploadFilename, ".").equals("csv"))) {
                throw new FailRuntimeException("请上传csv文件!");
            }

            targetFile = TmsUtils.uploadFile(fileDiskPath, multipartFile);

            //解析csv文件到集合
            List<TimeLimit> limitList = CsvReadUtils.resolveCsvTL(targetFile, csvHead);
            LOGGER.info("时效表导入条数,uploadCount={}", limitList.size());
            //集合去重
            for (int i = 0; i < limitList.size() - 1; i++) {
                for (int j = limitList.size() - 1; j > i; j--) {
                    TimeLimit limitJ = limitList.get(j);
                    TimeLimit limitI = limitList.get(i);
                    if (limitJ.getWarehouse().equals(limitI.getWarehouse()) && limitJ.getProvince().equals(limitI.getProvince())
                            && limitJ.getCity().equals(limitI.getCity())) {
                        limitList.remove(j);
                    }
                }
            }
            LOGGER.info("时效表导入去重后条数={}", limitList.size());
            if (CollectionUtils.isNotEmpty(limitList)) {
                timeLimitMapper.clearTimeLimit();
                Integer uploadCount = timeLimitMapper.saveTimeLimitBatch(limitList);
                result = StringUtils.join("时效表成功导入条数:", uploadCount);
                LOGGER.info("时效表成功导入条数,uploadCount={}", uploadCount);
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        //导入成功后记录操作日志
        Fileup fileup = new Fileup();
        fileup.setFileName(targetFile.getName());
        fileup.setFileUrl(fileServerUrl + targetFile.getName());
        fileup.setBelongTo("1-0");
        fileup.setIp(currentUser.getIp());
        fileup.setUserId(currentUser.getUserId());
        fileupService.save(fileup);
        LOGGER.warn("upload End fileUrl={}", fileup.getFileUrl());
        return result;
    }


}