<div class="bjui-pageContent">
    <form id="add_courier_form" action="${request.contextPath}/setting/courier/add" method="post"
          data-reload-navtab="true"
          data-toggle="validate">
        <table class="table table-condensed table-hover">
            <tbody>
            <tr>
                <td colspan="2" align="center"><h3>新增快递</h3></td>
            </tr>
            <tr>
                <td>
                    <label class="control-label x90">快递公司编号：</label>
                    <input type="text" name="shipperCode"
                           data-rule="required;remote[${request.contextPath}/setting/courier/checkShipperCode]"
                           size="20"/>
                </td>
                <td>
                    <label class="control-label x90">快递公司名称：</label>
                    <input type="text" name="courierName"
                           data-rule="required;remote[${request.contextPath}/setting/courier/1/checkCourierName]"
                           size="20"/>
                </td>
            </tr>
            <tr>
                <td>
                    <label class="control-label x90">app_key：</label>
                    <input type="text" name="appKey" data-rule="required" size="20"/>
                </td>
                <td>
                    <label class="control-label x90">secret_key：</label>
                    <input type="text" name="secretKey" data-rule="required" size="20"/>
                </td>
            </tr>
            <tr>
                <td>
                    <label class="control-label x90">方法名：</label>
                    <input type="text" name="method" size="20"/>
                </td>
                <td>
                    <label class="control-label x90">注册标识：</label>
                    <input type="text" name="userId" size="20"/>
                </td>
            </tr>
            <tr>
                <td>
                    <label class="control-label x90">api_url：</label>
                    <input type="text" name="apiUrl" size="20"/>
                </td>
                <td>
                    <label class="control-label x90">api_version：</label>
                    <input type="text" name="apiVersion" size="20"/>
                </td>
            </tr>
            <tr>
                <td>
                    <label class="control-label x90">数据格式：</label>
                    <input type="text" name="format" size="20"/>
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