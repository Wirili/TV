// import cheerio from "../libs/cheerio.min.js";
import cheerio from "assets://js/lib/cheerio.min.js";
import "assets://js/lib/crypto-js.js";

let host = "https://www.ziziys.com";

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
    电影: "1",
    国产剧: "13",
    动漫: "3",
    美剧: "14",
    日韩剧: "15",
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
  $("div.module.module-wrapper div.module-item").each(function (index, e) {
    let vod_id = $(this)
      .find("a")
      .attr("href")
      .match(/\/vdetail\/(\S+).html/)[1];
    let vod_name = $(this).find("img").attr("alt");
    let vod_pic = $(this).find("img").attr("data-src");
    let vod_remarks = $(this).find("div.module-item-text").text();
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
  let url = host + `/list/${tid}-${pg}.html`;
  let rsq = req(url, { hearders: header });
  let $ = cheerio.load(rsq.content);

  let list = [];
  $("div.module-item").each(function (index, e) {
    let vod_id = $(this)
      .find("a")
      .attr("href")
      .match(/\/vdetail\/(\S+).html/)[1];
    let vod_name = $(this).find("img").attr("alt");
    let vod_pic = $(this).find("img").attr("data-src");
    let vod_remarks = $(this).find("div.module-item-text").text();
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
  let url = host + `/vdetail/${tid}.html`;
  let rsq = req(url, { hearders: header });
  let $ = cheerio.load(rsq.content);
  let title = $("h1").text();
  let pic = $("div.video-cover img").attr("data-src");
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

  let els = $(".tag-link");
  vod["type_name"] = $(els[1]).text().replace(/\s/g, "");
  vod["vod_year"] = $(els[2]).text().replace(/\s/g, "");
  vod["vod_area"] = $(els[3]).text().replace(/\s/g, "");

  let arrKey = {主演: "vod_actor", 导演: "vod_director" };

  $("div.video-info-items").each(function (index, e) {
    let txt = $(this).text();
    Object.keys(arrKey).forEach((key) => {
      if (txt.startsWith(key)) {
        let val = [];
        $(this)
          .find("*")
          .each(function (index, e) {
            val.push($(this).text().replace(/\s/g, ""));
          });
        vod[arrKey[key]] = val.join(",");
      }
    });
  });

  vod["vod_content"] = $("div.vod_content span").text().replace(/\s/g, "");

  let playFrom = [];

  $("div.tab-item span").each(function (index, e) {
    playFrom.push($(this).text());
  });

  let vod_play_from = playFrom.join("$$$");

  let playList = [];
  $("div.tab-list div.sort-item").each(function (index, e) {
    let vodItems = [];
    $(this)
      .find("a")
      .each(function (i, el) {
        vodItems.push(
          `${$(this).text()}$${
            $(this)
              .attr("href")
              .match(/\/video\/(\S+).html/)[1]
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
  let url = host + `/video/${id}.html`;
  let rsq = req(url, { hearders: header });
  let targetUrl = url;
  let res = {
    parse: "1",
    headers: "",
    playUrl: "",
    url: targetUrl,
  };
  try {
    let m = rsq.content.match(/player_.*=(\{.+?\})</);
    if (m != null) {
      let mJson = JSON.parse(m[1]);
      targetUrl = mJson["url"];
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
  let url = host + `/vsearch/${wd}--.html`;
  let rsq = req(url, { headers: header });
  let $ = cheerio.load(rsq.content);

  let list = [];
  $("div.module-search-item").each(function (index, e) {
    let vod_name = $(this).find("img").attr("alt");
    if (!vod_name.includes(wd)) return true;
    let vod_pic = $(this).find("img").attr("data-src");
    let vod_remarks = $(this).find("a.video-serial").text();
    let vod_id = $(this)
      .find("div.video-info-header>a")
      .attr("href")
      .match(/\/vdetail\/(\S+).html/)[1];
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
