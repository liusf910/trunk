<div class="bjui-pageContent">
    <form id="edit_carrier_form" action="${request.contextPath}/carriers/carrier/edit" method="post"
          data-reload-navtab="true"
          data-toggle="validate">
        <table class="table table-condensed table-hover">
            <tbody>
            <tr>
                <td colspan="2" align="center"><h3>编辑承运商</h3></td>
            </tr>
            <tr>
                <td>
                    <label class="control-label x90">承运商编号：</label>
                    <input type="text" name="carrierCode" value="${carrier.carrierCode}" readonly="readonly" size="20"/>
                </td>
                <td>
                    <label class="control-label x90">承运商名称：</label>
                    <input type="text" name="carrierName" value="${carrier.carrierName}"
                    	   data-rule="required"
                           size="20"/>
                </td>
            </tr>
            </tbody>
        </table>
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