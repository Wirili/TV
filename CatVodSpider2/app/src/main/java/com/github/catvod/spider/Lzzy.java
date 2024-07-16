package com.github.catvod.spider;

import android.text.TextUtils;

import com.github.catvod.bean.Class;
import com.github.catvod.bean.Filter;
import com.github.catvod.bean.Result;
import com.github.catvod.bean.Vod;
import com.github.catvod.crawler.Spider;
import com.github.catvod.net.OkHttp;
import com.github.catvod.utils.Util;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author zhixc
 * 量子资源
 */
public class Lzzy extends Spider {

    private final String siteUrl = "https://cj.lziapi.com/api.php/provide/vod";
    private String nextSearchUrlPrefix;
    private String nextSearchUrlSuffix;

    private Map<String, String> getHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("User-Agent", Util.CHROME);
        header.put("Referer", siteUrl + "/");
        return header;
    }

    private Map<String, String> getDetailHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("User-Agent", Util.CHROME);
        return header;
    }

    @Override
    public String homeContent(boolean filter) throws Exception {
        String json = OkHttp.string(siteUrl, getDetailHeader());
        Result rs = Result.objectFrom(json);
        return rs.string();
    }

    private List<Vod> parseVodListFromDoc(String html) {
        return parseVodListFromDoc(Jsoup.parse(html));
    }

    private List<Vod> parseVodListFromDoc(Document doc) {
        Elements items = doc.select("#post_container .post_hover");
        List<Vod> list = new ArrayList<>();
        for (Element item : items) {
            Element element = item.select("[class=zoom]").get(0);
            String vodId = element.attr("href");
            String name = element.attr("title").replaceAll("</?[^>]+>", "");
            String pic = element.select("img").attr("src");
            String remark = item.select("[rel=category tag]").text();
            list.add(new Vod(vodId, name, pic, remark));
        }
        return list;
    }

    @Override
    public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend) throws Exception {
        String json = OkHttp.string(siteUrl+"?ac=detail&t="+tid+"&page="+pg, getDetailHeader());
        Result rs = Result.objectFrom(json);
        return rs.string();
    }

    @Override
    public String detailContent(List<String> ids) throws Exception {
        String vodId = ids.get(0);
        String detailUrl = siteUrl + vodId;
        String json = OkHttp.string(siteUrl+"?ac=detail&ids="+vodId, getDetailHeader());
        Result rs = Result.objectFrom(json);
        return Result.string(rs.getList().get(0));
    }

    private String getStrByRegex(Pattern pattern, String str) {
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) return matcher.group(1).trim();
        return "";
    }

    private String getActorOrDirector(Pattern pattern, String str) {
        return getStrByRegex(pattern, str)
                .replaceAll("<br>", "")
                .replaceAll("&nbsp;", "")
                .replaceAll("&amp;", "")
                .replaceAll("middot;", "・")
                .replaceAll("　　　　　", ",")
                .replaceAll("　　　　 　", ",")
                .replaceAll("　", "");
    }

    private String getDescription(Pattern pattern, String str) {
        return getStrByRegex(pattern, str)
                .replaceAll("</?[^>]+>", "")
                .replaceAll("\n", "")
                .replaceAll("&amp;", "")
                .replaceAll("middot;", "・")
                .replaceAll("ldquo;", "【")
                .replaceAll("rdquo;", "】")
                .replaceAll("　", "");
    }

    @Override
    public String searchContent(String key, boolean quick) throws Exception {
        return searchContent(key, quick, "1");
    }

    @Override
    public String searchContent(String key, boolean quick, String pg) throws Exception {
        String searchUrl = siteUrl + "/e/search/index.php";
        if (pg.equals("1")) {
            RequestBody formBody = new FormBody.Builder()
                    .add("show", "title")
                    .add("tempid", "1")
                    .add("tbname", "article")
                    .add("mid", "1")
                    .add("dopost", "search")
                    .add("submit", "")
                    .addEncoded("keyboard", key)
                    .build();
            Request request = new Request.Builder().url(searchUrl)
                    .addHeader("User-Agent", Util.CHROME)
                    .addHeader("Origin", siteUrl)
                    .addHeader("Referer", siteUrl + "/")
                    .post(formBody)
                    .build();
            Response response = OkHttp.newCall(request);
            String[] split = String.valueOf(response.request().url()).split("\\?searchid=");
            nextSearchUrlPrefix = split[0] + "index.php?page=";
            nextSearchUrlSuffix = "&searchid=" + split[1];
            return Result.string(parseVodListFromDoc(response.body().string()));
        } else {
            int page = Integer.parseInt(pg) - 1;
            searchUrl = nextSearchUrlPrefix + page + nextSearchUrlSuffix;
            return Result.string(parseVodListFromDoc(OkHttp.string(searchUrl, getHeader())));
        }
    }

    @Override
    public String playerContent(String flag, String id, List<String> vipFlags) throws Exception {
        return Result.get().url(id).string();
    }
}
