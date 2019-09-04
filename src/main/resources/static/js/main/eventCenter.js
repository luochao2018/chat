var EVENT = {
    /**
     * onclick init
     * @param e
     */
    onclick: function (e) {
        $(".chat-open").click(function () {
            $(".chatBox").toggle(10);
        });//打开聊天框
        $(".chat-list-people").each(function () {
            $(this).click(function () {
                var n = $(this).index();
                $(".chatBox-head-one").toggle();
                $(".chatBox-head-two").toggle();
                $(".chatBox-list").fadeToggle();
                $(".chatBox-kuang").fadeToggle();
                //传名字
                $(".ChatInfoName").text($(this).children(".chat-name").children("p").eq(0).html());
                //传头像
                $(".ChatInfoHead>img").attr("src", $(this).children().eq(0).children("img").attr("src"));
                //聊天框默认最底部
                $(document).ready(function () {
                    $("#chatBox-content-demo").scrollTop($("#chatBox-content-demo")[0].scrollHeight);
                });
            })
        });//进聊天页面
        $("#chat-fasong").click(function () {
            //发送的消息
            var textContent = $(".div-textarea").html().replace(/[\n\r]/g, '<br>');
            //头像路径
            var avatars = $(".ChatInfoHead>img").attr("src");
            //名字
            var name = $(".ChatInfoName").text();
            if (textContent != "") {
                $(".chatBox-content-demo").append("<div class=\"clearfloat\">" +
                    "<div class=\"author-name\"><small class=\"chat-date\">2017-12-02 14:26:58</small> </div> " +
                    "<div class=\"right\"> <div class=\"chat-message\"> " + textContent + " </div> " +
                    "<div class=\"chat-avatars\"><img src=\"" + avatars + "\" alt=\"头像\" title='" + name + "'/></div> </div> </div>");
                //发送后清空输入框
                $(".div-textarea").html("");
                //聊天框默认最底部
                $(document).ready(function () {
                    $("#chatBox-content-demo").scrollTop($("#chatBox-content-demo")[0].scrollHeight);
                });
            }
        });//发送信息
        $("#chat-biaoqing").click(function () {
            $(".biaoqing-photo").toggle();
        });//表情
        $(document).click(function () {
            $(".biaoqing-photo").css("display", "none");
        });
        $("#chat-biaoqing").click(function (e) {
            e.stopPropagation();//阻止事件
        });
        $(".emoji-picker-image").each(function () {
            $(this).click(function () {
                var bq = $(this).parent().html();
                console.log(bq)
                $(".chatBox-content-demo").append("<div class=\"clearfloat\">" +
                    "<div class=\"author-name\"><small class=\"chat-date\">2017-12-02 14:26:58</small> </div> " +
                    "<div class=\"right\"> <div class=\"chat-message\"> " + bq + " </div> " +
                    "<div class=\"chat-avatars\"><img src=\"../../img/main/icon01.png\" alt=\"头像\" /></div> </div> </div>");
                //发送后关闭表情框
                $(".biaoqing-photo").toggle();
                //聊天框默认最底部
                $(document).ready(function () {
                    $("#chatBox-content-demo").scrollTop($("#chatBox-content-demo")[0].scrollHeight);
                });
            })
        });
        $("#chat-tuxiang")//图片
        $(".chat-close").click(function () {
            $(".chatBox").toggle(10);
        });//关闭聊天框
        $(".chat-return").click(function () {
            $(".chatBox-head-one").toggle(1);
            $(".chatBox-head-two").toggle(1);
            $(".chatBox-list").fadeToggle(1);
            $(".chatBox-kuang").fadeToggle(1);
        });//返回列表
    },
    /**
     * 界面设置
     */
    screenFuc: function () {
        var topHeight = $(".chatBox-head").innerHeight();//聊天头部高度
        //屏幕小于768px时候,布局change
        var minWidth = 768;
        var winWidth = $(window).innerWidth();
        if (winWidth <= minWidth) {
            var totalHeight = $(window).height(); //页面整体高度
            $(".chatBox-info").css("height", totalHeight - topHeight);
            var infoHeight = $(".chatBox-info").innerHeight();//聊天头部以下高度
            //中间内容高度
            $(".chatBox-content").css("height", infoHeight - 46);
            $(".chatBox-content-demo").css("height", infoHeight - 46);

            $(".chatBox-list").css("height", totalHeight - topHeight);
            $(".chatBox-kuang").css("height", totalHeight - topHeight);
            $(".div-textarea").css("width", winWidth - 106);
        } else {
            $(".chatBox-info").css("height", 495);
            $(".chatBox-content").css("height", 448);
            $(".chatBox-content-demo").css("height", 448);
            $(".chatBox-list").css("height", 495);
            $(".chatBox-kuang").css("height", 495);
            $(".div-textarea").css("width", 260);
        }
    },
    /**
     * 未读信息
     */
    messageNum: function () {
        //未读信息数量为空时
        var totalNum = $(".chat-message-num").html();
        if (totalNum == "") {
            $(".chat-message-num").css("padding", 0);
        }
        $(".message-num").each(function () {
            var wdNum = $(this).html();
            if (wdNum == "") {
                $(this).css("padding", 0);
            }
        });
    }
};