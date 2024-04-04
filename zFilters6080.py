import commentjson
import requests
from lxml import etree
import urllib.parse
from bs4 import BeautifulSoup

## 需要安装 pip install commentjson
## 格式化安装 "python.formatting.provider": "yapf"

## jar包地址
## github文件加速 https://ghproxy.net

_type_class = ["1", "2", "3", "4"]

filterParams = [
            "id",
            "area",
            "by",
            "class",
            "lang",
            "",
            "",
            "",
            "page",
            "",
            "",
            "year",
        ]

_type_sub = {
    "类型": "id",
    "剧情": "class",
    "地区": "area",
    "语言": "lang",
    "时间": "year"
}
_filter = {}
for item in _type_class:
    html = BeautifulSoup(
        requests.get(
            "https://www.6080dy4.com/vodshow/{0}-----------.html".format(item)
        ).text,'lxml'
    )
    _filter[item]=[]
    for e in html.select("div.scroll-content"):
        node = e.select("a")
        if node == []:
            continue
        sub = node[0].text
        for i in _type_sub:
            if "全部" + i == sub:
                aa = {"key":_type_sub[i],"name":i}
                # _filter[item].append({"key":_type_sub[i],"name":i})
                node2 = e.select("div.library-list > a")
                if "全部"+i =="全部类型":
                    nv = [{"n":"全部","v":item}]
                else:
                    nv = [{"n":"全部","v":""}]

                for i2 in node2:
                     n = i2.text
                     v = i2["href"].split("/")[-1].split(".")[0].split("-")
                     nv.append({"n":n,"v":urllib.parse.unquote(v[filterParams.index(_type_sub[i])])})
                aa["value"]=nv
                _filter[item].append(aa)

with open("./filters.json", "w", encoding="utf-8") as f:
    # commentjson.dump(_filter, f, ensure_ascii=False, indent=2)
    commentjson.dump(_filter, f, ensure_ascii=False)
print("执行成功！")