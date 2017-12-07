<#escape x as x?html>
<!DOCTYPE html>
<!--suppress ALL -->
<html lang="zh">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>TMS系统管理平台</title>
    <meta name="Keywords" content="TMS系统管理平台"/>
    <link href="${request.contextPath}/static/BJUI/themes/css/bootstrap.css" rel="stylesheet">
    <link href="${request.contextPath}/static/BJUI/themes/css/style.css" rel="stylesheet">
    <link href="${request.contextPath}/static/BJUI/themes/purple/core.css" id="bjui-link-theme" rel="stylesheet">
    <link href="${request.contextPath}/static/BJUI/plugins/kindeditor_4.1.10/themes/default/default.css"
          rel="stylesheet">
    <link href="${request.contextPath}/static/BJUI/plugins/colorpicker/css/bootstrap-colorpicker.min.css"
          rel="stylesheet">
    <link href="${request.contextPath}/static/BJUI/plugins/niceValidator/jquery.validator.css" rel="stylesheet">
    <link href="${request.contextPath}/static/BJUI/plugins/bootstrapSelect/bootstrap-select.css" rel="stylesheet">
    <link href="${request.contextPath}/static/BJUI/themes/css/FA/css/font-awesome.min.css" rel="stylesheet">
    <link href="${request.contextPath}/static/BJUI/themes/css/ie7.css" rel="stylesheet">
    <script src="${request.contextPath}/static/BJUI/other/html5shiv.min.js"></script>
    <script src="${request.contextPath}/static/BJUI/other/respond.min.js"></script>
    <script src="${request.contextPath}/static/BJUI/js/jquery-1.7.2.min.js"></script>
    <script src="${request.contextPath}/static/BJUI/js/jquery.cookie.js"></script>
    <script src="${request.contextPath}/static/BJUI/other/jquery.iframe-transport.js"></script>
    <script src="${request.contextPath}/static/BJUI/js/bjui-all.js"></script>
    <script src="${request.contextPath}/static/BJUI/plugins/swfupload/swfupload.js"></script>
    <script src="${request.contextPath}/static/BJUI/plugins/kindeditor_4.1.10/kindeditor-all.min.js"></script>
    <script src="${request.contextPath}/static/BJUI/plugins/kindeditor_4.1.10/lang/zh_CN.js"></script>
    <script src="${request.contextPath}/static/BJUI/plugins/colorpicker/js/bootstrap-colorpicker.min.js"></script>
    <script src="${request.contextPath}/static/BJUI/plugins/ztree/jquery.ztree.all-3.5.js"></script>
    <script src="${request.contextPath}/static/BJUI/plugins/niceValidator/jquery.validator.js"></script>
    <script src="${request.contextPath}/static/BJUI/plugins/niceValidator/jquery.validator.themes.js"></script>
    <script src="${request.contextPath}/static/BJUI/plugins/bootstrap.min.js"></script>
    <script src="${request.contextPath}/static/BJUI/plugins/bootstrapSelect/bootstrap-select.min.js"></script>
    <script src="${request.contextPath}/static/BJUI/plugins/icheck/icheck.min.js"></script>
    <script src="${request.contextPath}/static/BJUI/plugins/dragsort/jquery.dragsort-0.5.1.min.js"></script>
    <script src="${request.contextPath}/static/BJUI/plugins/highcharts/highcharts.js"></script>
    <script src="${request.contextPath}/static/BJUI/plugins/highcharts/highcharts-3d.js"></script>
    <script src="${request.contextPath}/static/BJUI/plugins/highcharts/themes/gray.js"></script>
    <script src="${request.contextPath}/static/BJUI/plugins/echarts/echarts.js"></script>
    <script src="${request.contextPath}/static/BJUI/plugins/other/jquery.autosize.js"></script>
    <link href="${request.contextPath}/static/BJUI/plugins/uploadify/css/uploadify.css" rel="stylesheet">
    <script src="${request.contextPath}/static/BJUI/plugins/uploadify/scripts/jquery.uploadify.min.js"></script>
    <link href="${request.contextPath}/static/css/trace.css" rel="stylesheet">
    <script src="${request.contextPath}/static/js/trace.js"></script>
    <link href="${request.contextPath}/static/css/common.css" rel="stylesheet">
    <link href="${request.contextPath}/static/css/multiple-select.css" rel="stylesheet">
    <script src="${request.contextPath}/static/js/multiple-select.js"></script>
    <style type="text/css">
        .btn-gray {
            color: grey;
        }
    </style>
    <script type="text/javascript">
        $(function () {
            BJUI.init({
                JSPATH: '${request.contextPath}/static/BJUI/',
                PLUGINPATH: '${request.contextPath}/static/BJUI/plugins/',
                loginInfo: {url: 'login_timeout.html', title: '登录', width: 400, height: 200},
                statusCode: {ok: 200, error: 300, timeout: 301},
                ajaxTimeout: 50000,
                alertTimeout: 3000,
                pageInfo: {
                    pageCurrent: 'pageCurrent',
                    pageSize: 'pageSize',
                    orderField: 'orderField',
                    orderDirection: 'orderDirection'
                },
                keys: {statusCode: 'statusCode', message: 'message'},
                ui: {
                    showSlidebar: true,
                    clientPaging: true,
                    overwriteHomeTab: false
                },
                debug: true,
                theme: 'orange'
            });
            var today = new Date(), time = today.getTime();
            $('#bjui-date').html(today.formatDate('yyyy/MM/dd'));
            setInterval(function () {
                today = new Date(today.setSeconds(today.getSeconds() + 1));
                $('#bjui-clock').html(today.formatDate('HH:mm:ss'))
            }, 1000)
        });

        function MainMenuClick(event, treeId, treeNode) {
            if (treeNode.isParent) {
                var zTree = $.fn.zTree.getZTreeObj(treeId);
                zTree.expandNode(treeNode);
                return
            }
            if (treeNode.target && treeNode.target == 'dialog')
                $(event.target).dialog({id: treeNode.tabid, url: treeNode.url, title: treeNode.name})
            else
                $(event.target).navtab({
                    id: treeNode.tabid,
                    url: treeNode.url,
                    title: treeNode.name,
                    fresh: treeNode.fresh,
                    external: treeNode.external
                });
            event.preventDefault()
        }
        function deleteOperate(json) {
            $(this).bjuiajax('ajaxDone', json)
                    .navtab('refresh')
                    .navtab('reloadFlag', json.tabid);
        }
        function doc_upload_success(file, data) {
            var json = $.parseJSON(data);
            if (json.statusCode == 200) {
                $(this).dialog('closeCurrent');
            }
            $(this).bjuiajax('ajaxDone', json)
                    .navtab('refresh');
        }
    </script>
</head>
<body>
<!--[if lte IE 7]>
<div id="errorie">
    <div>您还在使用老掉牙的IE，正常使用系统前请升级您的浏览器到 IE8以上版本 <a target="_blank"
                                                 href="http://windows.microsoft.com/zh-cn/internet-explorer/ie-8-worldwide-languages">点击升级</a>&nbsp;&nbsp;强烈建议您更改换浏览器：<a
            href="http://down.tech.sina.com.cn/content/40975.html" target="_blank">谷歌 Chrome</a></div>
</div>
<![endif]-->
<header id="bjui-header">
    <div class="bjui-navbar-header">
        <button type="button" class="bjui-navbar-toggle btn-default" data-toggle="collapse"
                data-target="#bjui-navbar-collapse">
            <i class="fa fa-bars"></i>
        </button>
        <a class="bjui-navbar-logo" href="#"><img src="${request.contextPath}/static/images/logo.png"></a>
    </div>
    <nav id="bjui-navbar-collapse">
        <ul class="bjui-navbar-right">
            <li class="datetime">
                <div><span id="bjui-date"></span><br><i class="fa fa-clock-o"></i> <span id="bjui-clock"></span></div>
            </li>
            <li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown"><@shiro.principal/>账户 <span
                    class="caret"></span></a>
                <ul class="dropdown-menu" role="menu">
                    <@shiro.hasPermission name="/setting/user/editUserPwd">
                        <li><a href="${request.contextPath}/setting/user/changePwd" data-toggle="dialog"
                               data-id="changepwd_page" data-mask="true" data-width="400" data-height="260">&nbsp;<span
                                class="glyphicon glyphicon-lock"></span>修改密码&nbsp;
                        </a></li>
                    </@shiro.hasPermission>
                    <li class="divider"></li>
                    <li><a href="${request.contextPath}/loginOut" class="red">&nbsp;<span
                            class="glyphicon glyphicon-off"></span> 注销登陆</a></li>
                </ul>
            </li>
            <li class="dropdown"><a href="#" class="dropdown-toggle theme purple" data-toggle="dropdown"><i
                    class="fa fa-tree"></i></a>
                <ul class="dropdown-menu" role="menu" id="bjui-themes">
                    <li><a href="javascript:;" class="theme_default" data-toggle="theme" data-theme="default">&nbsp;<i
                            class="fa fa-tree"></i> 黑白分明&nbsp;&nbsp;</a></li>
                    <li><a href="javascript:;" class="theme_orange" data-toggle="theme" data-theme="orange">&nbsp;<i
                            class="fa fa-tree"></i> 橘子红了</a></li>
                    <li class="active"><a href="javascript:;" class="theme_purple" data-toggle="theme"
                                          data-theme="purple">&nbsp;<i class="fa fa-tree"></i> 紫罗兰</a></li>
                    <li><a href="javascript:;" class="theme_blue" data-toggle="theme" data-theme="blue">&nbsp;<i
                            class="fa fa-tree"></i> 青出于蓝</a></li>
                    <li><a href="javascript:;" class="theme_red" data-toggle="theme" data-theme="red">&nbsp;<i
                            class="fa fa-tree"></i> 红红火火</a></li>
                    <li><a href="javascript:;" class="theme_green" data-toggle="theme" data-theme="green">&nbsp;<i
                            class="fa fa-tree"></i> 绿草如茵</a></li>
                </ul>
            </li>
        </ul>
    </nav>
</header>

<div id="bjui-container" class="clearfix">
    <div id="bjui-leftside">
        <div id="bjui-sidebar-s">
            <div class="collapse"></div>
        </div>
        <div id="bjui-sidebar">
            <div class="toggleCollapse"><h2><i class="fa fa-bars"></i> 导航栏 <i class="fa fa-bars"></i></h2><a
                    href="javascript:;" class="lock"><i class="fa fa-lock"></i></a></div>
            <div class="panel-group panel-main" data-toggle="accordion" id="bjui-accordionmenu"
                 data-heightbox="#bjui-sidebar" data-offsety="26">
                <div class="panel panel-default">
                    <div class="panel-heading panelContent">
                        <h4 class="panel-title"><a data-toggle="collapse" data-parent="#bjui-accordionmenu"
                                                   href="#bjui-collapse0" class="active"><i
                                class="fa fa-caret-square-o-down"></i>&nbsp;导航栏</a></h4>
                    </div>
                    <div id="bjui-collapse0" class="panel-collapse panelContent collapse in">
                        <div class="panel-body">
                            <ul id="bjui-tree0" class="ztree ztree_main" data-toggle="ztree"
                                data-on-click="MainMenuClick" data-expand-all="true">
                                <@shiro.hasPermission name="/tooksign">
                                    <li data-id="1" data-pid="0">揽签管理</li>
                                </@shiro.hasPermission>
                                <@shiro.hasPermission name="/tooksign/yt/list">
                                    <li data-id="11" data-pid="1" data-url="${request.contextPath}/tooksign/yt/list"
                                        data-tabid="yt">揽签导入
                                    </li>
                                </@shiro.hasPermission>
                                <@shiro.hasPermission name="/monito">
                                    <li data-id="2" data-pid="0">监测管理</li>
                                </@shiro.hasPermission>
                                <@shiro.hasPermission name="/monitoInfo/yuantong/list">
                                    <li data-id="211" data-pid="2"
                                        data-url="${request.contextPath}/monitoInfo/yuantong/list" data-tabid="list">
                                        综合列表
                                    </li>
                                </@shiro.hasPermission>
                                <@shiro.hasPermission name="/monitoInfo/yuantong/awaitPut">
                                    <li data-id="212" data-pid="2"
                                        data-url="${request.contextPath}/monitoInfo/yuantong/awaitPut"
                                        data-tabid="awaitPut">待揽提示
                                    </li>
                                </@shiro.hasPermission>
                                <@shiro.hasPermission name="/monitoInfo/yuantong/signFor">
                                    <li data-id="213" data-pid="2"
                                        data-url="${request.contextPath}/monitoInfo/yuantong/signFor"
                                        data-tabid="signFor">待签提示
                                    </li>
                                </@shiro.hasPermission>
                                <@shiro.hasPermission name="/monitoInfo/yuantong/abnormalTaking">
                                    <li data-id="214" data-pid="2"
                                        data-url="${request.contextPath}/monitoInfo/yuantong/abnormalTaking"
                                        data-tabid="abnormalTaking">揽收异常
                                    </li>
                                </@shiro.hasPermission>
                                <@shiro.hasPermission name="/monitoInfo/yuantong/abnormalSign">
                                    <li data-id="215" data-pid="2"
                                        data-url="${request.contextPath}/monitoInfo/yuantong/abnormalSign"
                                        data-tabid="abnormalSign">签收异常
                                    </li>
                                </@shiro.hasPermission>
                                <@shiro.hasPermission name="/setting">
                                    <li data-id="4" data-pid="0">系统管理</li>
                                </@shiro.hasPermission>
                                <@shiro.hasPermission name="/setting/courier/list">
                                    <li data-id="41" data-pid="4" data-url="${request.contextPath}/setting/courier/list"
                                        data-tabid="courier">快递设置
                                    </li>
                                </@shiro.hasPermission>
                                <@shiro.hasPermission name="/setting/storage/list">
                                    <li data-id="42" data-pid="4" data-url="${request.contextPath}/setting/storage/list"
                                        data-tabid="storage">仓库设置
                                    </li>
                                </@shiro.hasPermission>
                                <@shiro.hasPermission name="/setting/role/list">
                                    <li data-id="43" data-pid="4" data-url="${request.contextPath}/setting/role/list"
                                        data-tabid="role">角色设置
                                    </li>
                                </@shiro.hasPermission>
                                <@shiro.hasPermission name="/setting/user/list">
                                    <li data-id="44" data-pid="4" data-url="${request.contextPath}/setting/user/list"
                                        data-tabid="user">用户设置
                                    </li>
                                </@shiro.hasPermission>
                                <@shiro.hasPermission name="/setting/timelimit/list">
                                    <li data-id="45" data-pid="4"
                                        data-url="${request.contextPath}/setting/timelimit/list" data-tabid="timelimit">
                                        时效设置
                                    </li>
                                </@shiro.hasPermission>
                                <@shiro.hasPermission name="/setting/formula/list">
                                    <li data-id="46" data-pid="4" data-url="${request.contextPath}/setting/formula/list"
                                        data-tabid="formula">公式设置
                                    </li>
                                </@shiro.hasPermission>
                                <@shiro.hasPermission name="/setting/email/editConfig">
                                    <li data-id="47" data-pid="4" data-url="${request.contextPath}/setting/email/toPage"
                                        data-tabid="form-button">邮件设置
                                    </li>
                                </@shiro.hasPermission>
                                <@shiro.hasPermission name="/setting/log/list">
                                    <li data-id="48" data-pid="4" data-url="${request.contextPath}/setting/log/list"
                                        data-tabid="log">日志设置
                                    </li>
                                </@shiro.hasPermission>
                                <@shiro.hasPermission name="/setting/exportExcel/exportExcelSet">
                                    <li data-id="49" data-pid="4"
                                        data-url="${request.contextPath}/setting/exportExcel/exportExcelSet"
                                        data-tabid="allset">字段设置
                                    </li>
                                </@shiro.hasPermission>
                                <@shiro.hasPermission name="/setting/trigger/toPage">
                                    <li data-id="50" data-pid="4"
                                        data-url="${request.contextPath}/setting/trigger/toPage"
                                        data-tabid="jobSetting">Job触发设置
                                    </li>
                                </@shiro.hasPermission>
                                <@shiro.hasPermission name="/count">
                                    <li data-id="3" data-pid="0">统计管理</li>
                                </@shiro.hasPermission>
                                <@shiro.hasPermission name="/count/allList">
                                    <li data-id="31" data-pid="3" data-url="${request.contextPath}/count/allList"
                                        data-tabid="allcount">快递综合统计
                                    </li>
                                </@shiro.hasPermission>
                                <@shiro.hasPermission name="/count/avgList">
                                    <li data-id="32" data-pid="3" data-url="${request.contextPath}/count/avgList"
                                        data-tabid="avgcount">平均送达天数统计
                                    </li>
                                </@shiro.hasPermission>
                               <#-- <@shiro.hasPermission name="/admin">
                                    <li data-id="5" data-pid="0">后台监测</li>
                                </@shiro.hasPermission>
                                <@shiro.hasPermission name="/admin/monito/list">
                                    <li data-id="51" data-pid="5" data-url="${request.contextPath}/monito/all/list"
                                        data-tabid="ytform1">综合列表
                                    </li>
                                </@shiro.hasPermission>
                                <@shiro.hasPermission name="/admin/monito/allAwaitPut_list">
                                    <li data-id="52" data-pid="5"
                                        data-url="${request.contextPath}/monito/allAwaitPut/list"
                                        data-tabid="tookform2">待揽提示
                                    </li>
                                </@shiro.hasPermission>
                                <@shiro.hasPermission name="/admin/monito/allSignFor_list">
                                    <li data-id="53" data-pid="5"
                                        data-url="${request.contextPath}/monito/allSignFor/list" data-tabid="signform2">
                                        待签提示
                                    </li>
                                </@shiro.hasPermission>
                                <@shiro.hasPermission name="/admin/monito/tookAbnormal_list">
                                    <li data-id="54" data-pid="5" data-url="${request.contextPath}/monito/allTook/list"
                                        data-tabid="tookform1">揽收异常
                                    </li>
                                </@shiro.hasPermission>
                                <@shiro.hasPermission name="/admin/monito/signAbnormal_list">
                                    <li data-id="55" data-pid="5" data-url="${request.contextPath}/monito/allSign/list"
                                        data-tabid="signform1">签收异常
                                    </li>
                                </@shiro.hasPermission>-->
                                
                                <@shiro.hasPermission name="/logistics">
                                <li data-id="6" data-pid="0">物流管理</li>
                                </@shiro.hasPermission>
                                <@shiro.hasPermission name="/logistics/wl/list">
                                <li data-id="61" data-pid="6" data-url="${request.contextPath}/logistics/wl/list"
                                        data-tabid="wl">李宁电商入库预约
                                </li>
                                 </@shiro.hasPermission>
                                 <@shiro.hasPermission name="/storages/storageAppoint/list">
                                <li data-id="62" data-pid="6" data-url="${request.contextPath}/storages/storageAppoint/list"
                                        data-tabid="storageAppoint">仓库预约设置
                                </li>
                                </@shiro.hasPermission>
                                <@shiro.hasPermission name="/carriers/carrier/list">
                                    <li data-id="63" data-pid="6" data-url="${request.contextPath}/carriers/carrier/list"
                                        data-tabid="carrier">承运商设置
                                    </li>
                                </@shiro.hasPermission>
                            </ul>
                        </div>
                    </div>
                    <div class="panelFooter">
                        <div class="panelFooterContent"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div id="bjui-navtab" class="tabsPage">
        <div class="tabsPageHeader">
            <div class="tabsPageHeaderContent">
                <ul class="navtab-tab nav nav-tabs">
                    <li data-url="index_layout.html"><a href="javascript:;"><span><i
                            class="fa fa-home"></i> #maintab#</span></a></li>
                </ul>
            </div>
            <div class="tabsLeft"><i class="fa fa-angle-double-left"></i></div>
            <div class="tabsRight"><i class="fa fa-angle-double-right"></i></div>
            <div class="tabsMore"><i class="fa fa-angle-double-down"></i></div>
        </div>
        <ul class="tabsMoreList">
            <li><a href="javascript:;">#maintab#</a></li>
        </ul>
        <div class="navtab-panel tabsPageContent">
            <div class="navtabPage unitBox">
                <div class="bjui-pageHeader" style="background:#FFF;">
                    <div style="padding: 0 15px;">
                        <div class="row">
                            <div class="col-md-6">
                            </div>
                            <div class="col-md-6">
                                <h5 style="width: 76px;"><a></a></h5>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<footer id="bjui-footer">Copyright &copy; 2016 - 2017　<a target="_blank">TMS系统管理平台</a>
</footer>
</body>
</html>
</#escape>