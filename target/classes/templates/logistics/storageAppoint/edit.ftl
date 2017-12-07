<div class="bjui-pageContent">
    <form id="edit_storageAppoint_form" action="${request.contextPath}/storages/storageAppoint/edit" method="post"
          data-reload-navtab="true"
          data-toggle="validate">
        <table class="table table-condensed table-hover">
            <tbody>
            <tr>
                <td colspan="2" align="center"><h3>编辑仓库预约</h3></td>
            </tr>
            <tr>
                <td>
                    <label class="control-label x90">仓库ID：</label>
                    <input type="text" name="id"
                           readonly = "readonly"
                           value = "${storageAppoint.id!''}"
                           size="20"/>
                </td>
                <td>
                    <label class="control-label x90">仓库编号：</label>
                    <input type="text" name="warehouseCode"
                           readonly = "readonly"
                           value = "${storageAppoint.warehouseCode!''}"
                           size="20"/>
                </td>
            </tr>
            <tr>
                <td>
                    <label class="control-label x90">仓库名称：</label>
                    <input type="text" name="name"
                           readonly = "readonly" 
                    	   value = "${storageAppoint.name!''}"
                    	   data-rule="required"
                           size="20"/>
                </td>
                <td>
                    <label class="control-label x90">预约量：</label>
                    <input type="text" name="appointLimit"
                    	   value = "${storageAppoint.appointLimit!''}"
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