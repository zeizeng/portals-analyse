package personal.weizeng.portals.spider;

import personal.weizeng.portals.dto.tieba.CategoryDto;
import personal.weizeng.portals.spider.baidutieba.MultipleTieBaSpider;
import personal.weizeng.portals.spider.baidutieba.TieBaSpider;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2018/3/6.
 */
public class MainTask {

    public static void main(String[] args) throws SQLException {
        multipleThreadSpider();
    }

    public static void singleThreadSpider() {
        try {
            new TieBaSpider().singleThread();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void multipleThreadSpider() {
        ExecutorService threadPool = Executors.newFixedThreadPool(12);

        List<CategoryDto> categoryDtos = Collections.synchronizedList(TieBaSpider.crawlIndexOfTieBa());

        for (CategoryDto categoryDto : categoryDtos) {
            threadPool.execute(new MultipleTieBaSpider(categoryDto));
        }


    }
}
