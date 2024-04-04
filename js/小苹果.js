var rule = {
  title:'小苹果',//规则标题,没有实际作用,但是可以作为cms类名称依据
  host:'http://t.grelighting.cn',//网页的域名根,包含http头如 https://www,baidu.com
  homeUrl:'http://t.grelighting.cn/api.php/v2.vod/androidfilter10086?type=0&page=fypage&sortby=updatetime',//网站的首页链接,可以是完整路径或者相对路径,用于分类获取和推荐获取 fyclass是分类标签 fypage是页数
  url:'http://t.grelighting.cn/api.php/v2.vod/androidfilter10086?type=fyclass&page=fypage&sortby=updatetime',//网站的分类页面链接
  detailUrl:'http://ht.grelighting.cn/api.php/v2.vod/androiddetail?vod_id=fyid',//非必填,二级详情拼接链接,感觉没啥卵用
  searchUrl:'http://ht.grelighting.cn/api.php/v2.vod/androidsearch10086?page=1&wd=**',//搜索链接 可以是完整路径或者相对路径,用于分类获取和推荐获取 **代表搜索词 fypage代表页数
  searchable:1,//是否启用全局搜索,
  filterable:1,//是否启用筛选,
  // 筛选网站传参,会自动传到分类链接下(本示例中的url参数)-url里参数为fyfilter,可参考蓝莓影视.js
  filter_url:'class={{fl.class}}&year={{fl.year}}&area={{fl.area}}',
  filter:{"1":[{"key":"class","name":"类型","value":[{"n":"全部","v":""},{"n":"动作片","v":"动作片"},{"n":"喜剧片","v":"喜剧片"},{"n":"爱情片","v":"爱情片"},{"n":"科幻片","v":"科幻片"},{"n":"恐怖片","v":"恐怖片"},{"n":"犯罪片","v":"犯罪片"},{"n":"战争片","v":"战争片"},{"n":"动漫片","v":"动漫片"},{"n":"剧情片","v":"剧情片"},{"n":"记录片","v":"记录片"}]},{"key":"year","name":"年代","value":[{"n":"全部","v":""},{"n":"2023","v":"2023"},{"n":"2022","v":"2022"},{"n":"2021","v":"2021"},{"n":"2020","v":"2020"},{"n":"2019","v":"2019"},{"n":"2018","v":"2018"},{"n":"2017","v":"2017"},{"n":"2016","v":"2016"},{"n":"2015","v":"2015"},{"n":"2014","v":"2014"},{"n":"2013","v":"2013"},{"n":"2012","v":"2012"},{"n":"2011","v":"2011"},{"n":"2010","v":"2010"},{"n":"更早","v":"更早"}]},{"key":"area","name":"地区","value":[{"n":"全部","v":""},{"n":"大陆","v":"大陆"},{"n":"港台","v":"港台"},{"n":"日韩","v":"日韩"},{"n":"欧美","v":"欧美"},{"n":"海外","v":"海外"}]}],"2":[{"key":"class","name":"类型","value":[{"n":"全部","v":""},{"n":"古装","v":"古装"},{"n":"家庭","v":"家庭"},{"n":"历史","v":"历史"},{"n":"情感","v":"情感"},{"n":"悬疑","v":"悬疑"},{"n":"网剧","v":"网剧"},{"n":"偶像","v":"偶像"},{"n":"经典","v":"经典"},{"n":"乡村","v":"乡村"},{"n":"情景","v":"情景"},{"n":"商战","v":"商战"},{"n":"惊悚","v":"惊悚"},{"n":"言情","v":"言情"},{"n":"犯罪","v":"犯罪"},{"n":"奇幻","v":"奇幻"},{"n":"运动","v":"运动"},{"n":"冒险","v":"冒险"},{"n":"音乐","v":"音乐"}]},{"key":"year","name":"年代","value":[{"n":"全部","v":""},{"n":"2023","v":"2023"},{"n":"2022","v":"2022"},{"n":"2021","v":"2021"},{"n":"2020","v":"2020"},{"n":"2019","v":"2019"},{"n":"2018","v":"2018"},{"n":"2017","v":"2017"},{"n":"2016","v":"2016"},{"n":"2015","v":"2015"},{"n":"2014","v":"2014"},{"n":"2013","v":"2013"},{"n":"2012","v":"2012"},{"n":"2011","v":"2011"},{"n":"2010","v":"2010"},{"n":"更早","v":"更早"}]},{"key":"area","name":"地区","value":[{"n":"全部","v":""},{"n":"大陆","v":"大陆"},{"n":"港台","v":"港台"},{"n":"日韩","v":"日韩"},{"n":"欧美","v":"欧美"},{"n":"海外","v":"海外"}]}],"3":[{"key":"class","name":"类型","value":[{"n":"全部","v":""},{"n":"真人秀","v":"真人秀"},{"n":"脱口秀","v":"脱口秀"},{"n":"选秀","v":"选秀"},{"n":"晚会","v":"晚会"},{"n":"音乐","v":"音乐"},{"n":"情感","v":"情感"},{"n":"访谈","v":"访谈"},{"n":"歌舞","v":"歌舞"},{"n":"竞技","v":"竞技"},{"n":"搞笑","v":"搞笑"}]},{"key":"year","name":"年代","value":[{"n":"全部","v":""},{"n":"2023","v":"2023"},{"n":"2022","v":"2022"},{"n":"2021","v":"2021"},{"n":"2020","v":"2020"},{"n":"2019","v":"2019"},{"n":"2018","v":"2018"},{"n":"2017","v":"2017"},{"n":"2016","v":"2016"},{"n":"2015","v":"2015"},{"n":"2014","v":"2014"},{"n":"2013","v":"2013"},{"n":"2012","v":"2012"},{"n":"2011","v":"2011"},{"n":"2010","v":"2010"},{"n":"更早","v":"更早"}]},{"key":"area","name":"地区","value":[{"n":"全部","v":""},{"n":"大陆","v":"大陆"},{"n":"港台","v":"港台"},{"n":"日韩","v":"日韩"},{"n":"欧美","v":"欧美"},{"n":"海外","v":"海外"}]}],"4":[{"key":"class","name":"类型","value":[{"n":"全部","v":""},{"n":"热血","v":"热血"},{"n":"少女","v":"少女"},{"n":"魔幻","v":"魔幻"},{"n":"爆笑","v":"爆笑"},{"n":"冒险","v":"冒险"},{"n":"恋爱","v":"恋爱"},{"n":"校园","v":"校园"},{"n":"治愈","v":"治愈"},{"n":"灵异","v":"灵异"},{"n":"机战","v":"机战"},{"n":"格斗","v":"格斗"},{"n":"\u803d\u7f8e","v":"\u803d\u7f8e"},{"n":"推理","v":"推理"},{"n":"穿越","v":"穿越"}]},{"key":"year","name":"年代","value":[{"n":"全部","v":""},{"n":"2023","v":"2023"},{"n":"2022","v":"2022"},{"n":"2021","v":"2021"},{"n":"2020","v":"2020"},{"n":"2019","v":"2019"},{"n":"2018","v":"2018"},{"n":"2017","v":"2017"},{"n":"2016","v":"2016"},{"n":"2015","v":"2015"},{"n":"2014","v":"2014"},{"n":"2013","v":"2013"},{"n":"2012","v":"2012"},{"n":"2011","v":"2011"},{"n":"2010","v":"2010"},{"n":"更早","v":"更早"}]},{"key":"area","name":"地区","value":[{"n":"全部","v":""},{"n":"大陆","v":"大陆"},{"n":"港台","v":"港台"},{"n":"日韩","v":"日韩"},{"n":"欧美","v":"欧美"},{"n":"海外","v":"海外"}]}]},
  // 注意,由于猫有配置缓存,搜索配置没法热加载，修改了js不需要重启服务器
  // 但是需要tv_box进设置里换源使配置重新装载
  headers:{//网站的请求头,完整支持所有的,常带ua和cookies
      'User-Agent':'okhttp/3.12.11'
  },
  timeout:5000,//网站的全局请求超时,默认是3000毫秒
  class_name:'电影&电视剧&综艺&动漫',//静态分类名称拼接
  class_url:'1&2&3&4',//静态分类标识拼接
  // 服务器解析播放
  play_parse:true,
  // 自定义免嗅
  lazy:"js:input={url:input,header:JSON.stringify({'user-agent':'Lvaf/58.12.100'})}",
  // 首页推荐显示数量
  limit:20,
  推荐:'json:data;name;pic;updateInfo;id',
  一级:'json:data;name;pic;updateInfo;id',
  //二级发起访问前进行js处理。解决特殊情况一级给出的链接非二级真实源码而是前端重定向链接的源码
  二级:'js:var d=[];VOD={vod_id:input};try{let html=request(input);print(html);html=JSON.parse(html);let node=html.data;VOD={vod_id:node["id"],vod_name:node["name"].strip(),vod_pic:node["pic"],type_name:node["className"],vod_year:node["year"],vod_area:node["area"],vod_remarks:"",vod_actor:node["actor"],vod_director:node["director"],vod_content:node["content"].strip()};let episodes=node["urls"];let playMap={};episodes.forEach(function(ep){let source="线路"+ep["type"];if(!playMap.hasOwnProperty(source)){playMap[source]=[]}playMap[source].append(ep["key"].strip()+"$"+urlencode(ep["url"]))});let playFrom=[];let playList=[];Object.keys(playMap).forEach(function(key){playFrom.append(key);playList.append(playMap[key].join("#"))});let vod_play_from=playFrom.join("$$$");let vod_play_url=playList.join("$$$");VOD["vod_play_from"]=vod_play_from;VOD["vod_play_url"]=vod_play_url}catch(e){log("获取二级详情页发生错误:"+e.message)}',
  // 搜索可以是*,集成一级，或者跟一级一样的写法 列表;标题;图片;描述;链接;详情
  搜索:'json:data;name;pic;updateInfo;id',
}
