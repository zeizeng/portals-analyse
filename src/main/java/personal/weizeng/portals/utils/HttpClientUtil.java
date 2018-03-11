package personal.weizeng.portals.utils;

import org.apache.http.ConnectionClosedException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * Created by Administrator on 2018/3/6.
 */
public class HttpClientUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);


    public static String doGet(CloseableHttpClient httpClient, String url, String charset, HashMap<String, String> header) throws IOException {
        String html = null;
        logger.info("开始抓取网页：" + url);
        HttpGet httpGet = new HttpGet(url);
        int maxTry = 3;
        CloseableHttpResponse closeableHttpResponse = null;
        do {
            closeableHttpResponse = get(httpClient, httpGet, 1000, header);
        } while (closeableHttpResponse == null && (--maxTry) != 0);

        try {
            html = getHtml(closeableHttpResponse, charset);
        } finally {
            httpGet.releaseConnection();
            closeableHttpResponse.close();
        }
        return html;
    }

    public static CloseableHttpResponse get(CloseableHttpClient closeableHttpClient, HttpGet httpGet, long timeout, Map<String, String> headers) {
        try {
            logger.info("本次间隔时间：" + timeout / 1000 + "秒");
//        每次查询都暂停一定时间
            TimeUnit.MILLISECONDS.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        if (headers != null) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                httpGet.setHeader(header.getKey(), header.getValue());
            }
        }
        CloseableHttpResponse closeableHttpResponse = null;
        try {
            closeableHttpResponse = closeableHttpClient.execute(httpGet);
        } catch (ConnectTimeoutException | SocketTimeoutException e) {
            logger.error(e.getMessage(), e);
            logger.debug("请求  超时");
            return null;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            logger.debug(e.getMessage());
            return null;
        }
        return closeableHttpResponse;
    }

    public static String getHtml(CloseableHttpResponse closeableHttpResponse, String charset) {
        String html = null;
        if (closeableHttpResponse == null) {
            return html;
        }
        if (closeableHttpResponse.getStatusLine().getStatusCode() == 302) {
            Header location = closeableHttpResponse.getFirstHeader("Location");
            if (location == null)
                location = closeableHttpResponse.getFirstHeader("location");
            return location.getValue();
        }
        if (charset == null) {
            String content_type;
            try {
                content_type = closeableHttpResponse.getFirstHeader("Content-Type").getValue();       //  text/html; charset=utf-8
                String[] content_type_array = content_type.split(";");
                for (String type : content_type_array) {
                    if (type.toLowerCase().contains("charset")) {
                        type = type.toLowerCase().trim();
                        charset = type.substring(type.indexOf("charset") + 8);
                    }
                }
            } catch (NullPointerException e) {
                logger.error(e.getMessage(), e);
            }
            if (charset == null)
                charset = "UTF-8";
        }
        try {
            return EntityUtils.toString(closeableHttpResponse.getEntity(), charset);
        } catch (ConnectionClosedException e) {
            logger.error(e.getMessage(), e);
            return null;
        } catch (SocketTimeoutException e) {
            logger.error(e.getMessage(), e);
            return null;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }
}
