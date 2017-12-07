<@shiro.hasPermission name="/setting/role/edit"><#assign roleEditMark = true /></@shiro.hasPermission>
<@shiro.hasPermission name="/setting/role/delete"><#assign roleDelMark = true /></@shiro.hasPermission>
<div class="bjui-pageHeader">
    <form id="pagerForm" data-toggle="ajaxsearch" class="pageForm" action="${request.contextPath}/setting/role/list"
          data-toggle="validate"
          method="post">
        <div style="margin:15px auto 0;">
            <fieldset>
                <legend>查询条件</legend>
                <table class="table table-condensed table-hover">
                    <tbody>
                    <tr>
                        <td align="center" width="15%"><label class="label-control">角色名称：</label></td>
                        <td width="35%">
                            <input type="text" name="roleName" value="${role.roleName!""}" size="20"/>
                        </td>
                        <td>
                            <button type="submit" class="btn-default" data-icon="search">查询</button>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </fieldset>
        </div>
    </form>
</div>
<div class="bjui-pageContent" style="margin-top: 20px;">
    <fieldset>
        <legend>查询结果</legend>
        <table class="table table-bordered table-hover table-striped table-top">
            <thead>
            <tr>
                <th width="20%">角色名称</th>
                <th width="30%">创建时间</th>
                <th width="30%">操作</th>
            </tr>
            </thead>
            <tbody>
            <#if pageInfo.list??>
                <#list pageInfo.list as role>
                <tr data-id="1">
                    <td>${role.roleName}</td>
                    <td>${role.createDate?string("yyyy-MM-dd HH:mm:ss")}</td>
                    <td>
                        <#if roleEditMark>
                            <a href="${request.contextPath}/setting/role/role_edit?roleId=${role.roleId}"
                               class="btn-green" data-toggle="dialog"
                               data-width="400" data-height="600" data-title="编辑角色">编辑</a>
                        </#if>
                        <#if roleDelMark>
                            <a href="${request.contextPath}/setting/role/delete?roleId=${role.roleId}"
                               class="btn-red"
                               data-toggle="doajax"
                               data-confirm-msg="确定要删除该行信息吗?" data-callback="deleteOperate">删除</a>
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
        <@shiro.hasPermission name="/setting/role/add">
            <button href="${request.contextPath}/setting/role/role_add" type="submit" class="btn-green" data-icon="save"
                    data-toggle="dialog"
                    data-width="400" data-height="600" data-id="dialog">新增角色
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
