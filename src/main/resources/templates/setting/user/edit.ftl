<script type="text/javascript"> 
	function dialog_editCourier() {
	    $(".bjui-pageContent").dialog({
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
<div class="bjui-pageContent">
    <form id="edit_user_form" action="${request.contextPath}/setting/user/edit" method="post" data-reload-navtab="true"
          data-toggle="validate">
        <table class="table table-condensed table-hover">
            <tbody>
            <tr>
                <td colspan="2" align="center"><h3>编辑用户</h3></td>
            </tr>
            <tr>
                <td>
                    <input type="hidden" name="userId" value="${user.userId}">
                    <label class="control-label x90">登录名：</label>
                    <input type="text" name="logonName" value="${user.logonName}"
                           data-rule="登录名:required;remote[${request.contextPath}/setting/user/${user.userId}/checkUserName]"
                           size="20"/>
                </td>
                <td>
                    <label class="control-label x90">用户姓名：</label>
                    <input type="text" name="userName" value="${user.userName!""}" size="20"/>
                </td>
            </tr>
            <tr>
                <td>
                    <label class="control-label x90">联系电话：</label>
                    <input type="text" name="mobile" value="${user.mobile!""}" data-rule="mobile" size="20"/>
                </td>
                <td>
                    <label class="control-label x90">Email：</label>
                    <input type="text" name="email" value="${user.email!""}" data-rule="email:required;email" size="20"/>
                </td>
            </tr>
            <tr>
                <td colspan='2'>
                    <label class="control-label x90">用户角色：</label>
                    <select name="roleType" data-toggle="selectpicker" data-width="200" title="" multiple data-rule="用户角色:required;">
                    <#if roles??>
                        <#list roles as role>
                            <option value="${role.roleId}" <#if role.isSelected>selected</#if>>${role.roleName}</option>
                        </#list>
                    </#if>
                    </select>
                </td>
            </tr>
            <tr>
                <td colspan='2'>
                    <label class="control-label x90">所属仓库快递：</label>
                    <textarea name="courierName" id="courierName" cols="50" rows="4" data-rule="所属仓库快递:required;" >${user.courierName}</textarea>
                    <input type="hidden" name="shipperCode" id="shipperCode" value="${user.shipperCode}">
                    <a class="btn btn-green"    onclick="dialog_editCourier()"  >添加仓库快递</a> 
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