package personal.weizeng.portals.engine;

import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import personal.weizeng.portals.dto.SpiderResult;
import personal.weizeng.portals.spider.Spider;
import personal.weizeng.portals.utils.HttpClientGenerator;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Administrator on 2018/3/15.
 */
public class Engine {
    private static final Logger logger = LoggerFactory.getLogger(Engine.class);
    private static final String INDEX_URL;

    static {
        Properties properties = new Properties();
        try {
            properties.load(Engine.class.getClassLoader().getResourceAsStream("tieba_config.properties"));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        INDEX_URL = properties.getProperty("tieba.index_url");
    }

    public static void main(String[] args) {

        CloseableHttpClient closeableHttpClient = HttpClientGenerator.getHttpClient();
        Spider spider = new Spider(closeableHttpClient, INDEX_URL, "IndexPageThread");
        SpiderResult spiderResult = new SpiderResult();
        try {
                spiderResult = spider.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
