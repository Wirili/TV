package com.github.catvod.spider;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

import com.github.catvod.crawler.Spider;
import com.github.catvod.crawler.SpiderDebug;
import com.github.catvod.net.OkHttp;
import com.github.catvod.net.OkResult;
import com.github.catvod.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Author: @SDL
 */
public class Kuaikan extends Spider {

    private static String AESKey;
    private static long timeExpires;
    private static String umId;
    private HashMap<String, ArrayList<String>> analysesList = new HashMap<>();
    private static String RsaKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDitX/rWP6WWc3h\n" +
            "DzUcfD8Hbw79yCB7rz18GEkjDapjlUVIa3P8FEQFoOchu7N+vdK6HQNsl2Bql4lv\n" +
            "WOYuzRWNbLGQ7mNtMca28iCuPHck0AcJzOs9F6qruPL73eY7Ai7yvzs2IfEdyXSz\n" +
            "HP/yXgEOi5ZEBfsBZEPMJw6vfHJVueuD/lh/LkoJ/gXEKMCM/pkIzGpBPd2TsIg8\n" +
            "ZdvOGMPy+MOnJb1Qfe0y3QQdc5VPVdZFWoWsM9RAenHmE3d1THb+uNZZZcnM1tV7\n" +
            "xYgBbIhgHtXAZnWDWDRBM3xJPR3O9dfBHHt/tbyrZrSwK1IgwFgqfYm/tRbmp4y/\n" +
            "1DhIF6mDAgMBAAECggEAIzDjS2gEFNiZ0a6nouVSb1f47sHq8OgR1jp619seMNkR\n" +
            "6Rzs4xtON8VzO1REl47lsAgi6O9SgxlEtykIiglBqDNQGNw4SNHqM6nAEuvF3sv/\n" +
            "27CYb2JGFuPdq+UVAOHk4b93dH6uS95ipA8DV97psRVP3P1EqkGjGISTjf/2S4IG\n" +
            "10Q0UhP4xxiRS2iCtaIq0fbvnrChqYRlYQYpkkRCCH+Q4FNKquSsqmWyCu5yfeQM\n" +
            "Mek+aT9KgvmCh05UCeqC1x7xPtSneNbpJpLD5zW8CcG7EYFDtIP8b9BPDlud47ZH\n" +
            "5Y4/wyQQFHHLOaniTJVvEJZ7cIoXy2gOhvgcHpNryQKBgQDxNNtXnCfVK0JYNhnU\n" +
            "5WCOX9NeW9Q79HldhTM5/EqsjA5H21j5S7A/duBSRfX7iou1hyXaa2p+J7rxF9sj\n" +
            "NlwHPsKhprskxXaHJ7qU84gSmwORsTQQPmtTsizGwVsM330+Q2ZROaSEfDoUqH9+\n" +
            "KBKs6K+FOldcTOrCZ+ILk8werwKBgQDwnQUrKATzRThVdD6Hcgt/F/SAizDmvtiT\n" +
            "VvRcGkiV3EtB5Rx8zFNJ9GWrqSAArwIxTWgfYCYsvpUjbJW02Mg0Ufj2+YhjT713\n" +
            "O9yViwUq996St9eLwGQO5FDMD8FYi7bRAiZEAeAeIEeveDRsNK15++8VVJVo7b2l\n" +
            "zcapp5k3bQKBgQDSiJNHRhqSet6+xgIIDGoZ+1Qv0TFPX5UrZt8OpsK7FshEOhXQ\n" +
            "Cxt8WZN03HHXK9fEC4GjwwxBrwYB+BOjCYiMHmCd3j0M3HoXgDrgViyYKMuVuDk8\n" +
            "UG83r5ZbqVuCwfO8i/HbxddueEvtyiD2CZ47ZCIHxKOKAe0K4Mex2UBaKwKBgQDi\n" +
            "rEouOelP9Kn5dyVhHENQXBTu9DIBb1FAnO5fxjMTWxFa5qhLuYHNbfxKF24atsRu\n" +
            "BepNhJryFCkT0jvGv2L8Ry0wHiwqwvhO14obJ3ia3iBEQAVDlt+sV9L2KvGOpTB4\n" +
            "/nlmIA4u02I1IBziw02aWYkGo0SOMUo0ZQL+2PEykQKBgAV1uqiJlWuA9uPdFFJP\n" +
            "ZPL2YoOuTWdWfIwIb/UdsbUuTckCgqPIqUPi3HhiVSunOgcO7HWK3GD3j8k4XEbW\n" +
            "G4y+Ik0lLrWzBl728tZe1GENzTf8aapElHlUfFSM4OlrTRBsbMOsQBcJNh7QbyoZ\n" +
            "BHiZZTbRUY6xUR4DGYkCOksF";

    private static String RsaKey2= "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCiYJ5UFjeEgeUi5ynLKhj5EMF0\n" +
            "3sawTyAKoPbfUCSZZQ5QdrHhrINggrYxDtB/TWXoqSF+BJP85F0vq8B6gOqFWhK/\n" +
            "1PIKe2wKlRRuZSOdUzK/3vzRHmR3J3KGrvNzvwARH8gg8xZjbsalc/gBkcVxSxGd\n" +
            "8j8rJ4QhOSFh9F8B2wIDAQAB";

    public HashMap<String, String> getHeaders(long timestamp) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("appId", "1000300001");
        headers.put("version", "4");
        headers.put("timestamp", timestamp + "");
        headers.put("token", Utils.MD5("1000300001dc431681b806089dac1153fb13960f874" + timestamp + "notice"));
        headers.put("User-Agent", "okhttp/5.0.0-alpha.2");
        return headers;
    }
    private void getConfig() {
        if (System.currentTimeMillis() > timeExpires) {
            try {
                JSONObject json = new JSONObject();
                long timestamp = System.currentTimeMillis() / 1000;
                String sign = Utils.MD5("bf70a456195ae394184b2e0b1b471cae1000300001dc431681b806089dac1153fb13960f87" + timestamp + "notice");
                json.put("appId", "1000300001");
                json.put("sign", sign);
                json.put("timestamp", timestamp);
                String body = AESEncrypt("bf70a456195ae394", "184b2e0b1b471cae", json.toString());
                String ticket = RSAEncrypt(RsaKey, "bf70a456195ae394184b2e0b1b471cae");
                JSONObject json2 = new JSONObject();
                json2.put("body", body);
                json2.put("ticket", ticket);
                OkResult rs =OkHttp.postJson("http://ctlook.facaishiyi.com/api/v1/user/login.do", json2.toString(), getHeaders(timestamp));
                if (rs.getCode() == 200) {
                    try {
                        JSONObject rep = new JSONObject(rs.getBody());
                        String ticket1 = RSADecrypt(RsaKey2, rep.getString("ticket"));
                        JSONObject data = new JSONObject(AESDecrypt(ticket1.substring(0, 16), ticket1.substring(16, 32), rep.getString("body")));
                        timeExpires = System.currentTimeMillis() + data.getLong("duration") * 1000;
                        AESKey = data.getString("data");
                        getAnalysesList();
                    } catch (JSONException e) {
                    }
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    public void getAnalysesList() {
        try {
            JSONObject json = new JSONObject();
            long timestamp = System.currentTimeMillis() / 1000;
            String sign = Utils.MD5("1000300001dc431681b806089dac1153fb13960f8700" + umId + timestamp + "notice");
            json.put("adv", 0);
            json.put("appId", "1000300001");
            json.put("nav", 0);
            json.put("sign", sign);
            json.put("timestamp", timestamp);
            json.put("umId", umId);
            String body = AESEncrypt(AESKey.substring(0, 16), AESKey.substring(16, 32), json.toString());
            String ticket = RSAEncrypt(RsaKey, AESKey);
            JSONObject json2 = new JSONObject();
            json2.put("body", body);
            json2.put("ticket", ticket);
            OkResult rs = OkHttp.postJson("http://ctlook.facaishiyi.com/api/v1/user/config.do", json2.toString(), getHeaders(timestamp));

            if (rs.getCode() == 200)
            {
                try {
                    JSONObject rep = new JSONObject(rs.getBody());
                    String ticket1 = RSADecrypt(RsaKey2, rep.getString("ticket"));
                    JSONArray newAnalysis = new JSONObject(AESDecrypt(ticket1.substring(0, 16), ticket1.substring(16, 32), rep.getString("body"))).getJSONObject("data").getJSONArray("newAnalysis");
                    analysesList.clear();
                    for (int i = 0; i < newAnalysis.length(); i++) {
                        JSONObject lists = newAnalysis.getJSONObject(i);
                        ArrayList<String> arrayList = new ArrayList<>();
                        analysesList.put(lists.getString("id"), arrayList);
                        JSONArray item = lists.getJSONArray("analyses");
                        for (int j = 0; j < item.length(); j++) {
                            arrayList.add(item.getJSONObject(j).getString("url"));
                        }
                    }
                } catch (JSONException e) {
                }
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void init(Context context, String ext) {
        super.init(context, ext);
        SharedPreferences sharedPreferences = context.getSharedPreferences("spider_Kuaikan", 0);
        try {
            umId = sharedPreferences.getString("umId", null);
        } catch (Throwable th) {
        }finally {
            if (umId == null) {
                umId = Utils.MD5(UUID.randomUUID().toString()).toLowerCase();
                sharedPreferences.edit().putString("umId", umId).commit();
            }
        }
    }

    @Override
    public String homeContent(boolean filter) {
        try {
            getConfig();
            JSONObject json = new JSONObject();
            long timestamp = System.currentTimeMillis() / 1000;
            String sign = Utils.MD5("1000300001dc431681b806089dac1153fb13960f87" + timestamp + "notice");
            json.put("appId", "1000300001");
            json.put("sign", sign);
            json.put("timestamp", timestamp);
            String body = AESEncrypt(AESKey.substring(0, 16), AESKey.substring(16, 32), json.toString());
            String ticket = RSAEncrypt(RsaKey, AESKey);
            JSONObject json2 = new JSONObject();
            json2.put("body", body);
            json2.put("ticket", ticket);
            final JSONObject filters = new JSONObject();
            final JSONArray classes = new JSONArray();
            OkResult rs = OkHttp.postJson("http://ctlook.facaishiyi.com/api/v1/user/genres.do", json2.toString(), getHeaders(timestamp));
            if (rs.getCode() == 200){
                try {
                    JSONObject str = new JSONObject(rs.getBody());
                    String ticketx = RSADecrypt(RsaKey2, str.getString("ticket"));
                    JSONArray genres = new JSONObject(AESDecrypt(ticketx.substring(0, 16), ticketx.substring(16, 32), str.getString("body"))).getJSONArray("genres");
                    for (int i = 0; i < genres.length(); i++) {
                        JSONObject jSONObject5 = genres.getJSONObject(i);
                        String string = jSONObject5.getString("name");
                        final int typeId = jSONObject5.getInt("typeId");
                        if (typeId > 0) {
                            JSONObject type = new JSONObject();
                            type.put("type_id", typeId + "");
                            type.put("type_name", string);
                            classes.put(type);
                            JSONObject json3 = new JSONObject();
                            long timestamp1 = System.currentTimeMillis() / 1000;
                            sign = Utils.MD5("1000300001dc431681b806089dac1153fb13960f87" + typeId + timestamp1 + "notice");
                            json3.put("appId", "1000300001");
                            json3.put("tid", typeId);
                            json3.put("sign", sign);
                            json3.put("timestamp", timestamp1);
                            body = AESEncrypt(AESKey.substring(0, 16), AESKey.substring(16, 32), json3.toString());
                            ticket = RSAEncrypt(RsaKey, AESKey);
                            JSONObject json4 = new JSONObject();
                            json4.put("body", body);
                            json4.put("ticket", ticket);
                            new JSONArray();
                            OkResult rs1 = OkHttp.postJson("http://ctlook.facaishiyi.com/api/v1/search/category.do", json4.toString(), getHeaders(timestamp1));
                            if (rs1.getCode() == 200) {
                                try {
                                    JSONObject rep = new JSONObject(rs1.getBody());
                                    String ticketz = RSADecrypt(RsaKey2, rep.getString("ticket"));
                                    JSONObject data = new JSONObject(AESDecrypt(ticketz.substring(0, 16), ticketz.substring(16, 32), rep.getString("body")));

                                    JSONArray tags = data.getJSONArray("tags");
                                    JSONArray areas = data.getJSONArray("areas");
                                    JSONArray years = data.getJSONArray("years");

                                    JSONArray totalType = new JSONArray();

                                    JSONObject tagType = new JSONObject();
                                    tagType.put("key", "tagId");
                                    tagType.put("name", "类型");
                                    JSONArray tagValue = new JSONArray();
                                    for (int j=0; j < tags.length(); j++) {
                                        JSONObject tag = tags.getJSONObject(j);
                                        JSONObject tagnv = new JSONObject();
                                        tagnv.put("n", tag.getString("name"));
                                        tagnv.put("v", tag.getString("id"));
                                        tagValue.put(tagnv);
                                    }
                                    tagType.put("value", tagValue);
                                    totalType.put(tagType);

                                    JSONObject areaType = new JSONObject();
                                    areaType.put("key", "areaId");
                                    areaType.put("name", "地区");
                                    JSONArray areaValue = new JSONArray();
                                    for (int j = 0; j < areas.length(); j++) {
                                        JSONObject area = areas.getJSONObject(j);
                                        JSONObject areanv = new JSONObject();
                                        areanv.put("n", area.getString("name"));
                                        areanv.put("v", area.getString("id"));
                                        areaValue.put(areanv);
                                    }
                                    areaType.put("value", areaValue);
                                    totalType.put(areaType);

                                    JSONObject yearType = new JSONObject();
                                    yearType.put("key", "yearId");
                                    yearType.put("name", "年份");
                                    JSONArray yearValue = new JSONArray();
                                    for (int j = 0; j < years.length(); j++) {
                                        JSONObject year = years.getJSONObject(j);
                                        JSONObject yearnv = new JSONObject();
                                        yearnv.put("n", year.getString("name"));
                                        yearnv.put("v", year.getString("id"));
                                        yearValue.put(yearnv);
                                    }
                                    yearType.put("value", yearValue);
                                    totalType.put(yearType);
                                    filters.put(typeId + "", totalType);
                                } catch (JSONException unused) {
                                }
                            }
                        }
                    }
                } catch (JSONException unused1) {
                }
            }
            JSONObject result = new JSONObject();
            result.put("class", classes);
            if (filter) {
                result.put("filters", filters);
            }
            return result.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
            return "";
        }
    }

    @Override
    public String homeVideoContent() {
        try {
            getConfig();
            JSONObject json = new JSONObject();
            long timestamp = System.currentTimeMillis() / 1000;
            String f = Utils.MD5("1000300001dc431681b806089dac1153fb13960f87115" + timestamp + "notice");
            json.put("appId", "1000300001");
            json.put("id", 115);
            json.put("sign", f);
            json.put("timestamp", timestamp);
            String body = AESEncrypt(AESKey.substring(0, 16), AESKey.substring(16, 32), json.toString());
            String ticket = RSAEncrypt(RsaKey, AESKey);
            JSONObject json2 = new JSONObject();
            json2.put("body", body);
            json2.put("ticket", ticket);
            final JSONArray list = new JSONArray();
            OkResult rs =  OkHttp.postJson("http://ctlook.facaishiyi.com/api/v1/user/genre/115.do", json2.toString(), getHeaders(timestamp));
            if (rs.getCode() == 200) {
                try {
                    JSONObject rep = new JSONObject(rs.getBody());
                    String ticket1 = RSADecrypt(RsaKey2, rep.getString("ticket"));
                    JSONArray subGenres = new JSONObject(AESDecrypt(ticket1.substring(0, 16), ticket1.substring(16, 32), rep.getString("body"))).getJSONArray("subGenres");
                    for (int i = 0; i < subGenres.length(); i++) {
                        if (subGenres.getJSONObject(i).getInt("template") == 1) {
                            JSONArray videos = subGenres.getJSONObject(i).getJSONArray("video");
                            for (int j = 0; j < videos.length(); j++) {
                                JSONObject vObj = videos.getJSONObject(j);
                                JSONObject v = new JSONObject();
                                v.put("vod_id", vObj.getString("id"));
                                v.put("vod_name", vObj.getString("name"));
                                v.put("vod_pic", vObj.getString("pic"));
                                v.put("vod_remarks", vObj.getString("progress"));
                                list.put(v);
                            }
                        }
                    }
                } catch (JSONException unused) {
                }
            }
            JSONObject result = new JSONObject();
            result.put("list", list);
            return result.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
            return "";
        }
    }

    @Override
    public String categoryContent(String tid, final String pg, boolean filter, HashMap<String, String> extend) {
        try {
            getConfig();
            int areaId = extend.containsKey("areaId") ? Integer.parseInt(extend.get("areaId")) : 0;
            int tagId = extend.containsKey("tagId") ? Integer.parseInt(extend.get("tagId")) : 0;
            int yearId = extend.containsKey("yearId") ? Integer.parseInt(extend.get("yearId")) : 0;
//            int areaId = 0;
//            int tagId = 0;
//            int yearId = 0;
            JSONObject json = new JSONObject();
            long timestamp = System.currentTimeMillis() / 1000;
            String sign = Utils.MD5("1000300001dc431681b806089dac1153fb13960f87" + pg + 20 + tid + tagId + areaId + yearId + timestamp + "notice");
            json.put("appId", "1000300001");
            json.put("typeId", tid);
            json.put("areaId", areaId);
            json.put("tagId", tagId);
            json.put("yearId", yearId);
            json.put("page", Integer.parseInt(pg));
            json.put("pageSize", 20);
            json.put("sign", sign);
            json.put("timestamp", timestamp);
            String body = AESEncrypt(AESKey.substring(0, 16), AESKey.substring(16, 32), json.toString());
            String ticket = RSAEncrypt(RsaKey, AESKey);
            JSONObject json2 = new JSONObject();
            json2.put("body", body);
            json2.put("ticket", ticket);
            final JSONObject result = new JSONObject();
            OkResult rs = OkHttp.postJson("http://ctlook.facaishiyi.com/api/v1/search/video.do", json2.toString(), getHeaders(timestamp));
            if (rs.getCode() == 200){
                try {
                    JSONObject rep = new JSONObject(rs.getBody());
                    String ticket1 = RSADecrypt(RsaKey2, rep.getString("ticket"));
                    JSONObject data = new JSONObject(AESDecrypt(ticket1.substring(0, 16), ticket1.substring(16, 32), rep.getString("body")));
                    JSONArray list = new JSONArray();
                    JSONArray videos = data.getJSONArray("results");
                    for (int i = 0; i < videos.length(); i++) {
                        JSONObject vod = videos.getJSONObject(i);
                        JSONObject v = new JSONObject();
                        v.put("vod_id", vod.getString("id"));
                        v.put("vod_name", vod.getString("name"));
                        v.put("vod_pic", vod.getString("pic"));
                        v.put("vod_remarks", vod.getString("progress"));
                        list.put(v);
                    }
                    result.put("page", Integer.parseInt(pg));
                    int count = data.getInt("count");
                    result.put("pagecount", count % 20 == 0 ? count / 20 : (count / 20) + 1);
                    result.put("limit", 20);
                    result.put("total", count);
                    result.put("list", list);
                } catch (JSONException je) {
                }
            }
            return result.toString();
        } catch (Exception e1) {
            SpiderDebug.log(e1);
            return "";
        }
    }

    @Override
    public String detailContent(List<String> ids) {
        try {
            getConfig();
            JSONObject json = new JSONObject();
            long timestamp = System.currentTimeMillis() / 1000;
            String sign = Utils.MD5("1000300001dc431681b806089dac1153fb13960f87" + ids.get(0) + timestamp + "notice");
            json.put("appId", "1000300001");
            json.put("id", ids.get(0));
            json.put("sign", sign);
            json.put("timestamp", timestamp);
            String body = AESEncrypt(AESKey.substring(0, 16), AESKey.substring(16, 32), json.toString());
            String ticket = RSAEncrypt(RsaKey, AESKey);
            JSONObject json2 = new JSONObject();
            json2.put("body", body);
            json2.put("ticket", ticket);
            final JSONObject result = new JSONObject();
            OkResult rs = OkHttp.postJson("http://ctlook.facaishiyi.com/api/v1/video/play/" + ids.get(0) + ".do ", json2.toString(), getHeaders(timestamp));
            if (rs.getCode() == 200) {
                try {
                    JSONObject rep = new JSONObject(rs.getBody());
                    String ticket1 = RSADecrypt(RsaKey2, rep.getString("ticket"));
                    JSONObject vObj = new JSONObject(AESDecrypt(ticket1.substring(0, 16), ticket1.substring(16, 32), rep.getString("body"))).getJSONObject("data");

                    JSONObject vodAtom = new JSONObject();
                    vodAtom.put("vod_id", vObj.getString("id"));
                    vodAtom.put("vod_name", vObj.getString("name"));
                    vodAtom.put("vod_pic", vObj.getString("pic"));
                    vodAtom.put("vod_year", vObj.getString("year"));
                    vodAtom.put("vod_remarks", vObj.getString("progress"));
                    vodAtom.put("vod_actor", vObj.getString("actor"));
                    vodAtom.put("vod_director", vObj.getString("director"));
                    vodAtom.put("vod_content", vObj.getString("content").trim());
                    //获取多条线路
                    JSONArray playLines = new JSONArray(AESDecrypt("5551e2a82a21a9a1", "a586c5236206a2cb", vObj.getString("newPlayback")));
                    LinkedHashMap<String, String> vod_play = new LinkedHashMap<>();

                    for(int i=0;i<playLines.length();i++){
                        //获取单条线路
                        JSONObject playLine = playLines.getJSONObject(i);
                        //线路名称
                        String name = playLine.getString("name");
                        String id = playLine.getString("id");
                        JSONArray playList = playLine.getJSONArray("episodes");
                        ArrayList<String> vodItems = new ArrayList<>();

                        for (int j=0; j < playList.length(); j++) {
                            JSONObject play = playList.getJSONObject(j);
                            String title = play.getString("title");
                            String url = title + "|" + ids.get(0) + "|" + id + "|" + play.getString("url");
                            String keyRealUrl = Base64.encodeToString(url.getBytes("UTF-8"), Base64.DEFAULT | Base64.URL_SAFE | Base64.NO_WRAP);
                            vodItems.add(title + "$" + keyRealUrl);
                        }
                        vod_play.put(name, TextUtils.join("#", vodItems));
                    }

                    vodAtom.put("vod_play_from", TextUtils.join("$$$", vod_play.keySet()));
                    vodAtom.put("vod_play_url", TextUtils.join("$$$", vod_play.values()));
                    JSONArray list = new JSONArray();
                    list.put(vodAtom);
                    result.put("list", list);
                } catch (Throwable unused) {
                }
            }
            return result.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
            return "";
        }
    }

    @Override
    public String playerContent(String flag, String id, List<String> vipFlags) {
        try {
            getConfig();
            String[] split = new String(Base64.decode(id, Base64.DEFAULT | Base64.URL_SAFE | Base64.NO_WRAP), "UTF-8").split("\\|");
            ArrayList<String> arrayList = analysesList.get(split[2]);
            final JSONObject result = new JSONObject();
            if (arrayList != null && arrayList.size() > 0) {
                for(String url :arrayList){
                    JSONObject json = new JSONObject();
                    long timestamp = System.currentTimeMillis() / 1000;
                    String sign = Utils.MD5("1000300001dc431681b806089dac1153fb13960f87" + split[1] + split[0] + split[3] + timestamp + "notice");
                    json.put("appId", "1000300001");
                    json.put("id", split[1]);
                    json.put("title", split[0]);
                    json.put("url", split[3]);
                    json.put("sign", sign);
                    json.put("timestamp", timestamp);
                    String body = AESEncrypt(AESKey.substring(0, 16), AESKey.substring(16, 32), json.toString());
                    String ticket = RSAEncrypt(RsaKey, AESKey);
                    JSONObject json2 = new JSONObject();
                    json2.put("body", body);
                    json2.put("ticket", ticket);
                    OkResult rs = OkHttp.postJson(url, json2.toString(), getHeaders(timestamp));
                    if (rs.getCode() == 200) {
                        try {
                            json = new JSONObject(rs.getBody());
                            ticket = RSADecrypt(RsaKey2, json.getString("ticket"));
                            JSONObject b = new JSONObject(AESDecrypt(ticket.substring(0, 16), ticket.substring(16, 32), json.getString("body")));
                            if (!b.optString("msg", "").equals("success") || b.optInt("code", -1) != 0) {
                                return "";
                            }
                            result.put("parse", 0);
                            result.put("header", "");
                            result.put("playUrl", "");
                            result.put("url", b.getString("url"));
                        } catch (JSONException unused) {
                        }
                    }
                    if (result.has("url")) {
                        break;
                    }
                }
            }
            if (result.has("url")) {
                return result.toString();
            }
            if (!isVideoFormat(split[3])) {
                return "";
            }
            result.put("parse", 0);
            result.put("header", "");
            result.put("playUrl", "");
            result.put("url", split[3]);
            return result.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
            return "";
        }
    }

    @Override
    public String searchContent(String key, boolean quick) {
        try {
            getConfig();
            JSONObject json = new JSONObject();
            long timestamp = System.currentTimeMillis() / 1000;
            String sign = Utils.MD5("1000300001dc431681b806089dac1153fb13960f87112" + key + timestamp + "notice");
            json.put("appId", "1000300001");
            json.put("keyword", key);
            json.put("page", 1);
            json.put("pageSize", 12);
            json.put("sign", sign);
            json.put("timestamp", timestamp);
            String body = AESEncrypt(AESKey.substring(0, 16), AESKey.substring(16, 32), json.toString());
            String ticket = RSAEncrypt(RsaKey, AESKey);
            JSONObject json2 = new JSONObject();
            json2.put("body", body);
            json2.put("ticket", ticket);
            final JSONObject result = new JSONObject();
            OkResult rs = OkHttp.postJson("http://ctlook.facaishiyi.com/api/v1/search/page.do", json2.toString(), getHeaders(timestamp));
            if (rs.getCode() == 200) {
                try {
                    json = new JSONObject(rs.getBody());
                    ticket = RSADecrypt(RsaKey2, json.getString("ticket"));
                    JSONObject b = new JSONObject(AESDecrypt(ticket.substring(0, 16), ticket.substring(16, 32), json.getString("body")));
                    JSONArray list = new JSONArray();
                    JSONArray videos = b.getJSONArray("results");
                    for (int i = 0; i < videos.length(); i++) {
                        JSONObject vObj = videos.getJSONObject(i);
                        JSONObject v = new JSONObject();
                        v.put("vod_id", vObj.getString("id"));
                        v.put("vod_name", vObj.getString("name"));
                        v.put("vod_pic", vObj.getString("pic"));
                        list.put(v);
                    }
                    result.put("list", list);
                } catch (JSONException unused) {
                }
            }
            return result.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
            return "";
        }
    }
    /***
     * AES加密 1 加密
     * @param key
     * @param iv
     * @param str
     * @return
     */
    public static String AESEncrypt(String key,String iv,String str)
    {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(iv.getBytes()));
            return Base64.encodeToString(cipher.doFinal(str.getBytes()), Base64.NO_WRAP);
        } catch (Exception e) {
            SpiderDebug.log(e);
            return "";
        }
    }

    /***
     * AES解密 2 解密
     * @param key
     * @param iv
     * @param str
     * @return
     */
    public static String AESDecrypt(String key,String iv,String str)
    {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(iv.getBytes()));
            return new String(cipher.doFinal(Base64.decode(str, Base64.DEFAULT)), "UTF-8");
        } catch (Exception e) {
            SpiderDebug.log(e);
            return "";
        }
    }


    /***
     * RSA加密 1 加密
     * @param key
     * @param str
     * @return
     */
    public static String RSAEncrypt(String key,String str) {
        try {
            PrivateKey generatePrivate = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Base64.decode(key, Base64.DEFAULT)));
            Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, generatePrivate);
            byte[] inData = str.getBytes();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            if (inData.length <= 256) {
                outputStream.write(cipher.doFinal(inData));
            } else {
                for (int i = 0; i < inData.length; i += 256) {
                    outputStream.write(cipher.doFinal(inData, i, 256));
                }
            }
            outputStream.flush();
            String result = Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP);
            outputStream.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /***
     * RSA解密 2 解密
     * @param key
     * @param str
     * @return
     */
    public static String RSADecrypt(String key,String str) {
        try {
            RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.decode(key, Base64.DEFAULT)));
            Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, pubKey);
            byte[] inData = Base64.decode(str, Base64.DEFAULT);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            if (inData.length <= 256) {
                outputStream.write(cipher.doFinal(inData));
            } else {
                for (int i = 0; i < inData.length; i += 256) {
                    outputStream.write(cipher.doFinal(inData, i, 256));
                }
            }
            outputStream.flush();
            String result = new String(outputStream.toByteArray(), "UTF-8");
            outputStream.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
