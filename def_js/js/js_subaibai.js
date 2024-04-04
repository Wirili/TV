// import cheerio from "../libs/cheerio.min.js";
import cheerio from "assets://js/lib/cheerio.min.js";
import "assets://js/lib/crypto-js.js";

let host = "https://www.subaibaiys.com";

let header = {
  "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36 Edg/113.0.1774.50",
  Referer: host,
};

function init(ext) {
  console.log("init");
}

function home(filter) {
  console.log("home");
  let classes = [];
  let cateManual = {
    电影: "new-movie",
    电视剧: "tv-drama",
    热门电影: "hot-month",
    高分电影: "high-movie",
    动漫电影: "cartoon-movie",
    香港电影: "hongkong-movie",
    国产剧: "domestic-drama",
    欧美剧: "american-drama",
    日韩剧: "korean-drama",
    动漫剧: "anime-drama",
  };
  Object.keys(cateManual).forEach((key) => {
    classes.push({
      type_id: cateManual[key],
      type_name: key,
    });
  });

  let res = {
    class: classes,
  };
  let rsq = req(host, { hearders: header });
  let $ = cheerio.load(rsq.content);
  let list = [];
  $("div.mi_ne_kd li").each(function (index, e) {
    let vod_id = $(this)
      .find("a")
      .attr("href")
      .match(/\/movie\/(\S+).html/)[1];
    let vod_name = $(this).find("h3 a").text();
    let vod_pic = $(this).find("a > img").attr("data-original");
    let vod_remarks = $(this).find("div.jidi>span").text();
    if (vod_remarks == "") vod_remarks = $(this).find("div.hdinfo>span").text();
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
  let url = host + `/${tid}/page/${pg}`;
  let rsq = req(url, { hearders: header });
  let $ = cheerio.load(rsq.content);

  let list = [];
  $("div.mi_ne_kd li").each(function (index, e) {
    let vod_id = $(this)
      .find("a")
      .attr("href")
      .match(/\/movie\/(\S+).html/)[1];
    let vod_name = $(this).find("h3 a").text();
    let vod_pic = $(this).find("a > img").attr("data-original");
    let vod_remarks = $(this).find("div.jidi>span").text();
    if (vod_remarks == "") vod_remarks = $(this).find("div.hdinfo>span").text();
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
  let url = host + `/movie/${tid}.html`;
  let rsq = req(url, { hearders: header });
  let $ = cheerio.load(rsq.content);
  let title = $("div.moviedteail_tt h1").text();
  let pic = $("div.dyimg>img").attr("src");
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

  $("ul.moviedteail_list>li").each(function (index, e) {
    let txt = $(this).text();
    Object.keys(arrKey).forEach((key) => {
      if (txt.startsWith(key)) {
        let val = [];
        $(this)
          .find("*")
          .each(function (index, e) {
            val.push($(this).text());
          });
        vod[arrKey[key]] = val.join(",");
      }
    });
  });

  vod["vod_content"] = $("div.yp_context>p").text();

  let playFrom = [];

  $("div.mi_paly_box>div>div.ypxingq_t").each(function (index, e) {
    playFrom.push($(this).contents()[0].data);
  });

  let vod_play_from = playFrom.join("$$$");

  let playList = [];
  $("div.mi_paly_box div.paly_list_btn").each(function (index, e) {
    let vodItems = [];
    $(this)
      .find("a")
      .each(function (i, el) {
        vodItems.push(
          `${$(this).text()}$${
            $(this)
              .attr("href")
              .match(/\/v_play\/(\S+).html/)[1]
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
  let url = host + `/v_play/${id}.html`;
  let rsq = req(url, { hearders: header });
  let targetUrl = url;
  let res = {
    parse: "1",
    headers: "",
    playUrl: "",
    url: targetUrl,
  };
  try {
    let m = rsq.content.match(/var.*="(\S+)?";.*parse\("(\S+)"\).*parse\((\S+)\)/);
    if (m != null) {
      let enstr = m[1];
      let key = m[2];
      let iv = m[3];
      targetUrl = CBC(enstr, key, iv).match(/url: \"(\S+?)\"/)[1];
      res["parse"] = "0";
      res["url"] = targetUrl;
    }
  } catch {
    return JSON.stringify(res);
  }
  return JSON.stringify(res);
}

function search(wd, quick) {
  console.log("search");
  let url = host + `/search?q=${wd}`;
  let rsq = req(url, { headers: header });
  let $ = cheerio.load(rsq.content);

  let list = [];
  $("div.mi_ne_kd ul li").each(function (index, e) {
    let vod_name = $(this).find("h3 a").text();
    if (!vod_name.includes(wd)) return true;
    let vod_pic = $(this).find("a > img").attr("data-original");
    let vod_remarks = $(this).find("div.jidi>span").text();
    let vod_id = $(this)
      .find("a")
      .attr("href")
      .match(/\/movie\/(\S+).html/)[1];
    if (vod_remarks == "") vod_remarks = $(this).find("div.hdinfo>span").text();
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
