package com.ln.tms.service;

import com.ln.tms.mapper.FileupMapper;
import com.ln.tms.pojo.Fileup;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

/**
 * FileupService
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Service
public class FileupService extends BaseService<Fileup> {

    @Value("${file.disk.path}")
    private String fileDiskPath;

    @Value("${file.server.url}")
    private String fileServerUrl;

    @Autowired
    private FileupMapper fileupMapper;

    /**
     * 根据条件查询附件表
     *
     * @param fileup 查询条件
     * @return List
     */
    @Transactional(readOnly = true)
    public List<Fileup> queryFileupList(Fileup fileup) {
        return fileupMapper.queryFileupList(fileup);
    }

    /**
     * 根据条件查询附件表条数
     *
     * @param fileup 查询条件
     * @return Integer
     */
    @Transactional(readOnly = true)
    public Integer queryTotal(Fileup fileup) {
        return fileupMapper.queryTotal(fileup);
    }

    /**
     * 根据附件表编号删除
     *
     * @param fileId 附件表编号
     * @return Integer
     */
    @Transactional
    public Integer deleteFileup(Long fileId) {
        Fileup fileup = super.queryById(fileId);
        if (fileup != null) {
            String path = fileDiskPath + StringUtils.substringAfter(fileup.getFileUrl(), fileServerUrl);
            File file = new File(path);
            if (!file.delete()) {
                throw new NullPointerException("Folder Delete failed");
            }
        }
        return super.deleteById(fileId);
    }

    /**
     * 保存附件表
     *
     * @param fileup 数据
     * @return Integer
     */
    @Transactional
    public Integer saveFileup(Fileup fileup) {
        return super.saveSelective(fileup);
    }
}
