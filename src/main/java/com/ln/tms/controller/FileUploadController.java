package com.ln.tms.controller;

import com.ln.tms.bean.ExecuteResult;
import com.ln.tms.pojo.InfoTsTemp;
import com.ln.tms.pojo.LogisticsAppointment;
import com.ln.tms.pojo.LogisticsAppointmentTemp;
import com.ln.tms.pojo.User;
import com.ln.tms.service.InfoTsTempService;
import com.ln.tms.service.LogiAppointTempService;
import com.ln.tms.service.LogisticsService;
import com.ln.tms.service.TimeLimitService;
import com.ln.tms.shiro.Principal;
import com.ln.tms.util.CsvReadUtils;
import com.ln.tms.util.LogisticCsvReadUtils;
import com.ln.tms.util.POIExcelUtils;
import com.ln.tms.util.TmsUtils;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.http.HttpStatus;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FileUploadController
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Controller
@RequestMapping("/upload")
public class FileUploadController extends BaseController {

    @Autowired
    private TimeLimitService timeLimitService;

    @Autowired
    private InfoTsTempService infoTsTempService;
    
    @Autowired
    private LogiAppointTempService logiAppointTempService;
    
    @Autowired
    private LogisticsService logisticsService;

    @Value("${import.took.title}")
    private String csvHeadTook;


    @Value("${import.sign.title}")
    private String csvHeadSign;
    
    
    @Value("${import.logistics.title}")
    private String csvHeadLogistics;

    /**
     * 文件上传
     *
     * @param type          文件类型
     * @param multipartFile 文件
     * @param request
     * @return Map
     */
    @RequestMapping(value = "{type}", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> upload(@PathVariable(value = "type") String type,
                                      @RequestParam(value = "file") MultipartFile multipartFile,
                                      HttpServletRequest request) {
        Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
        User currentUser = principal.getUser();
        currentUser.setIp(TmsUtils.getRemoteHost(request));
        logger.info("IP={},admin={},file={},Start upload!", currentUser.getIp(), currentUser.getUserId(), multipartFile.getOriginalFilename());

        ExecuteResult result = new ExecuteResult();
        if (multipartFile.getSize() <= 0) {
            return result.jsonReturn(HttpStatus.SC_MULTIPLE_CHOICES, "文件内容为空，请检查!");
        }
        Map<String, Object> resMap = new HashMap<>();
        String message = "";
        try {
            if (type.equals("timelimit")) {
                message = timeLimitService.insertTimeLimit(multipartFile, currentUser);

            } else if (type.equals("took")) {
                List<List<Object>> lists = POIExcelUtils.importExcel(multipartFile, csvHeadTook);
                List<InfoTsTemp> tsTemps = infoTsTempService.checkTook(lists);
                message = infoTsTempService.importBatch(tsTemps, currentUser, "1-1");

            } else if (type.equals("sign")) {
                List<List<Object>> lists = POIExcelUtils.importExcel(multipartFile, csvHeadSign);
                List<InfoTsTemp> tsTemps = infoTsTempService.checkSign(lists);
                message = infoTsTempService.importBatch(tsTemps, currentUser, "2-1");
            }else if(type.equals("logistics")){
                File file =TmsUtils.uploadFile("/", multipartFile);
                List<LogisticsAppointmentTemp> lists = LogisticCsvReadUtils.resolveCsvTL(file, csvHeadLogistics);
                List<LogisticsAppointment> list=logiAppointTempService.checkLogistics(lists);
                logisticsService.insertLogisticsAppointmentList(list);
            }
            resMap = result.jsonReturn(HttpStatus.SC_OK, message);

        } catch (Exception e) {
            logger.error("type={}文件导入异常,错误信息:{}", type, e.getMessage());
            return exceptionHandler(e, request);
        }
        return resMap;
    }
}
