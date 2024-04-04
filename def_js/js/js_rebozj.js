// import cheerio from "assets://js/lib/cheerio.min.js";
// import "assets://js/lib/crypto-js.js";

let host = "https://rebozj.pro";

let header = {
  "User-Agent": "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1",
  Referer: host,
};
let filterConfig = {
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
    纪录片: "3",
    动漫: "4",
    综艺: "5",
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
      .match(/\/detail\/(\S+).html/)[1];
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
      .match(/\/detail\/(\S+).html/)[1];
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
  let url = host + `/detail/${tid}.html`;
  let rsq = req(url, { hearders: header });
  let $ = cheerio.load(rsq.content);
  let title = $("h1").text();
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

  $("div.stui-vodlist__head h4").each(function (index, e) {
    playFrom.push($(this).text());
  });

  let vod_play_from = playFrom.join("$$$");

  let playList = [];
  $("ul.stui-content__playlist").each(function (index, e) {
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
    targetUrl = decodeURI(mJson["url"]);
    res["url"] = targetUrl;
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
