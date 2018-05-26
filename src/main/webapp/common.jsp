<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>锂电智能生产执行系统 LinkMES</title>
    <meta name="viewport"
          content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <script type="text/javascript" src="script/echarts/echarts-all.js"></script>
    <script type="text/javascript" src="script/jquery/jquery-1.8.0.js"></script>
    <link rel="stylesheet" href="resources/style/style_erchang_track.css"/>
    <script type="text/javascript">

        //处理键盘事件 禁止后退键（Backspace）密码或单行、多行文本框除外
        function banBackSpace(e) {
            var ev = e || window.event;//获取event对象
            var obj = ev.target || ev.srcElement;//获取事件源

            var t = obj.type || obj.getAttribute('type');//获取事件源类型

            //获取作为判断条件的事件类型
            var vReadOnly = obj.getAttribute('readonly');
            var vEnabled = obj.getAttribute('enabled');
            //处理null值情况
            vReadOnly = (vReadOnly == null) ? false : vReadOnly;
            vEnabled = (vEnabled == null) ? true : vEnabled;

            //当敲Backspace键时，事件源类型为密码或单行、多行文本的，
            //并且readonly属性为true或enabled属性为false的，则退格键失效
            var flag1 = (ev.keyCode == 8 && (t == "password" || t == "text" || t == "textarea")
                && (vReadOnly == true || vReadOnly == "readonly" || vEnabled != true)) ? true : false;

            //当敲Backspace键时，事件源类型非密码或单行、多行文本的，则退格键失效
            var flag2 = (ev.keyCode == 8 && t != "password" && t != "text" && t != "textarea")
                ? true : false;
            //判断
            if (flag2) {
                return false;
            }
            if (flag1) {
                return false;
            }
        }

        //禁止后退键 作用于Firefox、Opera
        document.onkeypress = banBackSpace;
        //禁止后退键  作用于IE、Chrome
        document.onkeydown = banBackSpace;

    </script>

</head>