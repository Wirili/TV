import { Crypto, dayjs, jinja2, Uri, _ } from './lib/cat.js';

let key = 'JianPian';
let url = 'https://yjpapipxblwdohpakljwg.hxhzs.com';
let device = {};
let timeOffset = 0;
let siteKey = '';
let siteType = 0;

async function request(reqUrl) {
    let header = {
        'user-agent': 'jianpian-android/350',
        'Referer': url,
        'JPAUTH': 'epawNyWhdt3357Lvkkof/btM3TsGN5yBZuXT2xKFKX41'
    };
    let res = await req(reqUrl, {
        headers: header,
    });
    return res.content;
}

async function init(cfg) {
    siteKey = cfg.skey;
    siteType = cfg.stype;
}

async function home(filter) {
    let data = JSON.parse(await request(url + '/api.php/provide/filter')).data;
    let classes = [];
    let filterObj = {};
    let filterAll = [];
    for (const key in data) {
        classes.push({
            type_id: key,
            type_name: data[key][0].cat,
        });
        if (!filter) continue;
        try {
            let typeId = key.toString();
            if (_.isEmpty(filterAll)) {
                let filterData = JSON.parse(await request(url + '/api.php/provide/searchFilter?type_id=0&pagenum=1&pagesize=1')).data.conditions;
                // console.log(filterData);
                // 年份
                let year = {
                    key: 'year',
                    name: '年份',
                    init: '',
                };
                let yearValues = [];
                yearValues.push({ n: '全部', v: '' });
                filterData.y.forEach((e) => {
                    yearValues.push({ n: e.name, v: e.value });
                });
                year['value'] = yearValues;
                // 地区
                let area = {
                    key: 'area',
                    name: '地区',
                    init: '',
                };
                let areaValues = [];
                areaValues.push({ n: '全部', v: '' });
                filterData.a.forEach((e) => {
                    areaValues.push({ n: e.name, v: e.value });
                });
                area['value'] = areaValues;
                // 类型
                let type = {
                    key: 'category',
                    name: '类型',
                    init: '',
                };
                let typeValues = [];
                typeValues.push({ n: '全部', v: '' });
                filterData.scat.forEach((e) => {
                    typeValues.push({ n: e.name, v: e.value });
                });
                type['value'] = typeValues;

                filterAll.push(year, area, type);
            }
            if (!_.isEmpty(filterAll)) {
                filterObj[typeId] = filterAll;
            }
        } catch (e) {
            console.log(e);
        }
    }
    // console.log(classes);
    // console.log(filterObj);
    return JSON.stringify({
        class: classes,
        filters: filterObj,
    });
}

async function homeVod() {
}

async function category(tid, pg, filter, extend) {
    let reqUrl = url + '/api/crumb/list?area=0&category_id='+ tid +'&page='+ pg +'&type=0&limit=24&sort=hot&year=0';
    reqUrl += jinja2('year={{ext.year}}&category={{ext.category}}&area={{ext.area}}', { ext: extend });
    let data = JSON.parse(await request(reqUrl)).data;
    let videos = [];
    for (const vod of data.result) {
        videos.push({
            vod_id: vod.id,
            vod_name: vod.title,
            vod_pic: vod.videoCover,
            vod_remarks: vod.msg,
        });
    }
    return JSON.stringify({
        page: parseInt(data.page),
        pagecount: data.pagesize,
        limit: 24,
        total: data.total,
        list: videos,
    });
}

async function detail(id) {
    let data = JSON.parse(await request(url + '/api.php/provide/videoDetail?ids=' + id)).data;
    // console.log(data);
    let vod = {
        vod_id: data.id,
        vod_name: data.videoName,
        vod_pic: data.videoCover,
        type_name: data.subCategory,
        vod_year: data.year,
        vod_area: data.area,
        vod_remarks: data.msg,
        vod_actor: data.actor,
        vod_director: data.director,
        vod_content: data.brief.trim(),
    };
    let episodes = JSON.parse(await request(url + '/api.php/provide/videoPlaylist?ids=' + id)).data.episodes;
    let playlist = {};
    for (const episode of episodes) {
        let playurls = episode.playurls;
        for (const playurl of playurls) {
            let from = playurl.playfrom;
            let t = formatPlayUrl(vod.vod_name, playurl.title);
            if (t.length == 0) t = playurl.title.trim();
            if (!playlist.hasOwnProperty(from)) {
                playlist[from] = [];
            }
            playlist[from].push(t + '$' + playurl.playurl);
        }
    }
    vod.vod_play_from = _.keys(playlist).join('$$$');
    let urls = _.values(playlist);
    let vod_play_url = [];
    for (const urlist of urls) {
        vod_play_url.push(urlist.join('#'));
    }
    vod.vod_play_url = vod_play_url.join('$$$');
    return JSON.stringify({
        list: [vod],
    });
}

async function play(flag, id, flags) {
    try {
        let data = JSON.parse(await request(url + '/api.php/provide/parserUrl?url=' + id + '&retryNum=0')).data;
        let playHeader = data.playHeader;
        let jxUrl = data.url;
        let res = await req(jxUrl, {
            headers: {
                'user-agent': 'okhttp/3.12.0',
            },
        });
        let result = jsonParse(id, JSON.parse(res.content));
        result['parse'] = 0;
        if (playHeader) {
            result.header = _.merge(result.header, playHeader);
        }
        return JSON.stringify(result);
    } catch (e) {
        return JSON.stringify({
            parse: 0,
            url: id,
        });
    }
}

async function search(wd, quick, pg) {
    let page = pg || 1;
    if (page == 0) page = 1;
    let data = JSON.parse(await request(url + '/api.php/provide/searchVideo?searchName=' + wd + '&pg=' + page, 'okhttp/3.12.0'));
    let videos = [];
    for (const vod of data.data) {
        videos.push({
            vod_id: vod.id,
            vod_name: vod.videoName,
            vod_pic: vod.videoCover,
            vod_remarks: vod.msg,
        });
    }
    return JSON.stringify({
        page: page,
        pagecount: data.pages,
        list: videos,
    });
}

const charStr = 'abacdefghjklmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ0123456789';
function randStr(len, withNum) {
    var _str = '';
    let containsNum = withNum === undefined ? true : withNum;
    for (var i = 0; i < len; i++) {
        let idx = _.random(0, containsNum ? charStr.length - 1 : charStr.length - 11);
        _str += charStr[idx];
    }
    return _str;
}

function randDevice() {
    return {
        brand: 'Huawei',
        model: 'HUAWEI Mate 20',
        release: '10',
        buildId: randStr(3, false).toUpperCase() + _.random(11, 99) + randStr(1, false).toUpperCase(),
    };
}

function formatPlayUrl(src, name) {
    return name
        .trim()
        .replaceAll(src, '')
        .replace(/<|>|《|》/g, '')
        .replace(/\$|#/g, ' ')
        .trim();
}

function jsonParse(input, json) {
    try {
        let url = json.url ?? '';
        if (url.startsWith('//')) {
            url = 'https:' + url;
        }
        if (!url.startsWith('http')) {
            return {};
        }
        let headers = json['headers'] || {};
        let ua = (json['user-agent'] || '').trim();
        if (ua.length > 0) {
            headers['User-Agent'] = ua;
        }
        let referer = (json['referer'] || '').trim();
        if (referer.length > 0) {
            headers['Referer'] = referer;
        }
        return {
            header: headers,
            url: url,
        };
    } catch (error) {
        console.log(error);
    }
    return {};
}

export function __jsEvalReturn() {
    return {
        init: init,
        home: home,
        homeVod: homeVod,
        category: category,
        detail: detail,
        play: play,
        search: search,
    };
}
