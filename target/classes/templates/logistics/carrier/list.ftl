<@shiro.hasPermission name="/carriers/carrier/edit"><#assign carrierEditMark = true/></@shiro.hasPermission>
<@shiro.hasPermission name="/carriers/carrier/delete"><#assign carrierDelMark = true/></@shiro.hasPermission>
<div class="bjui-pageHeader">
    <form id="pagerForm" data-toggle="ajaxsearch" class="pageForm" action="${request.contextPath}/carriers/carrier/list"
          data-toggle="validate"
          method="post">
        <div style="margin:15px auto 0;">
            <fieldset>
                <legend>查询条件</legend>
                <table class="table table-condensed table-hover">
                    <tbody>
                    <tr>
                        <td align="center" width="15%"><label class="label-control">承运商编号：</label></td>
                        <td width="35%">
                            <input type="text" name="carrierCode" value="${carrier.carrierCode!""}" size="20"/>
                        </td>
                        <td align="center" width="15%"><label class="label-control">承运商名称：</label></td>
                        <td width="35%">
                            <input type="text" name="carrierName" value="${carrier.carrierName!""}" size="20"/>
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
                <th width="40%">承运商编号</th>
                <th width="40%">承运商名称</th>
                <th width="40%">操作</th>
            </tr>
            </thead>
            <tbody>
            <#if pageInfo.list??>
                <#list pageInfo.list as carrier>
                <tr data-id="1">
                    <td>${carrier.carrierCode!""}</td>
                    <td>${carrier.carrierName!""}</td>
                    <td>
                        <#if carrierEditMark>
                            <a href="${request.contextPath}/carriers/carrier/${carrier.carrierCode}/show_edit"
                               class="btn-green" data-toggle="dialog"
                               data-width="800" data-height="400" data-title="编辑承运商">编辑</a>
                        </#if>
                        <#if carrierDelMark>
                            <a href="${request.contextPath}/carriers/carrier/delete?carrierCode=${carrier.carrierCode}"
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
        <@shiro.hasPermission name="/carriers/carrier/add">
            <button href="${request.contextPath}/carriers/carrier/carrier_add" type="submit" class="btn-green"
                    data-icon="save"
                    data-toggle="dialog" data-title="新增承运商"
                    data-width="800" data-height="400" data-id="dialog">新增承运商
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
