<@shiro.hasPermission name="/storages/storageAppoint/edit"><#assign storageAppointEditMark = true/></@shiro.hasPermission>
<@shiro.hasPermission name="/storages/storageAppoint/delete"><#assign storageAppointDelMark = true/></@shiro.hasPermission>
<div class="bjui-pageHeader">
    <form id="pagerForm" data-toggle="ajaxsearch" class="pageForm" action="${request.contextPath}/storages/storageAppoint/list"
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
                            <input type="text" name="warehouseCode" value="${storageAppoint.warehouseCode!""}" size="20"/>
                        </td>
                        <td align="center" width="15%"><label class="label-control">仓库名称：</label></td>
                        <td width="35%">
                            <input type="text" name="name" value="${storageAppoint.name!""}" size="20"/>
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
                <th width="20%">ID</th>
                <th width="20%">编号</th>
                <th width="20%">名称</th>
                <th width='20%'>预约量</th>
                <th width='20%'>操作</th>
            </tr>
            </thead>
            <tbody>
            <#if pageInfo.list??>
                <#list pageInfo.list as storageAppoint>
                <tr data-id="1">
                	<td>${storageAppoint.id!""}</td>
                    <td>${storageAppoint.warehouseCode!""}</td>
                    <td>${storageAppoint.name!""}</td>
                    <td>${storageAppoint.appointLimit!""}</td>
                    <td>
                        <#if storageAppointEditMark>
                            <a href="${request.contextPath}/storages/storageAppoint/${storageAppoint.id}/show_edit"
                               class="btn-green" data-toggle="dialog"
                               data-width="800" data-height="400" data-title="编辑仓库预约">编辑</a>
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
