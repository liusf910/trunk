<#escape x as x?html>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>系统登录</title>
    <script type="text/javascript" src="${request.contextPath}/static/js/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="${request.contextPath}/static/js/jquery.cookie.js"></script>
    <!-- BJUI.all 分模块压缩版 -->
    <script src="${request.contextPath}/static/BJUI/js/bjui-all.js"></script>

    <!-- nice validate -->
    <script src="${request.contextPath}/static/BJUI/plugins/niceValidator/jquery.validator.js"></script>
    <script src="${request.contextPath}/static/BJUI/plugins/niceValidator/jquery.validator.themes.js"></script>
    <link href="${request.contextPath}/static/BJUI/themes/css/bootstrap.css" rel="stylesheet">
    <!-- core - css -->
    <link href="${request.contextPath}/static/BJUI/themes/css/style.css" rel="stylesheet">
    <link href="${request.contextPath}/static/BJUI/themes/purple/core.css" id="bjui-link-theme" rel="stylesheet">

    <link href="${request.contextPath}/static/css/login.css" rel="stylesheet" type="text/css"/>

    <script type="text/javascript">
        $(function () {
            choose_bg();

            var $captchaImage = $("#captchaImage");
            // 更换验证码
            $captchaImage.click(function () {
                $captchaImage.attr("src", "${request.contextPath}/common/captcha?dt=" + new Date().getTime());
            });

            $('#login_ok').click(function () {
                if ($('#j_username').val().trim() == '') {
                    $("#message").alertmsg('warn', '登录名不能为空');
                    return;
                }
                if ($('#j_password').val().trim() == '') {
                    $("#message").alertmsg('warn', '登录密码不能为空');
                    return;
                }
                if ($('#j_vcode').val().trim() == '') {
                    $("#message").alertmsg('warn', '验证码不能为空');
                    return;
                }
                $('#login_form').submit();
            });
            if (top.location != location) {
                top.location.href = location.href;
            }
        });
        function choose_bg() {
            var bg = Math.floor(Math.random() * 3 + 1);
            $('body').css('background-image', 'url(../images/loginbg_0' + bg + '.jpg)');
        }
    </script>
</head>
<body>
<div id="errorie">
    <div class="main_box">
        <div class="setting"><a href="javascript:;" onclick="choose_bg();" title="更换背景"><span
                class="glyphicon glyphicon-th-large"></span></a></div>
        <div class="login_box">
            <div class="login_logo">
                <img src="${request.contextPath}/images/logo.png">
            </div>
            <div class="login_form">
                <form action="${request.contextPath}/login" id="login_form" method="post">
                    <div class="form-group">
                        <label for="j_username" class="t">用户名：</label>
                        <input id="j_username" name="logonName" type="text" class="form-control x319 in"
                               autocomplete="off">
                    </div>
                    <div class="form-group">
                        <label for="j_password" class="t">密　码：</label>
                        <input id="j_password" name="logonPwd" type="password" class="form-control x319 in">
                    </div>
                    <div class="form-group">
                        <label for="j_captcha" class="t">验证码：</label>
                        <input type="text" id="j_vcode" name="vcode" class="form-control x260 in" maxlength="4"
                               autocomplete="off"/>
                        <img id="captchaImage" class="captchaImage" src="${request.contextPath}/common/captcha"/>
                        <span class="info">${message!""}</span>
                    </div>
                    <div id="message"></div>
                    <div class="form-group space">
                        <label class="t"></label>　　　
                        <input type="button" id="login_ok" value="&nbsp;登&nbsp;录&nbsp;" class="btn btn-primary btn-lg">&nbsp;&nbsp;&nbsp;&nbsp;
                        <input type="reset" value="&nbsp;重&nbsp;置&nbsp;" class="btn btn-default btn-lg">
                    </div>
                </form>
            </div>
        </div>
        <div class="bottom">Copyright &copy;2017 <a href="#">TMS后台 - 系统登陆</a></div>
    </div>
</body>
</html>
</#escape>