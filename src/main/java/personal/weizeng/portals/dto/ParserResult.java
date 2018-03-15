package personal.weizeng.portals.dto;

/**
 * Created by Administrator on 2018/3/15.
 */
public class ParserResult {

    private int retryTimes = 3;

    private int status = 0;

    private String page;

    private String url;

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

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
