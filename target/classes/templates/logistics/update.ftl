<div class="bjui-pageContent">
        <table class="table table-condensed table-hover">
            <tbody>
            <tr>
                <td>
                    <label class="control-label x90">修改日期：</label>
                    <input type="text" id="updateDate" name="updateDate" data-toggle="datepicker"
                               size="15">
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
        <li>
           <button id="okbtn" onClick="updateData();" class="btn-red">确认
			</button>
        </li>
    </ul>
</div>
<script type="text/javascript">
function updateData(upd){
	var checkval = $("input:checkbox[name='ids']:checked");
	var updateDate = $("#updateDate").val();
	if(updateDate==null||updateDate==''){
		$("#okbtn").alertmsg("warn","请填写日期");
		return;
	}
	if(checkval==null||checkval.length<=0){
		$("#okbtn").alertmsg("warn","未选择要修改项");
		return;
	}
	var ids = [];
	var data = {};
	for(var i=0;i<checkval.length;i++){
		ids[i]=$(checkval[i]).val();
	}
	data.ids = ids;
	data.updateDate = updateDate;
	$.ajax({
             type: "POST",
             url: "/logistics/updateBatch",
             data:JSON.stringify(data),
             dataType:'json',
             contentType: 'application/json;charset=UTF-8',
             success : function(res){
             	$("#updateView").navtab("reloadForm","true");
             	alert("操作成功");
             	$("#updateView").dialog("closeCurrent");
             }
         });
}
</script>