// { "framework": "Vue"}

/******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};
/******/
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/
/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId]) {
/******/ 			return installedModules[moduleId].exports;
/******/ 		}
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			i: moduleId,
/******/ 			l: false,
/******/ 			exports: {}
/******/ 		};
/******/
/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);
/******/
/******/ 		// Flag the module as loaded
/******/ 		module.l = true;
/******/
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/
/******/
/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;
/******/
/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;
/******/
/******/ 	// define getter function for harmony exports
/******/ 	__webpack_require__.d = function(exports, name, getter) {
/******/ 		if(!__webpack_require__.o(exports, name)) {
/******/ 			Object.defineProperty(exports, name, {
/******/ 				configurable: false,
/******/ 				enumerable: true,
/******/ 				get: getter
/******/ 			});
/******/ 		}
/******/ 	};
/******/
/******/ 	// getDefaultExport function for compatibility with non-harmony modules
/******/ 	__webpack_require__.n = function(module) {
/******/ 		var getter = module && module.__esModule ?
/******/ 			function getDefault() { return module['default']; } :
/******/ 			function getModuleExports() { return module; };
/******/ 		__webpack_require__.d(getter, 'a', getter);
/******/ 		return getter;
/******/ 	};
/******/
/******/ 	// Object.prototype.hasOwnProperty.call
/******/ 	__webpack_require__.o = function(object, property) { return Object.prototype.hasOwnProperty.call(object, property); };
/******/
/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "";
/******/
/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(__webpack_require__.s = 3);
/******/ })
/************************************************************************/
/******/ ([
/* 0 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});

var _typeof2 = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function (obj) { return typeof obj; } : function (obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; }; /**
                                                                                                                                                                                                                                                                                * CopyRight (C) 2017-2022 Alibaba Group Holding Limited.
                                                                                                                                                                                                                                                                                * Created by Tw93 on 17/11/01
                                                                                                                                                                                                                                                                                */

var _urlParse = __webpack_require__(6);

var _urlParse2 = _interopRequireDefault(_urlParse);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _defineProperty(obj, key, value) { if (key in obj) { Object.defineProperty(obj, key, { value: value, enumerable: true, configurable: true, writable: true }); } else { obj[key] = value; } return obj; }

var Utils = {
  UrlParser: _urlParse2.default,
  _typeof: function _typeof(obj) {
    return Object.prototype.toString.call(obj).slice(8, -1).toLowerCase();
  },
  isPlainObject: function isPlainObject(obj) {
    return Utils._typeof(obj) === 'object';
  },
  isString: function isString(obj) {
    return typeof obj === 'string';
  },
  isNonEmptyArray: function isNonEmptyArray() {
    var obj = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : [];

    return obj && obj.length > 0 && Array.isArray(obj) && typeof obj !== 'undefined';
  },
  isObject: function isObject(item) {
    return item && (typeof item === 'undefined' ? 'undefined' : _typeof2(item)) === 'object' && !Array.isArray(item);
  },
  isEmptyObject: function isEmptyObject(obj) {
    return Object.keys(obj).length === 0 && obj.constructor === Object;
  },
  decodeIconFont: function decodeIconFont(text) {
    // 正则匹配 图标和文字混排 eg: 我去上学校&#xe600;,天天不&#xe600;迟到
    var regExp = /&#x[a-z|0-9]{4,5};?/g;
    if (regExp.test(text)) {
      return text.replace(new RegExp(regExp, 'g'), function (iconText) {
        var replace = iconText.replace(/&#x/, '0x').replace(/;$/, '');
        return String.fromCharCode(replace);
      });
    } else {
      return text;
    }
  },
  mergeDeep: function mergeDeep(target) {
    for (var _len = arguments.length, sources = Array(_len > 1 ? _len - 1 : 0), _key = 1; _key < _len; _key++) {
      sources[_key - 1] = arguments[_key];
    }

    if (!sources.length) return target;
    var source = sources.shift();
    if (Utils.isObject(target) && Utils.isObject(source)) {
      for (var key in source) {
        if (Utils.isObject(source[key])) {
          if (!target[key]) {
            Object.assign(target, _defineProperty({}, key, {}));
          }
          Utils.mergeDeep(target[key], source[key]);
        } else {
          Object.assign(target, _defineProperty({}, key, source[key]));
        }
      }
    }
    return Utils.mergeDeep.apply(Utils, [target].concat(sources));
  },
  appendProtocol: function appendProtocol(url) {
    if (/^\/\//.test(url)) {
      var bundleUrl = weex.config.bundleUrl;

      return 'http' + (/^https:/.test(bundleUrl) ? 's' : '') + ':' + url;
    }
    return url;
  },
  encodeURLParams: function encodeURLParams(url) {
    var parsedUrl = new _urlParse2.default(url, true);
    return parsedUrl.toString();
  },
  goToH5Page: function goToH5Page(jumpUrl) {
    var animated = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : false;
    var callback = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : null;

    var Navigator = weex.requireModule('navigator');
    var jumpUrlObj = new Utils.UrlParser(jumpUrl, true);
    var url = Utils.appendProtocol(jumpUrlObj.toString());
    Navigator.push({
      url: Utils.encodeURLParams(url),
      animated: animated.toString()
    }, callback);
  },

  env: {
    isTaobao: function isTaobao() {
      var appName = weex.config.env.appName;

      return (/(tb|taobao|淘宝)/i.test(appName)
      );
    },
    isTrip: function isTrip() {
      var appName = weex.config.env.appName;

      return appName === 'LX';
    },
    isBoat: function isBoat() {
      var appName = weex.config.env.appName;

      return appName === 'Boat' || appName === 'BoatPlayground';
    },
    isWeb: function isWeb() {
      var platform = weex.config.env.platform;

      return (typeof window === 'undefined' ? 'undefined' : _typeof2(window)) === 'object' && platform.toLowerCase() === 'web';
    },
    isIOS: function isIOS() {
      var platform = weex.config.env.platform;

      return platform.toLowerCase() === 'ios';
    },

    /**
     * 是否为 iPhone X
     * @returns {boolean}
     */
    isIPhoneX: function isIPhoneX() {
      var deviceHeight = weex.config.env.deviceHeight;

      if (Utils.env.isWeb()) {
        return (typeof window === 'undefined' ? 'undefined' : _typeof2(window)) !== undefined && window.screen && window.screen.width && window.screen.height && parseInt(window.screen.width, 10) === 375 && parseInt(window.screen.height, 10) === 812;
      }
      return Utils.env.isIOS() && deviceHeight === 2436;
    },
    isAndroid: function isAndroid() {
      var platform = weex.config.env.platform;

      return platform.toLowerCase() === 'android';
    },
    isAlipay: function isAlipay() {
      var appName = weex.config.env.appName;

      return appName === 'AP';
    },
    isTmall: function isTmall() {
      var appName = weex.config.env.appName;

      return (/(tm|tmall|天猫)/i.test(appName)
      );
    },
    isAliWeex: function isAliWeex() {
      return Utils.env.isTmall() || Utils.env.isTrip() || Utils.env.isTaobao();
    },
    supportsEB: function supportsEB() {
      var weexVersion = weex.config.env.weexVersion || '0';
      var isHighWeex = Utils.compareVersion(weexVersion, '0.10.1.4') && (Utils.env.isIOS() || Utils.env.isAndroid());
      var expressionBinding = weex.requireModule('expressionBinding');
      return expressionBinding && expressionBinding.enableBinding && isHighWeex;
    },


    /**
     * 判断Android容器是否支持是否支持expressionBinding(处理方式很不一致)
     * @returns {boolean}
     */
    supportsEBForAndroid: function supportsEBForAndroid() {
      return Utils.env.isAndroid() && Utils.env.supportsEB();
    },


    /**
     * 判断IOS容器是否支持是否支持expressionBinding
     * @returns {boolean}
     */
    supportsEBForIos: function supportsEBForIos() {
      return Utils.env.isIOS() && Utils.env.supportsEB();
    },


    /**
     * 获取weex屏幕真实的设置高度，需要减去导航栏高度
     * @returns {Number}
     */
    getPageHeight: function getPageHeight() {
      var env = weex.config.env;

      var navHeight = Utils.env.isWeb() ? 0 : Utils.env.isIPhoneX() ? 176 : 132;
      return env.deviceHeight / env.deviceWidth * 750 - navHeight;
    }
  },

  /**
   * 版本号比较
   * @memberOf Utils
   * @param currVer {string}
   * @param promoteVer {string}
   * @returns {boolean}
   * @example
   *
   * const { Utils } = require('@ali/wx-bridge');
   * const { compareVersion } = Utils;
   * console.log(compareVersion('0.1.100', '0.1.11')); // 'true'
   */
  compareVersion: function compareVersion() {
    var currVer = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : '0.0.0';
    var promoteVer = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : '0.0.0';

    if (currVer === promoteVer) return true;
    var currVerArr = currVer.split('.');
    var promoteVerArr = promoteVer.split('.');
    var len = Math.max(currVerArr.length, promoteVerArr.length);
    for (var i = 0; i < len; i++) {
      var proVal = ~~promoteVerArr[i];
      var curVal = ~~currVerArr[i];
      if (proVal < curVal) {
        return true;
      } else if (proVal > curVal) {
        return false;
      }
    }
    return false;
  },

  /**
   * 分割数组
   * @param arr 被分割数组
   * @param size 分割数组的长度
   * @returns {Array}
   */
  arrayChunk: function arrayChunk() {
    var arr = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : [];
    var size = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : 4;

    var groups = [];
    if (arr && arr.length > 0) {
      groups = arr.map(function (e, i) {
        return i % size === 0 ? arr.slice(i, i + size) : null;
      }).filter(function (e) {
        return e;
      });
    }
    return groups;
  },
  truncateString: function truncateString(str, len) {
    var hasDot = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : true;

    var newLength = 0;
    var newStr = '';
    var singleChar = '';
    var chineseRegex = /[^\x00-\xff]/g;
    var strLength = str.replace(chineseRegex, '**').length;
    for (var i = 0; i < strLength; i++) {
      singleChar = str.charAt(i).toString();
      if (singleChar.match(chineseRegex) !== null) {
        newLength += 2;
      } else {
        newLength++;
      }
      if (newLength > len) {
        break;
      }
      newStr += singleChar;
    }

    if (hasDot && strLength > len) {
      newStr += '...';
    }
    return newStr;
  }
};

exports.default = Utils;

/***/ }),
/* 1 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
/**
 * Created by Tw93 on 2016/10/29.
 */

var GIF = exports.GIF = 'https://img.alicdn.com/tfs/TB1aks3PpXXXXcXXFXXXXXXXXXX-150-150.gif';
var BLACK_GIF = exports.BLACK_GIF = 'https://img.alicdn.com/tfs/TB1Ep_9NVXXXXb8XVXXXXXXXXXX-74-74.gif';
var PART = exports.PART = 'https://gtms02.alicdn.com/tfs/TB1y4QbSXXXXXbgapXXXXXXXXXX-50-50.gif';

/***/ }),
/* 2 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


module.exports = {
    "DEV": true,
    "pages": [{ id: "page.index", url: "app.weex.js" }, { id: "page.demo.popup", url: "weexui.weex.js" }, { id: "page.demo.animation", url: "demo-animation.js" }, { id: "page.demo.rich", url: "demo-rich.weex.js" }, { id: "page.demo.simpleflow", url: "demo-simpleflow.weex.js" }, { id: "page.demo.tags", url: "demo-tags.weex.js" }, { id: "page.demo.loading", url: "demo-loading.weex.js" }, { id: "page.demo.mask", url: "demo-mask.weex.js" }, { id: "page.demo.progress", url: "demo-progress.weex.js" }, { id: "page.demo.navbar", url: "demo-navbar.weex.js" }, { id: "page.demo.tabs", url: "demo-tabs.weex.js" }, { id: "page.demo.swiper", url: "demo-swiper.weex.js" }, { id: "page.demo.cell", url: "demo-cell.weex.js" }, { id: "page.demo.slider", url: "demo-slider.weex.js" }, { id: "page.demo.result", url: "demo-result.weex.js" },

    //Form
    { id: "page.demo.button", url: "demo-button.weex.js" }, { id: "page.demo.input", url: "demo-input.weex.js" }, { id: "page.demo.checkbox", url: "demo-checkbox.weex.js" }, { id: "page.demo.picker", url: "demo-picker.weex.js" }, { id: "page.demo.countdown", url: "demo-countdown.weex.js" }, { id: "page.demo.sliderbar", url: "demo-sliderbar.weex.js" }, { id: "page.demo.step", url: "demo-step.weex.js" }, { id: "page.demo.gridselect", url: "demo-gridselect.weex.js" },

    //demo
    { id: "page.demo.meituan", url: "meituan/demo-meituan.weex.js" }, { id: "page.demo.weixin", url: "weixin/demo-weixin.weex.js" }, { id: "page.demo.xiaomi", url: "xiaomi/demo-xiaomi.weex.js" }, { id: "page.demo.form", url: "form/demo-form.weex.js" }],
    Config: {
        notSupport: "该接口或功能仅在OpenSumslack移动端中支持！",
        svrurl: "http://192.168.1.154:7080/"
    },
    getPageUrl: function getPageUrl(id) {
        for (var i in this.pages) {
            if (this.pages[i].id === id) {
                if (this.DEV) {
                    return this.pages[i].url;
                } else {
                    var _url = this.pages[i].url;
                    if (_url.endsWith(".weex.js")) {
                        _url = _url.substring(0, _url.lastIndexOf(".weex")) + _url.substring(_url.lastIndexOf(".weex") + 5, _url.length);
                    }
                    console.log("xxxx:", _url);
                    return _url;
                }
            }
        }
        return null;
    }
};

/***/ }),
/* 3 */
/***/ (function(module, exports, __webpack_require__) {

var __vue_exports__, __vue_options__
var __vue_styles__ = []

/* styles */
__vue_styles__.push(__webpack_require__(4)
)

/* script */
__vue_exports__ = __webpack_require__(5)

/* template */
var __vue_template__ = __webpack_require__(42)
__vue_options__ = __vue_exports__ = __vue_exports__ || {}
if (
  typeof __vue_exports__.default === "object" ||
  typeof __vue_exports__.default === "function"
) {
if (Object.keys(__vue_exports__).some(function (key) { return key !== "default" && key !== "__esModule" })) {console.error("named exports are not supported in *.vue files.")}
__vue_options__ = __vue_exports__ = __vue_exports__.default
}
if (typeof __vue_options__ === "function") {
  __vue_options__ = __vue_options__.options
}
__vue_options__.__file = "F:\\WebstormProjectsWeex\\weexui-demo\\demo1\\test12\\src\\index.vue"
__vue_options__.render = __vue_template__.render
__vue_options__.staticRenderFns = __vue_template__.staticRenderFns
__vue_options__._scopeId = "data-v-2bf2ac52"
__vue_options__.style = __vue_options__.style || {}
__vue_styles__.forEach(function (module) {
  for (var name in module) {
    __vue_options__.style[name] = module[name]
  }
})
if (typeof __register_static_styles__ === "function") {
  __register_static_styles__(__vue_options__._scopeId, __vue_styles__)
}

module.exports = __vue_exports__
module.exports.el = 'true'
new Vue(module.exports)


/***/ }),
/* 4 */
/***/ (function(module, exports) {

module.exports = {
  "scroller": {
    "width": "750",
    "borderWidth": "0"
  },
  "item-container": {
    "width": "750"
  },
  "header": {
    "marginTop": "20",
    "display": "flex",
    "flexDirection": "column",
    "justifyContent": "flex-start",
    "alignItems": "center"
  },
  "container": {
    "display": "flex",
    "flexDirection": "column"
  },
  "api-item": {
    "display": "flex",
    "flexDirection": "row",
    "alignContent": "space-between",
    "height": "80",
    "lineHeight": "80"
  },
  "api-item-text": {
    "paddingLeft": "15",
    "height": "80",
    "lineHeight": "80",
    "color": "#006400",
    "fontSize": "30",
    "flex": 1
  },
  "logo32": {
    "width": "32",
    "height": "32",
    "marginTop": "24"
  },
  "logo": {
    "width": "120",
    "height": "120",
    "textAlign": "center",
    "alignItems": "center"
  },
  "title": {
    "color": "#A9A9A9",
    "paddingTop": "20",
    "paddingRight": "20",
    "paddingBottom": "20",
    "paddingLeft": "20"
  },
  "tip": {
    "paddingLeft": "15",
    "marginBottom": "10",
    "marginTop": "10",
    "justifyContent": "center",
    "backgroundColor": "#FFFFFF",
    "height": "80",
    "lineHeight": "80",
    "color": "#000000",
    "fontSize": "30",
    "fontWeight": "bold",
    "backgroundImage": "linear-gradient(to top,#71a43a,#7ed321)"
  },
  "panel": {
    "paddingLeft": "15",
    "marginTop": "2",
    "borderWidth": "0",
    "borderStyle": "solid",
    "borderColor": "rgb(162,217,192)",
    "backgroundColor": "rgba(162,217,192,0.2)"
  },
  "text": {
    "fontSize": "50",
    "textAlign": "center",
    "color": "#41B883"
  }
}

/***/ }),
/* 5 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
    value: true
});

var _utils = __webpack_require__(0);

var _utils2 = _interopRequireDefault(_utils);

var _wxcPartLoading = __webpack_require__(10);

var _wxcPartLoading2 = _interopRequireDefault(_wxcPartLoading);

var _wxcLoading = __webpack_require__(14);

var _wxcLoading2 = _interopRequireDefault(_wxcLoading);

var _wxcCell = __webpack_require__(19);

var _wxcCell2 = _interopRequireDefault(_wxcCell);

var _wxcTabBar = __webpack_require__(24);

var _wxcTabBar2 = _interopRequireDefault(_wxcTabBar);

var _wxcIcon = __webpack_require__(29);

var _wxcIcon2 = _interopRequireDefault(_wxcIcon);

var _wxcMinibar = __webpack_require__(35);

var _wxcMinibar2 = _interopRequireDefault(_wxcMinibar);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var config = __webpack_require__(2); //
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//

var Sumslack = __webpack_require__(40);

var _token = "";
exports.default = {
    components: { WxcMinibar: _wxcMinibar2.default, WxcIcon: _wxcIcon2.default, WxcTabBar: _wxcTabBar2.default, WxcCell: _wxcCell2.default, WxcLoading: _wxcLoading2.default, WxcPartLoading: _wxcPartLoading2.default },
    data: function data() {
        return {
            loadingText: '请稍候...',
            isShowLoading: false,
            tabTitles: [{
                title: 'WeexUI',
                icon: 'https://gw.alicdn.com/tfs/TB1MWXdSpXXXXcmXXXXXXXXXXXX-72-72.png',
                activeIcon: 'https://gw.alicdn.com/tfs/TB1kCk2SXXXXXXFXFXXXXXXXXXX-72-72.png'
            }, {
                title: '扩展接口',
                icon: 'http://h5.sumslack.com/logo57.png',
                activeIcon: 'http://h5.sumslack.com/logo57.png'
            }, {
                title: '综合实例',
                icon: 'http://wxapps.sumslack.com/app/menu.png',
                activeIcon: 'http://wxapps.sumslack.com/app/menu-active.png'
            }],
            tabStyles: {
                bgColor: '#FFFFFF',
                titleColor: '#666666',
                activeTitleColor: '#3D3D3D',
                activeBgColor: '#FFFFFF',
                isActiveTitleBold: true,
                iconWidth: 70,
                iconHeight: 70,
                width: 160,
                height: 120,
                fontSize: 24,
                textPaddingLeft: 10,
                textPaddingRight: 10
            },
            contentStyle: "",
            showApiListLogin: true,
            showApiListSumslack: true,
            showApiListUI: true,
            showApiListComplexUI: true,
            showApiFormUI: true,
            showApiListDevice: true,
            showApiListNet: true,
            showApiListMedia: true,
            showApiListLocation: true,
            showApiListData: true,
            imgurl: config.Config.imgurl,
            logoUrl: 'http://h5.sumslack.com/logo120.png',
            apiListLogin: [{ id: 1, title: "获取登录code码" }, { id: 3, title: "获取登录token，需解密" }, { id: 2, title: "获取当前用户信息" }],
            apiListSumslack: [{ id: 30, title: "选择联系人 - selectContact" }, { id: 34, title: "打开联系人详情 - openProfile" }, { id: 31, title: "创建群" }, { id: 32, title: "发送聊天消息" }, { id: 33, title: "打开带输入框的新窗口" }, { id: 35, title: "日期时间选择Native - picker" }],
            apiListDevice: [
            // {id:51,title:"获取手机网络状态"},
            // {id:52,title:"获取手机设备信息"},
            { id: 53, title: "打电话 - makePhoneCall" }, { id: 54, title: "扫一扫 - scanQrCode" }],
            apiListUI: [{ id: 100, title: "alert/confirm/prompt" }, { id: 103, title: "toast" }, { id: 104, title: "剪贴板操作" }, { id: 105, title: "动画", pageid: "page.demo.animation" }, { id: 106, title: "显示ActionSheet菜单" }, { id: 109, title: "设置页面标题" }, { id: 107, title: "页面跳转", pageid: "page.demo.animation" }, { id: 108, title: "页面跳转不返回" }, { id: 110, title: "关闭当前窗口" }, { id: 111, title: "打开Native界面" }, { id: 112, title: "右上角多级菜单" }],
            apiListComplexUI: [{ id: 801, title: "富文本显示", pageid: "page.demo.rich" }, { id: 802, title: "简单流程图", pageid: "page.demo.simpleflow" }, { id: 803, title: "Tags", pageid: "page.demo.tags" }, { id: 804, title: "Loading", pageid: "page.demo.loading" }, { id: 805, title: "Mask", pageid: "page.demo.mask" }, { id: 806, title: "进度条", pageid: "page.demo.progress" }, { id: 807, title: "导航栏", pageid: "page.demo.navbar" }, { id: 808, title: "Tabs", pageid: "page.demo.tabs" }, { id: 809, title: "Swiper", pageid: "page.demo.swiper" }, { id: 810, title: "Cell", pageid: "page.demo.cell" }, { id: 811, title: "Slider", pageid: "page.demo.slider" }, { id: 812, title: "Result", pageid: "page.demo.result" }],
            apiListFormUI: [{ id: 701, title: "Button", pageid: "page.demo.button" }, { id: 707, title: "输入框", pageid: "page.demo.input" }, { id: 702, title: "单复选", pageid: "page.demo.checkbox" }, { id: 703, title: "Picker", pageid: "page.demo.picker" }, { id: 704, title: "倒计时", pageid: "page.demo.countdown" }, { id: 705, title: "滑动输入条", pageid: "page.demo.sliderbar" }, { id: 706, title: "步进器", pageid: "page.demo.step" }, { id: 709, title: "grid-select", pageid: "page.demo.gridselect" }],
            apiListNet: [{ id: 151, title: "发起网络Http请求" }, { id: 152, title: "上传文件" }, { id: 153, title: "下载文件" }, { id: 154, title: "分享功能" }],
            apiListMedia: [{ id: 201, title: "拍照/相册选图片" }, { id: 202, title: "录音" }, { id: 203, title: "预览图片" }],
            apiListLocation: [{ id: 251, title: "获取当前位置" }],
            menuList: ["编辑项目", "删除项目", "项目归档"]
        };
    },
    created: function created() {
        var tabPageHeight = _utils2.default.env.getPageHeight();
        var tabStyles = this.tabStyles;

        this.contentStyle = { height: tabPageHeight - tabStyles.height + 'px' };
        Sumslack.init("Sumslack小程序演示", [{ "title": "刷新", "href": "javascript:refreshPage" }], function () {
            Sumslack.addGlobalEventListener("refreshPage", function () {
                Sumslack.refresh();
            });
        });
    },

    methods: {
        wxcTabBarCurrentTabSelected: function wxcTabBarCurrentTabSelected(e) {
            var index = e.page;
            // console.log(index);
        },
        onshow: function onshow() {
            var that = this;
            console.log("************page vewappear");
        },
        showApiList: function showApiList(vv) {
            console.log('show api list: ', vv);
            this.showApiListLogin = false;
            this.showApiListSumslack = false;
            this.showApiListUI = false;
            this.showApiListComplexUI = false, this.showApiFormUI = false, this.showApiListDevice = false;
            this.showApiListNet = false;
            this.showApiListMedia = false;
            this.showApiListLocation = false;
            this.showApiListData = false;
            if (vv == "showApiListLogin") {
                this.showApiListLogin = true;
            } else if (vv == "showApiListSumslack") {
                this.showApiListSumslack = true;
            } else if (vv == "showApiListUI") {
                this.showApiListUI = true;
            } else if (vv == "showApiListDevice") {
                this.showApiListDevice = true;
            } else if (vv == "showApiListNet") {
                this.showApiListNet = true;
            } else if (vv == "showApiListMedia") {
                this.showApiListMedia = true;
            } else if (vv == "showApiListLocation") {
                this.showApiListLocation = true;
            } else if (vv == "showApiListData") {
                this.showApiListData = true;
            } else if (vv == "showApiListComplexUI") {
                this.showApiListComplexUI = true;
            } else if (vv == "showApiFormUI") {
                this.showApiFormUI = true;
            }
        },
        callApi: function callApi(apiId, api) {
            if (api && api.pageid && api.pageid != "") {
                Sumslack.navigateTo(api.pageid);
                return;
            }
            var that = this;
            switch (apiId) {
                case 1:
                    Sumslack.login(function (code) {
                        Sumslack.alert(Sumslack.print(code));
                    });
                    break;
                case 2:
                    Sumslack.getUserInfo().then(function (data) {
                        Sumslack.alert(Sumslack.print(data));
                    });
                    break;
                case 3:
                    Sumslack.getToken(function (code) {
                        Sumslack.alert(Sumslack.print(code));
                        _token = code;
                    });
                    break;
                case 30:
                    Sumslack.selectContact({ "selectType": "1", "selectTarget": "both" }, function (ret) {
                        Sumslack.alert(Sumslack.print(ret));
                    });
                    break;
                case 31:
                    Sumslack.createGroup({ "name": "测试群", "type": 4, "userIDs": [1079] }, function (ret) {
                        Sumslack.alert(Sumslack.print(ret));
                    });
                    break;
                case 32:
                    Sumslack.sendMessage(1, 1079, "test msg");
                    break;
                case 33:
                    Sumslack.navigateToEmojiPanel("page.demo.popup", function (res) {
                        Sumslack.alert(Sumslack.print(res));
                    });
                    break;
                case 34:
                    Sumslack.openProfile(1065);
                    break;
                case 35:
                    Sumslack.picker("yyyyMMdd", { min: '', max: '' }, function (data) {
                        Sumslack.alert(data);
                    });
                    break;
                case 53:
                    Sumslack.makePhoneCall('057482815326', function (ret) {
                        console.log("make phone call: 057482815326");
                    });
                    break;
                case 54:
                    Sumslack.scanQrCode(function (ret) {
                        console.log("scan");
                    });
                    break;
                case 100:
                    Sumslack.confirm("confirm ok?", function (ret) {
                        Sumslack.alert(ret);
                        Sumslack.prompt("input value:", function (msg) {
                            Sumslack.alert(JSON.stringify(msg));
                        });
                    });
                    break;
                case 103:
                    Sumslack.toast("hellow,sumslack!");
                    break;
                case 104:
                    Sumslack.setClipboardData("你好,Sumslack!");
                    setTimeout(function () {
                        Sumslack.getClipboardData(function (msg) {
                            Sumslack.alert("剪贴板内容：" + Sumslack.print(msg));
                        });
                    }, 1000);
                    break;
                case 106:
                    Sumslack.showActionSheet({ "itemList": ["菜单1", "菜单2", "菜单3"] }, function (ret) {
                        Sumslack.alert(Sumslack.print(ret));
                    });
                    break;
                case 108:
                    Sumslack.redirectTo("page.demo.popup", { a: "test" });
                    break;
                case 109:
                    Sumslack.setTitle("新标题！");
                    break;
                case 110:
                    Sumslack.close();
                    break;
                case 111:
                    Sumslack.openNativeUI({ "appId": 60, "appCallbackLink": "native:quan" });
                    break;
                case 112:
                    Sumslack.layoutNaviBar({
                        title: "测试窗口名",
                        items: [{ title: "新页面", href: "http://www.baidu.com" }, { title: "更多", href: "javascript:doActionSheet" }]
                    });
                    Sumslack.addGlobalEventListener("doActionSheet", function () {
                        Sumslack.alert("hellow!");
                    });
                    break;
                case 151:
                    Sumslack.request("http://wx.sumslack.com/restful/stat/stat.jhtml", {
                        v: "qbapp"
                    }).then(function (data) {
                        Sumslack.alert('收到网络响应：' + Sumslack.print(data));
                    });
                    break;
                case 152:
                    Sumslack.chooseImage(1, 0, function (res) {
                        res = Sumslack.toJSON(res);
                        var _imgs = res.list;
                        if (_imgs && _imgs.length === 1) {
                            that.isShowLoading = true;
                            that.loadingText = '正在上传...';
                            Sumslack.uploadImage(_imgs[0], function (result) {
                                result = Sumslack.toJSON(result);
                                if (result.status === "success") {
                                    Sumslack.alert("上传成功：" + Sumslack.print(result));
                                    that.isShowLoading = false;
                                } else if (result.status === "fail") {
                                    Sumslack.alert("上传失败：" + Sumslack.print(result));
                                    that.isShowLoading = false;
                                } else {
                                    that.loadingText = '已传' + Math.round(result.bytesWritten * 100 / result.totalSize) + '%';
                                }
                            });
                        }
                    });
                    break;
                case 153:
                    Sumslack.downloadFile('http://wxapps.sumslack.com/demo2/index.js', function (res) {
                        Sumslack.alert(Sumslack.print(res));
                    });
                    break;
                case 154:
                    Sumslack.share({
                        title: "基于Vue2的在线出试卷小系统，开源已发布",
                        url: "http://cxlh.iteye.com/blog/2399877",
                        logo: "http://dl2.iteye.com/upload/attachment/0127/8169/e08cb2f7-5793-3479-a6bc-e65ac490f3b1.png",
                        description: "一份在线面试的小系统,在线出卷，支持链接分享，小程序码扫描微信直接答题"
                    }, function (res) {
                        console.log('share:', res);
                    });
                    break;
                case 201:
                    Sumslack.chooseImage(5, 0, function (res) {
                        Sumslack.alert(Sumslack.print(res));
                    });
                    break;
                case 203:
                    Sumslack.previewImage({ 'current': 'http://h5.sumslack.com/logo27.png', 'urls': ['http://h5.sumslack.com/logo27.png', 'http://h5.sumslack.com/logo57.png', 'http://h5.sumslack.com/logo80.png'] });
                    break;
                case 900:
                    Sumslack.setStorage("k", { a: "你好", b: "Sumslack" }, function (res) {
                        Sumslack.alert("设置JSON对象：" + Sumslack.print(res));
                        Sumslack.getStorage("k", function (result) {
                            Sumslack.alert("读取key为k的数据：" + Sumslack.print(result));
                        });
                    });
                    break;
                case 251:
                    Sumslack.getLocation(function (pos) {
                        Sumslack.alert(Sumslack.print(pos));
                    });
                    break;
                default:
                    Sumslack.alert("接口编写中...");
            }
        }
    },
    init: function init() {
        console.log('在初始化内部变量，并且添加了事件功能后被触发');
    },
    ready: function ready() {
        console.log('模板已经编译并且生成了 Virtual DOM 之后被触发');
    },
    destroyed: function destroyed() {
        console.log('在页面被销毁时调用');
    }
};

/***/ }),
/* 6 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/* WEBPACK VAR INJECTION */(function(global) {

var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function (obj) { return typeof obj; } : function (obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; };

var required = __webpack_require__(8),
    qs = __webpack_require__(9),
    protocolre = /^([a-z][a-z0-9.+-]*:)?(\/\/)?([\S\s]*)/i,
    slashes = /^[A-Za-z][A-Za-z0-9+-.]*:\/\//;

/**
 * These are the parse rules for the URL parser, it informs the parser
 * about:
 *
 * 0. The char it Needs to parse, if it's a string it should be done using
 *    indexOf, RegExp using exec and NaN means set as current value.
 * 1. The property we should set when parsing this value.
 * 2. Indication if it's backwards or forward parsing, when set as number it's
 *    the value of extra chars that should be split off.
 * 3. Inherit from location if non existing in the parser.
 * 4. `toLowerCase` the resulting value.
 */
var rules = [['#', 'hash'], // Extract from the back.
['?', 'query'], // Extract from the back.
['/', 'pathname'], // Extract from the back.
['@', 'auth', 1], // Extract from the front.
[NaN, 'host', undefined, 1, 1], // Set left over value.
[/:(\d+)$/, 'port', undefined, 1], // RegExp the back.
[NaN, 'hostname', undefined, 1, 1] // Set left over.
];

/**
 * These properties should not be copied or inherited from. This is only needed
 * for all non blob URL's as a blob URL does not include a hash, only the
 * origin.
 *
 * @type {Object}
 * @private
 */
var ignore = { hash: 1, query: 1 };

/**
 * The location object differs when your code is loaded through a normal page,
 * Worker or through a worker using a blob. And with the blobble begins the
 * trouble as the location object will contain the URL of the blob, not the
 * location of the page where our code is loaded in. The actual origin is
 * encoded in the `pathname` so we can thankfully generate a good "default"
 * location from it so we can generate proper relative URL's again.
 *
 * @param {Object|String} loc Optional default location object.
 * @returns {Object} lolcation object.
 * @api public
 */
function lolcation(loc) {
  loc = loc || global.location || {};

  var finaldestination = {},
      type = typeof loc === 'undefined' ? 'undefined' : _typeof(loc),
      key;

  if ('blob:' === loc.protocol) {
    finaldestination = new URL(unescape(loc.pathname), {});
  } else if ('string' === type) {
    finaldestination = new URL(loc, {});
    for (key in ignore) {
      delete finaldestination[key];
    }
  } else if ('object' === type) {
    for (key in loc) {
      if (key in ignore) continue;
      finaldestination[key] = loc[key];
    }

    if (finaldestination.slashes === undefined) {
      finaldestination.slashes = slashes.test(loc.href);
    }
  }

  return finaldestination;
}

/**
 * @typedef ProtocolExtract
 * @type Object
 * @property {String} protocol Protocol matched in the URL, in lowercase.
 * @property {Boolean} slashes `true` if protocol is followed by "//", else `false`.
 * @property {String} rest Rest of the URL that is not part of the protocol.
 */

/**
 * Extract protocol information from a URL with/without double slash ("//").
 *
 * @param {String} address URL we want to extract from.
 * @return {ProtocolExtract} Extracted information.
 * @api private
 */
function extractProtocol(address) {
  var match = protocolre.exec(address);

  return {
    protocol: match[1] ? match[1].toLowerCase() : '',
    slashes: !!match[2],
    rest: match[3]
  };
}

/**
 * Resolve a relative URL pathname against a base URL pathname.
 *
 * @param {String} relative Pathname of the relative URL.
 * @param {String} base Pathname of the base URL.
 * @return {String} Resolved pathname.
 * @api private
 */
function resolve(relative, base) {
  var path = (base || '/').split('/').slice(0, -1).concat(relative.split('/')),
      i = path.length,
      last = path[i - 1],
      unshift = false,
      up = 0;

  while (i--) {
    if (path[i] === '.') {
      path.splice(i, 1);
    } else if (path[i] === '..') {
      path.splice(i, 1);
      up++;
    } else if (up) {
      if (i === 0) unshift = true;
      path.splice(i, 1);
      up--;
    }
  }

  if (unshift) path.unshift('');
  if (last === '.' || last === '..') path.push('');

  return path.join('/');
}

/**
 * The actual URL instance. Instead of returning an object we've opted-in to
 * create an actual constructor as it's much more memory efficient and
 * faster and it pleases my OCD.
 *
 * @constructor
 * @param {String} address URL we want to parse.
 * @param {Object|String} location Location defaults for relative paths.
 * @param {Boolean|Function} parser Parser for the query string.
 * @api public
 */
function URL(address, location, parser) {
  if (!(this instanceof URL)) {
    return new URL(address, location, parser);
  }

  var relative,
      extracted,
      parse,
      instruction,
      index,
      key,
      instructions = rules.slice(),
      type = typeof location === 'undefined' ? 'undefined' : _typeof(location),
      url = this,
      i = 0;

  //
  // The following if statements allows this module two have compatibility with
  // 2 different API:
  //
  // 1. Node.js's `url.parse` api which accepts a URL, boolean as arguments
  //    where the boolean indicates that the query string should also be parsed.
  //
  // 2. The `URL` interface of the browser which accepts a URL, object as
  //    arguments. The supplied object will be used as default values / fall-back
  //    for relative paths.
  //
  if ('object' !== type && 'string' !== type) {
    parser = location;
    location = null;
  }

  if (parser && 'function' !== typeof parser) parser = qs.parse;

  location = lolcation(location);

  //
  // Extract protocol information before running the instructions.
  //
  extracted = extractProtocol(address || '');
  relative = !extracted.protocol && !extracted.slashes;
  url.slashes = extracted.slashes || relative && location.slashes;
  url.protocol = extracted.protocol || location.protocol || '';
  address = extracted.rest;

  //
  // When the authority component is absent the URL starts with a path
  // component.
  //
  if (!extracted.slashes) instructions[2] = [/(.*)/, 'pathname'];

  for (; i < instructions.length; i++) {
    instruction = instructions[i];
    parse = instruction[0];
    key = instruction[1];

    if (parse !== parse) {
      url[key] = address;
    } else if ('string' === typeof parse) {
      if (~(index = address.indexOf(parse))) {
        if ('number' === typeof instruction[2]) {
          url[key] = address.slice(0, index);
          address = address.slice(index + instruction[2]);
        } else {
          url[key] = address.slice(index);
          address = address.slice(0, index);
        }
      }
    } else if (index = parse.exec(address)) {
      url[key] = index[1];
      address = address.slice(0, index.index);
    }

    url[key] = url[key] || (relative && instruction[3] ? location[key] || '' : '');

    //
    // Hostname, host and protocol should be lowercased so they can be used to
    // create a proper `origin`.
    //
    if (instruction[4]) url[key] = url[key].toLowerCase();
  }

  //
  // Also parse the supplied query string in to an object. If we're supplied
  // with a custom parser as function use that instead of the default build-in
  // parser.
  //
  if (parser) url.query = parser(url.query);

  //
  // If the URL is relative, resolve the pathname against the base URL.
  //
  if (relative && location.slashes && url.pathname.charAt(0) !== '/' && (url.pathname !== '' || location.pathname !== '')) {
    url.pathname = resolve(url.pathname, location.pathname);
  }

  //
  // We should not add port numbers if they are already the default port number
  // for a given protocol. As the host also contains the port number we're going
  // override it with the hostname which contains no port number.
  //
  if (!required(url.port, url.protocol)) {
    url.host = url.hostname;
    url.port = '';
  }

  //
  // Parse down the `auth` for the username and password.
  //
  url.username = url.password = '';
  if (url.auth) {
    instruction = url.auth.split(':');
    url.username = instruction[0] || '';
    url.password = instruction[1] || '';
  }

  url.origin = url.protocol && url.host && url.protocol !== 'file:' ? url.protocol + '//' + url.host : 'null';

  //
  // The href is just the compiled result.
  //
  url.href = url.toString();
}

/**
 * This is convenience method for changing properties in the URL instance to
 * insure that they all propagate correctly.
 *
 * @param {String} part          Property we need to adjust.
 * @param {Mixed} value          The newly assigned value.
 * @param {Boolean|Function} fn  When setting the query, it will be the function
 *                               used to parse the query.
 *                               When setting the protocol, double slash will be
 *                               removed from the final url if it is true.
 * @returns {URL}
 * @api public
 */
function set(part, value, fn) {
  var url = this;

  switch (part) {
    case 'query':
      if ('string' === typeof value && value.length) {
        value = (fn || qs.parse)(value);
      }

      url[part] = value;
      break;

    case 'port':
      url[part] = value;

      if (!required(value, url.protocol)) {
        url.host = url.hostname;
        url[part] = '';
      } else if (value) {
        url.host = url.hostname + ':' + value;
      }

      break;

    case 'hostname':
      url[part] = value;

      if (url.port) value += ':' + url.port;
      url.host = value;
      break;

    case 'host':
      url[part] = value;

      if (/:\d+$/.test(value)) {
        value = value.split(':');
        url.port = value.pop();
        url.hostname = value.join(':');
      } else {
        url.hostname = value;
        url.port = '';
      }

      break;

    case 'protocol':
      url.protocol = value.toLowerCase();
      url.slashes = !fn;
      break;

    case 'pathname':
      url.pathname = value.length && value.charAt(0) !== '/' ? '/' + value : value;

      break;

    default:
      url[part] = value;
  }

  for (var i = 0; i < rules.length; i++) {
    var ins = rules[i];

    if (ins[4]) url[ins[1]] = url[ins[1]].toLowerCase();
  }

  url.origin = url.protocol && url.host && url.protocol !== 'file:' ? url.protocol + '//' + url.host : 'null';

  url.href = url.toString();

  return url;
}

/**
 * Transform the properties back in to a valid and full URL string.
 *
 * @param {Function} stringify Optional query stringify function.
 * @returns {String}
 * @api public
 */
function toString(stringify) {
  if (!stringify || 'function' !== typeof stringify) stringify = qs.stringify;

  var query,
      url = this,
      protocol = url.protocol;

  if (protocol && protocol.charAt(protocol.length - 1) !== ':') protocol += ':';

  var result = protocol + (url.slashes ? '//' : '');

  if (url.username) {
    result += url.username;
    if (url.password) result += ':' + url.password;
    result += '@';
  }

  result += url.host + url.pathname;

  query = 'object' === _typeof(url.query) ? stringify(url.query) : url.query;
  if (query) result += '?' !== query.charAt(0) ? '?' + query : query;

  if (url.hash) result += url.hash;

  return result;
}

URL.prototype = { set: set, toString: toString };

//
// Expose the URL parser and some additional properties that might be useful for
// others or testing.
//
URL.extractProtocol = extractProtocol;
URL.location = lolcation;
URL.qs = qs;

module.exports = URL;
/* WEBPACK VAR INJECTION */}.call(exports, __webpack_require__(7)))

/***/ }),
/* 7 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function (obj) { return typeof obj; } : function (obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; };

var g;

// This works in non-strict mode
g = function () {
	return this;
}();

try {
	// This works if eval is allowed (see CSP)
	g = g || Function("return this")() || (1, eval)("this");
} catch (e) {
	// This works if the window reference is available
	if ((typeof window === "undefined" ? "undefined" : _typeof(window)) === "object") g = window;
}

// g can still be undefined, but nothing to do about it...
// We return undefined, instead of nothing here, so it's
// easier to handle this case. if(!global) { ...}

module.exports = g;

/***/ }),
/* 8 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


/**
 * Check if we're required to add a port number.
 *
 * @see https://url.spec.whatwg.org/#default-port
 * @param {Number|String} port Port number we need to check
 * @param {String} protocol Protocol we need to check against.
 * @returns {Boolean} Is it a default port for the given protocol
 * @api private
 */

module.exports = function required(port, protocol) {
  protocol = protocol.split(':')[0];
  port = +port;

  if (!port) return false;

  switch (protocol) {
    case 'http':
    case 'ws':
      return port !== 80;

    case 'https':
    case 'wss':
      return port !== 443;

    case 'ftp':
      return port !== 21;

    case 'gopher':
      return port !== 70;

    case 'file':
      return false;
  }

  return port !== 0;
};

/***/ }),
/* 9 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


var has = Object.prototype.hasOwnProperty;

/**
 * Decode a URI encoded string.
 *
 * @param {String} input The URI encoded string.
 * @returns {String} The decoded string.
 * @api private
 */
function decode(input) {
  return decodeURIComponent(input.replace(/\+/g, ' '));
}

/**
 * Simple query string parser.
 *
 * @param {String} query The query string that needs to be parsed.
 * @returns {Object}
 * @api public
 */
function querystring(query) {
  var parser = /([^=?&]+)=?([^&]*)/g,
      result = {},
      part;

  //
  // Little nifty parsing hack, leverage the fact that RegExp.exec increments
  // the lastIndex property so we can continue executing this loop until we've
  // parsed all results.
  //
  for (; part = parser.exec(query); result[decode(part[1])] = decode(part[2])) {}

  return result;
}

/**
 * Transform a query string to an object.
 *
 * @param {Object} obj Object that should be transformed.
 * @param {String} prefix Optional prefix.
 * @returns {String}
 * @api public
 */
function querystringify(obj, prefix) {
  prefix = prefix || '';

  var pairs = [];

  //
  // Optionally prefix with a '?' if needed
  //
  if ('string' !== typeof prefix) prefix = '?';

  for (var key in obj) {
    if (has.call(obj, key)) {
      pairs.push(encodeURIComponent(key) + '=' + encodeURIComponent(obj[key]));
    }
  }

  return pairs.length ? prefix + pairs.join('&') : '';
}

//
// Expose the module.
//
exports.stringify = querystringify;
exports.parse = querystring;

/***/ }),
/* 10 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});

var _index = __webpack_require__(11);

Object.defineProperty(exports, 'default', {
  enumerable: true,
  get: function get() {
    return _interopRequireDefault(_index).default;
  }
});

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

/***/ }),
/* 11 */
/***/ (function(module, exports, __webpack_require__) {

var __vue_exports__, __vue_options__
var __vue_styles__ = []

/* script */
__vue_exports__ = __webpack_require__(12)

/* template */
var __vue_template__ = __webpack_require__(13)
__vue_options__ = __vue_exports__ = __vue_exports__ || {}
if (
  typeof __vue_exports__.default === "object" ||
  typeof __vue_exports__.default === "function"
) {
if (Object.keys(__vue_exports__).some(function (key) { return key !== "default" && key !== "__esModule" })) {console.error("named exports are not supported in *.vue files.")}
__vue_options__ = __vue_exports__ = __vue_exports__.default
}
if (typeof __vue_options__ === "function") {
  __vue_options__ = __vue_options__.options
}
__vue_options__.__file = "F:\\WebstormProjectsWeex\\weexui-demo\\demo1\\test12\\node_modules\\weex-ui\\packages\\wxc-part-loading\\index.vue"
__vue_options__.render = __vue_template__.render
__vue_options__.staticRenderFns = __vue_template__.staticRenderFns
__vue_options__.style = __vue_options__.style || {}
__vue_styles__.forEach(function (module) {
  for (var name in module) {
    __vue_options__.style[name] = module[name]
  }
})
if (typeof __register_static_styles__ === "function") {
  __register_static_styles__(__vue_options__._scopeId, __vue_styles__)
}

module.exports = __vue_exports__


/***/ }),
/* 12 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});

var _type = __webpack_require__(1);

exports.default = {
  props: {
    show: {
      type: Boolean,
      default: false
    },
    width: {
      type: [Number, String],
      default: 36
    },
    height: {
      type: [Number, String],
      default: 36
    }
  },
  data: function data() {
    return {
      PART: _type.PART
    };
  },
  computed: {
    loadingStyle: function loadingStyle() {
      var height = this.height,
          width = this.width;

      return {
        height: height + 'px',
        width: width + 'px'
      };
    }
  }
}; //
//
//
//
//
//
//
//
//
//
//
//
//
//
//

/***/ }),
/* 13 */
/***/ (function(module, exports) {

module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', [(_vm.show) ? _c('image', {
    style: _vm.loadingStyle,
    attrs: {
      "src": _vm.PART,
      "resize": "contain",
      "quality": "original"
    }
  }) : _vm._e()])
},staticRenderFns: []}
module.exports.render._withStripped = true

/***/ }),
/* 14 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});

var _index = __webpack_require__(15);

Object.defineProperty(exports, 'default', {
  enumerable: true,
  get: function get() {
    return _interopRequireDefault(_index).default;
  }
});

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

/***/ }),
/* 15 */
/***/ (function(module, exports, __webpack_require__) {

var __vue_exports__, __vue_options__
var __vue_styles__ = []

/* styles */
__vue_styles__.push(__webpack_require__(16)
)

/* script */
__vue_exports__ = __webpack_require__(17)

/* template */
var __vue_template__ = __webpack_require__(18)
__vue_options__ = __vue_exports__ = __vue_exports__ || {}
if (
  typeof __vue_exports__.default === "object" ||
  typeof __vue_exports__.default === "function"
) {
if (Object.keys(__vue_exports__).some(function (key) { return key !== "default" && key !== "__esModule" })) {console.error("named exports are not supported in *.vue files.")}
__vue_options__ = __vue_exports__ = __vue_exports__.default
}
if (typeof __vue_options__ === "function") {
  __vue_options__ = __vue_options__.options
}
__vue_options__.__file = "F:\\WebstormProjectsWeex\\weexui-demo\\demo1\\test12\\node_modules\\weex-ui\\packages\\wxc-loading\\index.vue"
__vue_options__.render = __vue_template__.render
__vue_options__.staticRenderFns = __vue_template__.staticRenderFns
__vue_options__._scopeId = "data-v-23ef59de"
__vue_options__.style = __vue_options__.style || {}
__vue_styles__.forEach(function (module) {
  for (var name in module) {
    __vue_options__.style[name] = module[name]
  }
})
if (typeof __register_static_styles__ === "function") {
  __register_static_styles__(__vue_options__._scopeId, __vue_styles__)
}

module.exports = __vue_exports__


/***/ }),
/* 16 */
/***/ (function(module, exports) {

module.exports = {
  "wxc-loading": {
    "position": "fixed",
    "left": "287",
    "top": "500",
    "zIndex": 9999
  },
  "loading-box": {
    "alignItems": "center",
    "justifyContent": "center",
    "borderRadius": "20",
    "width": "175",
    "height": "175",
    "backgroundColor": "rgba(0,0,0,0.8)"
  },
  "trip-loading": {
    "backgroundColor": "rgba(0,0,0,0.2)"
  },
  "loading-trip-image": {
    "height": "75",
    "width": "75"
  },
  "loading-text": {
    "color": "#ffffff",
    "fontSize": "24",
    "lineHeight": "30",
    "height": "30",
    "marginTop": "8",
    "textOverflow": "ellipsis",
    "width": "140",
    "textAlign": "center"
  }
}

/***/ }),
/* 17 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});

var _type = __webpack_require__(1);

var _utils = __webpack_require__(0);

var _utils2 = _interopRequireDefault(_utils);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//

exports.default = {
  props: {
    show: {
      type: Boolean,
      default: false
    },
    loadingText: {
      type: String,
      default: ''
    },
    type: {
      type: String,
      default: 'default'
    },
    interval: {
      type: [Number, String],
      default: 0
    }
  },
  data: function data() {
    return {
      showLoading: false,
      tid: 0
    };
  },
  computed: {
    showText: function showText() {
      return this.loadingText;
    },
    loading: function loading() {
      var loading = {};
      switch (this.type) {
        case 'trip':
          loading = {
            url: _type.GIF,
            class: 'trip-loading'
          };
          break;
        default:
          loading = {
            url: _type.BLACK_GIF,
            class: 'default-loading'
          };
      }
      return loading;
    },
    topPosition: function topPosition() {
      return (_utils2.default.env.getPageHeight() - 200) / 2;
    },
    needShow: function needShow() {
      this.setShow();
      return this.show;
    }
  },
  methods: {
    setShow: function setShow() {
      var _this = this;

      var interval = this.interval,
          show = this.show,
          showLoading = this.showLoading;

      var stInterval = parseInt(interval);
      clearTimeout(this.tid);
      if (show) {
        if (showLoading) {
          return;
        }
        if (stInterval === 0) {
          this.showLoading = true;
        } else {
          this.tid = setTimeout(function () {
            _this.showLoading = true;
          }, stInterval);
        }
      } else {
        this.showLoading = false;
      }
    }
  }
};

/***/ }),
/* 18 */
/***/ (function(module, exports) {

module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', {
    attrs: {
      "hackShow": _vm.needShow
    }
  }, [(_vm.showLoading) ? _c('div', {
    staticClass: ["wxc-loading"],
    style: {
      top: _vm.topPosition + 'px'
    }
  }, [_c('div', {
    class: ['loading-box', _vm.loading.class],
    attrs: {
      "ariaHidden": true
    }
  }, [_c('image', {
    staticClass: ["loading-trip-image"],
    attrs: {
      "src": _vm.loading.url,
      "resize": "contain",
      "quality": "original"
    }
  }), (_vm.loadingText) ? _c('text', {
    staticClass: ["loading-text"]
  }, [_vm._v(_vm._s(_vm.loadingText))]) : _vm._e()])]) : _vm._e()])
},staticRenderFns: []}
module.exports.render._withStripped = true

/***/ }),
/* 19 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});

var _index = __webpack_require__(20);

Object.defineProperty(exports, 'default', {
  enumerable: true,
  get: function get() {
    return _interopRequireDefault(_index).default;
  }
});

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

/***/ }),
/* 20 */
/***/ (function(module, exports, __webpack_require__) {

var __vue_exports__, __vue_options__
var __vue_styles__ = []

/* styles */
__vue_styles__.push(__webpack_require__(21)
)

/* script */
__vue_exports__ = __webpack_require__(22)

/* template */
var __vue_template__ = __webpack_require__(23)
__vue_options__ = __vue_exports__ = __vue_exports__ || {}
if (
  typeof __vue_exports__.default === "object" ||
  typeof __vue_exports__.default === "function"
) {
if (Object.keys(__vue_exports__).some(function (key) { return key !== "default" && key !== "__esModule" })) {console.error("named exports are not supported in *.vue files.")}
__vue_options__ = __vue_exports__ = __vue_exports__.default
}
if (typeof __vue_options__ === "function") {
  __vue_options__ = __vue_options__.options
}
__vue_options__.__file = "F:\\WebstormProjectsWeex\\weexui-demo\\demo1\\test12\\node_modules\\weex-ui\\packages\\wxc-cell\\index.vue"
__vue_options__.render = __vue_template__.render
__vue_options__.staticRenderFns = __vue_template__.staticRenderFns
__vue_options__._scopeId = "data-v-6e03cedc"
__vue_options__.style = __vue_options__.style || {}
__vue_styles__.forEach(function (module) {
  for (var name in module) {
    __vue_options__.style[name] = module[name]
  }
})
if (typeof __register_static_styles__ === "function") {
  __register_static_styles__(__vue_options__._scopeId, __vue_styles__)
}

module.exports = __vue_exports__


/***/ }),
/* 21 */
/***/ (function(module, exports) {

module.exports = {
  "wxc-cell": {
    "height": "100",
    "flexDirection": "row",
    "alignItems": "center",
    "paddingLeft": "24",
    "paddingRight": "24",
    "backgroundColor": "#ffffff"
  },
  "cell-margin": {
    "marginBottom": "24"
  },
  "cell-title": {
    "flex": 1
  },
  "cell-indent": {
    "paddingBottom": "30",
    "paddingTop": "30"
  },
  "has-desc": {
    "paddingBottom": "18",
    "paddingTop": "18"
  },
  "cell-top-border": {
    "borderTopColor": "#e2e2e2",
    "borderTopWidth": "1"
  },
  "cell-bottom-border": {
    "borderBottomColor": "#e2e2e2",
    "borderBottomWidth": "1"
  },
  "cell-label-text": {
    "fontSize": "30",
    "color": "#666666",
    "width": "188",
    "marginRight": "10"
  },
  "cell-arrow-icon": {
    "width": "22",
    "height": "22"
  },
  "cell-content": {
    "color": "#333333",
    "fontSize": "30",
    "lineHeight": "40"
  },
  "cell-desc-text": {
    "color": "#999999",
    "fontSize": "24",
    "lineHeight": "30",
    "marginTop": "4"
  }
}

/***/ }),
/* 22 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});

var _utils = __webpack_require__(0);

var _utils2 = _interopRequireDefault(_utils);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

exports.default = {
  props: {
    label: {
      type: String,
      default: ''
    },
    title: {
      type: String,
      default: ''
    },
    desc: {
      type: String,
      default: ''
    },
    link: {
      type: String,
      default: ''
    },
    hasTopBorder: {
      type: Boolean,
      default: false
    },
    hasMargin: {
      type: Boolean,
      default: false
    },
    hasBottomBorder: {
      type: Boolean,
      default: true
    },
    hasArrow: {
      type: Boolean,
      default: false
    },
    arrowIcon: {
      type: String,
      default: 'https://gw.alicdn.com/tfs/TB11zBUpwMPMeJjy1XbXXcwxVXa-22-22.png'
    },
    hasVerticalIndent: {
      type: Boolean,
      default: true
    },
    cellStyle: {
      type: Object,
      default: function _default() {
        return {};
      }
    },
    autoAccessible: {
      type: Boolean,
      default: true
    }
  },
  methods: {
    cellClicked: function cellClicked(e) {
      var link = this.link;
      this.$emit('wxcCellClicked', { e: e });
      link && _utils2.default.goToH5Page(link, true);
    }
  }
}; //
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//

/***/ }),
/* 23 */
/***/ (function(module, exports) {

module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', {
    class: ['wxc-cell', _vm.hasTopBorder && 'cell-top-border', _vm.hasBottomBorder && 'cell-bottom-border', _vm.hasMargin && 'cell-margin', _vm.hasVerticalIndent && 'cell-indent', _vm.desc && 'has-desc'],
    style: _vm.cellStyle,
    attrs: {
      "accessible": _vm.autoAccessible,
      "ariaLabel": (_vm.label + "," + _vm.title + "," + _vm.desc)
    },
    on: {
      "click": _vm.cellClicked
    }
  }, [_vm._t("label", [(_vm.label) ? _c('div', [_c('text', {
    staticClass: ["cell-label-text"]
  }, [_vm._v(_vm._s(_vm.label))])]) : _vm._e()]), _c('div', {
    staticClass: ["cell-title"]
  }, [_vm._t("title", [_c('text', {
    staticClass: ["cell-content"]
  }, [_vm._v(_vm._s(_vm.title))]), (_vm.desc) ? _c('text', {
    staticClass: ["cell-desc-text"]
  }, [_vm._v(_vm._s(_vm.desc))]) : _vm._e()])], 2), _vm._t("value"), _vm._t("default"), (_vm.hasArrow) ? _c('image', {
    staticClass: ["cell-arrow-icon"],
    attrs: {
      "src": _vm.arrowIcon,
      "ariaHidden": true
    }
  }) : _vm._e()], 2)
},staticRenderFns: []}
module.exports.render._withStripped = true

/***/ }),
/* 24 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});

var _index = __webpack_require__(25);

Object.defineProperty(exports, 'default', {
  enumerable: true,
  get: function get() {
    return _interopRequireDefault(_index).default;
  }
});

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

/***/ }),
/* 25 */
/***/ (function(module, exports, __webpack_require__) {

var __vue_exports__, __vue_options__
var __vue_styles__ = []

/* styles */
__vue_styles__.push(__webpack_require__(26)
)

/* script */
__vue_exports__ = __webpack_require__(27)

/* template */
var __vue_template__ = __webpack_require__(28)
__vue_options__ = __vue_exports__ = __vue_exports__ || {}
if (
  typeof __vue_exports__.default === "object" ||
  typeof __vue_exports__.default === "function"
) {
if (Object.keys(__vue_exports__).some(function (key) { return key !== "default" && key !== "__esModule" })) {console.error("named exports are not supported in *.vue files.")}
__vue_options__ = __vue_exports__ = __vue_exports__.default
}
if (typeof __vue_options__ === "function") {
  __vue_options__ = __vue_options__.options
}
__vue_options__.__file = "F:\\WebstormProjectsWeex\\weexui-demo\\demo1\\test12\\node_modules\\weex-ui\\packages\\wxc-tab-bar\\index.vue"
__vue_options__.render = __vue_template__.render
__vue_options__.staticRenderFns = __vue_template__.staticRenderFns
__vue_options__._scopeId = "data-v-ba9b4886"
__vue_options__.style = __vue_options__.style || {}
__vue_styles__.forEach(function (module) {
  for (var name in module) {
    __vue_options__.style[name] = module[name]
  }
})
if (typeof __register_static_styles__ === "function") {
  __register_static_styles__(__vue_options__._scopeId, __vue_styles__)
}

module.exports = __vue_exports__


/***/ }),
/* 26 */
/***/ (function(module, exports) {

module.exports = {
  "wxc-tab-page": {
    "position": "absolute",
    "top": 0,
    "left": 0,
    "right": 0,
    "bottom": 0
  },
  "tab-title-list": {
    "flexDirection": "row",
    "justifyContent": "space-around"
  },
  "title-item": {
    "justifyContent": "center",
    "alignItems": "center",
    "borderBottomStyle": "solid"
  },
  "tab-page-wrap": {
    "width": "750",
    "flex": 1
  },
  "tab-container": {
    "flex": 1,
    "flexDirection": "row",
    "position": "absolute"
  },
  "tab-text": {
    "lines": 1,
    "textOverflow": "ellipsis"
  },
  "desc-tag": {
    "position": "absolute",
    "top": "10",
    "right": "20",
    "borderBottomRightRadius": "14",
    "borderBottomLeftRadius": 0,
    "borderTopLeftRadius": "14",
    "borderTopRightRadius": "14",
    "backgroundColor": "#FF5E00",
    "height": "26",
    "alignItems": "center",
    "justifyContent": "center",
    "paddingLeft": "6",
    "paddingRight": "6"
  },
  "dot": {
    "width": "12",
    "height": "12",
    "borderBottomRightRadius": "12",
    "borderBottomLeftRadius": "12",
    "borderTopLeftRadius": "12",
    "borderTopRightRadius": "12",
    "position": "absolute",
    "top": "10",
    "right": "40",
    "backgroundColor": "#FF5E00"
  },
  "desc-text": {
    "fontSize": "18",
    "color": "#ffffff"
  },
  "icon-font": {
    "marginBottom": "8"
  }
}

/***/ }),
/* 27 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});

var _utils = __webpack_require__(0);

var _utils2 = _interopRequireDefault(_utils);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//

var dom = weex.requireModule('dom');
var animation = weex.requireModule('animation');
exports.default = {
  props: {
    tabTitles: {
      type: Array,
      default: function _default() {
        return [];
      }
    },
    tabStyles: {
      type: Object,
      default: function _default() {
        return {
          bgColor: '#FFFFFF',
          titleColor: '#666666',
          activeTitleColor: '#3D3D3D',
          activeBgColor: '#FFFFFF',
          isActiveTitleBold: true,
          iconWidth: 70,
          iconHeight: 70,
          width: 160,
          height: 120,
          fontSize: 24,
          activeBottomColor: '#FFC900',
          activeBottomWidth: 120,
          activeBottomHeight: 6,
          textPaddingLeft: 10,
          textPaddingRight: 10
        };
      }
    },
    titleType: {
      type: String,
      default: 'icon'
    },
    titleUseSlot: {
      type: Boolean,
      default: false
    },
    isTabView: {
      type: Boolean,
      default: true
    },
    duration: {
      type: [Number, String],
      default: 300
    },
    timingFunction: {
      type: String,
      default: 'cubic-bezier(0.25, 0.46, 0.45, 0.94)'
    },
    wrapBgColor: {
      type: String,
      default: '#f2f3f4'
    }
  },
  data: function data() {
    return {
      currentPage: 0,
      translateX: 0
    };
  },
  created: function created() {
    var titleType = this.titleType,
        tabStyles = this.tabStyles;

    if (titleType === 'iconFont' && tabStyles.iconFontUrl) {
      dom.addRule('fontFace', {
        'fontFamily': "wxcIconFont",
        'src': 'url(\'' + tabStyles.iconFontUrl + '\')'
      });
    }
    this.isIPhoneX = _utils2.default.env.isIPhoneX();
  },

  methods: {
    next: function next() {
      var page = this.currentPage;
      if (page < this.tabTitles.length - 1) {
        page++;
      }
      this.setPage(page);
    },
    prev: function prev() {
      var page = this.currentPage;
      if (page > 0) {
        page--;
      }
      this.setPage(page);
    },
    setPage: function setPage(page) {
      var url = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : null;
      var animated = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : true;

      if (!this.isTabView) {
        this.jumpOut(url);
        return;
      }
      var previousPage = this.currentPage;
      var currentTabEl = this.$refs['wxc-tab-title-' + page][0];
      var width = this.tabStyles.width;

      var appearNum = parseInt(750 / width);
      var tabsNum = this.tabTitles.length;
      var offset = page > appearNum ? -(750 - width) / 2 : -width * 2;

      if (appearNum < tabsNum) {
        (previousPage > appearNum || page > 1) && dom.scrollToElement(currentTabEl, {
          offset: offset, animated: animated
        });

        page <= 1 && previousPage > page && dom.scrollToElement(currentTabEl, {
          offset: -width * page,
          animated: animated
        });
      }

      this.currentPage = page;
      this._animateTransformX(page, animated);
      this.$emit('wxcTabBarCurrentTabSelected', { page: page });
    },
    jumpOut: function jumpOut(url) {
      url && _utils2.default.goToH5Page(url);
    },
    _animateTransformX: function _animateTransformX(page, animated) {
      var duration = this.duration,
          timingFunction = this.timingFunction;

      var computedDur = animated ? duration : 0.00001;
      var containerEl = this.$refs['tab-container'];
      var dist = page * 750;
      animation.transition(containerEl, {
        styles: {
          transform: 'translateX(' + -dist + 'px)'
        },
        duration: computedDur,
        timingFunction: timingFunction,
        delay: 0
      }, function () {});
    }
  }
};

/***/ }),
/* 28 */
/***/ (function(module, exports) {

module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', {
    staticClass: ["wxc-tab-page"],
    style: {
      backgroundColor: _vm.wrapBgColor
    }
  }, [_c('div', {
    ref: "tab-page-wrap",
    staticClass: ["tab-page-wrap"]
  }, [_c('div', {
    ref: "tab-container",
    staticClass: ["tab-container"]
  }, [_vm._t("default")], 2)]), _c('div', {
    staticClass: ["tab-title-list"],
    style: {
      backgroundColor: _vm.tabStyles.bgColor,
      height: (_vm.tabStyles.height + (_vm.isIPhoneX ? 78 : 0)) + 'px',
      paddingBottom: _vm.isIPhoneX ? '78px' : '0'
    }
  }, [_vm._l((_vm.tabTitles), function(v, index) {
    return _c('div', {
      key: index,
      ref: 'wxc-tab-title-' + index,
      refInFor: true,
      staticClass: ["title-item"],
      style: {
        width: _vm.tabStyles.width + 'px',
        height: _vm.tabStyles.height + 'px',
        backgroundColor: _vm.currentPage == index ? _vm.tabStyles.activeBgColor : _vm.tabStyles.bgColor
      },
      attrs: {
        "accessible": true,
        "ariaLabel": ("" + (v.title?v.title:'标签'+index))
      },
      on: {
        "click": function($event) {
          _vm.setPage(index, v.url)
        }
      }
    }, [(_vm.titleType === 'icon' && !_vm.titleUseSlot) ? _c('image', {
      style: {
        width: _vm.tabStyles.iconWidth + 'px',
        height: _vm.tabStyles.iconHeight + 'px'
      },
      attrs: {
        "src": _vm.currentPage == index ? v.activeIcon : v.icon
      }
    }) : _vm._e(), (_vm.titleType === 'iconFont' && v.codePoint && !_vm.titleUseSlot) ? _c('text', {
      staticClass: ["icon-font"],
      style: {
        fontFamily: 'wxcIconFont',
        fontSize: _vm.tabStyles.iconFontSize + 'px',
        color: _vm.currentPage == index ? _vm.tabStyles.activeIconFontColor : _vm.tabStyles.iconFontColor
      }
    }, [_vm._v(_vm._s(v.codePoint))]) : _vm._e(), (!_vm.titleUseSlot) ? _c('text', {
      staticClass: ["tab-text"],
      style: {
        fontSize: _vm.tabStyles.fontSize + 'px',
        fontWeight: (_vm.currentPage == index && _vm.tabStyles.isActiveTitleBold) ? 'bold' : 'normal',
        color: _vm.currentPage == index ? _vm.tabStyles.activeTitleColor : _vm.tabStyles.titleColor,
        paddingLeft: _vm.tabStyles.textPaddingLeft + 'px',
        paddingRight: _vm.tabStyles.textPaddingRight + 'px'
      }
    }, [_vm._v(_vm._s(v.title))]) : _vm._e(), (v.badge && !_vm.titleUseSlot) ? _c('div', {
      staticClass: ["desc-tag"]
    }, [_c('text', {
      staticClass: ["desc-text"]
    }, [_vm._v(_vm._s(v.badge))])]) : _vm._e(), (v.dot && !v.badge && !_vm.titleUseSlot) ? _c('div', {
      staticClass: ["dot"]
    }) : _vm._e()])
  }), (_vm.titleUseSlot) ? _vm._t(("tab-title-" + _vm.index)) : _vm._e()], 2)])
},staticRenderFns: []}
module.exports.render._withStripped = true

/***/ }),
/* 29 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});

var _index = __webpack_require__(30);

Object.defineProperty(exports, 'default', {
  enumerable: true,
  get: function get() {
    return _interopRequireDefault(_index).default;
  }
});

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

/***/ }),
/* 30 */
/***/ (function(module, exports, __webpack_require__) {

var __vue_exports__, __vue_options__
var __vue_styles__ = []

/* styles */
__vue_styles__.push(__webpack_require__(31)
)

/* script */
__vue_exports__ = __webpack_require__(32)

/* template */
var __vue_template__ = __webpack_require__(34)
__vue_options__ = __vue_exports__ = __vue_exports__ || {}
if (
  typeof __vue_exports__.default === "object" ||
  typeof __vue_exports__.default === "function"
) {
if (Object.keys(__vue_exports__).some(function (key) { return key !== "default" && key !== "__esModule" })) {console.error("named exports are not supported in *.vue files.")}
__vue_options__ = __vue_exports__ = __vue_exports__.default
}
if (typeof __vue_options__ === "function") {
  __vue_options__ = __vue_options__.options
}
__vue_options__.__file = "F:\\WebstormProjectsWeex\\weexui-demo\\demo1\\test12\\node_modules\\weex-ui\\packages\\wxc-icon\\index.vue"
__vue_options__.render = __vue_template__.render
__vue_options__.staticRenderFns = __vue_template__.staticRenderFns
__vue_options__._scopeId = "data-v-1d0ed133"
__vue_options__.style = __vue_options__.style || {}
__vue_styles__.forEach(function (module) {
  for (var name in module) {
    __vue_options__.style[name] = module[name]
  }
})
if (typeof __register_static_styles__ === "function") {
  __register_static_styles__(__vue_options__._scopeId, __vue_styles__)
}

module.exports = __vue_exports__


/***/ }),
/* 31 */
/***/ (function(module, exports) {

module.exports = {
  "icon-font": {
    "color": "#666666"
  }
}

/***/ }),
/* 32 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});

var _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; }; //
//
//
//
//
//
//
//
//
//
//
//
//

var _type = __webpack_require__(33);

var _type2 = _interopRequireDefault(_type);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var dom = weex.requireModule('dom');

exports.default = {
  props: {
    name: {
      default: 'success',
      type: String
    },
    size: {
      default: 'small',
      type: String
    },
    iconStyle: {
      type: Object,
      default: function _default() {
        return {};
      }
    }
  },
  data: function data() {
    return {
      Icon: _type2.default
    };
  },
  beforeCreate: function beforeCreate() {
    dom.addRule('fontFace', {
      'fontFamily': "weexUiIconFont",
      'src': "url('//at.alicdn.com/t/font_520368_r89ekv69euahsemi.ttf')"
    });
  },

  computed: {
    mergeStyle: function mergeStyle() {
      var iconStyle = this.iconStyle,
          size = this.size;

      var fontSize = '48px';
      switch (size) {
        case 'xs':
          fontSize = '24px';
          break;
        case 'small':
          fontSize = '48px';
          break;
        case 'medium':
          fontSize = '72px';
          break;
        case 'big':
          fontSize = '128px';
          break;
        default:
          fontSize = '48px';
      }
      return _extends({
        fontFamily: 'weexUiIconFont',
        fontSize: fontSize
      }, iconStyle);
    }
  },
  methods: {
    itemClicked: function itemClicked(name) {
      this.$emit('wxcIconClicked', {
        name: name
      });
    }
  }
};

/***/ }),
/* 33 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = {
  less: '\uE6A5',
  'more_unfold': '\uE6A6',
  back: '\uE697',
  more: '\uE6A7',
  add: '\uE6B9',
  subtract: '\uE6FE',
  close: '\uE69A',
  cry: '\uE69C',
  delete: '\uE69D',
  help: '\uE6A3',
  refresh: '\uE6AA',
  search: '\uE6AC',
  success: '\uE6B1',
  warning: '\uE6B6',
  wrong: '\uE6B7',
  clock: '\uE6BB',
  scanning: '\uE6EC',
  filter: '\uE6F1',
  map: '\uE715',
  play: '\uE719'
};

/***/ }),
/* 34 */
/***/ (function(module, exports) {

module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('text', {
    staticClass: ["icon-font"],
    style: _vm.mergeStyle,
    on: {
      "click": function($event) {
        _vm.itemClicked(_vm.name)
      }
    }
  }, [_vm._v(_vm._s(_vm.Icon[_vm.name]))])
},staticRenderFns: []}
module.exports.render._withStripped = true

/***/ }),
/* 35 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});

var _index = __webpack_require__(36);

Object.defineProperty(exports, 'default', {
  enumerable: true,
  get: function get() {
    return _interopRequireDefault(_index).default;
  }
});

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

/***/ }),
/* 36 */
/***/ (function(module, exports, __webpack_require__) {

var __vue_exports__, __vue_options__
var __vue_styles__ = []

/* styles */
__vue_styles__.push(__webpack_require__(37)
)

/* script */
__vue_exports__ = __webpack_require__(38)

/* template */
var __vue_template__ = __webpack_require__(39)
__vue_options__ = __vue_exports__ = __vue_exports__ || {}
if (
  typeof __vue_exports__.default === "object" ||
  typeof __vue_exports__.default === "function"
) {
if (Object.keys(__vue_exports__).some(function (key) { return key !== "default" && key !== "__esModule" })) {console.error("named exports are not supported in *.vue files.")}
__vue_options__ = __vue_exports__ = __vue_exports__.default
}
if (typeof __vue_options__ === "function") {
  __vue_options__ = __vue_options__.options
}
__vue_options__.__file = "F:\\WebstormProjectsWeex\\weexui-demo\\demo1\\test12\\node_modules\\weex-ui\\packages\\wxc-minibar\\index.vue"
__vue_options__.render = __vue_template__.render
__vue_options__.staticRenderFns = __vue_template__.staticRenderFns
__vue_options__._scopeId = "data-v-be3f1604"
__vue_options__.style = __vue_options__.style || {}
__vue_styles__.forEach(function (module) {
  for (var name in module) {
    __vue_options__.style[name] = module[name]
  }
})
if (typeof __register_static_styles__ === "function") {
  __register_static_styles__(__vue_options__._scopeId, __vue_styles__)
}

module.exports = __vue_exports__


/***/ }),
/* 37 */
/***/ (function(module, exports) {

module.exports = {
  "wxc-minibar": {
    "width": "750",
    "height": "90",
    "flexDirection": "row",
    "justifyContent": "space-between",
    "alignItems": "center",
    "backgroundColor": "#009ff0"
  },
  "left": {
    "width": "180",
    "paddingLeft": "32"
  },
  "middle-title": {
    "fontSize": "30",
    "color": "#ffffff",
    "height": "36",
    "lineHeight": "34"
  },
  "right": {
    "width": "180",
    "paddingRight": "32",
    "alignItems": "flex-end"
  },
  "left-button": {
    "width": "21",
    "height": "36"
  },
  "right-button": {
    "width": "32",
    "height": "32"
  },
  "icon-text": {
    "fontSize": "28",
    "color": "#ffffff"
  }
}

/***/ }),
/* 38 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//

var Navigator = weex.requireModule('navigator');
exports.default = {
  props: {
    backgroundColor: {
      type: String,
      default: '#FFC900'
    },
    leftButton: {
      type: String,
      default: 'https://gw.alicdn.com/tfs/TB1x18VpwMPMeJjy1XdXXasrXXa-21-36.png'
    },
    textColor: {
      type: String,
      default: '#3D3D3D'
    },
    rightButton: {
      type: String,
      default: ''
    },
    title: {
      type: String,
      default: '标题'
    },
    leftText: {
      type: String,
      default: ''
    },
    rightText: {
      type: String,
      default: ''
    },
    useDefaultReturn: {
      type: Boolean,
      default: true
    },
    show: {
      type: Boolean,
      default: true
    }
  },
  methods: {
    leftButtonClicked: function leftButtonClicked() {
      var self = this;
      if (self.useDefaultReturn) {
        Navigator.pop({}, function (e) {});
      }
      self.$emit('wxcMinibarLeftButtonClicked', {});
    },
    rightButtonClicked: function rightButtonClicked() {
      var self = this;
      if (self.rightText || self.rightButton) {
        self.$emit('wxcMinibarRightButtonClicked', {});
      }
    }
  }
};

/***/ }),
/* 39 */
/***/ (function(module, exports) {

module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return (_vm.show) ? _c('div', {
    staticClass: ["wxc-minibar"],
    style: {
      backgroundColor: _vm.backgroundColor
    }
  }, [_c('div', {
    staticClass: ["left"],
    attrs: {
      "ariaLabel": "返回",
      "accessible": true
    },
    on: {
      "click": _vm.leftButtonClicked
    }
  }, [_vm._t("left", [(_vm.leftButton && !_vm.leftText) ? _c('image', {
    staticClass: ["left-button"],
    attrs: {
      "src": _vm.leftButton
    }
  }) : _vm._e(), (_vm.leftText) ? _c('text', {
    staticClass: ["icon-text"],
    style: {
      color: _vm.textColor
    }
  }, [_vm._v(_vm._s(_vm.leftText))]) : _vm._e()])], 2), _vm._t("middle", [_c('text', {
    staticClass: ["middle-title"],
    style: {
      color: _vm.textColor
    }
  }, [_vm._v(_vm._s(_vm.title))])]), _c('div', {
    staticClass: ["right"],
    on: {
      "click": _vm.rightButtonClicked
    }
  }, [_vm._t("right", [(_vm.rightButton && !_vm.rightText) ? _c('image', {
    staticClass: ["right-button"],
    attrs: {
      "src": _vm.rightButton,
      "ariaHidden": true
    }
  }) : _vm._e(), (_vm.rightText) ? _c('text', {
    staticClass: ["icon-text"],
    style: {
      color: _vm.textColor
    }
  }, [_vm._v(_vm._s(_vm.rightText))]) : _vm._e()])], 2)], 2) : _vm._e()
},staticRenderFns: []}
module.exports.render._withStripped = true

/***/ }),
/* 40 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
    value: true
});

var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function (obj) { return typeof obj; } : function (obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; };

exports.getTool = getTool;
exports.getHttp = getHttp;
exports.init = init;
exports.getConfig = getConfig;
exports.getToken = getToken;
exports.login = login;
exports.isSumslackEnv = isSumslackEnv;
exports.layoutNaviBar = layoutNaviBar;
exports.getSumslack = getSumslack;
exports.close = close;
exports.openNativeUI = openNativeUI;
exports.openProfile = openProfile;
exports.makePhoneCall = makePhoneCall;
exports.scanQrCode = scanQrCode;
exports.share = share;
exports.refresh = refresh;
exports.previewImage = previewImage;
exports.addGlobalEventListener = addGlobalEventListener;
exports.getUserInfo = getUserInfo;
exports.uploadFile = uploadFile;
exports.showActionSheet = showActionSheet;
exports.chooseLocation = chooseLocation;
exports.openLocation = openLocation;
exports.shareWeixin = shareWeixin;
exports.navigateToEmojiPanel = navigateToEmojiPanel;
exports.navigateTo = navigateTo;
exports.redirectTo = redirectTo;
exports.setTitle = setTitle;
exports.selectContact = selectContact;
exports.createGroup = createGroup;
exports.getLocation = getLocation;
exports.chooseImage = chooseImage;
exports.uploadImage = uploadImage;
exports.downloadFile = downloadFile;
exports.picker = picker;
exports.request = request;
exports.sendMessage = sendMessage;
exports.confirm = confirm;
exports.toast = toast;
exports.setClipboardData = setClipboardData;
exports.getClipboardData = getClipboardData;
exports.print = print;
exports.log = log;
exports.toJSON = toJSON;
exports.alert = alert;
exports.clearStorage = clearStorage;
exports.setStorage = setStorage;
exports.removeStorage = removeStorage;
exports.getStorage = getStorage;
exports.prompt = prompt;
exports.niceTime = niceTime;
exports.dateFormat = dateFormat;
var config = __webpack_require__(2);
var globalEvent = weex.requireModule('globalEvent');
var sumslack = weex.requireModule('event');
var modal = weex.requireModule('modal');
var clipboard = weex.requireModule('clipboard');
var storage = weex.requireModule('storage');
var stream = weex.requireModule('stream');

var getBaseURL = __webpack_require__(41).getBaseURL;

var navigator = weex.requireModule('navigator');

var Http = {
    getUrlParam: function getUrlParam(obj, key) {
        var url = "";
        if ((typeof obj === 'undefined' ? 'undefined' : _typeof(obj)) === "object") {
            url = obj.$getConfig().bundleUrl;
        } else {
            url = obj;
        }
        var reg = new RegExp('[?|&]' + key + '=([^&]+)');
        var match = url.match(reg);
        return match && match[1];
    },
    get: function get(_url, params, callback, err) {
        var me = this;
        if ((typeof params === 'undefined' ? 'undefined' : _typeof(params)) === "object") {
            for (var k in params) {
                console.log(k);
                if (_url.indexOf("?") > 0) {
                    _url += "&" + k + "=" + encodeURIComponent(params[k]);
                } else {
                    _url += "?" + k + "=" + encodeURIComponent(params[k]);
                }
            }
        }
        stream.fetch({
            //headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            method: 'GET',
            url: _url,
            type: 'json'
        }, function (ret) {
            if (typeof callback === "function") {
                callback(ret);
            }
        }, function (response) {
            console.log('http get in progress:' + response.length);
            if (typeof err === "function") {
                err(response);
            }
        });
    },
    post: function post(_url, param, cb) {
        var me = this;
        stream.fetch({
            method: 'POST',
            url: _url,
            type: 'json',
            //body:JSON.stringify({username:'weex'})//or you can just use JSON Object {username:'weex'}
            body: JSON.stringify(param)
        }, function (ret) {
            cb(ret);
        }, function (response) {
            console.log('http post in progress:' + response.length);
        });
    },
    fetch: function fetch(_url, params) {
        if ((typeof params === 'undefined' ? 'undefined' : _typeof(params)) === "object") {
            for (var k in params) {
                console.log(k);
                if (_url.indexOf("?") > 0) {
                    _url += "&" + k + "=" + encodeURIComponent(params[k]);
                } else {
                    _url += "?" + k + "=" + encodeURIComponent(params[k]);
                }
            }
        }
        return new Promise(function (resolve, reject) {
            stream.fetch({
                method: 'GET',
                url: _url,
                type: 'json'
            }, function (response) {
                if (response.status == 200) {
                    resolve(response.data);
                } else {
                    reject(response);
                }
            }, function () {});
        });
    }
};

var Tool = {
    dateFormat: function dateFormat(date, fmt) {
        var o = {
            "M+": date.getMonth() + 1,
            "d+": date.getDate(),
            "h+": date.getHours(),
            "m+": date.getMinutes(),
            "s+": date.getSeconds(),
            "q+": Math.floor((date.getMonth() + 3) / 3),
            "S": date.getMilliseconds()
        };
        if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o) {
            if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
        }return fmt;
    },
    uuid: function uuid() {
        var d = new Date().getTime();
        var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
            var r = (d + Math.random() * 16) % 16 | 0;
            d = Math.floor(d / 16);
            return (c == 'x' ? r : r & 0x7 | 0x8).toString(16);
        });
        return uuid;
    }
};

function getTool() {
    return Tool;
}

function getHttp() {
    return Http;
}

function init(_title, _items, cb) {
    try {
        if (isSumslackEnv()) {
            if (typeof _items != "undefined") {
                sumslack.layoutNaviBar({ "title": _title, "items": _items });
            } else {
                sumslack.layoutNaviBar({ "title": _title });
            }
            getUserInfo().then(function (user) {
                config.Config.cUser = user;
                sumslack.getAuthToken(function (ret) {
                    console.log("==========current token:", ret);
                    config.Config.token = ret;
                    if (typeof cb === "function") {
                        cb();
                    }
                });
            });
            //sumslack.getAllUser("",function(ret) {
            //    ret = JSON.parse(ret);
            //    for(var i in ret){
            //        var _uid = ret[i].userId.replace(/user\_/g,"");
            //        config.Config.allUsers[_uid] = ret[i];
            //    }
            //});
        } else {
            if (typeof cb === "function") {
                cb();
            }
        }
    } catch (e) {
        console.log(e);
    }
    return sumslack;
}
function getConfig() {
    return config.Config;
}
function getToken(cb) {
    if (!isSumslackEnv()) {
        alert(config.Config.notSupport);
        return;
    }
    sumslack.getAuthToken(function (res) {
        if (typeof cb === "function") {
            cb(res);
        }
    });
}
function login(cb) {
    if (!isSumslackEnv()) {
        alert(config.Config.notSupport);
        return;
    }
    sumslack.getAuthToken(function (token) {
        console.log("***sumslack.js***", token);
        sumslack.requestOauth(token, function (res) {
            if (typeof cb === "function") {
                res = toJSON(res);
                cb(res);
            }
        });
    });
}
function isSumslackEnv() {
    return typeof sumslack.layoutNaviBar != "undefined";
}
function layoutNaviBar(param) {
    if (!isSumslackEnv()) {
        alert(config.Config.notSupport);
        return;
    }
    sumslack.layoutNaviBar(param);
}
function getSumslack() {
    return sumslack;
}
function close() {
    if (!isSumslackEnv()) {
        alert(config.Config.notSupport);
        return;
    }
    sumslack.popViewController();
}

function openNativeUI(param) {
    if (!isSumslackEnv()) {
        alert(config.Config.notSupport);
        return;
    }
    sumslack.pushViewController(param);
}

function openProfile(uid) {
    if (!isSumslackEnv()) {
        alert(config.Config.notSupport);
        return;
    }
    sumslack.openProfile(uid);
}

function makePhoneCall(phoneNumber) {
    if (!isSumslackEnv()) {
        alert(config.Config.notSupport);
        return;
    }
    sumslack.makePhoneCall({ 'phoneNumber': phoneNumber });
}

function scanQrCode(cb) {
    if (!isSumslackEnv()) {
        alert(config.Config.notSupport);
        return;
    }
    sumslack.scan(function (ret) {
        if (typeof cb === "function") {
            cb(ret);
        }
    });
}

function share(param, cb) {
    if (!isSumslackEnv()) {
        alert(config.Config.notSupport);
        return;
    }
    sumslack.shareWeixin({
        title: param.title || "标题",
        description: param.description || param.title,
        url: param.url,
        logo: param.logo || ""
    });
}

function refresh() {
    if (!isSumslackEnv()) {
        alert(config.Config.notSupport);
        return;
    }
    sumslack.refresh();
}

function previewImage(param) {
    if (!isSumslackEnv()) {
        alert(config.Config.notSupport);
        return;
    }
    sumslack.previewImage(param);
}

function addGlobalEventListener(funcName, cb) {
    globalEvent.addEventListener(funcName, function (e) {
        cb(e);
    });
}
/*
  avatar:头像地址
  nick:昵称
  departId:部门ID
  id:用户ID
  sex:性别
  name:用户名
  telphone:电话
 */
function getUserInfo() {
    if (!isSumslackEnv()) {
        alert(config.Config.notSupport);
        return;
    }
    return new Promise(function (resolve) {
        sumslack.getRunUserInfo(function (ret) {
            resolve(ret);
        });
    });
}

function uploadFile(filepath, cb) {
    if (!isSumslackEnv()) {
        alert(config.Config.notSupport);
        return;
    }
    sumslack.uploadFile(function (ret) {
        cb(ret);
    });
}

function showActionSheet(actionSheetParam, cb) {
    if (sumslack && isSumslackEnv()) {
        sumslack.showActionSheet(actionSheetParam, function (ret) {
            if (typeof cb === "function") {
                cb(ret);
            }
        });
    } else {
        alert(config.Config.notSupport);
    }
}

function chooseLocation(cb) {
    if (sumslack && isSumslackEnv()) {
        sumslack.chooseLocation(function (ret) {
            if (typeof cb === "function") {
                cb(ret);
            }
        });
    } else {
        alert(config.Config.notSupport);
    }
}

function openLocation(_lat, _lot, building, address) {
    if (sumslack && isSumslackEnv()) {
        sumslack.openLocation({ 'lat': _lat, 'lot': _lot, "building": building, "address": address });
    } else {
        alert(config.Config.notSupport);
    }
}

function shareWeixin(title, url, description, logo) {
    if (sumslack && isSumslackEnv()) {
        if (typeof logo != "undefined") sumslack.shareWeixin({ 'title': title, 'description': description, "url": url, "logo": logo });else sumslack.shareWeixin({ 'title': title, 'description': description, "url": url });
    } else {
        alert(config.Config.notSupport);
    }
}
function navigateToEmojiPanel(title, placeholder, pageId, params) {
    var _url = config.getPageUrl(pageId);
    if ((typeof params === 'undefined' ? 'undefined' : _typeof(params)) === "object") {
        for (var k in params) {
            if (_url.indexOf("?") > 0) {
                _url += "&" + k + "=" + encodeURIComponent(params[k]);
            } else {
                _url += "?" + k + "=" + encodeURIComponent(params[k]);
            }
        }
    }
    _url = getBaseURL(this) + _url;
    if (sumslack && isSumslackEnv()) {
        sumslack.navigateToEmojiPanel({ 'url': _url, 'title': title, 'placeholder': placeholder });
    } else {
        alert(config.Config.notSupport);
    }
}
function navigateTo(pageId, params) {
    var _url = config.getPageUrl(pageId);
    if ((typeof params === 'undefined' ? 'undefined' : _typeof(params)) === "object") {
        for (var k in params) {
            if (_url.indexOf("?") > 0) {
                _url += "&" + k + "=" + encodeURIComponent(params[k]);
            } else {
                _url += "?" + k + "=" + encodeURIComponent(params[k]);
            }
        }
    }
    _url = getBaseURL(this) + _url;
    if (sumslack && isSumslackEnv()) {
        sumslack.navigateTo(_url);
    } else {
        navigator.push({
            url: _url,
            animated: "false"
        }, function (event) {});
    }
}

function redirectTo(pageId, params) {
    var _url = config.getPageUrl(pageId);
    if ((typeof params === 'undefined' ? 'undefined' : _typeof(params)) === "object") {
        for (var k in params) {
            if (_url.indexOf("?") > 0) {
                _url += "&" + k + "=" + encodeURIComponent(params[k]);
            } else {
                _url += "?" + k + "=" + encodeURIComponent(params[k]);
            }
        }
    }
    console.log("======gotopage redirect:", _url);
    _url = getBaseURL(this) + _url;
    if (sumslack && isSumslackEnv()) {
        sumslack.redirectTo(_url);
    } else {
        alert(config.Config.notSupport);
    }
}

function setTitle(title) {
    if (sumslack && isSumslackEnv()) {
        sumslack.setNavigationBarTitle(title);
    } else {
        alert(config.Config.notSupport);
    }
}

function selectContact(selectContactParam, callback) {
    if (sumslack && isSumslackEnv()) {
        sumslack.selectContact(selectContactParam, function (ret) {
            callback(ret);
        });
    } else {
        alert(config.Config.notSupport);
    }
}

function createGroup(group, callback) {
    if (sumslack && isSumslackEnv()) {
        sumslack.createGroup(group, function (ret) {
            callback(ret);
        });
    } else {
        alert(config.Config.notSupport);
    }
}
function getLocation(cb) {
    if (sumslack && isSumslackEnv()) {
        sumslack.getLocation(function (ret) {
            cb(toJSON(ret));
        });
    } else {
        alert(config.Config.notSupport);
    }
}

function chooseImage(count, type, cb) {
    if (!isSumslackEnv()) {
        alert(config.Config.notSupport);
        return;
    }
    sumslack.chooseImage(count, type, function (ret) {
        if (typeof cb === 'function') {
            cb(ret);
        }
    });
}
function uploadImage(filePath, cb) {
    if (!isSumslackEnv()) {
        alert(config.Config.notSupport);
        return;
    }
    sumslack.uploadFile(filePath, function (data) {
        if (typeof cb === 'function') {
            cb(data);
        }
    });
}
function downloadFile(url, cb) {
    if (!isSumslackEnv()) {
        alert(config.Config.notSupport);
        return;
    }
    sumslack.downloadFile(url, function (data) {
        if (typeof cb === 'function') {
            cb(data);
        }
    });
}

function picker(fmt, param, cb) {
    var type = 0;
    if (fmt === 'MMddhhmm' || fmt === 'mmddhhii') type = 1;else if (fmt === 'yyyyMMdd' || fmt === 'yyyymmdd') type = 2;else if (fmt === "MMdd" || fmt === "mmdd") type = 3;else if (fmt === 'hhmm' || fmt === 'hhii') type = 4;
    if (!isSumslackEnv()) {
        alert(config.Config.notSupport);
        return;
    }
    sumslack.pickDateTime({
        'type': type,
        'min': param.min || "",
        'max': param.max || "",
        'current': param.current || ""
    }, function (data) {
        if (typeof cb === "function") {
            cb(data);
        }
    });
}
function request(url, param) {
    return new Promise(function (resolve) {
        Http.fetch(url, param).then(function (data) {
            resolve(data);
        });
    });
}
function sendMessage(type, typeId, msg) {
    if (!isSumslackEnv()) {
        alert(config.Config.notSupport);
        return;
    }
    sumslack.sendMessage(type, typeId, msg);
}
function confirm(msg, cb) {
    modal.confirm({
        message: msg,
        duration: 0.3
    }, function (value) {
        cb(value);
    });
}

function toast(msg) {
    modal.toast({ message: msg });
}
function setClipboardData(msg) {
    clipboard.setString(msg);
}
function getClipboardData(cb) {
    clipboard.getString(function (ret) {
        cb(ret);
    });
}
function print(obj) {
    if ((typeof obj === 'undefined' ? 'undefined' : _typeof(obj)) === "object") {
        return JSON.stringify(obj);
    } else {
        return obj;
    }
}
function log(s, s2, s3) {
    if (typeof s3 != "undefined") console.log("***sumslack log***", s, s2, s3);else if (typeof s2 != "undefined") {
        console.log("***sumslack log***", s, s2);
    } else {
        console.log("***sumslack log***", s);
    }
}
function toJSON(obj) {
    if (typeof obj === "string") {
        if (obj.substring(0, 1) === "{" && obj.substring(obj.length - 1, obj.length) === "}") {
            return JSON.parse(obj);
        } else {
            return obj;
        }
    } else {
        return obj;
    }
}
function alert(msg) {
    modal.alert({
        message: msg,
        duration: 0.3
    });
}

function clearStorage() {
    storage.getAllKeys(function (event) {
        if (event.result === 'success') {
            if (event.data && event.data.length > 0) {
                for (var index in event.data) {
                    storage.removeItem(event.data[index], function (event) {
                        console.log('delete value:', event.data[index]);
                    });
                }
            }
        }
    });
}

function setStorage(key, value, cb) {
    if ((typeof value === 'undefined' ? 'undefined' : _typeof(value)) == "object") {
        value = JSON.stringify(value);
    }
    storage.setItem(key, value, function (e) {
        if (typeof cb === "function") {
            cb(e);
        }
    });
}

function removeStorage(key, cb) {
    storage.removeItem(key, value, function (e) {
        if (typeof cb === "function") {
            cb(e);
        }
    });
}

function getStorage(key, cb, err) {
    storage.getItem(key, function (e) {
        if (e.result === "success") {
            if (typeof cb === "function") {
                cb(toJSON(e.data));
            }
        } else {
            if (typeof err === "function") {
                err(e);
            }
        }
    });
}

function prompt(msg, cb) {
    modal.prompt({
        message: msg,
        duration: 0.3
    }, function (value) {
        cb(value);
    });
}

function niceTime(time_stamp) {
    time_stamp = time_stamp / 1000;
    var now_d = new Date();
    var now_time = now_d.getTime() / 1000; //获取当前时间的秒数
    var f_d = new Date();
    f_d.setTime(time_stamp * 1000);
    var f_time = f_d.toLocaleDateString();

    var ct = now_time - time_stamp;
    var day = 0;
    if (ct < 0) {
        f_time = dateFormat(f_d, "yyyy-MM-dd hh:mm");
    } else if (ct < 5) {
        f_time = '刚刚';
    } else if (ct < 60) {
        f_time = Math.floor(ct) + '秒前';
    } else if (ct < 3600) {
        f_time = Math.floor(ct / 60) + '分钟前';
    } else if (ct < 86400) //一天
        {
            f_time = Math.floor(ct / 3600) + '小时前';
        } else if (ct < 604800) //7天
        {
            day = Math.floor(ct / 86400);
            if (day < 2) f_time = '昨天';else f_time = day + '天前';
        } else {
        day = Math.floor(ct / 86400);
        f_time = day + '天前';
    }
    return f_time;
}

function dateFormat(date, fmt) {
    if (typeof date === "number") {
        date = new Date(date);
    }
    var o = {
        "y+": date.getFullYear(),
        "M+": date.getMonth() + 1, //月份
        "d+": date.getDate(), //日
        "h+": date.getHours(), //小时
        "m+": date.getMinutes(), //分
        "s+": date.getSeconds(), //秒
        "q+": Math.floor((date.getMonth() + 3) / 3), //季度
        "S+": date.getMilliseconds() //毫秒
    };
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(fmt)) {
            if (k == "y+") {
                fmt = fmt.replace(RegExp.$1, ("" + o[k]).substr(4 - RegExp.$1.length));
            } else if (k == "S+") {
                var lens = RegExp.$1.length;
                lens = lens == 1 ? 3 : lens;
                fmt = fmt.replace(RegExp.$1, ("00" + o[k]).substr(("" + o[k]).length - 1, lens));
            } else {
                fmt = fmt.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
            }
        }
    }
    return fmt;
}

/***/ }),
/* 41 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function (obj) { return typeof obj; } : function (obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; };

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
exports.getBaseURL = function () {
    // if(true){
    //    return "http://192.168.1.154:8081/";
    // }

    var bundleUrl = weex.config.bundleUrl;
    console.log("********bundleUrl*************:", bundleUrl);
    var nativeBase;
    var isAndroidAssets = weex.config.env.platform.toLowerCase().indexOf("android") >= 0;
    var isiOSAssets = weex.config.env.platform.toLowerCase().indexOf("ios") >= 0;
    if (isAndroidAssets) {
        nativeBase = 'file://assets/';
    } else if (isiOSAssets) {
        // file:///var/mobile/Containers/Bundle/Application/{id}/WeexDemo.app/
        // file:///Users/{user}/Library/Developer/CoreSimulator/Devices/{id}/data/Containers/Bundle/Application/{id}/WeexDemo.app/
        nativeBase = bundleUrl.substring(0, bundleUrl.lastIndexOf('/') + 1);
    } else {
        var host = 'localhost:12580';
        var matches = /\/\/([^\/]+?)\//.exec(weex.config.bundleUrl);
        if (matches && matches.length >= 2) {
            host = matches[1];
        }
        //nativeBase = 'http://' + host + '/' + vm.dir + '/build/';
        nativeBase = 'http://' + host + '/';
    }
    // in Native
    var base = nativeBase;
    if ((typeof window === "undefined" ? "undefined" : _typeof(window)) === 'object') {
        // in Browser or WebView
        //var h5Base = './vue.html?page=./' + vm.dir + '/build/';
        var h5Base = "http://192.168.1.154:8081/?hot-reload_controller&loader=xhr&wsport=8082&type=vue&page=";
        base = h5Base;
    }
    console.log("********base*************:", base);
    return base;
};

/***/ }),
/* 42 */
/***/ (function(module, exports) {

module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', [_c('wxc-tab-bar', {
    attrs: {
      "tabTitles": _vm.tabTitles,
      "tabStyles": _vm.tabStyles,
      "titleType": "icon"
    },
    on: {
      "wxcTabBarCurrentTabSelected": _vm.wxcTabBarCurrentTabSelected
    }
  }, [_c('div', {
    staticClass: ["item-container"],
    style: _vm.contentStyle
  }, [_c('scroller', {
    staticClass: ["scroller"]
  }, [_c('div', {
    staticClass: ["header"]
  }, [_c('image', {
    staticClass: ["logo"],
    attrs: {
      "src": _vm.logoUrl
    }
  }), _c('text', {
    staticClass: ["title"]
  }, [_vm._v("以下演示Weex提供的UI能力，基于WeexUI版本基础上修改，展现了常用组件")])]), _c('div', {
    staticClass: ["container"]
  }, [_c('text', {
    staticClass: ["tip"],
    on: {
      "click": function($event) {
        _vm.showApiList('showApiListUI')
      }
    }
  }, [_vm._v("基础UI")]), _vm._l((_vm.apiListUI), function(api) {
    return (_vm.showApiListUI) ? _c('div', {
      key: api.id,
      staticClass: ["api-item"],
      on: {
        "click": function($event) {
          _vm.callApi(api.id, api)
        }
      }
    }, [_c('text', {
      staticClass: ["api-item-text"]
    }, [_vm._v(_vm._s(api.title))]), _c('image', {
      staticClass: ["logo32"],
      attrs: {
        "src": "http://h5.sumslack.com/wx/right-arrow.png"
      }
    })]) : _vm._e()
  }), _c('text', {
    staticClass: ["tip"],
    on: {
      "click": function($event) {
        _vm.showApiList('showApiListComplexUI')
      }
    }
  }, [_vm._v("复合UI")]), _vm._l((_vm.apiListComplexUI), function(api) {
    return (_vm.showApiListComplexUI) ? _c('div', {
      key: api.id,
      staticClass: ["api-item"],
      on: {
        "click": function($event) {
          _vm.callApi(api.id, api)
        }
      }
    }, [_c('text', {
      staticClass: ["api-item-text"]
    }, [_vm._v(_vm._s(api.title))]), _c('image', {
      staticClass: ["logo32"],
      attrs: {
        "src": "http://h5.sumslack.com/wx/right-arrow.png"
      }
    })]) : _vm._e()
  }), _c('text', {
    staticClass: ["tip"],
    on: {
      "click": function($event) {
        _vm.showApiList('showApiFormUI')
      }
    }
  }, [_vm._v("表单组件")]), _vm._l((_vm.apiListFormUI), function(api) {
    return (_vm.showApiFormUI) ? _c('div', {
      key: api.id,
      staticClass: ["api-item"],
      on: {
        "click": function($event) {
          _vm.callApi(api.id, api)
        }
      }
    }, [_c('text', {
      staticClass: ["api-item-text"]
    }, [_vm._v(_vm._s(api.title))]), _c('image', {
      staticClass: ["logo32"],
      attrs: {
        "src": "http://h5.sumslack.com/wx/right-arrow.png"
      }
    })]) : _vm._e()
  })], 2)])]), _c('div', {
    staticClass: ["item-container"],
    style: _vm.contentStyle
  }, [_c('scroller', {
    staticClass: ["scroller"]
  }, [_c('div', {
    staticClass: ["header"]
  }, [_c('image', {
    staticClass: ["logo"],
    attrs: {
      "src": _vm.logoUrl
    }
  }), _c('text', {
    staticClass: ["title"]
  }, [_vm._v("以下演示Sumslack小程序开放接口能力，自定义Weex组件，底层接口封装等；")])]), _c('div', {
    staticClass: ["container"]
  }, [_c('text', {
    staticClass: ["tip"],
    on: {
      "click": function($event) {
        _vm.showApiList('showApiListLogin')
      }
    }
  }, [_vm._v("登录相关")]), _vm._l((_vm.apiListLogin), function(api) {
    return (_vm.showApiListLogin) ? _c('div', {
      key: api.id,
      staticClass: ["api-item"],
      on: {
        "click": function($event) {
          _vm.callApi(api.id, api)
        }
      }
    }, [_c('text', {
      staticClass: ["api-item-text"]
    }, [_vm._v(_vm._s(api.title))]), _c('image', {
      staticClass: ["logo32"],
      attrs: {
        "src": "http://h5.sumslack.com/wx/right-arrow.png"
      }
    })]) : _vm._e()
  }), _c('text', {
    staticClass: ["tip"],
    on: {
      "click": function($event) {
        _vm.showApiList('showApiListDevice')
      }
    }
  }, [_vm._v("设备")]), _vm._l((_vm.apiListDevice), function(api) {
    return (_vm.showApiListDevice) ? _c('div', {
      key: api.id,
      staticClass: ["api-item"],
      on: {
        "click": function($event) {
          _vm.callApi(api.id, api)
        }
      }
    }, [_c('text', {
      staticClass: ["api-item-text"]
    }, [_vm._v(_vm._s(api.title))]), _c('image', {
      staticClass: ["logo32"],
      attrs: {
        "src": "http://h5.sumslack.com/wx/right-arrow.png"
      }
    })]) : _vm._e()
  }), _c('text', {
    staticClass: ["tip"],
    on: {
      "click": function($event) {
        _vm.showApiList('showApiListNet')
      }
    }
  }, [_vm._v("网络")]), _vm._l((_vm.apiListNet), function(api) {
    return (_vm.showApiListNet) ? _c('div', {
      key: api.id,
      staticClass: ["api-item"],
      on: {
        "click": function($event) {
          _vm.callApi(api.id, api)
        }
      }
    }, [_c('text', {
      staticClass: ["api-item-text"]
    }, [_vm._v(_vm._s(api.title))]), _c('image', {
      staticClass: ["logo32"],
      attrs: {
        "src": "http://h5.sumslack.com/wx/right-arrow.png"
      }
    })]) : _vm._e()
  }), _c('text', {
    staticClass: ["tip"],
    on: {
      "click": function($event) {
        _vm.showApiList('showApiListMedia')
      }
    }
  }, [_vm._v("媒体")]), _vm._l((_vm.apiListMedia), function(api) {
    return (_vm.showApiListMedia) ? _c('div', {
      key: api.id,
      staticClass: ["api-item"],
      on: {
        "click": function($event) {
          _vm.callApi(api.id, api)
        }
      }
    }, [_c('text', {
      staticClass: ["api-item-text"]
    }, [_vm._v(_vm._s(api.title))]), _c('image', {
      staticClass: ["logo32"],
      attrs: {
        "src": "http://h5.sumslack.com/wx/right-arrow.png"
      }
    })]) : _vm._e()
  }), _c('text', {
    staticClass: ["tip"],
    on: {
      "click": function($event) {
        _vm.showApiList('showApiListLocation')
      }
    }
  }, [_vm._v("位置")]), _vm._l((_vm.apiListLocation), function(api) {
    return (_vm.showApiListLocation) ? _c('div', {
      key: api.id,
      staticClass: ["api-item"],
      on: {
        "click": function($event) {
          _vm.callApi(api.id, api)
        }
      }
    }, [_c('text', {
      staticClass: ["api-item-text"]
    }, [_vm._v(_vm._s(api.title))]), _c('image', {
      staticClass: ["logo32"],
      attrs: {
        "src": "http://h5.sumslack.com/wx/right-arrow.png"
      }
    })]) : _vm._e()
  }), _c('text', {
    staticClass: ["tip"],
    on: {
      "click": function($event) {
        _vm.showApiList('showApiListData')
      }
    }
  }, [_vm._v("数据")]), (_vm.showApiListData) ? _c('div', {
    staticClass: ["api-item"],
    on: {
      "click": function($event) {
        _vm.callApi(900, _vm.api)
      }
    }
  }, [_c('text', {
    staticClass: ["api-item-text"]
  }, [_vm._v("离线存取")]), _c('image', {
    staticClass: ["logo32"],
    attrs: {
      "src": "http://h5.sumslack.com/wx/right-arrow.png"
    }
  })]) : _vm._e(), _c('text', {
    staticClass: ["tip"],
    on: {
      "click": function($event) {
        _vm.showApiList('showApiListSumslack')
      }
    }
  }, [_vm._v("SumslackUI")]), _vm._l((_vm.apiListSumslack), function(api) {
    return (_vm.showApiListSumslack) ? _c('div', {
      key: api.id,
      staticClass: ["api-item"],
      on: {
        "click": function($event) {
          _vm.callApi(api.id, api)
        }
      }
    }, [_c('text', {
      staticClass: ["api-item-text"]
    }, [_vm._v(_vm._s(api.title))]), _c('image', {
      staticClass: ["logo32"],
      attrs: {
        "src": "http://h5.sumslack.com/wx/right-arrow.png"
      }
    })]) : _vm._e()
  })], 2)])]), _c('div', {
    staticClass: ["item-container"],
    style: _vm.contentStyle
  }, [_c('scroller', {
    staticClass: ["scroller"]
  }, [_c('div', {
    staticClass: ["header"]
  }, [_c('image', {
    staticClass: ["logo"],
    attrs: {
      "src": _vm.logoUrl
    }
  }), _c('text', {
    staticClass: ["title"]
  }, [_vm._v("结合Weex和OpenSumslack接口，高仿实现几个常用的综合实例")])]), _c('div', {
    staticClass: ["container"]
  }, [_c('wxc-cell', {
    attrs: {
      "title": "复杂表单",
      "desc": "展现了复杂表单的效果",
      "hasArrow": true,
      "hasTopBorder": true
    },
    on: {
      "wxcCellClicked": function($event) {
        _vm.callApi('form', {
          pageid: 'page.demo.form'
        })
      }
    }
  }), _c('wxc-cell', {
    attrs: {
      "title": "仿美团首页",
      "desc": "仿美团首页",
      "hasArrow": true,
      "hasTopBorder": true
    },
    on: {
      "wxcCellClicked": function($event) {
        _vm.callApi('meituan', {
          pageid: 'page.demo.meituan'
        })
      }
    }
  }), _c('wxc-cell', {
    attrs: {
      "title": "语音视频Demo",
      "desc": "语音视频类使用Demo",
      "hasArrow": true,
      "hasTopBorder": true
    },
    on: {
      "wxcCellClicked": function($event) {
        _vm.callApi('weixin', {
          pageid: 'page.demo.weixin'
        })
      }
    }
  }), _c('wxc-cell', {
    attrs: {
      "title": "一个商城例子",
      "desc": "展现了超多内容的一个商城例子，代码来自官方",
      "hasArrow": true,
      "hasTopBorder": true
    },
    on: {
      "wxcCellClicked": function($event) {
        _vm.callApi('xiaomi', {
          pageid: 'page.demo.xiaomi'
        })
      }
    }
  })], 1)])])]), _c('wxc-loading', {
    attrs: {
      "show": _vm.isShowLoading,
      "type": 'default',
      "loadingText": _vm.loadingText,
      "interval": 0
    }
  })], 1)
},staticRenderFns: []}
module.exports.render._withStripped = true

/***/ })
/******/ ]);