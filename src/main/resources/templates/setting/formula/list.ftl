<@shiro.hasPermission name="/setting/formula/edit"><#assign formulaEditMark = true/></@shiro.hasPermission>
<div class="bjui-pageHeader">
    <form id="pagerForm" data-toggle="ajaxsearch" class="pageForm" action="${request.contextPath}/setting/formula/list"
          data-toggle="validate"
          method="post">
        <div style="margin:15px auto 0;">
            <fieldset>
                <legend>查询条件</legend>
                <table class="table table-condensed table-hover">
                    <tbody>
                    <tr>
                        <td align="center" width="15%"><label class="label-control">公式名称：</label></td>
                        <td width="35%">
                            <input type="text" name="formulaName" value="${formula.formulaName!""}" size="20"/>
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
        <table class="table table-bordered table-hover table-striped table-top" <#--data-selected-multi="true"-->>
            <thead>
            <tr>
                <th width="20%">公式名称</th>
                <th width="30%">表达式</th>
                <th width="30%">创建时间</th>
                <th width="20%">操作</th>
            </tr>
            </thead>
            <tbody>
            <#if pageInfo.list??>
                <#list pageInfo.list as formula>
                <tr data-id="1">
                    <td>${formula.formulaName}</td>
                    <td>${formula.expression}</td>
                    <td>${formula.createDate?string("yyyy-MM-dd HH:mm:ss")}</td>
                    <td>
                        <#if formulaEditMark>
                            <a href="${request.contextPath}/setting/formula/formula_edit?formulaId=${formula.formulaId}"
                               class="btn-green" data-toggle="dialog"
                               data-width="400" data-height="300" data-title="编辑公式">编辑</a>
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
        <@shiro.hasPermission name="/setting/formula/add">
            <button href="${request.contextPath}/setting/formula/formula_add" type="submit" class="btn-green"
                    data-icon="save"
                    data-toggle="dialog"
                    data-width="400" data-height="300" data-id="dialog">新增公式
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
