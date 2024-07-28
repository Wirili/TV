import commentjson
import requests
from bs4 import BeautifulSoup

## 需要安装 pip install commentjson

## jar包地址
## github文件加速 https://ghproxy.net

# strJar1 = "https://raw.githubusercontent.com/Wirili/TvJar/main/custom_spider.jar"
s = requests.Session()
hotel = "http://www.foodieguide.com/iptvsearch/hoteliptv.php"

rsp = s.post(
    url=hotel,
    data={
        "saerch": "广东电信",
        "Submit": "",
        "names": "Tom",
        "city": "HeZhou",
        "address": "Ca94122",
    },
    headers={
        "Host": "www.foodieguide.com",
        "Origin": "http://www.foodieguide.com",
        "Referer": "http://www.foodieguide.com/iptvsearch/hoteliptv.php",
    },
)
rsp.encoding = "utf-8"
root = BeautifulSoup(rsp.text, "lxml")
els = root.select('div[style="color:limegreen; "]')
ips = []
lines = []
lines.append("酒店组播,#genre#")
for item in els:
    ips.append(item.parent.parent.a.get_text().strip())

for item in ips:
    url = "http://www.foodieguide.com/iptvsearch/hotellist.html?s={0}&Submit=+&y=y".format(item)
    rsp = s.get(
        url,
        headers={
            "Host": "www.foodieguide.com",
            "Referer": "http://www.foodieguide.com/iptvsearch/hotellist.html?s={0}".format(
                item
            )
        },
    )
    url = "http://www.foodieguide.com/iptvsearch/alllist.php?s={0}&y=y".format(item)
    rsp = s.get(
        url,
        headers={
            "Host": "www.foodieguide.com",
            "Referer": "http://www.foodieguide.com/iptvsearch/hotellist.html?s={0}&y=false".format(
                item
            )
        },
    )
    if rsp.status_code == 200:
        root = BeautifulSoup(rsp.text, "lxml")
        els = root.select("div.m3u8")
        for i in els:
            name = i.parent.select(".channel")[0].get_text().strip()
            ip = i.get_text().strip()
            if "高清" in name:
                lines.append("{0},{1}".format(name.replace("高清", ""), ip))


print("执行成功！")
