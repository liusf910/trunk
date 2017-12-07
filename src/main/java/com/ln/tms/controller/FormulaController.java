package com.ln.tms.controller;

import com.ln.tms.bean.ExecuteResult;
import com.ln.tms.pojo.Formula;
import com.ln.tms.service.FormulaService;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * FormulaController - 公式设置
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Controller
@RequestMapping("/setting/formula")
public class FormulaController extends BaseController {

    @Autowired
    private FormulaService formulaService;

    /**
     * 分页多条件查询公式列表
     *
     * @param pageCurrent 页
     * @param pageSize    条
     * @param formula     条件
     * @return ModelAndView
     */
    @RequestMapping(value = "list")
    public ModelAndView list(@RequestParam(value = "pageCurrent", defaultValue = "1") Integer pageCurrent,
                             @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, Formula formula) {
        ModelAndView mv = new ModelAndView("/setting/formula/list");
        mv.addObject("pageInfo", formulaService.queryFormulaPage(formula, pageCurrent, pageSize));
        mv.addObject("formula", formula);
        return mv;
    }

    /**
     * 转向公式新增页面
     *
     * @return ModelAndView
     */
    @RequestMapping(value = "formula_add", method = RequestMethod.GET)
    public ModelAndView addPage() {
        return new ModelAndView("/setting/formula/add");
    }

    /**
     * 转向公式编辑页面
     *
     * @return ModelAndView
     */
    @RequestMapping(value = "formula_edit", method = RequestMethod.GET)
    public ModelAndView editPage(@RequestParam(value = "formulaId") Integer formulaId) {
        ModelAndView mv = new ModelAndView("/setting/formula/edit");
        mv.addObject("formula", formulaService.queryById(formulaId));
        return mv;
    }

    /**
     * 添加公式信息
     *
     * @param formula 公式
     * @return map
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> add(Formula formula) {
    	if (formulaService.checkFormulaName(formula.getFormulaId(), formula.getFormulaName())) {
            return new ExecuteResult().jsonReturn(HttpStatus.SC_MULTIPLE_CHOICES, "该公式名称已存在,请重新填写!");
        }
        formulaService.saveSelective(formula);
        return new ExecuteResult().jsonReturn(HttpStatus.SC_OK);
    }

    /**
     * 修改公式信息
     *
     * @param formula 公式
     * @return map
     */
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> edit(Formula formula) {
        if (formula.getFormulaId() != null) {
        	if (formulaService.checkFormulaName(formula.getFormulaId(), formula.getFormulaName())) {
                return new ExecuteResult().jsonReturn(HttpStatus.SC_MULTIPLE_CHOICES, "该公式名称已存在,请重新填写!");
            }
        	formulaService.updateSelective(formula);            
        }
        return new ExecuteResult().jsonReturn(HttpStatus.SC_OK);
    }

    /**
     * 校验表达式
     *
     * @param expression 表达式
     * @return map
     */
    @RequestMapping(value = "checkExpression")
    @ResponseBody
    public Map<String, Object> checkExpression(String expression) {
        ExecuteResult result = new ExecuteResult();
        if(StringUtils.isNotBlank(expression) && expression.contains(formulaService.SHIPMENTSDAY)){
           if(formulaService.isValidExpression(expression)){
              return result.remoteReturn("ok","");
           }
        }
        return result.remoteReturn("error", "无效表达式!");
    }
    
    
    /**
     * 校验公式名称
     * @param formulaId 公式id
     * @param formulaName 公式名称
     * @return Map<String, Object> 校验Map 
     */
    @RequestMapping(value = "{formulaId}/checkFormulaName")
    @ResponseBody
    public Map<String, Object> checkFormulaName(@PathVariable(value = "formulaId") Integer formulaId, String formulaName) {
        ExecuteResult result = new ExecuteResult();
        if (formulaService.checkFormulaName(formulaId, formulaName)) {
            return result.remoteReturn("error", "该公式名称已存在,请重新填写!");
        }
        return result.remoteReturn("ok", "");
    }
}
