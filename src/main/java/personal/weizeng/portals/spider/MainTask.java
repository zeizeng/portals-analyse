package personal.weizeng.portals.spider;

import personal.weizeng.portals.spider.baidutieba.TieBaSpider;

/**
 * Created by Administrator on 2018/3/6.
 */
public class MainTask {

    public static void main(String[] args) {
        new TieBaSpider().singleThread();
    }
}
