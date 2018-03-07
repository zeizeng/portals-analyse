package personal.weizeng.portals.spider.baidutieba;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import personal.weizeng.portals.utils.HttpClientUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

/**
 * Created by Administrator on 2018/3/6.
 */
public class TieBaSpider {

    private static final Logger logger = LoggerFactory.getLogger(TieBaSpider.class);
    private static final String INDEX_URL;

    private static final CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
    private static final String DEFAULT_CHARSET;
    private static final HashMap<String,String> HEADER =new HashMap<>();

    static {
        Properties properties = new Properties();
        try {
            properties.load(TieBaSpider.class.getClassLoader().getResourceAsStream("tieba_config.properties"));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        INDEX_URL = properties.getProperty("tieba.index_url");
        DEFAULT_CHARSET = properties.getProperty("tieba.default_charset");
        HEADER.put("Accept","Accept text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        HEADER.put("Accept-Charset","GB2312,utf-8;q=0.7,*;q=0.7");
        HEADER.put("Accept-Encoding","gzip, deflate");
        HEADER.put("Accept-Language","zh-cn,zh;q=0.5");
        HEADER.put("Connection","keep-alive");
        HEADER.put("User-Agent","Mozilla/5.0 (Windows NT 6.1; rv:6.0.2) Gecko/20100101 Firefox/6.0.2");
    }

    public void crawlIndexOfTieBa() {
        String html=null;
        try {
            html = HttpClientUtil.doGet(closeableHttpClient, INDEX_URL, DEFAULT_CHARSET,HEADER);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(html);

    }


    public static void main(String[] args) {

        new TieBaSpider().crawlIndexOfTieBa();
    }
}
