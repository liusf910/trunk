<div class="bjui-pageContent">
    <form id="user_pwschange_form" action="${request.contextPath}/setting/user/editUserPwd" data-toggle="validate" method="post">
        <hr/>
        <div class="form-group">
            <label class="control-label x85">旧密码：</label>
            <input type="password" data-rule="required" name="oldPwd" placeholder="旧密码"
                   size="20">
        </div>
        <div class="form-group" style="margin: 20px 0 20px; ">
            <label class="control-label x85">新密码：</label>
            <input type="password" data-rule="新密码:required;length[6~]" name="newPwd" placeholder="新密码" size="20">
        </div>
        <div class="form-group">
            <label class="control-label x85">确认密码：</label>
            <input type="password" data-rule="required;match(newPwd)" name="newPwd2" placeholder="确认新密码" size="20">
        </div>
    </form>
</div>
<div class="bjui-pageFooter">
    <ul>
        <li>
            <button type="button" class="btn-close">取消</button>
        </li>
        <li>
            <button type="submit" class="btn-default">保存</button>
        </li>
    </ul>
</div>