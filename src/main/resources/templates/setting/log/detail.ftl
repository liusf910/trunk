<style type="text/css">
    .words {
        display: inline-block;
        width: 650px;
        word-break: break-all;
    }
</style>
<div class="bjui-pageContent">
    <div style="margin:15px auto 0; width:800px;">
        <fieldset>
            <legend>列表详细信息</legend>
            <table class="table table-condensed table-hover">
                <tbody>
                <tr>
                    <td align="right"><label class="label-control">操作：</label></td>
                    <td><label class="label-control">${log.operation}</label></td>
                    <td align="right"><label class="label-control">操作员：</label></td>
                    <td><label class="label-control">${log.logonName}</label></td>
                </tr>
                <tr>
                    <td align="right" style="width: 80px;"><label class="label-control">创建时间：</label></td>
                    <td>
                        <label class="label-control">${log.createDate?string("yyyy-MM-dd HH:mm:ss")}</label>
                    </td>
                    <td align="right"><label class="label-control">修改时间：</label></td>
                    <td>
                        <label class="label-control">${log.modifyDate?string("yyyy-MM-dd HH:mm:ss")}</label>
                    </td>
                </tr>
                <tr>
                    <td align="right"><label class="label-control">IP：</label></td>
                    <td colspan="3"><label class="label-control">${log.ip!""}</label></td>
                </tr>
                <tr>
                    <td align="right"><label class="label-control">请求参数：</label></td>
                    <td colspan="3"><label class="label-control words">${log.parameter!""}</label></td>
                </tr>
                <tr>
                    <td align="right"><label class="label-control">备注：</label></td>
                    <td colspan="3"><label class="label-control words">${log.content!""}</label></td>
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