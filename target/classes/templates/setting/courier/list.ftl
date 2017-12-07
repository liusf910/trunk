<@shiro.hasPermission name="/setting/courier/edit"><#assign courierEditMark = true/></@shiro.hasPermission>
<@shiro.hasPermission name="/setting/courier/delete"><#assign courierDelMark = true/></@shiro.hasPermission>
<div class="bjui-pageHeader">
    <form id="pagerForm" data-toggle="ajaxsearch" class="pageForm" action="${request.contextPath}/setting/courier/list"
          data-toggle="validate"
          method="post">
        <div style="margin:15px auto 0;">
            <fieldset>
                <legend>查询条件</legend>
                <table class="table table-condensed table-hover">
                    <tbody>
                    <tr>
                        <td align="center" width="15%"><label class="label-control">快递公司编号：</label></td>
                        <td width="35%">
                            <input type="text" name="shipperCode" value="${courier.shipperCode!""}" size="20"/>
                        </td>
                        <td align="center" width="15%"><label class="label-control">快递公司名称：</label></td>
                        <td width="35%">
                            <input type="text" name="courierName" value="${courier.courierName!""}" size="20"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
                <div align="center">
                    <button type="submit" class="btn-default" data-icon="search">查询</button>
                    &nbsp;
                    <a class="btn btn-orange" href="javascript:;" data-toggle="reloadsearch" data- clear-query="true"
                       data-icon="undo">清空查询</a>
                </div>
            </fieldset>
        </div>
    </form>
</div>
<div class="bjui-pageContent">
    <fieldset>
        <legend>查询结果</legend>
        <table class="table table-bordered table-hover table-striped table-top">
            <thead>
            <tr>
                <th width="10%">快递公司编号</th>
                <th width="10%">快递公司名称</th>
                <th width="15%">方法名</th>
                <th width="10%">注册标识</th>
                <th width="20%">ApiUrl</th>
                <th width="15%">ApiVersion</th>
                <th width="10%">数据格式</th>
                <th width="10%">操作</th>
            </tr>
            </thead>
            <tbody>
            <#if pageInfo.list??>
                <#list pageInfo.list as courier>
                <tr data-id="1">
                    <td><a href="${request.contextPath}/setting/courier/${courier.shipperCode}/show_detail"
                           class="btn-red"
                           data-toggle="navtab" data-id="courNavtab"
                           data-reload-warn="本页已有打开的内容，确定将刷新本页内容，是否继续？" data-title="快递详情">${courier.shipperCode}</a>
                    </td>
                    <td>${courier.courierName}</td>
                    <td>${courier.method!""}</td>
                    <td>${courier.userId!""}</td>
                    <td>${courier.apiUrl!""}</td>
                    <td>${courier.apiVersion!""}</td>
                    <td>${courier.format!""}</td>
                    <td>
                        <#if courierEditMark>
                            <a href="${request.contextPath}/setting/courier/${courier.shipperCode}/show_edit"
                               class="btn-green" data-toggle="dialog"
                               data-width="800" data-height="400" data-title="编辑用户">编辑</a>
                        </#if>
                        <#if courierDelMark>
                            <a href="${request.contextPath}/setting/courier/delete?shipperCode=${courier.shipperCode}"
                               class="btn-red"
                               data-toggle="doajax"
                               data-confirm-msg="确定要删除该行信息吗？" data-callback="deleteOperate">删除</a>
                        </#if>
                    </td>
                </tr>
                </#list>
            </#if>
            </tbody>
        </table>
    </fieldset>
</div>
<div class="bjui-pageFooter">
    <ul>
        <li>
        <@shiro.hasPermission name="/setting/courier/add">
            <button href="${request.contextPath}/setting/courier/courier_add" type="submit" class="btn-green"
                    data-icon="save"
                    data-toggle="dialog" data-title="新增快递"
                    data-width="800" data-height="400" data-id="dialog">新增快递
            </button>
        </@shiro.hasPermission>
        </li>
    </ul>
    <div class="pages">
        <span>每页&nbsp;</span>
        <div class="selectPagesize">
            <select data-toggle="selectpicker" data-toggle-change="changepagesize">
                <option value="10">10</option>
                <option value="20">20</option>
                <option value="30">30</option>
                <option value="40">40</option>
            </select>
        </div>
        <span>&nbsp;条，共 ${pageInfo.total?string('#')} 条</span>
    </div>
    <div class="pagination-box" data-toggle="pagination" data-total="${pageInfo.total?string('#')}"
         data-page-size="${pageInfo.pageSize}" data-page-current="${pageInfo.pageNum}">
    </div>
</div>
