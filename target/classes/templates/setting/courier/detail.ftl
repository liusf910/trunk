<div class="bjui-pageContent">
    <div style="margin:15px auto 0; width:800px;">
        <fieldset>
            <legend>列表详细信息</legend>
            <table class="table table-condensed table-hover">
                <tbody>
                <tr>
                    <td align="right"><label class="label-control">快递公司编号：</label></td>
                    <td><label class="label-control">${courier.shipperCode}</label></td>
                    <td align="right"><label class="label-control">快递名称：</label></td>
                    <td><label class="label-control">${courier.courierName}</label></td>
                </tr>
                <tr>
                    <td align="right"><label class="label-control">app_key：</label></td>
                    <td><label class="label-control">${courier.appKey}</label></td>
                    <td align="right"><label class="label-control">secret_key：</label></td>
                    <td><label class="label-control">${courier.secretKey}</label></td>
                </tr>
                <tr>
                    <td align="right"><label class="label-control">分配的方法名：</label></td>
                    <td><label class="label-control">${courier.method!""}</label></td>
                    <td align="right"><label class="label-control">注册标识：</label></td>
                    <td><label class="label-control">${courier.userId!""}</label></td>
                </tr>
                <tr>
                    <td align="right"><label class="label-control">api_url：</label></td>
                    <td>
                        <label class="label-control">${courier.apiUrl!""}</label>
                    </td>
                    <td align="right"><label class="label-control">api_version：</label></td>
                    <td>
                        <label class="label-control">${courier.apiVersion!""}</label>
                    </td>
                </tr>
                <tr>
                    <td align="right"><label class="label-control">创建时间：</label></td>
                    <td>
                        <label class="label-control">${courier.createDate?string("yyyy-MM-dd HH:mm:ss")}</label>
                    </td>
                    <td align="right"><label class="label-control">修改时间：</label></td>
                    <td>
                        <label class="label-control">${courier.modifyDate?string("yyyy-MM-dd HH:mm:ss")}</label>
                    </td>
                </tr>
                <tr>
                    <td align="right"><label class="label-control">数据格式：</label></td>
                    <td><label class="label-control">${courier.format!""}</td>
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