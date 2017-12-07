<div class="bjui-pageContent">
    <table class="table table-condensed table-hover">
        <tbody>
        <tr>
            <td colspan="2" align="center"><h3>时效上传下载</h3></td>
        </tr>
        <tr style="height: 40px;">
            <td align="center">
                <label class="control-label x90">时效模板：</label>
            </td>
            <td>
                <a class="btn btn-orange" href="http://10.4.33.144/时效模板.csv" target="_self"
                   data-icon="save">下载模板文件</a>
            </td>
        </tr>
        <tr style="height: 40px;">
            <td align="center">
                <label class="control-label x90">文件上传：</label>
            </td>
            <td>
                <div style="display:inline-block; vertical-align:middle;">
                    <div id="doc_pic_up" data-toggle="upload"
                         data-uploader="${request.contextPath}/upload/timelimit"
                         data-icon="cloud-upload"
                         data-file-type-exts="*.csv;"
                         data-file-size-limit="2048"
                         data-multi="false"
                         data-auto="false"
                         data-on-upload-success="doc_upload_success"></div>
                    <input type="hidden" name="file" id="doc_pic">
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