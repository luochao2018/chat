/**
 * 初始化
 */
var img_main = "../../img/main/";
window.onload = function () {
    EVENT.screenFuc();
    EVENT.messageNum();
    EVENT.onclick();
};
/**
 * 屏幕大小改变
 */
(window.onresize = function () {
    EVENT.screenFuc();
})();


//发送图片
function selectImg(pic) {
    if (!pic.files || !pic.files[0]) {
        return;
    }
    var reader = new FileReader();
    reader.onload = function (evt) {
        var images = evt.target.result;
        $(".chatBox-content-demo").append("<div class=\"clearfloat\">" +
            "<div class=\"author-name\"><small class=\"chat-date\">2017-12-02 14:26:58</small> </div> " +
            "<div class=\"right\"> <div class=\"chat-message\"><img src=" + images + "></div> " +
            "<div class=\"chat-avatars\"><img src=\"../../img/main/icon01.png\" alt=\"头像\" /></div> </div> </div>");
        //聊天框默认最底部
        $(document).ready(function () {
            $("#chatBox-content-demo").scrollTop($("#chatBox-content-demo")[0].scrollHeight);
        });
    };
    reader.readAsDataURL(pic.files[0]);
}