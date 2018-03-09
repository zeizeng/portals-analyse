package personal.weizeng.portals.parser.baidutieba;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import personal.weizeng.portals.dto.tieba.CategoryDto;
import personal.weizeng.portals.dto.tieba.TiebaDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/3/6.
 */
public class TieBaParser {

    public static ArrayList<CategoryDto> getCategoryAndUrl(String indexHtmlString) {

        ArrayList<CategoryDto> categoryDtos = new ArrayList<>();
        if (indexHtmlString.contains("抱歉，根据相关法律法规和政策，本吧暂不开放。")) {
            return categoryDtos;
        }
        Document indexDoc = Jsoup.parse(indexHtmlString);
        Element right_sec = indexDoc.getElementById("right-sec");
        if (right_sec == null) {
            return categoryDtos;
        }
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
        if (html.contains("抱歉，根据相关法律法规和政策，本吧暂不开放。")) {
            return 0;
        }
        Document doc = Jsoup.parse(html);
        Elements pagination = doc.getElementsByClass("pagination");
        if (pagination == null) {
            return 0;
        }
        Element lastPageEle = pagination.first().getElementsByTag("a").last();
        String lastPageHref = lastPageEle.attr("href");
        String lastPageNum = lastPageHref.substring(lastPageHref.trim().indexOf("pn=") + 3).trim();
        int totalPageNum = Integer.parseInt(lastPageNum);
        return totalPageNum;
    }

    public static HashMap<String, String> getTieBaNameAndUrl(String html) {
        HashMap<String, String> tieBaNameAndUrl = new HashMap<>();
        if (html.contains("抱歉，根据相关法律法规和政策，本吧暂不开放。")) {
            return tieBaNameAndUrl;
        }
        Document doc = Jsoup.parse(html);
        Element ba_list = doc.getElementById("ba_list");
        if (ba_list == null) {
            return tieBaNameAndUrl;
        }
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

    public static void getHeadInfo(String html, TiebaDto tiebaDto) {
        if (html.contains("抱歉，根据相关法律法规和政策，本吧暂不开放。")) {
            return;
        }
        Document doc = Jsoup.parse(html);

        Element pageHead = doc.getElementById("pagelet_html_forum/pagelet/forum_card_number");
        if (pageHead == null)
            return;
        Document spanDoc = Jsoup.parse(pageHead.getElementsByTag("code").first().childNode(0).attributes().get("comment"));
        if (spanDoc == null)
            return;
        Element card_menNum = spanDoc.getElementsByClass("card_menNum").first();
        if (card_menNum != null) {
            int focus = Integer.parseInt(card_menNum.text().replaceAll("[^\\d]+", ""));

            tiebaDto.setFoucs(focus);
        }
        Element card_infoNum = spanDoc.getElementsByClass("card_infoNum").first();
        if (card_infoNum != null) {
            int postTotal = Integer.parseInt(card_infoNum.text().replaceAll("[^\\d]+", ""));

            tiebaDto.setPostTotal(postTotal);
        }
    }

    public static void getAlbumInfo(String html, TiebaDto tiebaDto) {
        if (html.contains("抱歉，根据相关法律法规和政策，本吧暂不开放。")) {
            return;
        }
        Document doc = Jsoup.parse(html);

        Elements codeElements = doc.select("code[id=pagelet_html_album/pagelet/album_good]");
        if (codeElements == null || codeElements.first() == null || codeElements.first().childNode(0) == null || codeElements.first().childNode(0).attributes() == null) {
            return;
        }
        String attributes = codeElements.first().childNode(0).attributes().get("comment");
        if (attributes == null)
            return;
        Element albumElements = Jsoup.parse(codeElements.first().childNode(0).attributes().get("comment"));
        if (albumElements == null)
            return;
        Element picTotalEle = albumElements.getElementsByClass("picture_amount_total_wrapper").first();
        if (picTotalEle != null) {
            String totalText = picTotalEle.text();
            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(totalText);
            int pictureTotal = 0;
            if (matcher.find()) {
                pictureTotal = Integer.parseInt(matcher.group());
            }
            tiebaDto.setPicNum(pictureTotal);
        }
    }

    public static void getGoodInfo(String html, TiebaDto tiebaDto) {
        if (html.contains("抱歉，根据相关法律法规和政策，本吧暂不开放。")) {
            return;
        }
        Document doc = Jsoup.parse(html);

        Element goodElements = Jsoup.parse(doc.select("code[id=pagelet_html_frs-list/pagelet/thread_list]").first().childNode(0).attributes().get("comment"));
        if (goodElements == null)
            return;
        Element postSuperiorEle = goodElements.getElementsByClass("th_footer_l").first().getElementsByClass("red_text").first();
        if (postSuperiorEle != null) {
            String postSuperiorText = postSuperiorEle.text();
            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(postSuperiorText);
            int postSuperiorTotal = 0;
            if (matcher.find()) {
                postSuperiorTotal = Integer.parseInt(matcher.group());
            }
            tiebaDto.setPostSuperior(postSuperiorTotal);
        }
    }

    public static void getGroupInfo(String html, TiebaDto tiebaDto) {
        if (html.contains("抱歉，根据相关法律法规和政策，本吧暂不开放。")) {
            return;
        }
        Document doc = Jsoup.parse(html);
        Element groupElements = Jsoup.parse(doc.select("code[id=pagelet_html_group/pagelet/group]").first().childNode(0).attributes().get("comment"));
        if (groupElements == null)
            return;
        Element groupMemberEle = groupElements.getElementsByClass("member_count").first();
        if (groupMemberEle != null) {
            String groupMemberString = groupMemberEle.text().replaceAll("[^\\d]+", "").trim();
            tiebaDto.setGroupMenber(Integer.parseInt(groupMemberString));
        }

        Element groupNumEle = groupElements.getElementsByClass("group_count").first();
        if (groupNumEle != null) {
            String groupNum = groupNumEle.text().replaceAll("[^\\d]+", "").trim();
            tiebaDto.setGroups(Integer.parseInt(groupNum));
        }

    }
}
