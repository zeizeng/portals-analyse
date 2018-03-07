package personal.weizeng.portals.parser.baidutieba;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashMap;

/**
 * Created by Administrator on 2018/3/6.
 */
public class TieBaParser {

    public static HashMap<String,String>getCategoryAndUrl(String indexHtmlString){
        HashMap<String,String> result = new HashMap<>();

        Document indexDoc = Jsoup.parse(indexHtmlString);





        return result;
    }
}
