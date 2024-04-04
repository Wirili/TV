import cheerio from "assets://js/lib/cheerio.min.js";
import "assets://js/lib/crypto-js.js";

let host = "https://www.smdyy.cc";

let header = {
  "User-Agent": "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1",
  Referer: host,
};
let filterConfig = {
  1: [
    {
      key: "id",
      name: "类型",
      value: [
        { n: "全部", v: "1" },
        { n: "动作片", v: "6" },
        { n: "喜剧片", v: "7" },
        { n: "爱情片", v: "8" },
        { n: "科幻片", v: "9" },
        { n: "恐怖片", v: "10" },
        { n: "剧情片", v: "11" },
        { n: "战争片", v: "12" },
        { n: "犯罪片", v: "20" },
        { n: "惊悚片", v: "21" },
        { n: "冒险片", v: "22" },
        { n: "悬疑片", v: "23" },
        { n: "武侠片", v: "24" },
        { n: "奇幻片", v: "25" },
        { n: "纪录片", v: "26" },
        { n: "动画片", v: "27" },
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
      key: "lang",
      name: "语言",
      value: [
        { n: "全部", v: "" },
        { n: "国语", v: "国语" },
        { n: "英语", v: "英语" },
        { n: "粤语", v: "粤语" },
        { n: "闽南语", v: "闽南语" },
        { n: "韩语", v: "韩语" },
        { n: "日语", v: "日语" },
        { n: "法语", v: "法语" },
        { n: "德语", v: "德语" },
        { n: "其它", v: "其它" },
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
      key: "id",
      name: "类型",
      value: [
        { n: "全部", v: "2" },
        { n: "国产剧", v: "13" },
        { n: "港台剧", v: "14" },
        { n: "日韩剧", v: "15" },
        { n: "欧美剧", v: "16" },
        { n: "泰国剧", v: "28" },
      ],
    },
    {
      key: "area",
      name: "地区",
      value: [
        { n: "全部", v: "" },
        { n: "大陆", v: "大陆" },
        { n: "香港", v: "香港" },
        { n: "韩国", v: "韩国" },
        { n: "美国", v: "美国" },
        { n: "日本", v: "日本" },
        { n: "法国", v: "法国" },
        { n: "英国", v: "英国" },
        { n: "德国", v: "德国" },
        { n: "台湾", v: "台湾" },
        { n: "泰国", v: "泰国" },
        { n: "印度", v: "印度" },
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
      key: "lang",
      name: "语言",
      value: [
        { n: "全部", v: "" },
        { n: "国语", v: "国语" },
        { n: "英语", v: "英语" },
        { n: "粤语", v: "粤语" },
        { n: "闽南语", v: "闽南语" },
        { n: "韩语", v: "韩语" },
        { n: "日语", v: "日语" },
        { n: "其它", v: "其它" },
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
      ],
    },
    {
      key: "lang",
      name: "语言",
      value: [
        { n: "全部", v: "" },
        { n: "国语", v: "国语" },
        { n: "英语", v: "英语" },
        { n: "粤语", v: "粤语" },
        { n: "闽南语", v: "闽南语" },
        { n: "韩语", v: "韩语" },
        { n: "日语", v: "日语" },
        { n: "其它", v: "其它" },
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
      key: "area",
      name: "地区",
      value: [
        { n: "全部", v: "" },
        { n: "内地", v: "+内地" },
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
      ],
    },
    {
      key: "lang",
      name: "语言",
      value: [
        { n: "全部", v: "" },
        { n: "国语", v: "国语" },
        { n: "英语", v: "英语" },
        { n: "粤语", v: "粤语" },
        { n: "闽南语", v: "闽南语" },
        { n: "韩语", v: "韩语" },
        { n: "日语", v: "日语" },
        { n: "其它", v: "其它" },
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
  15: [
    {
      key: "area",
      name: "地区",
      value: [
        { n: "全部", v: "" },
        { n: "日本", v: "日本" },
        { n: "韩国", v: "韩国" },
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
      key: "lang",
      name: "语言",
      value: [
        { n: "全部", v: "" },
        { n: "韩语", v: "韩语" },
        { n: "日语", v: "日语" },
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
  16: [
    {
      key: "area",
      name: "地区",
      value: [
        { n: "全部", v: "" },
        { n: "美国", v: "美国" },
        { n: "法国", v: "法国" },
        { n: "英国", v: "英国" },
        { n: "德国", v: "德国" },
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
      key: "lang",
      name: "语言",
      value: [
        { n: "全部", v: "" },
        { n: "英语", v: "英语" },
        { n: "法语", v: "法语" },
        { n: "德语", v: "德语" },
        { n: "其它", v: "其它" },
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
    电视剧: "2",
    综艺: "3",
    动漫: "4",
    日韩剧: "15",
    欧美剧: "16",
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
  $("ul.stui-vodlist:not(:last-child) a.stui-vodlist__thumb").each(function (index, e) {
    let vod_id = $(this)
      .attr("href")
      .match(/\/kan\/(\S+).html/)[1];
    let vod_name = $(this).attr("title");
    let vod_pic = $(this).attr("data-original");
    let vod_remarks = $(this).find("span.pic-text").text();
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
  let url = host + `/show/${suffix}.html`;
  let rsq = req(url, { hearders: header });
  let $ = cheerio.load(rsq.content);

  let list = [];
  $("ul.stui-vodlist a.stui-vodlist__thumb").each(function (index, e) {
    let vod_id = $(this)
      .attr("href")
      .match(/\/kan\/(\S+).html/)[1];
    let vod_name = $(this).attr("title");
    let vod_pic = $(this).attr("data-original");
    let vod_remarks = $(this).find("span.pic-text").text();
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
  let url = host + `/kan/${tid}.html`;
  let rsq = req(url, { hearders: header });
  let $ = cheerio.load(rsq.content);
  let title = $("h1.title").text();
  let pic = $("div.stui-content__thumb img").attr("data-original");
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

  let arrKey = { 类型: "type_name", 年份: "vod_year", 地区: "vod_area", 主演: "vod_actor", 导演: "vod_director" };

  $("p.data").each(function (index, e) {
    let txt = $(this).text();
    Object.keys(arrKey).forEach((key) => {
      let reg = new RegExp(`${key}：([\\s\\S]*)`);
      if (txt.match(reg)) {
        vod[arrKey[key]] = txt.match(reg)[1].split("/")[0].trim();
      }
    });
  });

  vod["vod_content"] = $("span.detail-content").text();

  let playFrom = [];

  $("div.stui-pannel h3").each(function (index, e) {
    playFrom.push($(this).text());
  });

  let vod_play_from = playFrom.join("$$$");

  let playList = [];
  $("div.stui-pannel ul").each(function (index, e) {
    let vodItems = [];
    $(this)
      .find("a")
      .each(function (i, el) {
        vodItems.push(
          `${$(this).text()}$${
            $(this)
              .attr("href")
              .match(/\/play\/(\S+).html/)[1]
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
  let url = host + `/play/${id}.html`;
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
    targetUrl = pcJson[mJson["from"]]["parse"] + mJson["url"];

    rsp = req(targetUrl, { headers: header });
    let params = `url=${rsp.content.match(/"url":.*"(.*?)"/)[1]}&vkey=${rsp.content.match(/"vkey":.*"(.*?)"/)[1]}&token=${rsp.content.match(/"token":.*"(.*?)"/)[1]}&sign=smdyycc`;
    let nheader = {
      Accept: "application/json, text/javascript, */*; q=0.01",
      "Content-Type": "application/x-www-form-urlencoded; charset=UTF-8",
      "User-Agent": "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1",
      Orgin: pcJson[mJson["from"]]["parse"].split("/player/")[0],
    };
    rsp = req(pcJson[mJson["from"]]["parse"].slice(0, pcJson[mJson["from"]]["parse"].lastIndexOf("/")) + "/xinapi.php", {
      method: "POST",
      body: params,
      headers: nheader,
    });
    let enstr = JSON.parse(rsp.content)["url"].substring(8);
    res["url"] = CryptoJS.enc.Utf8.stringify(CryptoJS.enc.Base64.parse(enstr)).slice(8, -8);
    res["parse"] = "0";
  } catch {
    return JSON.stringify(res);
  }
  return JSON.stringify(res);
}

function formatDate(date) {
  var y = date.getFullYear();
  var m = date.getMonth() + 1;
  m = m < 10 ? "0" + m : m;
  var d = date.getDate();
  d = d < 10 ? "0" + d : d;
  return y + m + d + "";
}

function search(wd, quick) {
  console.log("search");
  let url = host + `/index.php/ajax/suggest?mid=1&wd=${wd}`;
  let rsq = req(url, { headers: header });
  let jo = JSON.parse(rsq.content);
  let vodList = jo["list"];
  let list = [];
  vodList.forEach((e) => {
    let vod_name = e["name"];
    if (!vod_name.includes(wd)) return true;
    list.push({
      vod_id: e["id"],
      vod_name: vod_name,
      vod_pic: e["pic"],
      vod_remarks: "",
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
