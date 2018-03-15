package personal.weizeng.portals.parser;

import org.apache.http.ConnectionClosedException;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import personal.weizeng.portals.dto.ParserResult;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.Callable;

/**
 * Created by Administrator on 2018/3/15.
 */
public class Parser implements Callable<ParserResult> {


    private static final Logger logger = LoggerFactory.getLogger(Parser.class);

    private CloseableHttpResponse closeableHttpResponse;

    private String url;

    private String threadName;

    private String charset;


    @Override
    public ParserResult call() throws Exception {
        return doParser();
    }

    public Parser(CloseableHttpResponse closeableHttpResponse, String url, String threadName) {
        this.closeableHttpResponse = closeableHttpResponse;
        this.url = url;
        this.threadName = threadName;
    }

    public Parser(CloseableHttpResponse closeableHttpResponse, String url, String threadName, String charset) {
        this.closeableHttpResponse = closeableHttpResponse;
        this.url = url;
        this.threadName = threadName;
        this.charset = charset;
    }

    private ParserResult doParser() {
        logger.info(threadName + " 开始获取" + url + " html 文件");
        ParserResult parserResult = new ParserResult();
        if (closeableHttpResponse.getStatusLine().getStatusCode() == 302) {
            Header location = closeableHttpResponse.getFirstHeader("Location");
            if (location == null) {
                location = closeableHttpResponse.getFirstHeader("location");
                parserResult.setStatus(302);
                parserResult.setUrl(location.getValue());
                return parserResult;
            }
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
        String html;
        try {
            html = EntityUtils.toString(closeableHttpResponse.getEntity(), charset);
        } catch (ConnectionClosedException e) {
            logger.error(threadName + " " + url + e.getMessage(), e);
            parserResult.setStatus(-1);
            parserResult.setUrl(url);
            return parserResult;
        } catch (SocketTimeoutException e) {
            logger.error(threadName + " " + url + e.getMessage(), e);
            parserResult.setStatus(-1);
            parserResult.setUrl(url);
            return parserResult;
        } catch (IOException e) {
            logger.error(threadName + " " + url + e.getMessage(), e);
            parserResult.setStatus(-1);
            parserResult.setUrl(url);
            return parserResult;
        }
        parserResult.setPage(html);

        return parserResult;

    }
}
