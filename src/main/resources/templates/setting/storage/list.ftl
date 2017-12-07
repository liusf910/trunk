<@shiro.hasPermission name="/setting/storage/edit"><#assign storageEditMark = true/></@shiro.hasPermission>
<@shiro.hasPermission name="/setting/storage/delete"><#assign storageDelMark = true/></@shiro.hasPermission>
<div class="bjui-pageHeader">
    <form id="pagerForm" data-toggle="ajaxsearch" class="pageForm" action="${request.contextPath}/setting/storage/list"
          data-toggle="validate"
          method="post">
        <div style="margin:15px auto 0;">
            <fieldset>
                <legend>查询条件</legend>
                <table class="table table-condensed table-hover">
                    <tbody>
                    <tr>
                        <td align="center" width="15%"><label class="label-control">仓库编号：</label></td>
                        <td width="35%">
                            <input type="text" name="storageCode" value="${storage.storageCode!""}" size="20"/>
                        </td>
                        <td align="center" width="15%"><label class="label-control">仓库名称：</label></td>
                        <td width="35%">
                            <input type="text" name="storageName" value="${storage.storageName!""}" size="20"/>
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
                <th>仓库编号</th>
                <th>仓库名称</th>
                <th>拥有快递公司</th>
                <th width="120">操作</th>
            </tr>
            </thead>
            <tbody>
            <#if storageList??>
                <#list storageList as storage>
                <tr data-id="1">
                    <td>
                        <a href="${request.contextPath}/setting/storage/storage_detail?storageCode=${storage.storageCode}"
                           class="btn-red"
                           data-toggle="navtab" data-id="courNavtab"
                           data-reload-warn="本页已有打开的内容，确定将刷新本页内容，是否继续？" data-title="仓库详情">${storage.storageCode}</a>
                    </td>
                    <td>${storage.storageName!""}</td>
                    <td>${storage.courierName!""}</td>
                    <td>
                        <#if storageEditMark>
                            <a href="${request.contextPath}/setting/storage/storage_edit?storageCode=${storage.storageCode}"
                               class="btn-green" data-toggle="dialog"
                               data-width="800" data-height="400" data-title="编辑仓库">编辑</a>
                        </#if>
                        <#if storageDelMark>
                            <a href="${request.contextPath}/setting/storage/delete?storageCode=${storage.storageCode}"
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
        <@shiro.hasPermission name="/setting/storage/add">
            <button href="${request.contextPath}/setting/storage/storage_add" type="submit" class="btn-green"
                    data-icon="save"
                    data-toggle="dialog" data-title="新增仓库"
                    data-width="800" data-height="400" data-id="dialog">新增仓库
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
        <span>&nbsp;条，共 ${total?string('#')} 条</span>
    </div>
    <div class="pagination-box" data-toggle="pagination" data-total="${total?string('#')}"
         data-page-size="${storage.pageSize}" data-page-current="${storage.pageCurrent}">
    </div>
</div>
