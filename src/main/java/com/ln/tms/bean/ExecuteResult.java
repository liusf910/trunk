package com.ln.tms.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * CountData - 响应结构
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@SuppressWarnings("Duplicates")
public class ExecuteResult {
    /**
     * @param statusCode 状态码
     * @return map
     */
    public Map<String, Object> jsonReturn(int statusCode) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("statusCode", statusCode);
        String msg = "";
        if (statusCode == 200) {
            msg = "操作成功";
        } else if (statusCode == 300) {
            msg = "操作失败";
        }
        returnMap.put("message", msg);
        returnMap.put("closeCurrent", true);
        return returnMap;
    }

    /**
     * @param statusCode 状态码
     * @param msg        消息
     * @return map
     */
    public Map<String, Object> jsonReturn(int statusCode, String msg) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("statusCode", statusCode);
        returnMap.put("message", msg);
        returnMap.put("closeCurrent", true);
        return returnMap;
    }

    /**
     * @param statusCode   状态码
     * @param msg          消息
     * @param closeCurrent 标识
     * @return map
     */
    public Map<String, Object> jsonReturn(int statusCode, String msg, Boolean closeCurrent) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("statusCode", statusCode);
        returnMap.put("message", msg);
        returnMap.put("closeCurrent", closeCurrent);
        return returnMap;
    }

    /**
     * @param flag 标识
     * @param msg  消息
     * @return map
     */
    public Map<String, Object> remoteReturn(String flag, String msg) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put(flag, msg);
        return result;
    }
}
