import cheerio from "assets://js/lib/cheerio.min.js";
import "assets://js/lib/crypto-js.js";

let host = "https://www.gfysys3.com";

let header = {
  "User-Agent": "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1",
  Referer: host,
};
let filterConfig = {
  1: [
    {
      key: "class",
      name: "剧情",
      value: [
        { n: "全部", v: "" },
        { n: "喜剧", v: "喜剧" },
        { n: "爱情", v: "爱情" },
        { n: "恐怖", v: "恐怖" },
        { n: "动作", v: "动作" },
        { n: "科幻", v: "科幻" },
        { n: "剧情", v: "剧情" },
        { n: "战争", v: "战争" },
        { n: "警匪", v: "警匪" },
        { n: "犯罪", v: "犯罪" },
        { n: "动画", v: "动画" },
        { n: "奇幻", v: "奇幻" },
        { n: "武侠", v: "武侠" },
        { n: "冒险", v: "冒险" },
        { n: "枪战", v: "枪战" },
        { n: "恐怖", v: "恐怖" },
        { n: "悬疑", v: "悬疑" },
        { n: "惊悚", v: "惊悚" },
        { n: "经典", v: "经典" },
        { n: "青春", v: "青春" },
        { n: "文艺", v: "文艺" },
        { n: "微电影", v: "微电影" },
        { n: "古装", v: "古装" },
        { n: "历史", v: "历史" },
        { n: "运动", v: "运动" },
        { n: "农村", v: "农村" },
        { n: "儿童", v: "儿童" },
        { n: "网络电影", v: "网络电影" },
      ],
    },
    {
      key: "area",
      name: "地区",
      value: [
        { n: "全部", v: "" },
        { n: "大陆", v: "大陆" },
        { n: "香港", v: "香港" },
        { n: "台湾", v: "台湾" },
        { n: "美国", v: "美国" },
        { n: "法国", v: "法国" },
        { n: "英国", v: "英国" },
        { n: "日本", v: "日本" },
        { n: "韩国", v: "韩国" },
        { n: "德国", v: "德国" },
        { n: "泰国", v: "泰国" },
        { n: "印度", v: "印度" },
        { n: "意大利", v: "意大利" },
        { n: "西班牙", v: "西班牙" },
        { n: "加拿大", v: "加拿大" },
        { n: "其他", v: "其他" },
      ],
    },
    {
      key: "year",
      name: "年份",
      value: [
        { n: "全部", v: "" },
        { n: "2023", v: "2023" },
        { n: "2022", v: "2022" },
        { n: "2021", v: "2021" },
        { n: "2020", v: "2020" },
        { n: "2019", v: "2019" },
        { n: "2018", v: "2018" },
        { n: "2017", v: "2017" },
        { n: "2016", v: "2016" },
        { n: "2015", v: "2015" },
        { n: "2014", v: "2014" },
        { n: "2013", v: "2013" },
        { n: "2012", v: "2012" },
        { n: "2011", v: "2011" },
        { n: "2010", v: "2010" },
      ],
    },
    {
      key: "by",
      name: "排序",
      value: [
        { n: "时间", v: "time" },
        { n: "人气", v: "hits" },
        { n: "评分", v: "score" },
      ],
    },
  ],
  2: [
    {
      key: "class",
      name: "剧情",
      value: [
        { n: "全部", v: "" },
        { n: "古装", v: "古装" },
        { n: "战争", v: "战争" },
        { n: "青春偶像", v: "青春偶像" },
        { n: "喜剧", v: "喜剧" },
        { n: "家庭", v: "家庭" },
        { n: "犯罪", v: "犯罪" },
        { n: "动作", v: "动作" },
        { n: "奇幻", v: "奇幻" },
        { n: "剧情", v: "剧情" },
        { n: "历史", v: "历史" },
        { n: "经典", v: "经典" },
        { n: "乡村", v: "乡村" },
        { n: "情景", v: "情景" },
        { n: "商战", v: "商战" },
        { n: "网剧", v: "网剧" },
        { n: "其他", v: "其他" },
      ],
    },
    {
      key: "area",
      name: "地区",
      value: [
        { n: "全部", v: "" },
        { n: "内地", v: "内地" },
        { n: "韩国", v: "韩国" },
        { n: "香港", v: "香港" },
        { n: "台湾", v: "台湾" },
        { n: "日本", v: "日本" },
        { n: "美国", v: "美国" },
        { n: "泰国", v: "泰国" },
        { n: "英国", v: "英国" },
        { n: "新加坡", v: "新加坡" },
        { n: "其他", v: "其他" },
      ],
    },
    {
      key: "year",
      name: "年份",
      value: [
        { n: "全部", v: "" },
        { n: "2023", v: "2023" },
        { n: "2022", v: "2022" },
        { n: "2021", v: "2021" },
        { n: "2020", v: "2020" },
        { n: "2019", v: "2019" },
        { n: "2018", v: "2018" },
        { n: "2017", v: "2017" },
        { n: "2016", v: "2016" },
        { n: "2015", v: "2015" },
        { n: "2014", v: "2014" },
        { n: "2013", v: "2013" },
        { n: "2012", v: "2012" },
        { n: "2011", v: "2011" },
        { n: "2010", v: "2010" },
        { n: "2009", v: "2009" },
        { n: "2008", v: "2008" },
        { n: "2006", v: "2006" },
        { n: "2005", v: "2005" },
        { n: "2004", v: "2004" },
      ],
    },
    {
      key: "by",
      name: "排序",
      value: [
        { n: "时间", v: "time" },
        { n: "人气", v: "hits" },
        { n: "评分", v: "score" },
      ],
    },
  ],
  3: [
    {
      key: "class",
      name: "剧情",
      value: [
        { n: "全部", v: "" },
        { n: "选秀", v: "选秀" },
        { n: "情感", v: "情感" },
        { n: "访谈", v: "访谈" },
        { n: "播报", v: "播报" },
        { n: "旅游", v: "旅游" },
        { n: "音乐", v: "音乐" },
        { n: "美食", v: "美食" },
        { n: "纪实", v: "纪实" },
        { n: "曲艺", v: "曲艺" },
        { n: "生活", v: "生活" },
        { n: "游戏互动", v: "游戏互动" },
        { n: "财经", v: "财经" },
        { n: "求职", v: "求职" },
      ],
    },
    {
      key: "area",
      name: "地区",
      value: [
        { n: "全部", v: "" },
        { n: "内地", v: "内地" },
        { n: "港台", v: "港台" },
        { n: "日韩", v: "日韩" },
        { n: "欧美", v: "欧美" },
      ],
    },
    {
      key: "year",
      name: "年份",
      value: [
        { n: "全部", v: "" },
        { n: "2023", v: "2023" },
        { n: "2022", v: "2022" },
        { n: "2021", v: "2021" },
        { n: "2020", v: "2020" },
        { n: "2019", v: "2019" },
        { n: "2018", v: "2018" },
        { n: "2017", v: "2017" },
        { n: "2016", v: "2016" },
        { n: "2015", v: "2015" },
        { n: "2014", v: "2014" },
        { n: "2013", v: "2013" },
        { n: "2012", v: "2012" },
        { n: "2011", v: "2011" },
        { n: "2010", v: "2010" },
        { n: "2009", v: "2009" },
        { n: "2008", v: "2008" },
        { n: "2007", v: "2007" },
        { n: "2006", v: "2006" },
        { n: "2005", v: "2005" },
        { n: "2004", v: "2004" },
      ],
    },
    {
      key: "by",
      name: "排序",
      value: [
        { n: "时间", v: "time" },
        { n: "人气", v: "hits" },
        { n: "评分", v: "score" },
      ],
    },
  ],
  4: [
    {
      key: "class",
      name: "剧情",
      value: [
        { n: "全部", v: "" },
        { n: "情感", v: "情感" },
        { n: "科幻", v: "科幻" },
        { n: "热血", v: "热血" },
        { n: "推理", v: "推理" },
        { n: "搞笑", v: "搞笑" },
        { n: "冒险", v: "冒险" },
        { n: "萝莉", v: "萝莉" },
        { n: "校园", v: "校园" },
        { n: "动作", v: "动作" },
        { n: "机战", v: "机战" },
        { n: "运动", v: "运动" },
        { n: "战争", v: "战争" },
        { n: "少年", v: "少年" },
        { n: "少女", v: "少女" },
        { n: "社会", v: "社会" },
        { n: "原创", v: "原创" },
        { n: "亲子", v: "亲子" },
        { n: "益智", v: "益智" },
        { n: "励志", v: "励志" },
        { n: "其他", v: "其他" },
      ],
    },
    {
      key: "area",
      name: "地区",
      value: [
        { n: "全部", v: "" },
        { n: "国产", v: "国产" },
        { n: "日本", v: "日本" },
        { n: "欧美", v: "欧美" },
        { n: "其他", v: "其他" },
      ],
    },
    {
      key: "year",
      name: "年份",
      value: [
        { n: "全部", v: "" },
        { n: "2023", v: "2023" },
        { n: "2022", v: "2022" },
        { n: "2021", v: "2021" },
        { n: "2020", v: "2020" },
        { n: "2019", v: "2019" },
        { n: "2018", v: "2018" },
        { n: "2017", v: "2017" },
        { n: "2016", v: "2016" },
        { n: "2015", v: "2015" },
        { n: "2014", v: "2014" },
        { n: "2013", v: "2013" },
        { n: "2012", v: "2012" },
        { n: "2011", v: "2011" },
        { n: "2010", v: "2010" },
        { n: "2009", v: "2009" },
        { n: "2008", v: "2008" },
        { n: "2007", v: "2007" },
        { n: "2006", v: "2006" },
        { n: "2005", v: "2005" },
        { n: "2004", v: "2004" },
      ],
    },
    {
      key: "by",
      name: "排序",
      value: [
        { n: "时间", v: "time" },
        { n: "人气", v: "hits" },
        { n: "评分", v: "score" },
      ],
    },
  ],
};

function init(ext) {
  console.log("init");
}

function home(filter) {
  console.log("home");
  let classes = [];
  let cateManual = {
    电影: "1",
    剧集: "2",
    综艺: "3",
    动漫: "4",
  };
  Object.keys(cateManual).forEach((key) => {
    classes.push({
      type_id: cateManual[key],
      type_name: key,
    });
  });

  let res = {
    class: classes,
    filters: filterConfig,
  };
  let rsq = req(host, { hearders: header });
  let $ = cheerio.load(rsq.content);
  let list = [];
  $("div.ewave-vodlist__box div.lazyload").each(function (index, e) {
    let vod_id = $(this)
      .find("a")
      .attr("href")
      .match(/\/voddetail\/(\S+).html/)[1];
    let vod_name = $(this).attr("title");
    let vod_pic = $(this).attr("data-original");
    let vod_remarks = "";
    list.push({
      vod_id: vod_id,
      vod_name: vod_name,
      vod_pic: vod_pic,
      vod_remarks: vod_remarks,
    });
  });
  res["list"] = list;
  return JSON.stringify(res);
}

function homeVod(params) {}

function category(tid, pg, filter, extend) {
  console.log("category");
  if (!("id" in extend)) {
    extend["id"] = tid;
  }

  extend["page"] = pg;
  let filterParams = ["id", "area", "by", "class", "lang", "", "", "", "page", "", "", "year"];
  let params = ["", "", "", "", "", "", "", "", "", "", "", ""];

  let len = filterParams.length;
  for (let i = 0; i < len; i++) {
    if (filterParams[i] in extend) params[i] = extend[filterParams[i]];
  }

  let suffix = params.join("-");
  let url = host + `/vodshow/${suffix}.html`;
  let rsq = req(url, { hearders: header });
  let $ = cheerio.load(rsq.content);

  let list = [];
  $("div.ewave-vodlist__box div.lazyload").each(function (index, e) {
    let vod_id = $(this)
      .find("a")
      .attr("href")
      .match(/\/voddetail\/(\S+).html/)[1];
    let vod_name = $(this).attr("title");
    let vod_pic = $(this).attr("data-original");
    let vod_remarks = "";
    list.push({
      vod_id: vod_id,
      vod_name: vod_name,
      vod_pic: vod_pic,
      vod_remarks: vod_remarks,
    });
  });
  return JSON.stringify({
    page: pg,
    pagecount: 100,
    limit: 999,
    total: 99900,
    list: list,
  });
}

function detail(tid) {
  console.log("detail");
  let url = host + `/voddetail/${tid}.html`;
  let rsq = req(url, { hearders: header });
  let $ = cheerio.load(rsq.content);
  let title = $("h1.title").text();
  let pic = $("div.picture>img").attr("data-original");
  let vod = {
    vod_id: tid,
    vod_name: title,
    vod_pic: pic,
    type_name: "",
    vod_year: "",
    vod_area: "",
    vod_remarks: "",
    vod_actor: "",
    vod_director: "",
    vod_content: "",
  };

  let arrKey = { 类型: "type_name", 年份: "vod_year", 地区: "vod_area", 主演: "vod_actor", 导演: "vod_director", 简介: "vod_content" };

  $("span.text-muted").each(function (index, e) {
    let txt = $(this).text().split("：")[0];
    Object.keys(arrKey).forEach((key) => {
      if (txt.startsWith(key)) {
        vod[arrKey[key]] = $(this).get(0).nextSibling.data;
      }
    });
  });

  let playFrom = [];

  $("ul.nav-tabs li a").each(function (index, e) {
    playFrom.push($(this).text().split("（")[0]);
  });

  let vod_play_from = playFrom.join("$$$");

  let playList = [];
  $("div.tab-content ul").each(function (index, e) {
    let vodItems = [];
    $(this)
      .find("a")
      .each(function (i, el) {
        vodItems.push(
          `${$(this).text()}$${
            $(this)
              .attr("href")
              .match(/\/vodplay\/(\S+).html/)[1]
          }`
        );
      });
    playList.push(vodItems.join("#"));
  });

  let vod_play_url = playList.join("$$$");

  vod["vod_play_from"] = vod_play_from;
  vod["vod_play_url"] = vod_play_url;

  return JSON.stringify({
    list: [vod],
  });
}

function play(flag, id, flags) {
  console.log("play");
  let url = host + `/vodplay/${id}.html`;
  let rsp = req(url, { headers: header });
  let targetUrl = url;
  let res = {
    parse: "1",
    headers: "",
    playUrl: "",
    url: targetUrl,
  };
  try {
    let mJson = JSON.parse(rsp.content.match(/player_.*=(\{.+?\})</)[1]);
    rsp = req(`${host}/static/js/playerconfig.js?t=${formatDate(new Date())}`, { headers: header });
    let pcJson = JSON.parse(rsp.content.match(/player_list*=(\{.+?\}),MacPlayerConfig/)[1]);
    res["url"] = pcJson[mJson["from"]]["parse"] + mJson["url"];
  } catch {
    return JSON.stringify(res);
  }
  return JSON.stringify(res);
}

function formatDate(date) {
  console.log(date);
  var y = date.getFullYear();
  console.log(y);
  var m = date.getMonth() + 1;
  m = m < 10 ? "0" + m : m;
  var d = date.getDate();
  d = d < 10 ? "0" + d : d;
  return y + m + d + "";
}

function search(wd, quick) {
  console.log("search");
  let url = host + `/vodsearch/-------------.html`;
  let h = {
    "User-Agent": "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1",
    Referer: host,
    "content-type": "application/x-www-form-urlencoded; charset=utf-8",
  };
  let rsq = req(url, { method: "POST", body: `wd=${wd}`, headers: h });
  let $ = cheerio.load(rsq.content);

  let list = [];
  $("a.lazyload").each(function (index, e) {
    let vod_name = $(this).attr("title");
    if (!vod_name.includes(wd)) return true;
    let vod_id = $(this)
      .attr("href")
      .match(/\/voddetail\/(\S+).html/)[1];
    let vod_pic = $(this).attr("data-original");
    let vod_remarks = "";
    list.push({
      vod_id: vod_id,
      vod_name: vod_name,
      vod_pic: vod_pic,
      vod_remarks: vod_remarks,
    });
  });
  return JSON.stringify({
    list: list,
  });
}

function CBC(word, vkey, viv) {
  var key = CryptoJS.enc.Utf8.parse(vkey);
  var iv = CryptoJS.enc.Utf8.parse(viv);
  var decrypt = CryptoJS.AES.decrypt(word, key, {
    iv: iv,
    mode: CryptoJS.mode.CBC,
    padding: CryptoJS.pad.Pkcs7,
  });
  var decryptedStr = decrypt.toString(CryptoJS.enc.Utf8);
  return decryptedStr.toString();
}

// __JS_SPIDER__ = {
//   init: init,
//   home: home,
//   homeVod: homeVod,
//   category: category,
//   detail: detail,
//   play: play,
//   search: search,
// };

// 导出函数对象
export default {
  init: init,
  home: home,
  homeVod: homeVod,
  category: category,
  detail: detail,
  play: play,
  search: search,
};
