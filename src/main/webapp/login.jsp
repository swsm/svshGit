<%@ page language="java"
         import="java.util.*,com.swsm.login.session.LoginResult"
         pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
    String ok = LoginResult.LOGINOK.getContext();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>蔚至迪锂电MES</title>
    <base href="<%=basePath%>">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <link rel="stylesheet" type="text/css"
          href="resources/style/default/common.css">
    <link rel="stylesheet" type="text/css"
          href="resources/style/default/layout.css">
    <link rel="stylesheet" type="text/css"
          href="resources/style/css/xcConfirm.css">
    <script type="text/javascript" src="script/jquery/jquery-1.8.0.js"></script>
    <script type="text/javascript" src="script/xcConfirm.js"></script>
    <script>
        $(function () {
            if (window.frameElement) {
                window.parent.location.href = window.location.href;
            }

            $("#userName").focus();
            $("#userName").keypress(function (e) {
                if (e.keyCode == 13) {
                    $(".btn_login").click();
                }
            });

            $("#passWord").keypress(function (e) {
                if (e.keyCode == 13) {
                    $(".btn_login").click();
                }
            });
            $(".btn_login").click(
                function () {
                    if ($("#userName").val() == ""
                        || $("#passWord").val() == "") {
                        $(".text_tip").slideDown(100);
                        setTimeout(function () {
                            $(".text_tip").slideUp(100);
                        }, 1000);
                    } else {
                        $.ajax({
                            url: 'main/login.mvc',// 跳转到 action
                            data: {
                                loginName: $("#userName").val(),
                                loginPassword: $("#passWord").val()
                            },
                            type: 'post',
                            dataType: 'json',
                            async: false,
                            success: function (data) {
                                if (data['msg'] == '<%=ok%>') {
                                    $.ajax({
                                        url: 'main/checkUserIsLogin.mvc',// 跳转到 action
                                        data: {
                                            loginName: $("#userName").val()
                                        },
                                        type: 'post',
                                        dataType: 'text',
                                        async: false,
                                        success: function (data) {
                                            if (data === 'true') {
                                                window.wxc.xcConfirm('该用户已被登录，是否继续？', window.wxc.xcConfirm.typeEnum.confirm, {
                                                    onOk: function () {
                                                        window.location.href = "forwardPage.mvc?page=index";
                                                    }
                                                })
                                            } else {
                                                window.location.href = "forwardPage.mvc?page=index";
                                            }
                                        },
                                        error: function (XMLHttpRequest, textStatus, errorThrown) {

                                        }
                                    });
                                } else {
                                    $(".text_tip")[0].innerHTML = data['msg'];
                                    $(".text_tip").slideDown(100);
                                    setTimeout(function () {
                                        $(".text_tip").slideUp(100);
                                    }, 1000);
                                }
                            },
                            error: function (XMLHttpRequest, textStatus, errorThrown) {

                            }
                        });
                    }
                });
            //重置
            $(".btn_reset").click(function () {
                $("#userName").val("");
                $("#passWord").val("");
            });

            $("#btn_resert").click(function () {
                $("#cardnum").val("");
                $("#cardnum").removeAttr("readonly");
                $("#cardnum").focus();
            });

            //nav
            $("#nav li .open_sub").click(
                function () {
                    if ($(this).parent("li").hasClass("current")) {
                        return false;
                    } else {
                        $(this).parent("li").addClass("current").siblings("li")
                            .removeClass("current");
                        $(".nav_sub").slideUp(200);
                        $(this).parent("li").find("ul").slideDown(200);
                    }
                });
            $(".nav_sub li").click(function () {
                $(".nav_sub li").removeClass("current");
                $(this).addClass("current").siblings("li").removeClass("current");
            });


            //输入框清除按钮
            if ($("#userName").val().length == 0)
                $("#userName_ico").hide();

            $("#userName").bind('input propertychange', function () {
                if ($("#userName").val().length == 0)
                    $("#userName_ico").hide();
                else
                    $("#userName_ico").show();
            });
            $("#userName_ico").click(function () {
                $("#userName").val("");
                $("#userName").focus();
                $("#userName_ico").hide();
            });

            if ($("#passWord_ico").val().length == 0)
                $("#passWord_ico").hide();
            $("#passWord").bind('input propertychange', function () {
                if ($("#passWord").val().length == 0)
                    $("#passWord_ico").hide();
                else
                    $("#passWord_ico").show();
            });

            $("#passWord_ico").click(function () {
                $("#passWord").val("");
                $("#passWord").focus();
                $("#passWord_ico").hide();
            });

            $(".login_card_content").hide();
            $("#accountnum_login").css("color", "#099f8f");
            $("#accountnum_login").click(function () {
                $("#accountnum_login").css("color", "#099f8f");
                $("#worknum_login").css("color", "#8e8e8e");
                $(".login_content").show();
                $("#userName").focus();
            });
        });


    </script>
</head>
<body>
<!-- box_panel s -->
<div class="box_panel">
    <div class="login_main">
        <div class="login_head">
            <div class="login_logo">
            </div>
        </div>
        <div class="login_body">
            <div class="box_login">
                <!-- login_title s -->
                <div class="login_title">
                    <ul class="login_switcher">
                        <li style="width: 100%;">
                            <a id="accountnum_login">账号登录</a>
                        </li>
                    </ul>
                </div>
                <div class="login_content">
                    <!-- box_input s -->
                    <div class="box_input">
                        <span class="span_icon icon_name"></span>
                        <span class="span_ico" id="userName_ico"></span>
                        <div class="input_content">
                            <input placeholder="用户名" type="text" class="file_adapt_100" id="userName"
                                   value="">
                        </div>


                    </div>
                    <div class="box_input">
                        <span class="span_icon icon_code"></span>
                        <span class="span_ico" id="passWord_ico"></span>
                        <div class="input_content">
                            <input placeholder="密码" type="password" class="file_adapt_100" id="passWord" value="">
                        </div>
                    </div>
                    <div class="box_login_btn">
                        <div class='text_tip'>用户名或密码不正确！</div>
                        <input type="button" class="btn_login"
                               value="登&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;录">
                    </div>
                </div>

            </div>
        </div>
        <div class="footer">
            <div class="copyright">蔚至迪智能科技有限公司版权所有</div>
        </div>
    </div>
</div>
</body>
</html>
