<script type="text/javascript"> 	
	function dialog_addCourier() {
	    $("#bodyDiv").dialog({
	        type:'POST',
	        id:'addCourier_dialog',
	        url:'/setting/storage/getAllStorageCourier',
	        data:{"shipperCode":$("#shipperCode").val()},
	        mask:true,
	        title:'添加仓库快递',
	        height:400,
	        width:800,
	        fresh:true
	    })
	} 
</script>
<div id="bodyDiv"></div>
<div class="bjui-pageContent">
    <form id="add_user_form" action="${request.contextPath}/setting/user/add" method="post" data-reload-navtab="true"
          data-toggle="validate">
        <table class="table table-condensed table-hover">
            <tbody>
            <tr>
                <td colspan="2" align="center"><h3>新增用户</h3></td>
            </tr>
            <tr>
                <td>
                    <label class="control-label x90">登录名：</label>
                    <input type="text" name="logonName"
                           data-rule="登录名:required;remote[${request.contextPath}/setting/user/1/checkUserName]" size="20"/>
                </td>
                <td>
                    <label class="control-label x90">用户姓名：</label>
                    <input type="text" name="userName" size="20"/>
                </td>
            </tr>
            <tr>
                <td>
                    <label class="control-label x90">密码：</label>
                    <input type="text" name="logonPwd" data-rule="密码:required;length[6~]" size="20"/>
                </td>
                <td>
                    <label class="control-label x90">确认密码：</label>
                    <input type="text" name="logonPwd2" data-rule="required;match(logonPwd)" size="20"/>
                </td>
            </tr>
            <tr>
                <td>
                    <label class="control-label x90">联系电话：</label>
                    <input type="text" name="mobile" data-rule="mobile" size="20"/>
                </td>
                <td>
                    <label class="control-label x90">Email：</label>
                    <input type="text" name="email" data-rule="email:required;email" size="20"/>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <label class="control-label x90">用户角色：</label>
                    <select name="roleType" data-toggle="selectpicker" data-width="200" title="" multiple data-rule="用户角色:required;">
                    <#if roles??>
                        <#list roles as role>
                            <option value="${role.roleId}">${role.roleName}</option>
                        </#list>
                    </#if>
                    </select>
                </td>
            </tr>
             <tr>
                <td colspan="2">
                    <label class="control-label x90">所属仓库快递：</label>
                    <textarea name="courierName" id="courierName" cols="50" rows="4" data-rule="所属仓库快递:required;"></textarea>
                    <input type="hidden" name="shipperCode" id="shipperCode">
                    <a class="btn btn-green"    onclick="dialog_addCourier()" >添加仓库快递</a> 
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