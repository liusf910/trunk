<@shiro.hasPermission name="/setting/log/delete"><#assign logDelMark = true/></@shiro.hasPermission>
<div class="bjui-pageHeader">
    <form id="pagerForm" data-toggle="ajaxsearch" class="pageForm" action="${request.contextPath}/setting/log/list"
          data-toggle="validate"
          method="post">
        <div style="margin:15px auto 0;">
            <fieldset>
                <legend>查询条件</legend>
                <table class="table table-condensed table-hover">
                    <tbody>
                    <tr>
                        <td align="center" width="15%"><label class="label-control">操作：</label></td>
                        <td width="35%">
                            <input type="text" name="operation" value="${log.operation!""}" size="20"/>
                        </td>
                        <td align="center" width="15%"><label class="label-control">创建日期：</label></td>
                        <td width="35%">
                            <input type="text" name="startDate" data-toggle="datepicker" value="${log.startDate!""}"
                                   size="15">&nbsp;至&nbsp;
                            <input type="text" name="endDate" data-toggle="datepicker" value="${log.endDate!""}"
                                   size="15">
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
                <th width="10%"><input type="checkbox" class="checkboxCtrl" data-group="ids" data-toggle="icheck"></th>
                <th width="25%">操作名称</th>
                <th width="10%">操作员</th>
                <th width="10%" align="center">IP</th>
                <th width="20%">创建日期</th>
                <th width="20%">操作</th>
            </tr>
            </thead>
            <tbody>
            <#if lists??>
                <#list lists as log>
                <tr data-id="1">
                    <td><input type="checkbox" name="ids" data-toggle="icheck" value="${log.id}"/></td>
                    <td>${log.operation}</td>
                    <td>${log.logonName}</td>
                    <td>${log.ip}</td>
                    <td>${log.createDate?string("yyyy-MM-dd HH:mm:ss")}</td>
                    <td>
                        <a href="${request.contextPath}/setting/log/detail?id=${log.id?string('#')}" class="btn-green"
                           data-toggle="navtab" data-id="logNavtab"
                           data-reload-warn="本页已有打开的内容，确定将刷新本页内容，是否继续？" data-title="日志详情">查看详情</a>
                        <#if logDelMark>
                            <a href="${request.contextPath}/setting/log/delete?id=${log.id?string('#')}" class="btn-red"
                               data-toggle="doajax"
                               data-confirm-msg="确定要删除该日志吗？" data-callback="deleteOperate">删除</a>
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
        <@shiro.hasPermission name="/setting/log/deleteBatch">
            <button href="${request.contextPath}/setting/log/deleteBatch" data-toggle="doajaxchecked"
                    data-confirm-msg="确定要删除选中项吗？" data-idname="ids" data-callback="deleteOperate"
                    data-group="ids" class="btn-red">批量删除日志
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
         data-page-size="${log.pageSize}" data-page-current="${log.pageCurrent}">
    </div>
</div>