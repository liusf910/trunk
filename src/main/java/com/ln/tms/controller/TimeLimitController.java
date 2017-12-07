package com.ln.tms.controller;

import com.ln.tms.bean.ExecuteResult;
import com.ln.tms.pojo.Fileup;
import com.ln.tms.service.FileupService;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * TimeLimitController - 时效
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Controller
@RequestMapping("/setting/timelimit")
public class TimeLimitController extends BaseController {

    @Autowired
    private FileupService fileupService;

    /**
     * 时效分页多条件查询文件记录列表
     *
     * @param pageCurrent 页
     * @param pageSize    条数
     * @param fileup      查询条件
     * @return ModelAndView
     */
    @RequestMapping(value = "list")
    public ModelAndView timeFileup(@RequestParam(value = "pageCurrent", defaultValue = "1") Integer pageCurrent,
                                   @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, Fileup fileup) {
        ModelAndView mv = new ModelAndView("/setting/timelimit/list");
        fileup.setPageCurrent(pageCurrent);
        fileup.setPageSize(pageSize);
        fileup.setBelongTo("1-0");
        mv.addObject("lists", fileupService.queryFileupList(fileup));
        mv.addObject("fileup", fileup);
        mv.addObject("total", fileupService.queryTotal(fileup));
        return mv;
    }

    /**
     * 转向用时效上传页面
     *
     * @return ModelAndView
     */
    @RequestMapping(value = "time_upload", method = RequestMethod.GET)
    public ModelAndView tookPage() {
        return new ModelAndView("/setting/timelimit/time_upload");
    }

    /**
     * 删除文件记录和文件
     *
     * @param fileId 文件记录编号
     * @return Map
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> delete(@RequestParam(value = "fileId") Long fileId) {
        ExecuteResult result = new ExecuteResult();
        if (fileId == null) {
            return result.jsonReturn(HttpStatus.SC_MULTIPLE_CHOICES);
        }
        try {
            fileupService.deleteFileup(fileId);
            return result.jsonReturn(HttpStatus.SC_OK);
        } catch (Exception e) {
            logger.error("文件日志删除失败", e);
            return result.jsonReturn(HttpStatus.SC_MULTIPLE_CHOICES);
        }

    }
}
