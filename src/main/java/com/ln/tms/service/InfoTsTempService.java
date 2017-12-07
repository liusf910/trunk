package com.ln.tms.service;

import com.ln.tms.enums.OrderType;
import com.ln.tms.enums.SignType;
import com.ln.tms.enums.StateType;
import com.ln.tms.enums.TookType;
import com.ln.tms.exception.FailRuntimeException;
import com.ln.tms.mapper.InfoTsTempMapper;
import com.ln.tms.pojo.*;
import com.ln.tms.util.CsvWriterUtils;
import com.ln.tms.util.DateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * InfoTsTempService
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Service
public class InfoTsTempService extends BaseService<InfoTsTemp> {

    private static final Logger LOGGER = LoggerFactory.getLogger(InfoTsTempService.class);

    @Value("${file.server.url}")
    private String fileServerUrl;

    @Value("${file.disk.path}")
    private String fileDiskPath;

    @Autowired
    private InfoTsTempMapper infoTsTempMapper;

    @Autowired
    private ExportFieldsService exportFieldsService;

    @Autowired
    private FileupService fileupService;

    @Autowired
    private LogService logService;

    @Autowired
    private SendMailService sendMailService;

    @Autowired
    private EmailConfService emailConfService;

    /**
     * 校验揽收的导入数据
     *
     * @param tooks 揽收文件数据集合
     * @return List
     */
    public List<InfoTsTemp> checkTook(List<List<Object>> tooks) {
        if (CollectionUtils.isEmpty(tooks)) {
            return null;
        }
        List<InfoTsTemp> infoTsTemps = new ArrayList<InfoTsTemp>();
        try {
            for (List<Object> took : tooks) {
                InfoTsTemp tsTemp = new InfoTsTemp();

                String logisticCode = (String) took.get(0);
                if (StringUtils.isBlank(logisticCode)) {
                    throw new FailRuntimeException("快递单号不能为空,上传失败");
                }
                String courierName = (String) took.get(1);
                if (StringUtils.isBlank(courierName)) {
                    throw new FailRuntimeException("快递公司不能为空,上传失败");
                }
                String tookDate = (String) took.get(2);
                if (StringUtils.isBlank(tookDate)) {
                    tsTemp.setFileTookTime(null);
                } else {
                    tsTemp.setFileTookTime(tookDate);
                }
                String type = (String) took.get(3);
                if (StringUtils.isBlank(type)) {
                    tsTemp.setTookReason(null);
                } else {
                    TookType tookType = TookType.checkDesc(type);
                    if (tookType == null) {
                        throw new FailRuntimeException("揽件超时原因未按规定填写,上传失败");
                    } else {
                        tsTemp.setTookReason(tookType);
                        tsTemp.setTimeOut("是");
                    }
                }
                tsTemp.setLogisticCode(logisticCode);
                tsTemp.setCourierName(courierName);
                infoTsTemps.add(tsTemp);
            }

            for (int i = 0; i < infoTsTemps.size() - 1; i++) {
                for (int j = infoTsTemps.size() - 1; j > i; j--) {
                    if (infoTsTemps.get(j).getLogisticCode().equals(infoTsTemps.get(i).getLogisticCode())) {
                        throw new FailRuntimeException("导入的同一批数据，快递单号不允许重复,上传失败");
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return infoTsTemps;
    }

    /**
     * 校验签收的导入数据
     *
     * @param signs 签收文件数据集合
     * @return List
     */
    public List<InfoTsTemp> checkSign(List<List<Object>> signs) {
        if (CollectionUtils.isEmpty(signs)) {
            return null;
        }
        List<InfoTsTemp> infoTsTemps = new ArrayList<InfoTsTemp>();
        try {
            for (List<Object> sign : signs) {
                InfoTsTemp tsTemp = new InfoTsTemp();

                String logisticCode = (String) sign.get(0);
                if (StringUtils.isBlank(logisticCode)) {
                    throw new FailRuntimeException("快递单号不能为空,上传失败");
                }
                String courierName = (String) sign.get(1);
                if (StringUtils.isBlank(courierName)) {
                    throw new FailRuntimeException("快递公司不能为空,上传失败");
                }

                String orderState = (String) sign.get(2);
                if (StringUtils.isBlank(orderState)) {
                    throw new FailRuntimeException("配送状态不能为空,上传失败");
                } else {
                    OrderType orderType = OrderType.checkDesc(orderState);
                    if (orderType == null) {
                        throw new FailRuntimeException("配送状态未按规定填写,上传失败");
                    } else {
                        tsTemp.setOrderStatus(orderType);
                    }
                }
                String signDate = (String) sign.get(3);
                if (StringUtils.isBlank(signDate)) {
                    tsTemp.setFileSignTime(null);
                } else {
                    int code = tsTemp.getOrderStatus().getCode();
                    if (code == 0 || code == 1) {
                        tsTemp.setFileSignTime(signDate);
                    } else {
                        tsTemp.setFileSignTime(null);
                    }
                }
                String type = (String) sign.get(4);
                if (StringUtils.isBlank(type)) {
                    tsTemp.setSignReason(null);
                } else {
                    SignType signType = SignType.checkDesc(type);
                    if (signType == null) {
                        throw new FailRuntimeException("签收超时原因未按规定填写,上传失败");
                    } else {
                        tsTemp.setSignReason(signType);
                        tsTemp.setTimeOut("是");
                    }
                }
                tsTemp.setLogisticCode(logisticCode);
                tsTemp.setCourierName(courierName);
                tsTemp.setSignStr(StringUtils.join(tsTemp.getOrderStatus().getCode(), '&', tsTemp.getFileSignTime(), '&', tsTemp.getSignReason() == null ? null : tsTemp.getSignReason().getCode(), '#'));
                infoTsTemps.add(tsTemp);
            }

            for (int i = 0; i < infoTsTemps.size() - 1; i++) {
                for (int j = infoTsTemps.size() - 1; j > i; j--) {
                    if (infoTsTemps.get(j).getLogisticCode().equals(infoTsTemps.get(i).getLogisticCode())) {
                        throw new FailRuntimeException("导入的同一批数据，快递单号不允许重复,上传失败");
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return infoTsTemps;
    }


    /**
     * 导入揽签文件操作
     *
     * @param infoTsTemps 处理的揽签导入集合
     * @param currentUser 当前用户
     * @param fileType    文件类型(揽件或签收)
     * @return boolean
     */
    @Transactional
    public String importBatch(List<InfoTsTemp> infoTsTemps, final User currentUser, String fileType) {
        final StringBuffer buf = new StringBuffer();
        String opera = "";
        Integer successNum = 0;
        Integer ignoreNum = 0;
        try {
            Integer saveTotal = saveInfoTsTempBatch(infoTsTemps);
            buf.append(StringUtils.join("文件导入临时表数据:", saveTotal, "条，"));

            Integer updateSCNum = infoTsTempMapper.updateShipperCode();
            LOGGER.warn("批量更新快递编号数量={}", updateSCNum);

            Integer ignoreRepeat = infoTsTempMapper.updateIgnoreRepeat();
            buf.append(StringUtils.join("忽略重复数据：", ignoreRepeat, "，"));

            Integer ignoreTookNum = 0;
            Integer updateBatch = 0;
            if (fileType.equals("1-1")) {
                ignoreTookNum = infoTsTempMapper.updateIgnoreTook();
                updateBatch = infoTsTempMapper.updateTookBatch();
                buf.append(StringUtils.join("忽略已系统对接的揽收时间数据：", ignoreTookNum, "，"));
            } else {
                updateBatch = infoTsTempMapper.updateSignBatch();
            }

            Integer saveBatch = infoTsTempMapper.saveBatch();
            buf.append(StringUtils.join("导入揽签收信息表数据：", saveBatch, "，更新数据：", (updateBatch / 2), "。"));

            LOGGER.warn(buf.toString());

            successNum = saveBatch + (updateBatch / 2);
            ignoreNum = ignoreRepeat + ignoreTookNum;

            List<InfoTsTemp> tsTemps = infoTsTempMapper.queryInfos();
            if (CollectionUtils.isNotEmpty(tsTemps)) {
                String fileName = writeFile(fileType, tsTemps, currentUser);
                opera = "文件导入成功";
                buf.append(StringUtils.join("\r\n成功导出文件：", fileName, "，文件路径：", fileServerUrl, fileName));
                LOGGER.warn("导出文件成功，文件名称={}," + fileName);
            }
            //修改tms_info表中的状态
            if ("1-1".equals(fileType)) {
                infoTsTempMapper.updTookInfoState();//(1:已揽收)
            } else {
                List<Info> tsTempsList = infoTsTempMapper.getTsTempsInfo();
                if (null != tsTempsList && tsTempsList.size() > 0) {
                    for (Info info : tsTempsList) {
                        //1、超时天数实际到达时间减去计划到达时间 //2、在途天数修改（签收时间-发货时间）
                        if (info.getSignTime() == null && info.getFileSignTime() != null) {
                            info.setOverDay(DateUtils.getDateDifference(info.getPlanTime(), info.getFileSignTime()));
                            info.setAttritDay(DateUtils.getDateDifference(info.getShipmentsTime(), info.getFileSignTime()));
                        } else if (info.getSignTime() != null && info.getFileSignTime() == null) {
                            info.setOverDay(DateUtils.getDateDifference(info.getPlanTime(), info.getSignTime()));
                            info.setAttritDay(DateUtils.getDateDifference(info.getShipmentsTime(), info.getSignTime()));
                        } else if (info.getSignTime() != null && info.getFileSignTime() != null) {
                            if (DateUtils.getDateHours(info.getSignTime(), info.getFileSignTime()) < 24) {
                                info.setOverDay(DateUtils.getDateDifference(info.getPlanTime(), info.getFileSignTime()));
                                info.setAttritDay(DateUtils.getDateDifference(info.getShipmentsTime(), info.getFileSignTime()));
                            } else {
                                info.setOverDay(DateUtils.getDateDifference(info.getPlanTime(), info.getSignTime()));
                                info.setAttritDay(DateUtils.getDateDifference(info.getShipmentsTime(), info.getSignTime()));
                            }
                        }

                        //根据配送状态修改快递状态
                        if ("签收".equals(info.getOrderStatus())) {
                            info.setState(StateType.YQS);
                        } else if ("退回签收".equals(info.getOrderStatus())) {
                            info.setState(StateType.JTJ);
                        } else if ("退回中".equals(info.getOrderStatus())) {
                            info.setState(StateType.JTJ);
                        } else if ("遗失".equals(info.getOrderStatus())) {
                            info.setState(StateType.WTJ);
                        }
                        infoTsTempMapper.updSignInfoState(info);//(3:已签收；超时天数)
                    }
                }
            }
        } catch (Exception e) {
            opera = "文件导入失败";
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            Log log = new Log();
            log.setOperation(StringUtils.join(fileType.equals("1-1") ? "揽件" : "签收", opera));
            log.setUserId(currentUser.getUserId());
            log.setIp(currentUser.getIp());
            log.setContent(buf.toString());
            logService.save(log);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                String sendResult = sendEmail(buf.toString(), currentUser);
                LOGGER.warn(sendResult);
            }
        }).start();
        return StringUtils.join("导入成功条数：", successNum, "，忽略条数：", ignoreNum);
    }


    /**
     * 批量文件导入临时表
     *
     * @param infoTsTemps 导入文件集合
     * @return Integer
     */
    private Integer saveInfoTsTempBatch(List<InfoTsTemp> infoTsTemps) {
        if (CollectionUtils.isNotEmpty(infoTsTemps)) {
            infoTsTempMapper.clearInfoTsTemp();
        }
        return infoTsTempMapper.saveInfoTsTempBatch(infoTsTemps);
    }

    /**
     * 导出下载文件
     *
     * @param fileType    文件类型(揽件或签收)
     * @param infoTsTemps 下载文件集合
     * @param currentUser 当前用户
     * @return String
     */
    private String writeFile(String fileType, List<InfoTsTemp> infoTsTemps, User currentUser) {
        String fileName = "";
        try {
            ExportFields exportFields = new ExportFields();
            exportFields.setBelongTo(fileType);
            ExportFields ef = exportFieldsService.queryOne(exportFields);
            String file_pre = fileType.equals("1-1") ? "揽件" : "签收";
            fileName = CsvWriterUtils.initCsvData(ef.getExpNameList().replace("#", ","), file_pre, fileDiskPath);
            Map<String, Object> result = CsvWriterUtils.listToTookCsvData(infoTsTemps, fileType);
            String data = (String) result.get("data");
            if (StringUtils.isNotEmpty(data)) {
                CsvWriterUtils.exportCsv(fileName, data);
            }

            fileName = StringUtils.isNotBlank(fileName) ? StringUtils.replace(StringUtils.substringAfter(fileName, fileDiskPath), "\\", "/") : null;
            //导入成功后记录操作日志
            Fileup fileup = new Fileup();
            fileup.setFileName(fileName);
            fileup.setFileUrl(fileServerUrl + fileName);
            fileup.setBelongTo(fileType);
            fileup.setIp(currentUser.getIp());
            fileup.setUserId(currentUser.getUserId());
            fileupService.saveFileup(fileup);
            LOGGER.info("upload End fileUrl={}", fileup.getFileUrl());
        } catch (Exception e) {
            //删除文件
            File file = new File(fileName);
            if (file.exists()) {
                if (!file.delete()) {
                    throw new NullPointerException("Folder Delete failed");

                }
            }
            throw new RuntimeException(e.getMessage(), e);
        }
        return fileName;
    }


    /**
     * 邮件发送
     *
     * @param content     邮件发送的内容
     * @param currentUser 登录的用户
     * @return String
     */
    private String sendEmail(String content, User currentUser) {
        MailModel mailModel = new MailModel();

        List<EmailConf> emailConfs = emailConfService.queryAll();

        if (CollectionUtils.isNotEmpty(emailConfs)) {
            EmailConf emailConf = emailConfs.get(0);
            mailModel.setHost(emailConf.getServer());
            mailModel.setUserName(emailConf.getSmtpName());
            mailModel.setPassword(emailConf.getSmtpPwd());
            //发件人邮箱
            mailModel.setFromEmail(emailConf.getSmtpName());
            //收件人邮箱
            mailModel.setToEmails(currentUser.getEmail());

            //邮件主题
            mailModel.setSubject("快递公司导入文件通知");
            //邮件内容
            mailModel.setContent(content);
            return sendMailService.sendMail(mailModel);
        }
        return "发件主机邮箱地址未设置，邮件无法发送";
    }
}
