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
import com.ln.tms.pojo.StorageAppoint;
import com.ln.tms.service.StorageAppointService;

@Controller
@RequestMapping("/storages/storageAppoint")
public class StorageAppointController {

	@Autowired
	private StorageAppointService storageAppointService;
	
	@RequestMapping(value="list")
	public ModelAndView queryStorageAppoint(
			@RequestParam(value="pageCurrent",defaultValue="1")Integer pageCurrent,
			@RequestParam(value="pageSize",defaultValue="10")Integer pageSize,
			StorageAppoint storageAppoint){
		ModelAndView mv = new ModelAndView("/logistics/storageAppoint/list");
		mv.addObject("pageInfo", storageAppointService.queryStorageAppointPage(storageAppoint, pageCurrent, pageSize));
		mv.addObject("storageAppoint", storageAppoint);
		
		return mv;
	}
	
	
	@RequestMapping(value="storageAppoint_add",method=RequestMethod.GET)
	public ModelAndView addPage(){
		return new ModelAndView("logistics/storageAppoint/add");
	}
	
	@RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> add(StorageAppoint storageAppoint) {
        if (storageAppointService.checkStorageAppoint(storageAppoint.getWarehouseCode())) {
            return new ExecuteResult().jsonReturn(300, "承运商编号已存在!!!");
        }
        storageAppointService.saveSelective(storageAppoint);
        return new ExecuteResult().jsonReturn(HttpStatus.SC_OK);
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> edit(StorageAppoint storageAppoint) {
        storageAppointService.updateSelective(storageAppoint);
        return new ExecuteResult().jsonReturn(HttpStatus.SC_OK);
    }
	
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> delete(@RequestParam(value = "id") Integer id) {
        ExecuteResult result = new ExecuteResult();
        if (StringUtils.isNotBlank(id+"")) {
        	storageAppointService.deleteById(id);
            return result.jsonReturn(HttpStatus.SC_OK);
        } else {
            return result.jsonReturn(HttpStatus.SC_MULTIPLE_CHOICES);
        }
    }
    
    @RequestMapping(value = "{id}/show_{type}", method = RequestMethod.GET)
    public ModelAndView show(@PathVariable(value = "id") Integer id, @PathVariable(value = "type") String type) {
        ModelAndView mv = new ModelAndView("/logistics/storageAppoint/" + type);
        mv.addObject("storageAppoint", storageAppointService.queryById(id));
        return mv;
    }
    
    @RequestMapping(value = "checkWarehouseCode")
    @ResponseBody
    public Map<String, Object> checkstorageAppointCode(String warehouseCode) {
        ExecuteResult result = new ExecuteResult();
        if (storageAppointService.checkStorageAppoint(warehouseCode)) {
            return result.remoteReturn("error", "仓库编号已存在,请重新填写!");
        }
        return result.remoteReturn("ok", "");
    }
}
