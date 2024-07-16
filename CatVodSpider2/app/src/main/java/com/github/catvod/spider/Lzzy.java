package com.github.catvod.spider;

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
        JSONObject obj = new JSONObject(json);
        ArrayList<String> ids = new ArrayList<>();
        JSONArray vodArray = obj.getJSONArray("list");
        for (int j = 0; j < vodArray.length(); j++) {
            JSONObject vod = vodArray.getJSONObject(j);
            ids.add(vod.optString("vod_id").trim());
        }
        json = OkHttp.string(siteUrl+"?ac=detail&ids="+TextUtils.join(",", ids), getDetailHeader());
        Result rs = Result.objectFrom(json);
        return rs.string();
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
