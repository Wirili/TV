import commentjson
import base64
import requests
import re

## 需要安装 pip install commentjson
## 格式化安装 "python.formatting.provider": "yapf"


## jar包地址
## github文件加速 https://ghproxy.net
def cleanText(src):
    clean = re.sub(
        "[\U0001F600-\U0001F64F\U0001F300-\U0001F5FF\U0001F680-\U0001F6FF\U0001F1E0-\U0001F1FF]",
        "",
        src,
    )
    return clean


if __name__ == "__main__":

    header = {
        "User-Agent": "okhttp/4.11.0",
    }

    lists = {
        # "饭太硬": "http://饭太硬.ml/tv",
        # "肥猫": "http://我不是.肥猫.love:63/接口禁止贩卖",
        "牛蛙": "http://tvbox.王二小放牛娃.xyz",
        # "OK1": "http://ok321.top/tv",
        # "OK2": "http://ok321.top/ok",
    }

    for item in lists:

        html = requests.get(lists[item], headers=header)

        if len(html.text.split("**")) > 1:
            fjson = commentjson.loads(base64.b64decode(html.text.split("**")[1]))
        else:
            with open("./接口/{0}.json".format(item), "wb") as f:
                f.write(html.content)
            # fjson = commentjson.loads(''.join(line for line in html.text.split("\n") if not line.startswith('//')))
            fjson = commentjson.loads(
                "".join(
                    line for line in cleanText(html.text).split("\n") if not line.startswith("//")
                )
            )

        spiderUrl = fjson["spider"].split(";md5")[0]

        html = requests.get(spiderUrl, headers=header)

        with open("./接口/{0}.json".format(item), "w", encoding="utf-8") as f:
            commentjson.dump(fjson, f, ensure_ascii=False, indent=2)

        with open("./接口/{0}.jar".format(item), "wb") as f:
            f.write(html.content)

    print("执行成功！")
