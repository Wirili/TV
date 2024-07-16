import commentjson
import base64
import requests

## 需要安装 pip install commentjson

## jar包地址
## github文件加速 https://ghproxy.net

jar = requests.get("https://hub.gitmirror.com/https://raw.githubusercontent.com/Wirili/TvJar/main/custom_spider.jar")
# jar = requests.get("https://hub.gitmirror.com/https://raw.githubusercontent.com/FongMi/CatVodSpider/main/jar/custom_spider.jar")

with open("custom_spider.jar", "wb") as code:
   code.write(jar.content)

md5Rsq = requests.get(
    "https://hub.gitmirror.com/https://raw.githubusercontent.com/Wirili/TvJar/main/custom_spider.jar.md5"
    # "https://hub.gitmirror.com/https://raw.githubusercontent.com/FongMi/CatVodSpider/main/jar/custom_spider.jar.md5"
)

# spider = "https://ghproxy.net/https://raw.githubusercontent.com/Wirili/TvJar/main/custom_spider.jar;md5;{0}".format(
#     md5Rsq.text.strip()
# )

# spider1 = "https://raw.githubusercontent.com/Wirili/TvJar/main/custom_spider.jar;md5;{0}".format(
#     md5Rsq.text.strip()
# )

spider = "https://hub.gitmirror.com/https://raw.githubusercontent.com/Wirili/TV/main/custom_spider.jar;md5;{0}".format(
    md5Rsq.text.strip()
)

spider1 = "https://hub.gitmirror.com/https://raw.githubusercontent.com/Wirili/TV/main/custom_spider.jar;md5;{0}".format(
    md5Rsq.text.strip()
)

with open("./zbuild/lives.json", "r", encoding="utf-8") as f:
    lives = commentjson.load(f)

with open("./zbuild/sites.json", "r", encoding="utf-8") as f:
    sites = commentjson.load(f)


with open("./zbuild/parses.json", "r", encoding="utf-8") as f:
    parses = commentjson.load(f)

with open("./zbuild/aduit_sites.json", "r", encoding="utf-8") as f:
    aduit_sites = commentjson.load(f)

with open("./zbuild/rules.json", "r", encoding="utf-8") as f:
    rules = commentjson.load(f)

with open("./zbuild/ads.json", "r", encoding="utf-8") as f:
    ads = commentjson.load(f)

# lives["lives"].append(
#     {
#         "group": "redirect",
#         "channels": [
#             {
#                 "name": "LiveX",
#                 "urls": [
#                     "proxy://do=live&type=txt&ext="
#                     + base64.b64encode(lives["lives_old"].encode("utf-8")).decode()
#                 ],
#             }
#         ],
#     }
# )

# 默认js
js = {
    "spider": spider,
    "lives": lives["lives"],
    "sites": sites["sites"],
    "parses": parses["parses"],
    "rules": rules["rules"],
    "ads": ads["ads"],
}

js1 = {
    "spider": spider1,
    "lives": lives["lives"],
    "sites": sites["sites"],
    "parses": parses["parses"],
    "rules": rules["rules"],
    "ads": ads["ads"],
}

with open("./js.json", "w", encoding="utf-8") as f:
    commentjson.dump(js, f, ensure_ascii=False, indent=2)

with open("./js1.json", "w", encoding="utf-8") as f:
    commentjson.dump(js1, f, ensure_ascii=False, indent=2)

# 默认aduit
ad = {"spider": spider, "lives": lives["lives"], "sites": aduit_sites["sites"]}

for item in sites["sites_m"]:
    ad["sites"].insert(0, item)

with open("./ad.json", "w", encoding="utf-8") as f:
    commentjson.dump(ad, f, ensure_ascii=False, indent=2)


print("执行成功！")
