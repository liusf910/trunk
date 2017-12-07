<div class="bjui-pageContent">
    <div style="margin:15px auto 0; width:800px;">
        <fieldset>
            <legend>邮箱基础设置</legend>
            <form action="${request.contextPath}/setting/email/editConfig" class="pageForm" method="post"
                  data-toggle="validate">
                <input type="hidden" name="emailId" <#if emailConf??>value="${emailConf.emailId}"</#if>>
                <table class="table table-condensed table-hover">
                    <tbody>
                    <tr>
                        <td align="right">
                            <label class="label-control">SMTP服务器地址：</label>
                        </td>
                        <td>
                            <input type="text" name="server" <#if emailConf??>value="${emailConf.server}"</#if>
                                   data-rule="required;" size="20"/>

                        </td>
                    </tr>
                    <tr>
                        <td align="right">
                            <label class="label-control">SMTP服务器端口：</label>
                        </td>
                        <td>
                            <input type="text" name="port" <#if emailConf??>value="${emailConf.port}"</#if>
                                   data-rule="端口号:required;digits" size="20"/>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">
                            <label class="label-control">SMTP用户名：</label>
                        </td>
                        <td>
                            <input type="text" name="smtpName" <#if emailConf??>value="${emailConf.smtpName}"</#if>
                                   data-rule="required;" size="20"/>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">
                            <label class="label-control">SMTP密码：</label>
                        </td>
                        <td>
                            <input type="text" name="smtpPwd" <#if emailConf??>value="${emailConf.smtpPwd}"</#if>
                                   data-rule="required;length[6~]" size="20"/>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">
                            <label class="label-control">SMTP是否启用SSL：</label>
                        </td>
                        <td>
                            <input type="radio" name="isSsl" id="doc-radio1" data-toggle="icheck" value="0"
                                   data-rule="checked"
                            <#if emailConf??><#if emailConf.isSsl == "0">checked="checked"</#if></#if> data-label="否"/>
                            <input type="radio" name="isSsl" id="doc-radio2" data-toggle="icheck" value="1"
                                   data-rule="checked"
                            <#if emailConf??><#if emailConf.isSsl != "0">checked="checked"</#if></#if> data-label="是"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
                <br/><br/>
                <div align="center">
                    <button type="submit" class="btn-default">保存</button>
                    <button type="button" class="btn-close" style="margin-left: 20px;">关闭</button>
                </div>
            </form>
        </fieldset>
    </div>
</div>