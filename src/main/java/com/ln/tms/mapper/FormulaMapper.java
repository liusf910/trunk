package com.ln.tms.mapper;

import com.ln.tms.pojo.Formula;
import com.ln.tms.mymapper.MyMapper;

/**
 * FormulaMapper
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
public interface FormulaMapper extends MyMapper<Formula> {
    
	/**
	 * 校验公式名称是否存在
	 * @param formula
	 * @return int 存在? >0:0
	 */
	Integer checkFormulaName(Formula formula);
}
