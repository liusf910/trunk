package com.ln.tms.controller;

import java.util.Map;

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

import com.ln.tms.bean.ExecuteResult;
import com.ln.tms.pojo.Carrier;
import com.ln.tms.service.CarrierService;

@Controller
@RequestMapping("/carriers/carrier")
public class CarrierController extends BaseController{

	@Autowired
	private CarrierService carrierService;
	
	@RequestMapping(value="list")
	public ModelAndView queryCarrierPage(
			@RequestParam(value="pageCurrent",defaultValue="1")Integer pageCurrent,
			@RequestParam(value="pageSize",defaultValue="10")Integer pageSize,
			Carrier carrier){
		ModelAndView mv = new ModelAndView("/logistics/carrier/list");
		mv.addObject("pageInfo", carrierService.queryCarrierPage(carrier, pageCurrent, pageSize));
		mv.addObject("carrier", carrier);
		return mv;
	}
	
	/**
	 * 转向新增承运商页面
	 * @return ModelAndView
	 */
	@RequestMapping(value="carrier_add",method=RequestMethod.GET)
	public ModelAndView addPage(){
		return new ModelAndView("logistics/carrier/add");
	}
	
	/**
	 * 添加承运商信息
	 * @param carrier
	 * @return
	 */
	@RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> add(Carrier carrier) {
        if (carrierService.queryById(carrier.getCarrierCode()) != null) {
            return new ExecuteResult().jsonReturn(300, "承运商编号已存在!!!");
        }
        carrierService.saveSelective(carrier);
        return new ExecuteResult().jsonReturn(HttpStatus.SC_OK);
    }

	/**
	 * 修改承运商信息
	 * @param carrier
	 * @return
	 */
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> edit(Carrier carrier) {
        carrierService.updateSelective(carrier);
        return new ExecuteResult().jsonReturn(HttpStatus.SC_OK);
    }
	
    /**
     * 删除承运商信息
     * @param carrierCode
     * @return
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> delete(@RequestParam(value = "carrierCode") String carrierCode) {
        ExecuteResult result = new ExecuteResult();
        if (StringUtils.isNotBlank(carrierCode)) {
        	carrierService.deleteById(carrierCode);
            return result.jsonReturn(HttpStatus.SC_OK);
        } else {
            return result.jsonReturn(HttpStatus.SC_MULTIPLE_CHOICES);
        }
    }
    
    /**
     * 修改承运商信息页面
     * @param carrierCode
     * @param type
     * @return
     */
    @RequestMapping(value = "{carrierCode}/show_{type}", method = RequestMethod.GET)
    public ModelAndView show(@PathVariable(value = "carrierCode") String carrierCode, @PathVariable(value = "type") String type) {
        ModelAndView mv = new ModelAndView("/logistics/carrier/" + type);
        mv.addObject("carrier", carrierService.queryById(carrierCode));
        return mv;
    }
    
    /**
     * 验证承运商code
     * @param carrierCode
     * @return
     */
    @RequestMapping(value = "checkCarrierCode")
    @ResponseBody
    public Map<String, Object> checkCarrierCode(String carrierCode) {
        ExecuteResult result = new ExecuteResult();
        if (carrierService.checkCarrierCode(carrierCode)) {
            return result.remoteReturn("error", "承运商编号已存在,请重新填写!");
        }
        return result.remoteReturn("ok", "");
    }
}
