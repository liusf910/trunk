<div class="bjui-pageContent">
    <table class="table table-condensed table-hover">
        <tbody>
        <tr>
            <div id="mask" class="mask"></div>
            <td colspan="2" align="center	"><h3>物流预约上传下载</h3></td>
        </tr>
        <tr style="height: 40px;">
            <td align="center">
                <label class="control-label x90">物流预约模板：</label>
            </td>
            <td>
                <a id="tookTemplate" class="btn btn-orange" href="http://10.4.33.144/物流预约模板.csv"
                   data-icon="save">下载物流预约模板</a>
            </td>
        </tr>
        <tr style="height: 40px;">
            <td align="center">
                <label class="control-label x90">文件上传：</label>
            </td>
            <td>
                <div style="display:inline-block; vertical-align:middle;">
                    <div id="doc_pic_up" data-toggle="upload"
                         data-uploader="${request.contextPath}/upload/logistics"
                         data-icon="cloud-upload"
                         data-file-type-exts="*.csv;"
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