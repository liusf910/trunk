<div class="bjui-pageContent">
    <form id="edit_storage_form" action="${request.contextPath}/setting/storage/edit" method="post"
          data-reload-navtab="true"
          data-toggle="validate">
        <table class="table table-condensed table-hover">
            <tbody>
            <tr>
                <td colspan="2" align="center"><h3>编辑仓库</h3></td>
            </tr>
            <tr>
                <td>
                    <label class="control-label x90">仓库编号：</label>
                    <input type="text" name="storageCode" value="${storage.storageCode!""}" size="20"
                           readonly="readonly"/>
                </td>
                <td>
                    <label class="control-label x90">仓库名称：</label>
                    <input type="text" name="storageName" value="${storage.storageName!""}"
                           data-rule="required;;remote[${request.contextPath}/setting/storage/${storage.storageCode}/checkStorageName]"
                           size="20"/>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <label class="control-label x90">拥有快递公司：</label>
                    <select name="storageCourier" data-toggle="selectpicker" data-width="200" multiple>
                    <#if couriers??>
                        <#list couriers as courier>
                            <option value="${courier.shipperCode}"
                                    <#if courier.isSelected>selected</#if>>${courier.courierName}</option>
                        </#list>
                    </#if>
                    </select>
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