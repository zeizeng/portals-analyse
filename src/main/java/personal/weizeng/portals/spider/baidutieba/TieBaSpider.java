package personal.weizeng.portals.spider.baidutieba;

import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import personal.weizeng.portals.dto.tieba.CategoryDto;
import personal.weizeng.portals.dto.tieba.TiebaDto;
import personal.weizeng.portals.parser.baidutieba.TieBaParser;
import personal.weizeng.portals.save.tieba.SaveToMysql;
import personal.weizeng.portals.utils.DBUtils;
import personal.weizeng.portals.utils.HttpClientGenerator;
import personal.weizeng.portals.utils.HttpClientUtil;
import personal.weizeng.portals.utils.JDBCUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Administrator on 2018/3/6.
 */
public class TieBaSpider {

    private static final Logger logger = LoggerFactory.getLogger(TieBaSpider.class);
    private static final String INDEX_URL;

    private static final String DEFAULT_CHARSET;
    private static final String TIEBA_PRE_URL;
    private static final String TIEBA_POSTFIX;
    private static final HashMap<String, String> HEADER = new HashMap<>();

    static {
        Properties properties = new Properties();
        try {
            properties.load(TieBaSpider.class.getClassLoader().getResourceAsStream("tieba_config.properties"));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        INDEX_URL = properties.getProperty("tieba.index_url");
        TIEBA_PRE_URL = properties.getProperty("tieba.pre_url");
        TIEBA_POSTFIX = properties.getProperty("tieba.postfix");
        DEFAULT_CHARSET = properties.getProperty("tieba.default_charset");
        HEADER.put("Accept", "Accept text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        HEADER.put("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
        HEADER.put("Accept-Encoding", "gzip, deflate");
        HEADER.put("Accept-Language", "zh-cn,zh;q=0.5");
        HEADER.put("Connection", "keep-alive");
        HEADER.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:6.0.2) Gecko/20100101 Firefox/6.0.2");
    }

    public static ArrayList<CategoryDto> crawlIndexOfTieBa() {
        CloseableHttpClient closeableHttpClient = HttpClientGenerator.getHttpClient();
        String html = null;
        try {
            html = HttpClientUtil.doGet(closeableHttpClient, INDEX_URL, DEFAULT_CHARSET, HEADER);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<CategoryDto> categoryDtos = TieBaParser.getCategoryAndUrl(html);


        System.out.println(categoryDtos.size());

        return categoryDtos;
    }


    private static ArrayList<TiebaDto> crawlLowCategoryPage(CategoryDto categoryDto) {
        ArrayList<TiebaDto> tiebaDtos = new ArrayList<>();
        CloseableHttpClient closeableHttpClient = HttpClientGenerator.getHttpClient();
        String html = null;
        String url = TIEBA_PRE_URL + categoryDto.getLowCategoryURL();
        try {
            html = HttpClientUtil.doGet(closeableHttpClient, url, DEFAULT_CHARSET, HEADER);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int lastPageNum = TieBaParser.getLastPageNum(html);
        String urlWithOutPn = url + "&rn=20&pn=";
        HashMap<String, String> tieBaNameAndUrl = new HashMap<>();
        for (int pageNum = 1; pageNum <= lastPageNum; pageNum++) {
            String crawlUrl = urlWithOutPn + pageNum;
            try {
                html = HttpClientUtil.doGet(closeableHttpClient, crawlUrl, DEFAULT_CHARSET, HEADER);
            } catch (IOException e) {
                e.printStackTrace();
            }
            tieBaNameAndUrl.putAll(TieBaParser.getTieBaNameAndUrl(html));
        }
        for (Map.Entry<String, String> entry : tieBaNameAndUrl.entrySet()) {
            TiebaDto tiebaDto = new TiebaDto();
            tiebaDto.setSuperCategory(categoryDto.getSuperCategory());
            tiebaDto.setLowCategory(categoryDto.getLowCategory());
            tiebaDto.setName(entry.getKey());
            tiebaDto.setUrl(entry.getValue());
            tiebaDtos.add(tiebaDto);
        }
        return tiebaDtos;
    }

    private static void crawlTieBaHeadInfo(TiebaDto tiebaDto) {
        String url = TIEBA_PRE_URL + tiebaDto.getUrl();
        CloseableHttpClient closeableHttpClient = HttpClientGenerator.getHttpClient();
        String html = null;
        try {
            html = HttpClientUtil.doGet(closeableHttpClient, url, DEFAULT_CHARSET, HEADER);
        } catch (IOException e) {
            e.printStackTrace();
        }
        TieBaParser.getHeadInfo(html, tiebaDto);
    }

    private static void crawlTieBaAlbumInfo(TiebaDto tiebaDto) {
        String url = TIEBA_PRE_URL + tiebaDto.getUrl() + TIEBA_POSTFIX + "album";
        CloseableHttpClient closeableHttpClient = HttpClientGenerator.getHttpClient();
        String html = null;
        try {
            html = HttpClientUtil.doGet(closeableHttpClient, url, DEFAULT_CHARSET, HEADER);
        } catch (IOException e) {
            e.printStackTrace();
        }
        TieBaParser.getAlbumInfo(html, tiebaDto);
    }

    private static void crawlTieBaGoodInfo(TiebaDto tiebaDto) {
        String url = TIEBA_PRE_URL + tiebaDto.getUrl() + TIEBA_POSTFIX + "good";
        CloseableHttpClient closeableHttpClient = HttpClientGenerator.getHttpClient();
        String html = null;
        try {
            html = HttpClientUtil.doGet(closeableHttpClient, url, DEFAULT_CHARSET, HEADER);
        } catch (IOException e) {
            e.printStackTrace();
        }
        TieBaParser.getGoodInfo(html, tiebaDto);
    }

    private static void crawlTieBaGroupInfo(TiebaDto tiebaDto) {
        String url = TIEBA_PRE_URL + tiebaDto.getUrl() + TIEBA_POSTFIX + "group";
        CloseableHttpClient closeableHttpClient = HttpClientGenerator.getHttpClient();
        String html = null;
        try {
            html = HttpClientUtil.doGet(closeableHttpClient, url, DEFAULT_CHARSET, HEADER);

        } catch (IOException e) {
            e.printStackTrace();
        }
        TieBaParser.getGroupInfo(html, tiebaDto);
    }


    public static void singleThread() throws SQLException {
        ArrayList<CategoryDto> categoryDtos = crawlIndexOfTieBa();
        String query = "insert into tieba " +
                "(uuid,tieba_name,crawl_date,super_category,low_category,url,focus,post_total,post_superior,pic_num," +
                "groups,group_member)" +
                "values (?,?,?,?,?,?,?,?,?,?,?,?)";

        for (CategoryDto categoryDto : categoryDtos) {
            Connection conn = JDBCUtils.getConnection();
            ArrayList<TiebaDto> tiebaDtos = crawlLowCategoryPage(categoryDto);
            int count = 1;
            for (TiebaDto tiebaDto : tiebaDtos) {
                logger.info("No." + (count++) + " 开始爬取 " + categoryDto.getSuperCategory() + "-->" + categoryDto.getLowCategory() + "-->" + tiebaDto.getName());
                crawlTieBaHeadInfo(tiebaDto);
                crawlTieBaAlbumInfo(tiebaDto);
                crawlTieBaGoodInfo(tiebaDto);
                crawlTieBaGroupInfo(tiebaDto);
            }
            SaveToMysql.save2MySql(query, conn, tiebaDtos);
        }


    }

    public static void crawlByCategory(CategoryDto categoryDto) throws SQLException {
        String query = "insert into tieba " +
                "(uuid,tieba_name,crawl_date,super_category,low_category,url,focus,post_total,post_superior,pic_num," +
                "groups,group_member)" +
                "values (?,?,?,?,?,?,?,?,?,?,?,?)";

        Connection conn = DBUtils.getConnection();
        ArrayList<TiebaDto> tiebaDtos = crawlLowCategoryPage(categoryDto);
        int count = 1;
        for (TiebaDto tiebaDto : tiebaDtos) {
            logger.info("No." + (count++) + " 开始爬取 " + categoryDto.getSuperCategory() + "-->" + categoryDto.getLowCategory() + "-->" + tiebaDto.getName());
            crawlTieBaHeadInfo(tiebaDto);
            crawlTieBaAlbumInfo(tiebaDto);
            crawlTieBaGoodInfo(tiebaDto);
            crawlTieBaGroupInfo(tiebaDto);
        }
        SaveToMysql.save2MySql(query, conn, tiebaDtos);
    }
}
