package personal.weizeng.portals.spider;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import personal.weizeng.portals.dto.SpiderResult;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/3/15.
 */
public class Spider implements Callable<SpiderResult> {

    private static final Logger logger = LoggerFactory.getLogger(Spider.class);

    private CloseableHttpClient httpClient;

    private String threadName;

    private String url;

    @Override
    public SpiderResult call() throws Exception {
        return spiderPage();
    }

    public Spider(CloseableHttpClient httpClient, String url, String threadName) {
        this.httpClient = httpClient;
        this.threadName = threadName;
        this.url = url;
    }

    private SpiderResult spiderPage() {
        SpiderResult result = new SpiderResult();
        try {
            logger.info(threadName + " 本次间隔时间：" + 1000 / 1000 + "秒");
//        每次查询都暂停一定时间
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            result.setRetryTimes(result.getRetryTimes() - 1);
            result.setStatus(-1);
            return result;
        }
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse closeableHttpResponse = null;
        try {
            closeableHttpResponse = httpClient.execute(httpGet);
        } catch (ConnectTimeoutException | SocketTimeoutException e) {
            logger.error(e.getMessage(), e);
            logger.debug("请求  超时");
            result.setRetryTimes(result.getRetryTimes() - 1);
            result.setStatus(-1);
            return result;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            logger.debug(e.getMessage());
            result.setRetryTimes(result.getRetryTimes() - 1);
            result.setStatus(-1);
            return result;
        }
        result.setCloseableHttpResponse(closeableHttpResponse);
        return result;
    }
}
