<div class="bjui-pageContent">
    <form id="add_formula_form" action="${request.contextPath}/setting/formula/add" class="pageForm" method="post"
          data-toggle="validate"
          data-reload-navtab="true">
        <div style="margin:15px auto 0;">
            <fieldset>
                <legend>新增公式</legend>
                <table class="table table-condensed table-hover">
                    <tbody>
                    <tr>
                        <td align="right"><label class="label-control">公式名称：</label></td>
                        <td>
                            <input type="text" name="formulaName" data-rule="required;remote[${request.contextPath}/setting/formula/000/checkFormulaName]" size="20">
                        </td>
                    </tr>
                    <tr>
                        <td align="right"><label class="label-control">公式：</label></td>
                        <td>
                            <input type="text" name="expression"
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