<script type="text/javascript"> 
	function getStorageCode(){
	    var chk_list=$("input[name='ids']");
	    var storageCode="";
	    var storageName="";
	    for(var i=0;i<chk_list.length;i++){
			if(chk_list[i].checked==true){
			   storageCode+=chk_list[i].alt+",";
			   storageName+=chk_list[i].title+",";
			}
		}
		$("#storageName").val(storageName.substring(0,storageName.length-1));
		$("#storageCode").val(storageCode.substring(0,storageCode.length-1));
		$("#shipperCode").val('');
		$("#courierName").val('');
		$("#nowDClose").click();
	}
</script>
<div class="bjui-pageContent">
    <fieldset>
        <table class="table table-bordered table-hover table-striped table-top">
            <thead>
            <tr>
                <th><input type="checkbox"  class="checkboxCtrl"  data-group="ids" data-toggle="icheck"></th>
                <th>仓库编号</th>
                <th>仓库名称</th>
            </tr>
            </thead>
            <tbody>
            <#if storageList??>
                <#list storageList as storage>
                <tr>
                    <td><input type="checkbox" data-toggle="icheck" name="ids" alt="${storage.storageCode}" title="${storage.storageName}" 
                        <#if storageCodes?? && storageCodes?index_of(storage.storageCode)!=-1>checked</#if> ifClicked="getAll()">
                    <input type="hidden" name="storageCode" value="${storage.storageCode}">
                    <input type="hidden" name="storageName" value="${storage.storageName}">
                    </td>
                    <td>${storage.storageCode}</td>
                    <td>${storage.storageName}</td>
                </tr>
                </#list>
            </#if>
            </tbody>
        </table>
    </fieldset>
</div>
<div class="bjui-pageFooter">
    <ul>
        <li>
            <button type="button" class="btn-close" id="nowDClose">关闭</button>
        </li>
        <li>
            <button type="submit" class="btn-default" onclick="getStorageCode()">保存</button>
        </li>
    </ul>
</div>