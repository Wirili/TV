import cheerio from './libs/cheerio.min.js';
import './libs/crypto-js.js';
import "./libs/util.ym.js";
import spider from "./js/js_bttwo.js";
globalThis.cheerio = cheerio;

//需要安装npm install sync-fetch

let s = performance.now();

console.log(spider.home(false));
console.log(`Home: ${((s=(performance.now()-s))/1000).toFixed(3)}`);

console.log(spider.category("zgjun","1","",{}));
console.log(`Category: ${((s=(performance.now()-s))/1000).toFixed(3)}`);

console.log(spider.detail(["7973"]));
console.log(`Detail: ${((s=(performance.now()-s))/1000).toFixed(3)}`);

console.log(spider.play("","7973-1-1",""));
console.log(`Play: ${((s=(performance.now()-s))/1000).toFixed(3)}`);

console.log(spider.search("平凡之路",false));
console.log(`Search: ${((s=(performance.now()-s))/1000).toFixed(3)}`);