import commentjson
import base64
import requests
import hashlib

## 需要安装 pip install commentjson

## jar包地址
## github文件加速 https://ghproxy.net

# strJar1 = "https://raw.githubusercontent.com/Wirili/TvJar/main/custom_spider.jar"
strJar1 = "https://raw.githubusercontent.com/FongMi/CatVodSpider/main/jar/custom_spider.jar"
# strJar = "https://hub.gitmirror.com/" + strJar1
proxy = "https://gh-proxy.com/"
strJar = proxy + strJar1
# strJar = "https://ghgo.xyz/" + strJar1
# strJar = "https://gh.idayer.com/" + strJar1
# strJar = "https://ghproxy.cc/" + strJar1


jar = requests.get(strJar)
# jar = requests.get("https://hub.gitmirror.com/https://raw.githubusercontent.com/FongMi/CatVodSpider/main/jar/custom_spider.jar")

with open("custom_spider.txt", "wb") as code:
    code.write(jar.content)

md5txt = hashlib.new('md5', jar.content).hexdigest()

spider = "https://cdn.jsdelivr.net/gh/Wirili/TV@main/custom_spider.txt" + ";md5;{0}".format(md5txt)

spider1 = strJar1 + ";md5;{0}".format(md5txt)

with open("./zbuild/lives.json", "r", encoding="utf-8") as f:
    lives = commentjson.loads(f.read().replace("*代理*",proxy))

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

with open("./jscdn.json", "w", encoding="utf-8") as f:
    commentjson.dump(js, f, ensure_ascii=False, indent=2)

with open("./js1.json", "w", encoding="utf-8") as f:
    commentjson.dump(js1, f, ensure_ascii=False, indent=2)

# 默认aduit
ad = {"spider": spider, "lives": lives["lives"], "sites": aduit_sites["sites"]}

for item in sites["sites_m"]:
    ad["sites"].insert(0, item)

with open("./adcdn.json", "w", encoding="utf-8") as f:
    commentjson.dump(ad, f, ensure_ascii=False, indent=2)


print("执行成功！")
