<div class="bjui-pageContent">
    <form id="edit_courier_form" action="${request.contextPath}/setting/courier/edit" method="post"
          data-reload-navtab="true"
          data-toggle="validate">
        <table class="table table-condensed table-hover">
            <tbody>
            <tr>
                <td colspan="2" align="center"><h3>编辑快递</h3></td>
            </tr>
            <tr>
                <td>
                    <label class="control-label x90">快递公司编号：</label>
                    <input type="text" name="shipperCode" value="${courier.shipperCode}" readonly="readonly" size="20"/>
                </td>
                <td>
                    <label class="control-label x90">快递公司名称：</label>
                    <input type="text" name="courierName" value="${courier.courierName}"
                           data-rule="required;remote[${request.contextPath}/setting/courier/${courier.shipperCode}/checkCourierName]"
                           size="20"/>
                </td>
            </tr>
            <tr>
                <td>
                    <label class="control-label x90">app_key：</label>
                    <input type="text" name="appKey" value="${courier.appKey}" data-rule="required" size="20"/>
                </td>
                <td>
                    <label class="control-label x90">secret_key：</label>
                    <input type="text" name="secretKey" value="${courier.secretKey}" data-rule="required" size="20"/>
                </td>
            </tr>
            <tr>
                <td>
                    <label class="control-label x90">方法名：</label>
                    <input type="text" name="method" value="${courier.method!""}" size="20"/>
                </td>
                <td>
                    <label class="control-label x90">注册标识：</label>
                    <input type="text" name="userId" value="${courier.userId!""}" size="20"/>
                </td>
            </tr>
            <tr>
                <td>
                    <label class="control-label x90">api_url：</label>
                    <input type="text" name="apiUrl" value="${courier.apiUrl!""}" size="20"/>
                </td>
                <td>
                    <label class="control-label x90">api_version：</label>
                    <input type="text" name="apiVersion" value="${courier.apiVersion!""}" size="20"/>
                </td>
            </tr>
            <tr>
                <td>
                    <label class="control-label x90">数据格式：</label>
                    <input type="text" name="format" value="${courier.format!""}" size="20"/>
                </td>
                <td>
                    <label class="control-label x90"></label>
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