<div class="bjui-pageContent">
    <table class="table table-condensed table-hover">
        <tbody>
        <tr>
            <td colspan="2" align="center"><h3>签收上传下载</h3></td>
        </tr>
        <tr style="height: 40px;">
            <td align="center">
                <label class="control-label x90">签收模板：</label>
            </td>
            <td>
                <a id="tookTemplate" class="btn btn-orange" href="http://10.4.33.144/签收模板.xls"
                   data-icon="save">下载签收模板</a>
            </td>
        </tr>
        <tr style="height: 40px;">
            <td align="center">
                <label class="control-label x90">文件上传：</label>
            </td>
            <td>
                <div style="display:inline-block; vertical-align:middle;">
                    <div id="doc_pic_up" data-toggle="upload"
                         data-uploader="${request.contextPath}/upload/sign"
                         data-icon="cloud-upload"
                         data-file-type-exts="*.xls;*.xlsx;"
                         data-file-size-limit="2048"
                         data-multi="false"
                         data-auto="false"
                         data-on-upload-success="doc_upload_success"></div>
                    <input type="hidden" name="file" id="doc_pic"/>
                </div>
            </td>
        </tr>
        </tbody>
    </table>
</div>
<div class="bjui-pageFooter">
    <ul>
        <li>
            <button type="button" class="btn-close">关闭</button>
        </li>
    </ul>
</div>