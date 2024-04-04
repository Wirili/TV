import sys
import time

sys.dont_write_bytecode = True
import os
import requests
from importlib.machinery import SourceFileLoader
import json
from runner import Runner
import base64
import timeit


def create_file(file_path):
    if os.path.exists(file_path) is False:
        os.makedirs(file_path)


def write_file(name, content):
    with open(name, "wb") as f:
        f.write(content)


def redirect(url):
    rsp = requests.get(url, allow_redirects=False, verify=False)
    if "Location" in rsp.headers:
        return redirect(rsp.headers["Location"])
    else:
        return rsp


def download_file(name, ext):
    if ext.startswith("http"):
        write_file(name, redirect(ext).content)
    else:
        write_file(name, str.encode(ext))


def init_py(path, name, ext):
    create_file(path)
    py_name = path + name + ".py"
    download_file(py_name, ext)
    return SourceFileLoader(name, py_name).load_module().Spider()


def str2json(content):
    return json.loads(content)


def init(ru, extend):
    ru.init([""])


def getName(ru):
    result = ru.getName()
    formatJo = json.dumps(result, ensure_ascii=False)
    return formatJo


def homeContent(ru, filter):
    result = ru.homeContent(filter)
    formatJo = json.dumps(result, ensure_ascii=False)
    return formatJo


def homeVideoContent(ru):
    result = ru.homeVideoContent()
    formatJo = json.dumps(result, ensure_ascii=False)
    return formatJo


def categoryContent(ru, tid, pg, filter, extend):
    result = ru.categoryContent(tid, pg, filter, str2json(extend))
    formatJo = json.dumps(result, ensure_ascii=False)
    return formatJo


def detailContent(ru, array):
    result = ru.detailContent(str2json(array))
    formatJo = json.dumps(result, ensure_ascii=False)
    return formatJo


def playerContent(ru, flag, id, vipFlags):
    result = ru.playerContent(flag, id, str2json(vipFlags))
    formatJo = json.dumps(result, ensure_ascii=False)
    return formatJo


def searchContent(ru, key, quick):
    result = ru.searchContent(key, quick)
    formatJo = json.dumps(result, ensure_ascii=False)
    return formatJo


def fetch(url, headers={}, cookies=""):
    rsp = requests.get(url, headers=headers, cookies=cookies)
    rsp.encoding = "utf-8"
    return rsp


def postJson(url, json, headers={}, cookies={}):
    rsp = requests.post(url, json=json, headers=headers, cookies=cookies)
    rsp.encoding = "utf-8"
    return rsp


def post(url, data, headers={}, cookies={}):
    rsp = requests.post(url, data=data, headers=headers, cookies=cookies)
    # rsp.encoding = 'utf-8'
    return rsp


def classification(img):
    code = ""
    try:
        print("通过drpy_ocr验证码接口过验证...")
        html = requests.post(
            "http://drpy.nokia.press:8028/ocr/drpy/text",
            data={"img": base64.b64encode(img.content)},
        )
        return html
    except Exception as e:
        print("OCR识别验证码发生错误:{0}".format(e))
    return code


# from py.py_test import Spider
from py.py_subaibai1 import Spider


def run():
    ru = Runner(Spider())

    # 参数为空时不执行
    num = 1

    hid = ""
    cid = ""
    tid = ""
    pid = ""
    skey = ""
    isShowCP = ""
    isShowCP = True

    hid = "运行"
    cid = "1"
    tid = '["119010"]'
    pid = "119010-1-1"
    skey = "爱情公寓"

    # cid = "1"
    # tid = '["27379"]'
    # pid = "27379-1-1"
    # skey = "平凡之路"



    print("Spider Name: %s \n" % getName(ru))
    # print(homeVideoContent(ru))
    tt = 0
    if hid:
        if isShowCP:
            t = timeit.timeit(lambda: print(homeContent(ru, False)), number=num)
        else:
            t = timeit.timeit(lambda: homeContent(ru, False), number=num)
        tt += t
        print("    HomeContent Time: %.3f" % t)

    if cid:
        if isShowCP:
            t = timeit.timeit(
                lambda: print("\n" + categoryContent(ru, cid, "1", "", "{}")), number=num
            )
        else:
            t = timeit.timeit(lambda: categoryContent(ru, cid, "1", "", "{}"), number=num)

        tt += t
        print("CategoryContent Time: %.3f" % t)

    if tid:
        if isShowCP:
            t = timeit.timeit(lambda: print("\n" + detailContent(ru, tid)), number=num)
        else:
            t = timeit.timeit(lambda: detailContent(ru, tid), number=num)

        tt += t
        print("  DetailContent Time: %.3f" % t)

    if pid:
        if isShowCP:
            t = timeit.timeit(lambda: print("\n" + playerContent(ru, "", pid, "{}")), number=num)
        else:
            t = timeit.timeit(lambda: playerContent(ru, "", pid, "{}"), number=num)

        tt += t
        print("  PlayerContent Time: %.3f" % t)
    if skey:
        if isShowCP:
            t = timeit.timeit(lambda: print("\n" + searchContent(ru, skey, "")), number=num)
        else:
            t = timeit.timeit(lambda: searchContent(ru, skey, ""), number=num)

        tt += t
        print("  SearchContent Time: %.3f" % t)
        print(" \n    Num %d Total Time: %.3f" % (num, tt))

    pass


if __name__ == "__main__":
    run()
