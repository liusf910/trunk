<@shiro.hasPermission name="/setting/user/edit"><#assign userEditMark = true/></@shiro.hasPermission>
<@shiro.hasPermission name="/setting/user/delete"><#assign userDelMark = true/></@shiro.hasPermission>
<div class="bjui-pageHeader">
    <form id="pagerForm" data-toggle="ajaxsearch" class="pageForm" action="${request.contextPath}/setting/user/list"
          data-toggle="validate"
          method="post">
        <div style="margin:15px auto 0;">
            <fieldset>
                <legend>查询条件</legend>
                <table class="table table-condensed table-hover">
                    <tbody>
                    <tr>
                        <td align="center" width="15%"><label class="label-control">登录名：</label></td>
                        <td width="35%">
                            <input type="text" name="logonName" value="${user.logonName!""}" size="20"/>
                        </td>
                        <td align="center" width="15%"><label class="label-control">所属仓库：</label></td>
                        <td width="35%">
                            <select name="storageCode" data-toggle="selectpicker" data-width="200">
                                <option value="">全部</option>
                            <#if storages??>
                                <#list  storages as storage>
                                    <option value="${storage.storageCode}" <#if user.storageCode??><#if user.storageCode == storage.storageCode>selected</#if></#if>>${storage.storageName}</option>
                                </#list>
                            </#if>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td align="center" width="15%"><label class="label-control">所属快递：</label>
                        </td>
                        <td width="35%">
                            <select name="shipperCode" data-toggle="selectpicker" data-width="200">
                                <option value="">全部</option>
                            <#if couriers??>
                                <#list couriers as courier>
                                    <option value="${courier.shipperCode}" <#if user.shipperCode??><#if user.shipperCode == courier.shipperCode >selected</#if></#if>>${courier.courierName}</option>
                                </#list>
                            </#if>
                            </select>
                        </td>

                        <td align="center" width="15%"><label class="label-control">是否可用：</label>
                        </td>
                        <td width="35%">
                            <select name="isDelete" data-toggle="selectpicker" data-width="200">
                                <option value="">---------请选择--------</option>
                                <option value="0" <#if user.isDelete??><#if user.isDelete == "0">selected</#if></#if>>
                                    正常
                                </option>
                                <option value="1" <#if user.isDelete??><#if user.isDelete == "1">selected</#if></#if>>
                                    已删除
                                </option>
                            </select>
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
                <th width="10%">登录名</th>
                <th width="10%">用户名</th>
                <th width="10%">联系电话</th>
                <th width="45%">所属仓库快递</th>
                <th width="10%">用户角色</th>
                <th width="5%">是否可用</th>
                <th width="10%">操作</th>
            </tr>
            </thead>
            <tbody>
            <#if lists??>
                <#list lists as user>
                <tr data-id="1">
                    <td><a href="${request.contextPath}/setting/user/detail?id=${user.userId}" class="btn-red"
                           data-toggle="navtab" data-id="userNavtab"
                           data-reload-warn="本页已有打开的内容，确定将刷新本页内容，是否继续？" data-title="用户详情">${user.logonName}</a></td>
                    <td>${user.userName}</td>
                    <td>${user.mobile}</td>
                    <td>${user.courierName}</td>
                    <td>${user.roleName}</td>
                    <td><#if user.isDelete == "0">正常<#else>已删除</#if></td>
                    <td>
                        <#if user.isDelete == "0">
                            <#if userEditMark>
                                <a href="${request.contextPath}/setting/user/user_edit?id=${user.userId}"
                                   class="btn-green" data-toggle="dialog"
                                   data-width="800" data-height="400" data-title="编辑用户">编辑</a>
                            </#if>
                            <#if userDelMark>
                                <a href="${request.contextPath}/setting/user/delete?id=${user.userId}" class="btn-red"
                                   data-toggle="doajax"
                                   data-confirm-msg="确定要删除该行信息吗？" data-callback="deleteOperate">删除
                                </a>
                            </#if>
                        <#else>
                            <a href="javascript:;" class="btn-gray">编辑</a>
                            <a href="javascript:;" class="btn-gray">删除</a>
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
        <@shiro.hasPermission name="/setting/user/add">
            <button href="${request.contextPath}/setting/user/user_add" type="submit" class="btn-green" data-icon="save"
                    data-toggle="dialog"
                    data-width="800" data-height="400" data-id="dialog">新增用户
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
         data-page-size="${user.pageSize}" data-page-current="${user.pageCurrent}">
    </div>
</div>