import re
import timeit
import commentjson
import requests


def regStr(src, reg, group=1):
    m = re.search(reg, src)
    src = ""
    if m:
        src = m.group(group)
    return src


def testUrl():
    with open("./js.json", "r", encoding="utf-8") as f:
        js = commentjson.load(f)

    sites = js["sites"]
    t = 0
    b = 0
    e = 0
    r = 0
    for item in sites:
        url = ""
        try:
            if (
                "ext" in item
                and (item["ext"].endswith(".js") or item["ext"].endswith(".py"))
                # and item["key"] == "dr_小白菜电影"
            ):
                rsp = requests.get(item["ext"])
                reTxt = "host.*?[:=].*['\"](.*?)['\"]"
                # if item["ext"].endswith(".py"):
                #     reTxt = "host.*?=.*['\"](.*?)['\"]"

                url = regStr(
                    "\n".join(
                        line for line in rsp.text.split("\n") if not line.strip().startswith("//")
                    ),
                    reTxt,
                )
                header = {
                    "User-Agent": "Mozilla/5.0",
                }

                code = requests.head(url, headers=header).status_code
                if code == 200:
                    pass
                elif code == 301:
                    print(f"  重定向：{item['key']} {url}")
                    r += 1
                else:
                    print(f"验证失败：{item['key']} {url}")
                    b += 1
                t += 1
        except:
            e += 1
            t += 1
            print(f"出错跳过：{item['key']} {url}")
            pass

    print(f"\n  共验证： {t}\n验证失败： {b}\n出错跳过： {e}\n  重定向： {r}\n执行完成！")


if __name__ == "__main__":
    t = timeit.timeit(lambda: testUrl(), number=1)
    print(f"共计用时：{t:.3f}")
