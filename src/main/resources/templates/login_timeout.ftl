<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN""http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link href="${request.contextPath}/static/images/favicon.ico" rel="icon" type="image/x-icon"/>
    <title>登录超时</title>
    <link href="${request.contextPath}/static/css/style.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${request.contextPath}/static/js/jquery.js"></script>
    <script language="javascript">
        $(function () {
            $('.error').css({'position': 'absolute', 'left': ($(window).width() - 490) / 2});
            $(window).resize(function () {
                $('.error').css({'position': 'absolute', 'left': ($(window).width() - 490) / 2});
            })
        });
    </script>
</head>
<body style="background:#edf6fa;">
<div class="unauth">
    <h2>登录超时</h2>
</div>
</body>
</html>