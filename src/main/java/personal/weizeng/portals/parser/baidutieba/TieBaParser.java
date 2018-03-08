package personal.weizeng.portals.parser.baidutieba;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import personal.weizeng.portals.dto.tieba.CategoryDto;
import personal.weizeng.portals.dto.tieba.TiebaDto;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2018/3/6.
 */
public class TieBaParser {

    public static ArrayList<CategoryDto> getCategoryAndUrl(String indexHtmlString) {

        ArrayList<CategoryDto> categoryDtos = new ArrayList<>();

        Document indexDoc = Jsoup.parse(indexHtmlString);
        Element right_sec = indexDoc.getElementById("right-sec");
        Elements class_items = right_sec.getElementsByClass("class-item");

        for (Element element : class_items) {
            Element superEle = element.children().first();
            Element lowEle = element.children().last();
            String superCategory = superEle.text();
            for (Element e : lowEle.getElementsByTag("li")) {
                CategoryDto categoryDto = new CategoryDto();
                categoryDto.setSuperCategory(superCategory);
                categoryDto.setLowCategory(e.text());
                categoryDto.setLowCategoryURL(e.getElementsByTag("a").first().attr("href"));
                categoryDtos.add(categoryDto);
            }
        }
        return categoryDtos;
    }


    public static int getLastPageNum(String html) {
        Document doc = Jsoup.parse(html);
        Element ba_list = doc.getElementById("ba_list");
        Elements pagination = doc.getElementsByClass("pagination");
        Element lastPageEle = pagination.first().getElementsByTag("a").last();
        String lastPageHref = lastPageEle.attr("href");
        String lastPageNum = lastPageHref.substring(lastPageHref.trim().indexOf("pn=") + 3).trim();
        int totalPageNum = Integer.parseInt(lastPageNum);
        return totalPageNum;
    }

    public static HashMap<String, String> getTieBaNameAndUrl(String html) {
        HashMap<String, String> tieBaNameAndUrl = new HashMap<>();
        Document doc = Jsoup.parse(html);
        Element ba_list = doc.getElementById("ba_list");
        Elements ba_info = ba_list.getElementsByClass("ba_info");
        for (Element element : ba_info) {
            Element ba_href = element.getElementsByClass("ba_href").first();
            String url = ba_href.attr("href");
            Elements ba_name = element.getElementsByClass("ba_name");
            String name = ba_name.text();
            tieBaNameAndUrl.put(name, url);
        }
        return tieBaNameAndUrl;
    }

    public static void getDetails(String html,TiebaDto tiebaDto){
        Document doc = Jsoup.parse(html);


    }
}
