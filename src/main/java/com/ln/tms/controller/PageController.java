package com.ln.tms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * PageController
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Controller
@RequestMapping("/page")
public class PageController extends BaseController {

    /**
     * 页面转向
     *
     * @param page
     * @return String
     */
    @RequestMapping(value = "{page}", method = RequestMethod.GET)
    public String page(@PathVariable("page") String page) {
        return page;
    }

}
