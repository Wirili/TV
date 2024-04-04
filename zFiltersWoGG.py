import commentjson
import requests
from lxml import etree
import urllib.parse
from bs4 import BeautifulSoup

## 需要安装 pip install commentjson
## 格式化安装 "python.formatting.provider": "yapf"

## jar包地址
## github文件加速 https://ghproxy.net

_type_class = ["1", "20", "24", "28", "37", "38", "32"]
_type_sub = {
    "类型": 0,
    "剧情": 3,
    "地区": 1,
    "语言": 4,
    "时间": 11,
    "排序": 2,
}
_filter = {}
for item in _type_class:
    html = etree.HTML(
        requests.get(
            "https://tvfan.xxooo.cf/index.php/vodshow/{0}-----------.html".format(item)
        ).text
    )
    _filter[item]=[]
    for e in html.xpath("//div[@class='scroll-content']"):
        node = e.xpath("./a/text()")

        if node == []:
            continue
        sub = node[0].strip()
        for i in _type_sub:
            if "全部" + i == sub:
                aa = {"key":_type_sub[i],"name":i}
                # _filter[item].append({"key":_type_sub[i],"name":i})
                node2 = e.xpath("./div[@class='library-list']/a")
                if "全部"+i =="全部类型":
                    nv = [{"n":"全部","v":item}]
                else:
                    nv = [{"n":"全部","v":""}]

                for i2 in node2:
                     n = i2.xpath("./text()")[0]
                     v = i2.xpath("./@href")[0].split("/")[-1].split(".")[0].split("-")
                     nv.append({"n":n,"v":urllib.parse.unquote(v[_type_sub[i]])})
                aa["value"]=nv
                _filter[item].append(aa)

with open("./filters.json", "w", encoding="utf-8") as f:
    # commentjson.dump(_filter, f, ensure_ascii=False, indent=2)
    commentjson.dump(_filter, f, ensure_ascii=False)
print("执行成功！")