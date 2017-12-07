package com.ln.tms.handler;

import com.ln.tms.bean.HttpResult;
import com.ln.tms.util.SpringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * HttpClientHandler-HttpClient处理
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@Component
public class HttpClientHandler {

    // 日志
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientHandler.class);

    @Autowired(required = false)
    private RequestConfig requestConfig;

    /**
     * 有参POST
     *
     * @param url   URL
     * @param param map参数
     * @return HttpResult
     * @throws IOException
     */
    public HttpResult doPost(String url, Map<String, String> param) throws IOException {
        Assert.hasText(url);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Pass HttpClientHandler By doPost(String url={}, Map<String, String> param={})", url, param);
        }
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(this.requestConfig);
        if (!CollectionUtils.isEmpty(param)) {
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> entry : param.entrySet()) {
                parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters, "UTF-8");
            httpPost.setEntity(formEntity);
        }
        return getHttpResult(httpPost);
    }

    /**
     * getHttpClient
     *
     * @return CloseableHttpClient
     */
    private CloseableHttpClient getHttpClient() {
        return (CloseableHttpClient) SpringUtils.getBean("closeableHttpClient");
    }

    /**
     * @param httpPost HttpPost
     * @return HttpResult
     * @throws IOException
     */
    private HttpResult getHttpResult(HttpPost httpPost) throws IOException {
        Assert.notNull(httpPost);
        CloseableHttpResponse response = null;
        try {
            response = getHttpClient().execute(httpPost);
            return new HttpResult(response.getStatusLine().getStatusCode(), EntityUtils.toString(
                    response.getEntity(), "UTF-8"));
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    /**
     * doGet请求（EMS）
     *
     * @param apiUrl
     * @param maps
     * @return HttpResult
     * @throws IOException
     * @throws ClientProtocolException
     */
    public HttpResult doGet(String apiUrl, Map<String, String> maps) throws ClientProtocolException, IOException {
        Assert.hasText(apiUrl);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Pass HttpClientHandler By doPost(String url={}, Map<String, String> param={})", apiUrl, maps);
        }
        HttpGet httpGet = new HttpGet(apiUrl.replace("{mail_num}", maps.get("logisticCode")));
        httpGet.setHeader("version", maps.get("version"));
        httpGet.setHeader("authenticate", maps.get("authenticate"));
        CloseableHttpResponse response = (CloseableHttpResponse) ((CloseableHttpClient) SpringUtils.getBean("closeableHttpClient")).execute(httpGet);
        return new HttpResult(response.getStatusLine().getStatusCode(), EntityUtils.toString(
                response.getEntity(), "UTF-8"));
    }

    /**
     * 有参POST
     *
     * @param url  URL
     * @param json json格式的参数
     * @return HttpResult
     * @throws IOException
     */
    public HttpResult doPostJson(String url, String json) throws IOException {
        Assert.hasText(url);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        if (null != json) {
            StringEntity stringEntity = new StringEntity(json, "UTF-8");
            httpPost.setEntity(stringEntity);
        }
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            return new HttpResult(response.getStatusLine().getStatusCode(), EntityUtils.toString(
                    response.getEntity(), "UTF-8"));
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }
}
