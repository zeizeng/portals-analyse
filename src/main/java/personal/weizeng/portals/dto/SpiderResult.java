package personal.weizeng.portals.dto;

import org.apache.http.client.methods.CloseableHttpResponse;

/**
 * Created by Administrator on 2018/3/15.
 */
public class SpiderResult {

    private int retryTimes = 3;

    private int status = 0;

    private CloseableHttpResponse closeableHttpResponse;

    public int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public CloseableHttpResponse getCloseableHttpResponse() {
        return closeableHttpResponse;
    }

    public void setCloseableHttpResponse(CloseableHttpResponse closeableHttpResponse) {
        this.closeableHttpResponse = closeableHttpResponse;
    }
}
