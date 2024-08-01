import commentjson
import base64
import requests
import re

## 需要安装 pip install commentjson
## 格式化安装 "python.formatting.provider": "yapf"


## jar包地址
## github文件加速 https://ghproxy.net

if __name__ == "__main__":

    header = {
        "User-Agent": "okhttp/4.11.0",
    }

    lists = {
        "OK_leanback-python-arm64_v8a.apk": "https://raw.githubusercontent.com/FongMi/Release/main/apk/release/leanback-python-arm64_v8a.apk",
        "OK_leanback-python-armeabi_v7a.apk": "https://raw.githubusercontent.com/FongMi/Release/main/apk/release/leanback-python-armeabi_v7a.apk",
        "OK_mobile-python-arm64_v8a.apk": "https://raw.githubusercontent.com/FongMi/Release/main/apk/release/mobile-python-arm64_v8a.apk",
        "OK_leanback.json": "https://raw.githubusercontent.com/FongMi/Release/main/apk/release/leanback.json",
        "OK_mobile.json": "https://raw.githubusercontent.com/FongMi/Release/main/apk/release/mobile.json",
        "leanback-python-arm64_v8a.apk": "https://raw.githubusercontent.com/FongMi/Release/fongmi/apk/release/leanback-python-arm64_v8a.apk",
        "leanback-python-armeabi_v7a.apk": "https://raw.githubusercontent.com/FongMi/Release/fongmi/apk/release/leanback-python-armeabi_v7a.apk",
        "mobile-python-arm64_v8a.apk": "https://raw.githubusercontent.com/FongMi/Release/fongmi/apk/release/mobile-python-arm64_v8a.apk",
        "leanback.json": "https://raw.githubusercontent.com/FongMi/Release/fongmi/apk/release/leanback.json",
        "mobile.json": "https://raw.githubusercontent.com/FongMi/Release/fongmi/apk/release/mobile.json",
    }

    for item in lists:

        html = requests.get(lists[item])

        with open(f"./soft/{item}", "wb") as f:
            f.write(html.content)

    print("执行成功！")
