<div class="bjui-pageContent">
    <form action="${request.contextPath}/count/startAvgDay" method="post"
          data-reload-navtab="true"
          data-toggle="validate">
        <input type="hidden" name="type" value="count-5"/>
        <table class="table table-condensed table-hover">
            <tbody>
            <tr>
                <td colspan="2" align="center"><h3>统计条件选择</h3></td>
            </tr>
            <tr style="height: 40px;">
                <td align="center">
                    <label class="control-label x90">选择仓库：</label>
                </td>
                <td>
                    <select name="storageCode" id="storageCode" data-toggle="selectpicker"
                            data-nextselect="#count_shipperCode"
                            data-width="200"
                            data-refurl="${request.contextPath}/count/getCouriers?storageCode={value}"
                            data-rule="所属仓库:required;">
                        <option value="">---请选择---</option>
                    <#if storages??>
                        <#list storages as storage>
                            <option value="${storage.storageCode}">${storage.storageName}</option>
                        </#list>
                    </#if>
                    </select>
                </td>
            </tr>
            <tr style="height: 40px;">
                <td align="center">
                    <label class="control-label x90">选择快递：</label>
                </td>
                <td>
                    <select id="count_shipperCode" name="shipperCode" data-width="200" data-toggle="selectpicker"
                            multiple>
                    </select>
                </td>
            </tr>
            <tr style="height: 40px;">
                <td align="center">
                    <label class="control-label x90">发货时间：</label>
                </td>
                <td>
                    <input type="text" name="shipmentsStartDate" data-toggle="datepicker" size="18"
                           data-rule="required;">
                    &nbsp;至&nbsp;
                    <input type="text" name="shipmentsEndDate" data-toggle="datepicker" size="18" data-rule="required;">
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
            <button type="submit" class="btn-default">开始统计</button>
        </li>
    </ul>
</div>