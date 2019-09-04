/**
 * 自定义方法
 * @type {{currentFolder: FUN.currentFolder, localhostPath: (function(): string), projectName: (function(): string)}}
 */
var FUN = {
    currentFolder: function () {
        //页面解析到当前为止所有的script标签
        var e = document.scripts;
        if (!e) {
            return null;
        }
        //e[e.length - 1] 就是当前的js文件的路径
        e = e[e.length - 1].src.substring(0, e[e.length - 1].src.lastIndexOf("/") + 1);
        //输出当前js文件所在的目录
        _console(e);
        return e;
    },
    localhostPath: function () {
        var currentUrl = window.document.location.href;
        var pathName = window.document.location.pathname;
        var pos = currentUrl.indexOf(pathName);
        return currentUrl.substring(0, pos);
    },
    projectName: function () {
        var pathName = window.document.location.pathname;
        return pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    }

};
/**
 *
 * @type {{currentUrl: string, currentFolder: (*|string|*), currentPathName: string, localhostPath: (*|string), projectName: (*|string), actionUrlHead: PATH.actionUrlHead}}
 */
var PATH = {
    currentFolder: FUN.currentFolder(),//获取当前文件的目录
    currentUrl: window.document.location.href,//获取当前网址
    currentPathName: window.document.location.pathname,//获取主机地址之后的目录
    localhostPath: FUN.localhostPath(),//获取主机地址
    projectName: FUN.projectName(),//获取带"/"的项目名
    actionUrlHead: function (e) {
        if (!e) {
            return PATH.localhostPath + PATH.projectName;
        }
        var delimiter = e.indexOf("/") == -1 ? "/" : "";
        return PATH.localhostPath + delimiter + e;
    }
};

/**
 * 打印控制
 * @param e
 */
function _console(e) {
    console.log(e);
}