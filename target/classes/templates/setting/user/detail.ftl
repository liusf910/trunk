<div class="bjui-pageContent">
    <div style="margin:15px auto 0; width:800px;">
        <fieldset>
            <legend>列表详细信息</legend>
            <table class="table table-condensed table-hover">
                <tbody>
                <tr>
                    <td align="right"><label class="label-control">编号：</label></td>
                    <td><label class="label-control">${user.userId!""}</label></td>
                    <td align="right"><label class="label-control">登录名：</label></td>
                    <td><label class="label-control">${user.logonName}</label></td>
                </tr>
                <tr>
                    <td align="right"><label class="label-control">用户名称：</label></td>
                    <td><label class="label-control">${user.userName!""}</label></td>
                    <td align="right"><label class="label-control">手机号码：</label></td>
                    <td><label class="label-control">${user.mobile!""}</label></td>
                </tr>
                <tr>
                    <td align="right"><label class="label-control">邮箱：</label></td>
                    <td><label class="label-control">${user.email!""}</label></td>
                    <td align="right"><label class="label-control">用户角色：</label></td>
                    <td><label class="label-control">${user.roleName!""}</label></td>
                </tr>
                <tr>
                    <td align="right"><label class="label-control" style="width:110px;">所属仓库快递公司：</label></td>
                    <td colspan="3"><label class="label-control"><#if user.shipperCode?? && user.shipperCode != '全部'>${user.courierName}</#if></label></td>
                </tr>
                <tr>
                    <td align="right"><label class="label-control">创建人：</label></td>
                    <td>
                        <label class="label-control">${user.createName}</label>
                    </td>
                    <td align="right"><label class="label-control">最后修改人：</label></td>
                    <td>
                        <label class="label-control">${user.lastName!""}</label>
                    </td>
                </tr>
                <tr>
                    <td align="right"><label class="label-control">创建时间：</label></td>
                    <td>
                        <label class="label-control">${user.createDate?string("yyyy-MM-dd HH:mm:ss")}</label>
                    </td>
                    <td align="right"><label class="label-control">修改时间：</label></td>
                    <td>
                        <label class="label-control">${user.modifyDate?string("yyyy-MM-dd HH:mm:ss")}</label>
                    </td>
                </tr>
                <tr>
                    <td align="right"><label class="label-control">是否删除：</label></td>
                    <td><label class="label-control"><#if user.isDelete == "0">正常<#else>已删除</#if></label></td>
                    <td align="right"><label class="label-control">备注：</label></td>
                    <td><label class="label-control">${user.comment!""}</label></td>
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