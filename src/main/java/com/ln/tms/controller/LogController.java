package com.ln.tms.controller;

import com.ln.tms.bean.ExecuteResult;
import com.ln.tms.pojo.Log;
import com.ln.tms.service.LogService;
import com.ln.tms.service.UserService;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/**
 * LogController
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Controller
@RequestMapping("/setting/log")
public class LogController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private LogService logService;

    /**
     * 分页多条件查询日志列表
     *
     * @param pageCurrent 页
     * @param pageSize    条数
     * @param log         条件
     * @return ModelAndView
     */
    @RequestMapping(value = "list")
    public ModelAndView list(@RequestParam(value = "pageCurrent", defaultValue = "1") Integer pageCurrent,
                             @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, Log log) {
        ModelAndView mv = new ModelAndView("/setting/log/list");
        log.setPageCurrent(pageCurrent);
        log.setPageSize(pageSize);
        mv.addObject("lists", logService.queryLogList(log));
        mv.addObject("Log", log);
        mv.addObject("total", logService.queryTotal(log));
        return mv;
    }


    /**
     * 删除日志
     *
     * @param id 日志编号
     * @return Map
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> delete(@RequestParam(value = "id") Long id) {
        ExecuteResult result = new ExecuteResult();
        if (id != null) {
            logService.deleteById(id);
            return result.jsonReturn(HttpStatus.SC_OK);
        } else {
            return result.jsonReturn(HttpStatus.SC_MULTIPLE_CHOICES);
        }
    }

    /**
     * 查看日志详情
     *
     * @param id 日志编号
     * @return ModelAndView
     */
    @RequestMapping(value = "detail", method = RequestMethod.GET)
    public ModelAndView show(Long id) {
        ModelAndView mv = new ModelAndView("/setting/log/detail");
        mv.addObject("log", logService.queryByIdNew(id));
        return mv;
    }

    /**
     * 批量删除日志
     *
     * @param ids 编号集合
     * @return Map
     */
    @RequestMapping(value = "deleteBatch", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> deleteBatch(@RequestParam(value = "ids") List<Object> ids) {
        ExecuteResult result = new ExecuteResult();
        try {
            logService.deleteBatch(ids);
            return result.jsonReturn(HttpStatus.SC_OK, "批量删除日志成功");
        } catch (Exception e) {
            return result.jsonReturn(HttpStatus.SC_MULTIPLE_CHOICES);
        }
    }
}
