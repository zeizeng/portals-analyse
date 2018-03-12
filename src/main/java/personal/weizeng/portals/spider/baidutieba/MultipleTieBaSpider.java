package personal.weizeng.portals.spider.baidutieba;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import personal.weizeng.portals.dto.tieba.CategoryDto;

import java.sql.SQLException;

/**
 * Created by Administrator on 2018/3/11.
 */
public class MultipleTieBaSpider implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(MultipleTieBaSpider.class);
    private CategoryDto categoryDto;

    public MultipleTieBaSpider(CategoryDto categoryDto) {
        this.categoryDto = categoryDto;

    }

    @Override
    public void run() {
        crawlByCategory();
    }

    public void crawlByCategory() {
        try {
            logger.info(categoryDto.getLowCategory()+"线程开始执行");
            TieBaSpider.crawlByCategory(categoryDto);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
