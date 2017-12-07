<div class="bjui-pageHeader">
    <form id="pagerForm" data-toggle="ajaxsearch" class="pageForm" action="${request.contextPath}/count/allList"
          data-toggle="validate"
          method="post">
        <input type="hidden" name="storageCode" id="countStorageSelected" value="${countData.storageCode!""}">
        <div style="margin:15px auto 0;">
            <fieldset>
                <legend>查询条件</legend>
                <table class="table table-condensed table-hover">
                    <tbody>
                    <tr>
                        <td align="center" width="15%"><label class="label-control">操作时间：</label></td>
                        <td width="35%">
                            <input type="text" name="startDate" value="${fileup.startDate!""}" data-toggle="datepicker"
                                   size="15">
                            &nbsp;至&nbsp;
                            <input type="text" name="endDate" value="${fileup.endDate!""}" data-toggle="datepicker"
                                   size="15">
                        </td>
                        <td>
                            <button type="submit" class="btn-default" data-icon="search">查询</button>
                            &nbsp;
                            <a class="btn btn-orange" href="javascript:;" data-toggle="reloadsearch" data-
                               clear-query="true"
                               data-icon="undo">清空查询</a>
                            &nbsp;
                        <@shiro.hasPermission name="/count/startAllCal">
                            <button data-url="${request.contextPath}/count/all_select"
                                    class="btn-green" data-icon="save" data-title="统计条件选择"
                                    data-toggle="dialog"
                                    data-width="600" data-height="400" data-id="dialog">快递综合分析
                            </button>
                        </@shiro.hasPermission>
                        </td>
                    </tr>
                    </tbody>
                </table>
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
                <th width="5%">编号</th>
                <th width="25%">名称</th>
                <th width="15%">所属模块</th>
                <th width="15%">操作人</th>
                <th width="15%">操作客户端IP</th>
                <th width="15%">操作时间</th>
                <th width="10%">操作</th>
            </tr>
            </thead>
            <tbody>
            <#if lists??>
                <#list lists as fileup>
                <tr data-id="1">
                    <td>${fileup.fileId?string('#')}</td>
                    <td>${fileup.fileName}</td>
                    <td><#if fileup.belongTo == "4-0">快递综合分析</#if></td>
                    <td>${fileup.logonName}</td>
                    <td>${fileup.ip}</td>
                    <td>${fileup.createDate?string("yyyy-MM-dd HH:mm:ss")}</td>
                    <td>
                        <a href="${fileup.fileUrl}" class="btn-green" data-title="下载文件">下载</a>
                        <a href="${request.contextPath}/count/delete?fileId=${fileup.fileId?string('#')}"
                           class="btn-red" data-toggle="doajax"
                           data-confirm-msg="确定要删除该文件吗？" data-callback="deleteOperate">删除</a>
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
        <span>&nbsp;条，共 ${total?string('#')} 条</span>
    </div>
    <div class="pagination-box" data-toggle="pagination" data-total="${total?string('#')}"
         data-page-size="${fileup.pageSize}" data-page-current="${fileup.pageCurrent}">
    </div>
</div>