<div class="bjui-pageContent">
    <form id="edit_formula_form" action="${request.contextPath}/setting/formula/edit" class="pageForm" method="post"
          data-toggle="validate"
          data-reload-navtab="true">
        <div style="margin:15px auto 0;">
            <fieldset>
                <legend>编辑公式</legend>
                <table class="table table-condensed table-hover">

                    <tbody>
                    <tr>
                        <td align="right"><label class="label-control">公式名称：</label></td>
                        <td>
                            <input type="hidden" name="formulaId" value="${formula.formulaId}">
                            <input type="text" name="formulaName" readonly="readonly" value="${formula.formulaName}"
                                   data-rule="required;remote[${request.contextPath}/setting/formula/${formula.formulaId}/checkFormulaName]" size="20">
                        </td>
                    </tr>
                    <tr>
                        <td align="right"><label class="label-control">公式：</label></td>
                        <td>
                            <input type="text" name="expression" value="${formula.expression}"
                                   data-rule="required;remote[${request.contextPath}/setting/formula/checkExpression]"
                                   size="20" placeholder="关键字:sDay,vDay,oneDay">
                        </td>
                    </tr>
                    </tbody>
                </table>
            </fieldset>
        </div>
    </form>
</div>
<div class="bjui-pageFooter">
    <ul>
        <li>
            <button type="button" class="btn-close">关闭</button>
        </li>
        <li>
            <button type="submit" class="btn-default">保存</button>
        </li>
    </ul>
</div>