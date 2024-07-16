package com.github.catvod.spider;

import android.content.Context;
import android.text.TextUtils;

import com.github.catvod.bean.Class;
import com.github.catvod.bean.Filter;
import com.github.catvod.bean.Result;
import com.github.catvod.bean.Vod;
import com.github.catvod.crawler.Spider;
import com.github.catvod.net.OkHttp;
import com.github.catvod.utils.Util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collections;
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

    private final String siteUrl = "https://cj.lziapi.com/api.php/provide/vod/from/lzm3u8";

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
        JSONObject obj = new JSONObject(json);
        JSONArray classArray = obj.getJSONArray("class");
        List<Class> classes = new ArrayList<>();
        List<String> clist = new ArrayList<>();
        Collections.addAll(clist, "国产剧",
                "韩国剧",
                "日本剧",
                "电影片",
                "连续剧",
                "综艺片",
                "动漫片",
                "动作片",
                "喜剧片",
                "爱情片",
                "科幻片",
                "恐怖片",
                "剧情片",
                "战争片",
                "台湾剧",
                "香港剧",
                "欧美剧",
                "记录片",
                "海外剧",
                "泰国剧",
                "大陆综艺",
                "港台综艺",
                "日韩综艺",
                "欧美综艺",
                "国产动漫",
                "日韩动漫",
                "欧美动漫",
                "港台动漫",
                "海外动漫",
                "体育",
                "足球",
                "篮球",
                "网球",
                "斯诺克",
                "伦理片");
        for(String item : clist) {
            for (int j = 0; j < classArray.length(); j++) {
                JSONObject cl = classArray.getJSONObject(j);
                if (item.equals(cl.optString("type_name").trim())) {
                    classes.add(new Class(cl.optString("type_id").trim(), cl.optString("type_name").trim()));
                }
            }
        }

        JSONArray vodArray = obj.getJSONArray("list");
        List<String> ids = new ArrayList<>();
        for (int j = 0; j < vodArray.length(); j++) {
            JSONObject vod = vodArray.getJSONObject(j);
            ids.add(vod.optString("vod_id").trim());
        }
        json = OkHttp.string(siteUrl + "?ac=detail&ids=" + TextUtils.join(",", ids), getDetailHeader());
        Result rs = Result.objectFrom(json);
        return Result.string(classes, rs.getList(), new JSONObject());
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
        String json = OkHttp.string(siteUrl+"?ac=detail&ids="+vodId, getDetailHeader());
        Result rs = Result.objectFrom(json);
        return Result.string(rs.getList().get(0));
    }

    @Override
    public String searchContent(String key, boolean quick) throws Exception {
        return searchContent(key, quick, "1");
    }

    @Override
    public String searchContent(String key, boolean quick, String pg) throws Exception {
        String searchUrl = "https://search.lziapi.com/json-api/?dname=liangzi&key="+key+"&count=100";
        String json = OkHttp.string(searchUrl, getDetailHeader());
        JSONObject obj = new JSONObject(json);
        JSONArray vodArray = obj.getJSONArray("posts");
        ArrayList<String> ids = new ArrayList<>();
        for (int j = 0; j < vodArray.length(); j++) {
            JSONObject vod = vodArray.getJSONObject(j);
            ids.add(vod.optString("vod_id").trim());
        }
        json = OkHttp.string(siteUrl+"?ac=detail&ids="+TextUtils.join(",", ids), getDetailHeader());
        Result rs = Result.objectFrom(json);
        return rs.string();
    }

    @Override
    public String playerContent(String flag, String id, List<String> vipFlags) throws Exception {
        return Result.get().parse(Util.isVideoFormat(id) ? 0 : 1).url(id).string();
    }
}
