# coding=utf-8
#!/usr/bin/python
import sys

sys.path.append("..")
from base.spider import Spider
import requests
import datetime
import json
import time
import base64
from bs4 import BeautifulSoup


class Spider(Spider):  # 元类 默认的元类 type
    host = "https://www.voflix.me"

    def getName(self):
        return "py_Voflix"

    def init(self, extend=""):
        print("============{0}============".format(extend))
        pass

    def isVideoFormat(self, url):
        pass

    def manualVideoCheck(self):
        pass

    def homeContent(self, filter):
        result = {}
        cateManual = {"电影": "1", "剧集": "2", "综艺": "3", "动漫": "4"}
        classes = []
        for k in cateManual:
            classes.append({"type_name": k, "type_id": cateManual[k]})
        result["class"] = classes
        if filter:
            result["filters"] = self.config["filter"]

        rsp = self.fetch(self.host, headers=self.header)
        root = self.html(rsp.text)
        vodList = root.xpath("//div[@class='module']/div[contains(@class,'tab-list  active')]//a")
        videos = []
        for vod in vodList:
            name = vod.xpath("./@title")[0]
            pic = vod.xpath(".//img/@data-original")[0]
            mark = vod.xpath(".//div[@class='module-item-note']/text()")[0]
            sid = vod.xpath("./@href")[0]
            sid = self.regStr(sid, "/detail/(\\S+).html")
            videos.append({"vod_id": sid, "vod_name": name, "vod_pic": pic, "vod_remarks": mark})
        result["list"] = videos
        return result

    def homeVideoContent(self):
        pass

    def categoryContent(self, tid, pg, filter, extend):
        result = {}
        if "id" not in extend.keys():
            extend["id"] = tid
        extend["page"] = pg
        filterParams = ["id", "area", "by", "class", "", "", "", "", "page", "", "", "year"]
        params = ["", "", "", "", "", "", "", "", "", "", "", ""]
        for idx in range(len(filterParams)):
            fp = filterParams[idx]
            if fp in extend.keys():
                params[idx] = extend[fp]
        suffix = "-".join(params)
        url = self.host + "/show/{0}.html".format(suffix)

        rsp = self.fetch(url, headers=self.header)
        root = self.html(rsp.text)
        vodList = root.xpath("//div[contains(@class, 'module-items')]/a")
        videos = []
        for vod in vodList:
            name = vod.xpath("./@title")[0]
            pic = vod.xpath(".//img/@data-original")[0]
            mark = vod.xpath(".//div[contains(@class,'module-item-note')]/text()")[0]
            sid = vod.xpath("./@href")[0]
            sid = self.regStr(sid,"/detail/(\\S+).html")
            videos.append({"vod_id": sid, "vod_name": name, "vod_pic": pic, "vod_remarks": mark})
        result["list"] = videos
        return result

    def detailContent(self, array):
        tid = array[0]
        url = self.host + "/detail/{0}.html".format(tid)
        rsp = self.fetch(url, headers=self.header)
        root = self.html(rsp.text)
        node = root.xpath("//div[@class='main']")[0]
        title = node.xpath(".//h1/text()")[0]
        pic = node.xpath(".//div[@class='module-item-pic']/img/@data-original")[0]
        vod = {
            "vod_id": tid,
            "vod_name": title,
            "vod_pic": pic,
            "type_name": "",
            "vod_year": "",
            "vod_area": "",
            "vod_remarks": "",
            "vod_actor": "",
            "vod_director": "",
            "vod_content": "",
        }
        vod["type_name"] = (
            "".join(node.xpath(".//div[@class='module-info-tag-link']")[2].xpath(".//text()"))
            .replace("\n", "")
            .replace("\t", "")
            .replace(" ", "")
        )
        vod["vod_year"] = (
            "".join(node.xpath(".//div[@class='module-info-tag-link']")[0].xpath(".//text()"))
            .replace("\n", "")
            .replace("\t", "")
            .replace(" ", "")
        )
        vod["vod_area"] = (
            "".join(node.xpath(".//div[@class='module-info-tag-link']")[1].xpath(".//text()"))
            .replace("\n", "")
            .replace("\t", "")
            .replace(" ", "")
        )
        infoArray = node.xpath(".//div[@class='module-info-item']")
        for info in infoArray:
            txt = info.xpath("string(.)")
            if txt[0:3] == "主演：":
                vod["vod_actor"] = (
                    txt.replace("主演：", "").replace("\n", "").replace("\t", "").replace(" ", "")
                )
            elif txt[0:3] == "导演：":
                vod["vod_director"] = (
                    txt.replace("导演：", "").replace("\n", "").replace("\t", "").replace(" ", "")
                )

        vod["vod_content"] = node.xpath(
            ".//div[@class='module-info-introduction-content']/p/text()"
        )[0]

        vod_play_from = "$$$"
        playFrom = []
        vodHeader = root.xpath(".//div[contains(@class,'module-tab-item')]/span/text()")
        for v in vodHeader:
            playFrom.append(v.strip())
        vod_play_from = vod_play_from.join(playFrom)

        vod_play_url = "$$$"
        playList = []
        vodList = root.xpath(".//div[contains(@class,'module-play-list-content')]")
        for vl in vodList:
            vodItems = []
            aList = vl.xpath("./a")
            for tA in aList:
                href = tA.xpath("./@href")[0]
                name = tA.xpath(".//span/text()")[0]
                tId = self.regStr(href, "/play/(\\S+).html")
                vodItems.append(name + "$" + tId)
            joinStr = "#"
            joinStr = joinStr.join(vodItems)
            playList.append(joinStr)
        vod_play_url = vod_play_url.join(playList)

        vod["vod_play_from"] = vod_play_from
        vod["vod_play_url"] = vod_play_url

        result = {"list": [vod]}
        return result

    def searchContent(self, key, quick):
        url = self.host + "/index.php/ajax/suggest?mid=1&wd={0}".format(key)
        rsp = self.fetch(url, headers=self.header)
        jo = json.loads(rsp.text)
        vodList = jo["list"]
        videos = []
        for vod in vodList:
            name = vod["name"]
            pic = vod["pic"]
            mark = ""
            sid = vod["id"]
            videos.append({"vod_id": sid, "vod_name": name, "vod_pic": pic, "vod_remarks": mark})
        result = {"list": videos}
        return result

    def playerContent(self, flag, id, vipFlags):
        result = {}
        url = self.host + "/play/{0}.html".format(id)
        rsp = self.fetch(url, headers=self.header)

        try:
            mJson = json.loads(self.regStr(rsp.text, "player_.*=(\{.+?\})<"))
            if mJson["url"].startswith("http"):
                targetUrl = mJson["url"]
                result["parse"] = 0
            else:
                rsp = self.fetch(
                    self.host
                    + "/static/player/{0}.js?v={1}".format(
                        mJson["from"], datetime.date.today().strftime("%y%M%d")
                    )
                )
                pcUrl = self.regStr(rsp.text, "src=\"(.*?)'.*MacPlayer")
                targetUrl = pcUrl + mJson["url"]

                rsp = self.fetch(targetUrl, headers=self.header)
                token = self.regStr(rsp.text, '"token":.*"(.*?)"')
                if token:
                    rspset = self.fetch(
                        pcUrl.split("/?")[0]
                        + "/js/setting.js?t={0}".format(datetime.date.today().strftime("%Y%M%d"))
                    )
                    urlset = self.regStr(rspset.text, "url:.*'(.*?)'.*,")
                    sign = self.regStr(rspset.text, "sign:.*'(.*?)'.*")
                    params = {
                        "tm": int(round(time.time() * 1000)),
                        "url": self.regStr(rsp.text, '"url":.*"(.*?)"'),
                        "vkey": self.regStr(rsp.text, '"vkey":.*"(.*?)"'),
                        "token": self.regStr(rsp.text, '"token":.*"(.*?)"'),
                        "sign": sign,
                    }
                    rsp = requests.get(pcUrl.split("/?")[0] + "/" + urlset, params)
                    targetUrl = json.loads(rsp.text)["url"]
                    targetUrl = str(base64.b64decode(targetUrl[8:])[8:-8], encoding="utf8")
                    result["parse"] = "0"
        except:
            targetUrl = url
            result["parse"] = 1
            pass

        result["playUrl"] = ""
        result["url"] = targetUrl
        result["header"] = ""
        return result

    header = {"referer": host}

    config = {
        "player": {},
        "filter": {
            "1": [
                {
                    "key": "id",
                    "name": "类型",
                    "value": [
                        {"n": "全部", "v": "1"},
                        {"n": "动作", "v": "6"},
                        {"n": "喜剧", "v": "7"},
                        {"n": "爱情", "v": "8"},
                        {"n": "科幻", "v": "9"},
                        {"n": "恐怖", "v": "10"},
                        {"n": "剧情", "v": "11"},
                        {"n": "战争", "v": "12"},
                        {"n": "动画", "v": "23"},
                    ],
                },
                {
                    "key": "class",
                    "name": "剧情",
                    "value": [
                        {"n": "全部", "v": ""},
                        {"n": "喜剧", "v": "喜剧"},
                        {"n": "爱情", "v": "爱情"},
                        {"n": "恐怖", "v": "恐怖"},
                        {"n": "动作", "v": "动作"},
                        {"n": "科幻", "v": "科幻"},
                        {"n": "剧情", "v": "剧情"},
                        {"n": "战争", "v": "战争"},
                        {"n": "警匪", "v": "警匪"},
                        {"n": "犯罪", "v": "犯罪"},
                        {"n": "动画", "v": "动画"},
                        {"n": "奇幻", "v": "奇幻"},
                        {"n": "武侠", "v": "武侠"},
                        {"n": "冒险", "v": "冒险"},
                        {"n": "枪战", "v": "枪战"},
                        {"n": "恐怖", "v": "恐怖"},
                        {"n": "悬疑", "v": "悬疑"},
                        {"n": "惊悚", "v": "惊悚"},
                        {"n": "经典", "v": "经典"},
                        {"n": "青春", "v": "青春"},
                        {"n": "文艺", "v": "文艺"},
                        {"n": "微电影", "v": "微电影"},
                        {"n": "古装", "v": "古装"},
                        {"n": "历史", "v": "历史"},
                        {"n": "运动", "v": "运动"},
                        {"n": "农村", "v": "农村"},
                        {"n": "儿童", "v": "儿童"},
                        {"n": "网络电影", "v": "网络电影"},
                    ],
                },
                {
                    "key": "area",
                    "name": "地区",
                    "value": [
                        {"n": "全部", "v": ""},
                        {"n": "中国大陆", "v": "中国大陆"},
                        {"n": "中国香港", "v": "中国香港"},
                        {"n": "中国台湾", "v": "中国台湾"},
                        {"n": "美国", "v": "美国"},
                        {"n": "法国", "v": "法国"},
                        {"n": "英国", "v": "英国"},
                        {"n": "日本", "v": "日本"},
                        {"n": "韩国", "v": "韩国"},
                        {"n": "德国", "v": "德国"},
                        {"n": "泰国", "v": "泰国"},
                        {"n": "印度", "v": "印度"},
                        {"n": "意大利", "v": "意大利"},
                        {"n": "西班牙", "v": "西班牙"},
                        {"n": "加拿大", "v": "加拿大"},
                        {"n": "其他", "v": "其他"},
                    ],
                },
                {
                    "key": "year",
                    "name": "年份",
                    "value": [
                        {"n": "全部", "v": ""},
                        {"n": "2022", "v": "2022"},
                        {"n": "2021", "v": "2021"},
                        {"n": "2020", "v": "2020"},
                        {"n": "2019", "v": "2019"},
                        {"n": "2018", "v": "2018"},
                        {"n": "2017", "v": "2017"},
                        {"n": "2016", "v": "2016"},
                        {"n": "2015", "v": "2015"},
                        {"n": "2014", "v": "2014"},
                        {"n": "2013", "v": "2013"},
                        {"n": "2012", "v": "2012"},
                        {"n": "2011", "v": "2011"},
                        {"n": "2010", "v": "2010"},
                    ],
                },
                {
                    "key": "by",
                    "name": "排序",
                    "value": [
                        {"n": "最新", "v": "time"},
                        {"n": "最热", "v": "hits"},
                        {"n": "评分", "v": "score"},
                    ],
                },
            ],
            "2": [
                {
                    "key": "id",
                    "name": "类型",
                    "value": [
                        {"n": "全部", "v": "2"},
                        {"n": "国产剧", "v": "13"},
                        {"n": "港台剧", "v": "14"},
                        {"n": "日韩剧", "v": "15"},
                        {"n": "欧美剧", "v": "16"},
                        {"n": "纪 录片", "v": "21"},
                        {"n": "泰国剧", "v": "24"},
                    ],
                },
                {
                    "key": "class",
                    "name": "剧情",
                    "value": [
                        {"n": "全部", "v": ""},
                        {"n": "古装", "v": "古装"},
                        {"n": "战争", "v": "战争"},
                        {"n": "青春偶像", "v": "青春偶像"},
                        {"n": "喜剧", "v": "喜剧"},
                        {"n": "家庭", "v": "家庭"},
                        {"n": "犯罪", "v": "犯罪"},
                        {"n": "动作", "v": "动作"},
                        {"n": "奇幻", "v": "奇幻"},
                        {"n": "剧情", "v": "剧情"},
                        {"n": "历史", "v": "历史"},
                        {"n": "经典", "v": "经典"},
                        {"n": "乡村", "v": "乡村"},
                        {"n": "情景", "v": "情景"},
                        {"n": "商战", "v": "商战"},
                        {"n": "网剧", "v": "网剧"},
                        {"n": "其他", "v": "其他"},
                    ],
                },
                {
                    "key": "area",
                    "name": "地区",
                    "value": [
                        {"n": "全部", "v": ""},
                        {"n": "中国大陆", "v": "中国大陆"},
                        {"n": "中国台湾", "v": "中国台湾"},
                        {"n": "中国香港", "v": "中国香港"},
                        {"n": "韩国", "v": "韩国"},
                        {"n": "日本", "v": "日本"},
                        {"n": "美国", "v": "美国"},
                        {"n": "泰国", "v": "泰国"},
                        {"n": "英国", "v": "英国"},
                        {"n": "新加坡", "v": "新加坡"},
                        {"n": "其他", "v": "其他"},
                    ],
                },
                {
                    "key": "year",
                    "name": "年份",
                    "value": [
                        {"n": "全部", "v": ""},
                        {"n": "2022", "v": "2022"},
                        {"n": "2021", "v": "2021"},
                        {"n": "2020", "v": "2020"},
                        {"n": "2019", "v": "2019"},
                        {"n": "2018", "v": "2018"},
                        {"n": "2017", "v": "2017"},
                        {"n": "2016", "v": "2016"},
                        {"n": "2015", "v": "2015"},
                        {"n": "2014", "v": "2014"},
                        {"n": "2013", "v": "2013"},
                        {"n": "2012", "v": "2012"},
                        {"n": "2011", "v": "2011"},
                        {"n": "2010", "v": "2010"},
                        {"n": "2009", "v": "2009"},
                        {"n": "2008", "v": "2008"},
                        {"n": "2006", "v": "2006"},
                        {"n": "2005", "v": "2005"},
                        {"n": "2004", "v": "2004"},
                    ],
                },
                {
                    "key": "by",
                    "name": "排序",
                    "value": [
                        {"n": "最新", "v": "time"},
                        {"n": "最热", "v": "hits"},
                        {"n": "评分", "v": "score"},
                    ],
                },
            ],
            "3": [
                {
                    "key": "class",
                    "name": "剧情",
                    "value": [
                        {"n": "全部", "v": ""},
                        {"n": "选秀", "v": "选秀"},
                        {"n": "情感", "v": "情感"},
                        {"n": "访谈", "v": "访谈"},
                        {"n": "播报", "v": "播报"},
                        {"n": "旅游", "v": "旅游"},
                        {"n": "音乐", "v": "音乐"},
                        {"n": "美食", "v": "美食"},
                        {"n": "纪实", "v": "纪实"},
                        {"n": "曲艺", "v": "曲艺"},
                        {"n": "生活", "v": "生活"},
                        {"n": "游戏互动", "v": "游戏互动"},
                        {"n": "财经", "v": "财经"},
                        {"n": "求职", "v": "求职"},
                    ],
                },
                {
                    "key": "area",
                    "name": "地区",
                    "value": [
                        {"n": "全部", "v": ""},
                        {"n": "内地", "v": "内地"},
                        {"n": "港台", "v": "港台"},
                        {"n": "日韩", "v": "日韩"},
                        {"n": "欧美", "v": "欧美"},
                    ],
                },
                {
                    "key": "year",
                    "name": "年份",
                    "value": [
                        {"n": "全部", "v": ""},
                        {"n": "2022", "v": "2022"},
                        {"n": "2021", "v": "2021"},
                        {"n": "2020", "v": "2020"},
                        {"n": "2019", "v": "2019"},
                        {"n": "2018", "v": "2018"},
                        {"n": "2017", "v": "2017"},
                        {"n": "2016", "v": "2016"},
                        {"n": "2015", "v": "2015"},
                        {"n": "2014", "v": "2014"},
                        {"n": "2013", "v": "2013"},
                        {"n": "2012", "v": "2012"},
                        {"n": "2011", "v": "2011"},
                        {"n": "2010", "v": "2010"},
                        {"n": "2009", "v": "2009"},
                        {"n": "2008", "v": "2008"},
                        {"n": "2007", "v": "2007"},
                        {"n": "2006", "v": "2006"},
                        {"n": "2005", "v": "2005"},
                        {"n": "2004", "v": "2004"},
                    ],
                },
                {
                    "key": "by",
                    "name": "排序",
                    "value": [
                        {"n": "最新", "v": "time"},
                        {"n": "最热", "v": "hits"},
                        {"n": "评分", "v": "score"},
                    ],
                },
            ],
            "4": [
                {
                    "key": "class",
                    "name": "剧情",
                    "value": [
                        {"n": "全部", "v": ""},
                        {"n": "情感", "v": "情感"},
                        {"n": "科幻", "v": "科幻"},
                        {"n": "热血", "v": "热血"},
                        {"n": "推理", "v": "推理"},
                        {"n": "搞笑", "v": "搞笑"},
                        {"n": "冒险", "v": " 冒险"},
                        {"n": "萝莉", "v": "萝莉"},
                        {"n": "校园", "v": "校园"},
                        {"n": "动作", "v": "动作"},
                        {"n": "机战", "v": "机战"},
                        {"n": "运动", "v": "运动"},
                        {"n": "战争", "v": "战争"},
                        {"n": "少年", "v": "少年"},
                        {"n": "少女", "v": "少女"},
                        {"n": " 社会", "v": "社会"},
                        {"n": "原创", "v": "原创"},
                        {"n": "亲子", "v": "亲子"},
                        {"n": "益智", "v": "益智"},
                        {"n": "励志", "v": "励志"},
                        {"n": "其他", "v": "其他"},
                    ],
                },
                {
                    "key": "area",
                    "name": "地区",
                    "value": [
                        {"n": "全部", "v": ""},
                        {"n": "中国", "v": "中国"},
                        {"n": "日本", "v": "日本"},
                        {"n": "欧美", "v": "欧美"},
                        {"n": "其他", "v": "其他"},
                    ],
                },
                {
                    "key": "year",
                    "name": "年份",
                    "value": [
                        {"n": "全部", "v": ""},
                        {"n": "2022", "v": "2022"},
                        {"n": "2021", "v": "2021"},
                        {"n": "2020", "v": "2020"},
                        {"n": "2019", "v": "2019"},
                        {"n": "2018", "v": "2018"},
                        {"n": "2017", "v": "2017"},
                        {"n": "2016", "v": "2016"},
                        {"n": "2015", "v": "2015"},
                        {"n": "2014", "v": "2014"},
                        {"n": "2013", "v": "2013"},
                        {"n": "2012", "v": "2012"},
                        {"n": "2011", "v": "2011"},
                        {"n": "2010", "v": "2010"},
                        {"n": "2009", "v": "2009"},
                        {"n": "2008", "v": "2008"},
                        {"n": "2007", "v": "2007"},
                        {"n": "2006", "v": "2006"},
                        {"n": "2005", "v": "2005"},
                        {"n": "2004", "v": "2004"},
                    ],
                },
                {
                    "key": "by",
                    "name": "排序",
                    "value": [
                        {"n": "最新", "v": "time"},
                        {"n": "最热", "v": "hits"},
                        {"n": "评分", "v": "score"},
                    ],
                },
            ],
        },
    }

    def localProxy(self, param):
        action = {}
        return [200, "video/MP2T", action, ""]
