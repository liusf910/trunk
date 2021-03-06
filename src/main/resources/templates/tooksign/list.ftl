<@shiro.hasPermission name="/tooksign/yt/down"><#assign ytDownMark = true/></@shiro.hasPermission>
<@shiro.hasPermission name="/tooksign/yt/delete"><#assign ytDelMark = true/></@shiro.hasPermission>
<div class="bjui-pageHeader">
    <form id="pagerForm" data-toggle="ajaxsearch" class="pageForm"
          action="${request.contextPath}/tooksign/yt/list"
          data-toggle="validate" method="post">
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
                    <td align="center" width="15%"><label class="label-control">文件类型：</label></td>
                    <td>
                        <select name="belongTo" data-toggle="selectpicker" data-width="200">
                            <option value="">------------请选择----------</option>
                            <option value="1-1" <#if fileup.belongTo??><#if fileup.belongTo == "1-1">selected</#if></#if>>
                                揽收
                            </option>
                            <option value="2-1" <#if fileup.belongTo??><#if fileup.belongTo == "2-1">selected</#if></#if>>
                                签收
                            </option>
                        </select>
                    </td>
                </tr>
                </tbody>
            </table>
            <br/>
            <div align="center">
                <button type="submit" class="btn-default" data-icon="search">查询</button>
                &nbsp;
                <a class="btn btn-orange" href="javascript:;" data-toggle="reloadsearch" data- clear-query="true"
                   data-icon="undo">清空查询</a>
            </div>
        </fieldset>
    </form>
</div>
<div class="bjui-pageContent">
    <fieldset>
        <legend>查询结果</legend>
        <table class="table table-bordered table-hover table-striped table-top">
            <thead>
            <tr>
            <#--<th width="26"><input type="checkbox" class="checkboxCtrl" data-group="ids" data-toggle="icheck"></th>-->
                <th width="25%">名称</th>
                <th width="15%">类型</th>
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
                    <td>${fileup.fileName}</td>
                    <td><#if fileup.belongTo == "1-1"><span
                            style="color: red;">揽收</span><#else><span style="color: green;">签收</span></#if></td>
                    <td>${fileup.logonName}</td>
                    <td>${fileup.ip}</td>
                    <td>${fileup.createDate?string("yyyy-MM-dd HH:mm:ss")}</td>
                    <td>
                        <#if ytDownMark>
                            <a href="${fileup.fileUrl}" class="btn-green" data-title="下载文件">下载</a>
                        </#if>
                        <#if ytDelMark>
                            <a href="${request.contextPath}/tooksign/yt/delete?fileId=${fileup.fileId?string('#')}"
                               class="btn-red" data-toggle="doajax"
                               data-confirm-msg="确定要删除该文件吗？" data-callback="deleteOperate">删除</a>
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
    <@shiro.hasPermission name="/upload/took">
        <li>
            <button href="${request.contextPath}/tooksign/took_upload" type="submit" class="btn-red" data-icon="save"
                    data-toggle="dialog"
                    data-width="500" data-height="300" data-id="dialog">揽件上传
            </button>
        </li>
    </@shiro.hasPermission>
    <@shiro.hasPermission name="/upload/sign">
        <li>
            <button href="${request.contextPath}/tooksign/sign_upload" type="submit" class="btn-red" data-icon="save"
                    data-toggle="dialog"
                    data-width="500" data-height="300" data-id="dialog">签收上传
            </button>
        </li>
    </@shiro.hasPermission>
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
         data-page-size="${fileup.pageSize}" data-page-current="${fileup.pageCurrent}">
    </div>
</div>