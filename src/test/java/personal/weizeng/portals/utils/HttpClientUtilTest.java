package personal.weizeng.portals.utils;

import junit.framework.TestCase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Administrator on 2018/3/6.
 */
public class HttpClientUtilTest extends TestCase {

    @Test
    public void testDoGet(){
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        String url = "http://tieba.baidu.com/";
        try {
            System.out.println(HttpClientUtil.doGet(closeableHttpClient, url, "utf-8",new HashMap<>()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}