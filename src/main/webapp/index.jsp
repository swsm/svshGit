<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<html>
<head>
    <title>蔚至迪锂电MES</title>
    <jsp:include page="/common.jsp"></jsp:include>
    <script id="microloader" data-app="2283f1d1-7985-4a4c-8900-c8a709d27651" type="text/javascript"
            src="bootstrap.js"></script>
    <link type="text/css" rel="stylesheet" href="resources/style/index.css">
</head>
<body>
<div id="loading">
    <div class="loading-indicator">
        <img src="resources/style/default/images/large-loading.gif" width="32" height="32"/>
        <br/> <span id="loading-msg">加载中...</span>
    </div>
</div>
</body>
<script type="text/javascript">

    $(function () {
        //定时请求刷新
        setTimeout(scheduleTask, 1000);
    });
    var scheduleTask = function () {
        $.ajax({
            url: 'main/checkLoginStatus.mvc',
            data: {
                userName: '${userName}'
            },
            type: 'post',
            dataType: 'text',
            async: false,
            success: function (response) {
                var msg;
                if (response === '2') {
                    msg = '登录已过期，请重新登录！';
                } else if (response === '3') {
                    msg = '账号已经在其他电脑登录！';
                } else if (response === '4') {
                    msg = '账号已经被强制注销！';
                } else if (response === '5') {
                    msg = '账号已经被禁用！';
                }
                if (msg && msg !== 'undefined' && msg !== 'null') {
                    Ext.MessageBox.show({
                            title: Tool.str.msgConfirmTitle,
                            msg: '<br>' + msg,
                            width: 280,
                            height: 160,
                            closable: false,
                            buttons: Ext.MessageBox.OK,
                            icon: Ext.MessageBox.WARNING,
                            fn: function () {
                                window.location.href = "forwardPage.mvc?page=login";
                            }
                        }
                    );
                } else {
                    setTimeout(scheduleTask, 10000);
                }
            }
        });
    }

    window.history.forward(1);
    var currentLoginName = '${displayName}';
    var currentLoginUser = '${userName}';
    var organNames = '${organName}';
    var currentWorkNo = '${workNo}';
    var productJson = '${product}';
    /**
     *根据系统配置的变量名获取变量值
     */
    getVarValue = function (varName) {
        var product = Ext.decode(productJson, true);
        var varMap = product.varMap;
        console.log(varMap);
        if (varMap)
            return varMap[varName];
        else
            return null;
    }
</script>
</html>
