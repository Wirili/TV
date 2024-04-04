package com.github.catvod.spider;

import android.content.Context;
import android.text.TextUtils;

import com.github.catvod.crawler.Spider;
import com.github.catvod.crawler.SpiderDebug;
import com.github.catvod.net.OkHttp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class SP360 extends Spider {
    private static final String siteUrl = "https://auete.com";
    private static final String siteHost = "auete.com";

    /**
     * 播放源配置
     */
    private JSONObject playerConfig;
    /**
     * 筛选配置
     */
    private JSONObject filterConfig;
    //分类页URL规则
    private Pattern regexCategory = Pattern.compile("/(\\w+)/index.html");
    //详情页URL规则
    private Pattern regexVid = Pattern.compile("/(\\w+/\\w+/\\w+)/");
    //播放页URL规则
    private Pattern regexPlay = Pattern.compile("(/\\w+/\\w+/\\w+/play-\\d+-\\d+.html)");
    //筛选页URL规则
    //private Pattern regexPage = Pattern.compile("/\\w+/\\w+/index(\\d+).html");
    private Pattern regexPage = Pattern.compile("/index(\\d+).html");
    //首页URL
    private static final String urlHot = "https://api.web.360kan.com/v1/rank?cat=1";
    //搜索URL
    private static final String urlSearch = "https://api.so.360kan.com/index?force_v=1&kw=%s&from=&pageno=1&v_ap=1&tab=all";
    //详情URL
    private static final String urlDetail = "https://api.web.360kan.com/v1/detail";


    @Override
    public void init(Context context) {
        super.init(context);
        try {
            playerConfig = new JSONObject("{\"dphd\":{\"sh\":\"云播A线\",\"pu\":\"\",\"sn\":0,\"or\":999},\"ccyun\":{\"sh\":\"云播C线\",\"pu\":\"\",\"sn\":0,\"or\":999},\"dbm3u8\":{\"sh\":\"云播D线\",\"pu\":\"\",\"sn\":0,\"or\":999},\"i8i\":{\"sh\":\"云播E线\",\"pu\":\"\",\"sn\":0,\"or\":999},\"m3u8hd\":{\"sh\":\"云播F线\",\"pu\":\"https://auete.com/api/?url=\",\"sn\":1,\"or\":999},\"languang\":{\"sh\":\"云播G线\",\"pu\":\"https://auete.com/api/?url=\",\"sn\":1,\"or\":999},\"hyun\":{\"sh\":\"云播H线\",\"pu\":\"https://auete.com/api/?url=\",\"sn\":1,\"or\":999},\"kyun\":{\"sh\":\"云播K线\",\"pu\":\"https://auete.com/api/?url=\",\"sn\":1,\"or\":999},\"bpyueyu\":{\"sh\":\"云播粤语\",\"pu\":\"\",\"sn\":0,\"or\":999},\"bpguoyu\":{\"sh\":\"云播国语\",\"pu\":\"\",\"sn\":0,\"or\":999}}");
            filterConfig = new JSONObject("{\"urls\":{\"1\":\"https://api.web.360kan.com/v1\",\"2\":\"https://api.web.360kan.com/v1\",\"3\":\"https://api.web.360kan.com/v1\",\"4\":\"https://api.web.360kan.com/v1\",\"recommend\":\"https://api.web.360kan.com/v1/rank?cat=1\",\"search\":\"https://api.so.360kan.com/index?kw={wd}&pageno=1\"},\"classes\":[{\"type_name\":\"电影\",\"type_id\":\"1\"},{\"type_name\":\"电视剧\",\"type_id\":\"2\"},{\"type_name\":\"综艺\",\"type_id\":\"3\"},{\"type_name\":\"动漫\",\"type_id\":\"4\"}],\"filter\":{\"1\":[{\"key\":\"cat\",\"name\":\"类型\",\"value\":[{\"n\":\"全部\",\"v\":\"\"},{\"n\":\"喜剧\",\"v\":\"喜剧\"},{\"n\":\"爱情\",\"v\":\"爱情\"},{\"n\":\"动作\",\"v\":\"动作\"},{\"n\":\"恐怖\",\"v\":\"恐怖\"},{\"n\":\"科幻\",\"v\":\"科幻\"},{\"n\":\"剧情\",\"v\":\"剧情\"},{\"n\":\"犯罪\",\"v\":\"犯罪\"},{\"n\":\"奇幻\",\"v\":\"奇幻\"},{\"n\":\"战争\",\"v\":\"战争\"},{\"n\":\"悬疑\",\"v\":\"悬疑\"},{\"n\":\"动画\",\"v\":\"动画\"},{\"n\":\"文艺\",\"v\":\"文艺\"},{\"n\":\"纪录\",\"v\":\"纪录\"},{\"n\":\"传记\",\"v\":\"传记\"},{\"n\":\"歌舞\",\"v\":\"歌舞\"},{\"n\":\"古装\",\"v\":\"古装\"},{\"n\":\"历史\",\"v\":\"历史\"},{\"n\":\"惊悚\",\"v\":\"惊悚\"},{\"n\":\"伦理\",\"v\":\"伦理\"},{\"n\":\"其他\",\"v\":\"其他\"}]},{\"key\":\"year\",\"name\":\"年代\",\"value\":[{\"n\":\"全部\",\"v\":\"\"},{\"n\":\"2022\",\"v\":\"2022\"},{\"n\":\"2021\",\"v\":\"2021\"},{\"n\":\"2020\",\"v\":\"2020\"},{\"n\":\"2019\",\"v\":\"2019\"},{\"n\":\"2018\",\"v\":\"2018\"},{\"n\":\"2017\",\"v\":\"2017\"},{\"n\":\"2016\",\"v\":\"2016\"},{\"n\":\"2015\",\"v\":\"2015\"},{\"n\":\"2014\",\"v\":\"2014\"},{\"n\":\"2013\",\"v\":\"2013\"},{\"n\":\"2012\",\"v\":\"2012\"},{\"n\":\"2010\",\"v\":\"2010\"},{\"n\":\"2009\",\"v\":\"2009\"},{\"n\":\"2008\",\"v\":\"2008\"},{\"n\":\"2007\",\"v\":\"2007\"},{\"n\":\"更早\",\"v\":\"lt_year\"}]},{\"key\":\"area\",\"name\":\"地区\",\"value\":[{\"n\":\"全部\",\"v\":\"\"},{\"n\":\"内地\",\"v\":\"大陆\"},{\"n\":\"中国香港\",\"v\":\"香港\"},{\"n\":\"中国台湾\",\"v\":\"台湾\"},{\"n\":\"泰国\",\"v\":\"泰国\"},{\"n\":\"美国\",\"v\":\"美国\"},{\"n\":\"韩国\",\"v\":\"韩国\"},{\"n\":\"日本\",\"v\":\"日本\"},{\"n\":\"法国\",\"v\":\"法国\"},{\"n\":\"英国\",\"v\":\"英国\"},{\"n\":\"德国\",\"v\":\"德国\"},{\"n\":\"印度\",\"v\":\"印度\"},{\"n\":\"其他\",\"v\":\"其他\"}]},{\"key\":\"rank\",\"name\":\"排序\",\"value\":[{\"n\":\"最近热映\",\"v\":\"rankhot\"},{\"n\":\"最近上映\",\"v\":\"ranklatest\"},{\"n\":\"最受好评\",\"v\":\"rankpoint\"}]}],\"2\":[{\"key\":\"cat\",\"name\":\"类型\",\"value\":[{\"n\":\"全部\",\"v\":\"\"},{\"n\":\"言情\",\"v\":\"言情\"},{\"n\":\"剧情\",\"v\":\"剧情\"},{\"n\":\"伦理\",\"v\":\"伦理\"},{\"n\":\"喜剧\",\"v\":\"喜剧\"},{\"n\":\"悬疑\",\"v\":\"悬疑\"},{\"n\":\"都市\",\"v\":\"都市\"},{\"n\":\"偶像\",\"v\":\"偶像\"},{\"n\":\"古装\",\"v\":\"古装\"},{\"n\":\"军事\",\"v\":\"军事\"},{\"n\":\"警匪\",\"v\":\"警匪\"},{\"n\":\"历史\",\"v\":\"历史\"},{\"n\":\"励志\",\"v\":\"励志\"},{\"n\":\"神话\",\"v\":\"神话\"},{\"n\":\"谍战\",\"v\":\"谍战\"},{\"n\":\"青春\",\"v\":\"青春剧\"},{\"n\":\"家庭\",\"v\":\"家庭剧\"},{\"n\":\"动作\",\"v\":\"动作\"},{\"n\":\"情景\",\"v\":\"情景\"},{\"n\":\"武侠\",\"v\":\"武侠\"},{\"n\":\"科幻\",\"v\":\"科幻\"},{\"n\":\"其他\",\"v\":\"其他\"},{\"n\":\"全部\",\"v\":\"\"}]},{\"key\":\"year\",\"name\":\"年代\",\"value\":[{\"n\":\"2022\",\"v\":\"2022\"},{\"n\":\"2021\",\"v\":\"2021\"},{\"n\":\"2020\",\"v\":\"2020\"},{\"n\":\"2019\",\"v\":\"2019\"},{\"n\":\"2018\",\"v\":\"2018\"},{\"n\":\"2017\",\"v\":\"2017\"},{\"n\":\"2016\",\"v\":\"2016\"},{\"n\":\"2015\",\"v\":\"2015\"},{\"n\":\"2014\",\"v\":\"2014\"},{\"n\":\"2013\",\"v\":\"2013\"},{\"n\":\"2012\",\"v\":\"2012\"},{\"n\":\"2010\",\"v\":\"2010\"},{\"n\":\"2009\",\"v\":\"2009\"},{\"n\":\"2008\",\"v\":\"2008\"},{\"n\":\"2007\",\"v\":\"2007\"},{\"n\":\"更早\",\"v\":\"lt_year\"}]},{\"key\":\"area\",\"name\":\"地区\",\"value\":[{\"n\":\"全部\",\"v\":\"\"},{\"n\":\"内地\",\"v\":\"内地\"},{\"n\":\"中国香港\",\"v\":\"香港\"},{\"n\":\"中国台湾\",\"v\":\"台湾\"},{\"n\":\"泰国\",\"v\":\"泰国\"},{\"n\":\"日本\",\"v\":\"日本\"},{\"n\":\"韩国\",\"v\":\"韩国\"},{\"n\":\"美国\",\"v\":\"美国\"},{\"n\":\"英国\",\"v\":\"英国\"},{\"n\":\"新加坡\",\"v\":\"新加坡\"}]},{\"key\":\"rank\",\"name\":\"排序\",\"value\":[{\"n\":\"最近热映\",\"v\":\"rankhot\"},{\"n\":\"最近上映\",\"v\":\"ranklatest\"},{\"n\":\"最受好评\",\"v\":\"rankpoint\"}]}],\"3\":[{\"key\":\"cat\",\"name\":\"类型\",\"value\":[{\"n\":\"全部\",\"v\":\"\"},{\"n\":\"脱口秀\",\"v\":\"脱口秀\"},{\"n\":\"真人秀\",\"v\":\"真人秀\"},{\"n\":\"搞笑\",\"v\":\"搞笑\"},{\"n\":\"选秀\",\"v\":\"选秀\"},{\"n\":\"八卦\",\"v\":\"八卦\"},{\"n\":\"访谈\",\"v\":\"访谈\"},{\"n\":\"情感\",\"v\":\"情感\"},{\"n\":\"生活\",\"v\":\"生活\"},{\"n\":\"晚会\",\"v\":\"晚会\"},{\"n\":\"音乐\",\"v\":\"音乐\"},{\"n\":\"职场\",\"v\":\"职场\"},{\"n\":\"美食\",\"v\":\"美食\"},{\"n\":\"时尚\",\"v\":\"时尚\"},{\"n\":\"游戏\",\"v\":\"游戏\"},{\"n\":\"少儿\",\"v\":\"少儿\"},{\"n\":\"体育\",\"v\":\"体育\"},{\"n\":\"纪实\",\"v\":\"纪实\"},{\"n\":\"科教\",\"v\":\"科教\"},{\"n\":\"曲艺\",\"v\":\"曲艺\"},{\"n\":\"歌舞\",\"v\":\"歌舞\"},{\"n\":\"财经\",\"v\":\"财经\"},{\"n\":\"汽车\",\"v\":\"汽车\"},{\"n\":\"播报\",\"v\":\"播报\"},{\"n\":\"其他\",\"v\":\"其他\"}]},{\"key\":\"area\",\"name\":\"地区\",\"value\":[{\"n\":\"全部\",\"v\":\"\"},{\"n\":\"内地\",\"v\":\"大陆\"},{\"n\":\"中国香港\",\"v\":\"香港\"},{\"n\":\"中国台湾\",\"v\":\"台湾\"},{\"n\":\"日本\",\"v\":\"日本\"},{\"n\":\"欧美\",\"v\":\"欧美\"}]},{\"key\":\"rank\",\"name\":\"排序\",\"value\":[{\"n\":\"最近热映\",\"v\":\"rankhot\"},{\"n\":\"最近上映\",\"v\":\"ranklatest\"}]}],\"4\":[{\"key\":\"cat\",\"name\":\"类型\",\"value\":[{\"n\":\"全部\",\"v\":\"\"},{\"n\":\"热血\",\"v\":\"热血\"},{\"n\":\"科幻\",\"v\":\"科幻\"},{\"n\":\"美少女\",\"v\":\"美少女\"},{\"n\":\"魔幻\",\"v\":\"魔幻\"},{\"n\":\"经典\",\"v\":\"经典\"},{\"n\":\"励志\",\"v\":\"励志\"},{\"n\":\"少儿\",\"v\":\"少儿\"},{\"n\":\"冒险\",\"v\":\"冒险\"},{\"n\":\"搞笑\",\"v\":\"搞笑\"},{\"n\":\"推理\",\"v\":\"推理\"},{\"n\":\"恋爱\",\"v\":\"恋爱\"},{\"n\":\"治愈\",\"v\":\"治愈\"},{\"n\":\"幻想\",\"v\":\"幻想\"},{\"n\":\"校园\",\"v\":\"校园\"},{\"n\":\"动物\",\"v\":\"动物\"},{\"n\":\"机战\",\"v\":\"机战\"},{\"n\":\"亲子\",\"v\":\"亲子\"},{\"n\":\"儿歌\",\"v\":\"儿歌\"},{\"n\":\"运动\",\"v\":\"运动\"},{\"n\":\"悬疑\",\"v\":\"悬疑\"},{\"n\":\"怪物\",\"v\":\"怪物\"},{\"n\":\"战争\",\"v\":\"战争\"},{\"n\":\"益智\",\"v\":\"益智\"},{\"n\":\"青春\",\"v\":\"青春\"},{\"n\":\"童话\",\"v\":\"童话\"},{\"n\":\"竞技\",\"v\":\"竞技\"},{\"n\":\"动作\",\"v\":\"动作\"},{\"n\":\"社会\",\"v\":\"社会\"},{\"n\":\"友情\",\"v\":\"友情\"},{\"n\":\"真人版\",\"v\":\"真人版\"},{\"n\":\"电影版\",\"v\":\"电影版\"},{\"n\":\"OVA版\",\"v\":\"OVA版\"},{\"n\":\"TV版\",\"v\":\"TV版\"},{\"n\":\"新番动画\",\"v\":\"新番动画\"},{\"n\":\"完结动画\",\"v\":\"完结动画\"}]},{\"key\":\"year\",\"name\":\"年代\",\"value\":[{\"n\":\"全部\",\"v\":\"\"},{\"n\":\"2022\",\"v\":\"2022\"},{\"n\":\"2021\",\"v\":\"2021\"},{\"n\":\"2020\",\"v\":\"2020\"},{\"n\":\"2019\",\"v\":\"2019\"},{\"n\":\"2018\",\"v\":\"2018\"},{\"n\":\"2017\",\"v\":\"2017\"},{\"n\":\"2016\",\"v\":\"2016\"},{\"n\":\"2015\",\"v\":\"2015\"},{\"n\":\"2014\",\"v\":\"2014\"},{\"n\":\"2013\",\"v\":\"2013\"},{\"n\":\"2012\",\"v\":\"2012\"},{\"n\":\"2011\",\"v\":\"2011\"},{\"n\":\"2010\",\"v\":\"2010\"},{\"n\":\"2009\",\"v\":\"2009\"},{\"n\":\"2008\",\"v\":\"2008\"},{\"n\":\"2007\",\"v\":\"2007\"},{\"n\":\"2006\",\"v\":\"2006\"},{\"n\":\"2005\",\"v\":\"2005\"},{\"n\":\"2004\",\"v\":\"2004\"},{\"n\":\"更早\",\"v\":\"更早\"}]},{\"key\":\"area\",\"name\":\"地区\",\"value\":[{\"n\":\"全部\",\"v\":\"\"},{\"n\":\"内地\",\"v\":\"大陆\"},{\"n\":\"日本\",\"v\":\"日本\"},{\"n\":\"美国\",\"v\":\"美国\"}]},{\"key\":\"rank\",\"name\":\"排序\",\"value\":[{\"n\":\"最近热映\",\"v\":\"rankhot\"},{\"n\":\"最近上映\",\"v\":\"ranklatest\"}]}]}}");
        } catch (JSONException e) {
            SpiderDebug.log(e);
        }
    }

    /**
     * 爬虫headers
     *
     * @return
     */
    protected HashMap<String, String> getHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36");
        headers.put("referer", "https://api.web.360kan.com");
        return headers;
    }

    /**
     * 获取分类数据 + 首页最近更新视频列表数据
     *
     * @param filter 是否开启筛选 关联的是 软件设置中 首页数据源里的筛选开关
     * @return
     */
    @Override
    public String homeContent(boolean filter) {
        JSONObject result = new JSONObject();
        try {
            result.put("class", filterConfig.getJSONArray("classes"));
            if (filter) {
                result.put("filters", filterConfig.getJSONObject("filter"));
            }
        } catch (JSONException e) {
            SpiderDebug.log(e);
        }
        return result.toString();
    }

    @Override
    public String homeVideoContent() {
        JSONObject result = new JSONObject();
        try {
            JSONArray data = new JSONObject(OkHttp.string(urlHot, getHeaders())).getJSONArray("data");
            JSONArray videos = new JSONArray();
            for (int i = 0; i < data.length(); i++) {
                JSONObject vod = data.getJSONObject(i);
                JSONObject v = new JSONObject();
                v.put("vod_id", vod.getString("cat") + "_" + vod.getString("ent_id"));
                v.put("vod_name", vod.getString("title"));
                v.put("vod_pic", vod.getString("cover"));
                v.put("vod_remarks", vod.getString("upinfo"));
                videos.put(v);
            }
            result.put("list", videos);
        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return result.toString();
    }

    /**
     * 获取分类信息数据
     *
     * @param tid    分类id
     * @param pg     页数
     * @param filter 同homeContent方法中的filter
     * @param extend 筛选参数{k:v, k1:v1}
     * @return
     */
    @Override
    public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend) {
        // 获取分类数据的url
        try {
            String url = "https://api.web.360kan.com/v1/filter/list?catid=" + tid + "&pageno=" + pg;
            for (String item : extend.keySet()) {
                if (extend.get(item).trim().length() != 0) {
                    url = url + "&" + item + "=" + URLEncoder.encode(extend.get(item).trim());
                }
            }
            JSONObject data = new JSONObject(OkHttp.string(url, getHeaders())).getJSONObject("data");
            JSONArray movies = data.getJSONArray("movies");
            JSONArray videos = new JSONArray();
            for(int i=0;i<movies.length();i++){
                JSONObject vod = movies.getJSONObject(i);
                JSONObject v = new JSONObject();
                String cover = vod.getString("cover");
                String remarks = "";

                if (vod.has("upinfo")) {
                    remarks = vod.getString("upinfo");
                }
                v.put("vod_id", tid + "_" + vod.getString("id"));
                v.put("vod_name", vod.getString("title"));
                v.put("vod_pic", "https:" + cover);
                v.put("vod_remarks", remarks);
                videos.put(v);
            }

            JSONObject result = new JSONObject();
            int parseInt = Integer.parseInt(data.getString("current_page"));
            int total = data.getInt("total");
            int pagecount = total % 24 == 0 ? total / 24 : (total / 24) + 1;
            result.put("page", parseInt);
            result.put("pagecount", pagecount);
            result.put("limit", 24);
            result.put("total", total);
            result.put("list", videos);
            return result.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
            return "";
        }
    }

    /**
     * 视频详情信息
     *
     * @param ids 视频id
     * @return
     */
    @Override
    public String detailContent(List<String> ids) {
        try {
            String[] str = ids.get(0).split("_");
            String catId = str[0];
            String Id = str[1];
            // 视频详情url
            String url = urlDetail + "?cat=" + catId + "&id="+Id;
            //System.out.println(url);
            JSONObject data = new JSONObject(OkHttp.string(url, getHeaders())).getJSONObject("data");

            ArrayList<String> temp = new ArrayList<>();
            JSONArray Atemp = new JSONArray();
            // 取基本数据
            String cover = data.getString("cdncover");
            String title = data.getString("title");
            String desc = data.getString("description");

            String category = "";
            String area = "";
            String actor = "";
            String director = "";

            Atemp = data.getJSONArray("moviecategory");
            if(Atemp.length()>0){
                for(int i=0;i<Atemp.length();i++){
                    temp.add(Atemp.getString(i));
                }
                category = TextUtils.join("/",temp);
            }

            Atemp = data.getJSONArray("area");
            temp.clear();
            if(Atemp.length()>0){
                for(int i=0;i<Atemp.length();i++){
                    temp.add(Atemp.getString(i));
                }
                area = TextUtils.join("/",temp);
            }

            Atemp = data.getJSONArray("actor");
            temp.clear();
            if(Atemp.length()>0){
                for(int i=0;i<Atemp.length();i++){
                    temp.add(Atemp.getString(i));
                }
                actor = TextUtils.join("/",temp);
            }

            Atemp = data.getJSONArray("director");
            temp.clear();
            if(Atemp.length()>0){
                for(int i=0;i<Atemp.length();i++){
                    temp.add(Atemp.getString(i));
                }
                director = TextUtils.join("/",temp);
            }

            String year = data.getString("pubdate");
            String remark  = "";

            JSONArray sites = data.getJSONArray("playlink_sites");
            Map<String, String> vod_play = new TreeMap<>(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    try {
                        int sort1 = Integer.parseInt(o1);
                        int sort2 = Integer.parseInt(o2);
                        return sort1 - sort2 > 0 ? 1 : -1;
                    }catch (Exception e){
                        return 1;
                    }
                }
            });
            for (int i=0;i<sites.length();i++) {
                String site = sites.getString(i);
                if(!data.has("total")&&!data.has("tag")){
                    vod_play.put(site,title+"$"+data.getJSONObject("playlinksdetail").getJSONObject(site).getString("default_url"));
                }else {
                    if (data.has("tag")) {
                        JSONObject tagList = data.getJSONObject("tag");
                        ArrayList<String> tagKeys = new ArrayList<>();
                        Iterator<String> it = tagList.keys();
                        while (it.hasNext()) {
                            tagKeys.add((String) it.next());
                        }
                        Collections.reverse(tagKeys);
                        for(int k=0;k<tagKeys.size()&&k<2;k++){
                            String tagName = tagKeys.get(k);
                            List<String> vodItems = new ArrayList<>();
                            JSONArray AllData = new JSONObject(OkHttp.string(url + "&site=" + site + "&year=" + tagName, getHeaders())).getJSONObject("data").getJSONArray("defaultepisode");
                            if (AllData.length() > 0) {
                                for (int j = 0; j < AllData.length(); j++) {
                                    JSONObject vod = AllData.getJSONObject(j);
                                    vodItems.add(vod.getString("period") + (vod.getInt("is_vip") ==1 ? "V" : "") + "$" + vod.getString("url"));
                                }
                                vod_play.put(site + " " + tagName, TextUtils.join("#", vodItems));
                            }
                        }
                    }else{
                        int total = data.getJSONObject("allupinfo").getInt(site);
                        int num=1;
                        int start =1;
                        List<String> vodItems = new ArrayList<>();
                        while (num<=total){
                            num+=199;
                            if(num>total)
                            {
                                num=total;
                            }
                            JSONArray AllData = new JSONObject(OkHttp.string(url + "&site=" + site + "&start=" + start + "&end="+num, getHeaders())).getJSONObject("data").getJSONObject("allepidetail").getJSONArray(site);
                            if (AllData.length() > 0) {
                                for (int j = 0; j < AllData.length(); j++) {
                                    JSONObject vod = AllData.getJSONObject(j);
                                    vodItems.add(vod.getString("playlink_num") + "$" + vod.getString("url"));
                                }
                                vod_play.put(site, TextUtils.join("#", vodItems));
                            }
                            num++;
                            start+=200;
                        }
                    }
                }
            }
            JSONObject vodList = new JSONObject();
            vodList.put("vod_id", ids.get(0));
            vodList.put("vod_name", title);
            vodList.put("vod_pic", cover);
            vodList.put("type_name", category);
            vodList.put("vod_year", year);
            vodList.put("vod_area", area);
            vodList.put("vod_remarks", remark);
            vodList.put("vod_actor", actor);
            vodList.put("vod_director", director);
            vodList.put("vod_content", desc);

            if (vod_play.size() > 0) {
                String vod_play_from = TextUtils.join("$$$", vod_play.keySet());
                String vod_play_url = TextUtils.join("$$$", vod_play.values());
                vodList.put("vod_play_from", vod_play_from);
                vodList.put("vod_play_url", vod_play_url);
            }
            JSONObject result = new JSONObject();
            JSONArray list = new JSONArray();
            list.put(vodList);
            result.put("list", list);
            return result.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }

    /**
     * 获取视频播放信息
     *
     * @param flag     播放源
     * @param id       视频id
     * @param vipFlags 所有可能需要vip解析的源
     * @return
     */
    @Override
    public String playerContent(String flag, String id, List<String> vipFlags) {
        JSONObject result = new JSONObject();
        try {
            result.put("parse", 1);
            result.put("url", id);
            result.put("jx", "1");
            result.put("playUrl", "");
        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return result.toString();
    }

    /**
     * 搜索
     *
     * @param key
     * @param quick 是否播放页的快捷搜索
     * @return
     */
    @Override
    public String searchContent(String key, boolean quick) {
        JSONObject longData;
        JSONObject result = new JSONObject();
        try {
            JSONObject data = new JSONObject(OkHttp.string(String.format(urlSearch, key), getHeaders())).getJSONObject("data");
            if (data != null && (longData = data.getJSONObject("longData")) != null) {
                JSONArray list = longData.getJSONArray("rows");
                JSONArray videos = new JSONArray();
                for (int i = 0; i < list.length(); i++) {
                    JSONObject vod = list.getJSONObject(i);
                    JSONObject v = new JSONObject();
                    v.put("vod_id", vod.getString("cat_id") + "_" + vod.getString("en_id"));
                    v.put("vod_name", vod.getString("titleTxt"));
                    v.put("vod_pic", vod.getString("cover"));
                    v.put("vod_remarks", vod.getString("score"));
                    videos.put(v);
                }
                result.put("list", videos);
            }
        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return result.toString();
    }
}
