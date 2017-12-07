package com.ln.tms.service;

import com.github.pagehelper.PageInfo;
import com.greenpineyu.fel.FelEngine;
import com.greenpineyu.fel.FelEngineImpl;
import com.greenpineyu.fel.context.FelContext;
import com.ln.tms.mapper.FormulaMapper;
import com.ln.tms.pojo.Formula;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

/**
 * FormulaService - 公式
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Service
public class FormulaService extends BaseService<Formula> {

    @Autowired
    private FormulaMapper formulaMapper;

    /**
     * 发货日期变量
     */
    public static final String SHIPMENTSDAY = "sDay";

    /**
     * 快递公司时限
     */
    public static final String VALIDITYDAY = "vDay";

    /**
     * 一天的秒数变量
     */
    public static final String ONEDAY = "oneDay";

    public static final Integer ONEDAYSECONDS = 24 * 60 * 60 * 1000;

    /**
     * 验证表达式
     *
     * @param priceExpression 表达式
     * @return boolean
     */
    public boolean isValidExpression(String priceExpression) {
        Assert.hasText(priceExpression);
        if (!StringUtils.contains(priceExpression, "sDay+vDay+oneDay*")) {
            return false;
        }

        try {
            FelEngine fel = new FelEngineImpl();
            FelContext ctx = fel.getContext();
            ctx.set(SHIPMENTSDAY, new Date().getTime());
            ctx.set(VALIDITYDAY, ONEDAYSECONDS);
            ctx.set(ONEDAY, ONEDAYSECONDS);
            fel.eval(priceExpression);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 解析公式
     *
     * @param shipmentsTime   发货日期
     * @param vday            时效
     * @param priceExpression 公式
     * @return 计算结果
     */
    public Date analysisDayExpression(Date shipmentsTime, Integer vday, String priceExpression) {
        Assert.hasText(priceExpression);
        try {
            FelEngine fel = FelEngine.instance;
            FelContext ctx = fel.getContext();
            ctx.set(SHIPMENTSDAY, shipmentsTime.getTime());
            ctx.set(VALIDITYDAY, vday * ONEDAYSECONDS);
            ctx.set(ONEDAY, ONEDAYSECONDS);
            Object result = fel.eval(priceExpression);
            Long aLong = Long.parseLong(result.toString());
            return new Date(aLong);
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 分页查询公式列表
     *
     * @param formula 条件
     * @param page    页
     * @param row     条
     * @return PageInfo
     */
    @Transactional(readOnly = true)
    public PageInfo<Formula> queryFormulaPage(Formula formula, Integer page, Integer row) {
        Example example = new Example(Formula.class);
        example.setOrderByClause("create_date DESC");
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(formula.getFormulaName())) {
            criteria.andLike("formulaName", "%" + formula.getFormulaName() + "%");
        }
        return super.queryPageListByExample(example, page, row);
    }

    /**
     * 条件查询公式
     *
     * @param formula 查询条件
     * @return Formula
     */
    @Override
    @Transactional(readOnly = true)
    public Formula queryOne(Formula formula) {
        return super.queryOne(formula);
    }

    /**
     * 校验公式名称
     *
     * @param formulaId   公式id
     * @param formulaName 公式名称
     * @return boolean  不存在 ? false : true
     */
    public boolean checkFormulaName(Integer formulaId, String formulaName) {
        Formula formula = new Formula();
        formula.setFormulaId(formulaId);
        formula.setFormulaName(formulaName);
        return formulaMapper.checkFormulaName(formula) > 0;
    }
}
