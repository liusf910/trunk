<div class="bjui-pageContent"> 
    <div style="margin:15px auto 0; width:800px;">
        <fieldset>
            <legend>仓库详细信息</legend>
            <table class="table table-condensed table-hover">
                <tbody>
                <tr>
                    <td align="right"><label class="label-control">仓库编号：</label></td>
                    <td><label class="label-control">${storage.storageCode!""}</label></td>
                    <td align="right"><label class="label-control">仓库名称：</label></td>
                    <td><label class="label-control">${storage.storageName!""}</label></td>
                </tr>
                <tr>
                    <td align="right"><label class="label-control">拥有快递公司：</label></td>
                    <td colspan="3"><label class="label-control">${storage.courierName!""}</label></td>
                </tr>
                </tbody>
            </table>
        </fieldset>
    </div>
</div>
<div class="bjui-pageFooter">
    <ul>
        <li>
            <button type="button" class="btn-close" data-icon="close">关闭</button>
        </li>
    </ul>
</div>