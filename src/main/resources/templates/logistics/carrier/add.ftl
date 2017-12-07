<div class="bjui-pageContent">
    <form id="add_carrier_form" action="${request.contextPath}/carriers/carrier/add" method="post"
          data-reload-navtab="true"
          data-toggle="validate">
        <table class="table table-condensed table-hover">
            <tbody>
            <tr>
                <td colspan="2" align="center"><h3>新增承运商</h3></td>
            </tr>
            <tr>
                <td>
                    <label class="control-label x90">承运商编号：</label>
                    <input type="text" name="carrierCode"
                           data-rule="required;remote[${request.contextPath}/carriers/carrier/checkCarrierCode]"
                           size="20"/>
                </td>
                <td>
                    <label class="control-label x90">承运商名称：</label>
                    <input type="text" name="carrierName" 
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