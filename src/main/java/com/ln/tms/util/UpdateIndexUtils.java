package com.ln.tms.util;

import com.ln.tms.bean.HttpResult;
import com.ln.tms.handler.HttpClientHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * UpdateIndexUtils - 更新Solr索引工具
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Component
public class UpdateIndexUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateIndexUtils.class);

    @Value("${solr.host}")
    private String solrHost;

    @Autowired
    private HttpClientHandler httpClientHandler;

    private static final String apiUrl = "/collection1/dataimport";

    /**
     * 更新或者添加索引
     */
    public void updateOrAddIndex() {
        try {
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("command", "delta-import");
            maps.put("clean", "false");
            maps.put("commit", "true");
            maps.put("entity", "tmsInfo");
            maps.put("optimize", "false");
            HttpResult httpResult = httpClientHandler.doPost(solrHost.concat(apiUrl), maps);
            if (httpResult.getCode() == 200) {
                LOGGER.info("更新或者添加索引成功", httpResult.getData());
            } else {
                LOGGER.error("更新或者添加索引失败", httpResult.getData());
            }
        } catch (IOException e) {
            LOGGER.error("生成索引错误");
            throw new RuntimeException("生成索引错误");
        }
    }
}
