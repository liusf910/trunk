<script type="text/javascript">    
	function saveStorageCode(){
	    var chk_list=$("input[name='idss']");
	    var shipperCode="";
	    var courierName="";
	    var checkedNum=0;
	    for(var i=0;i<chk_list.length;i++){
			if(chk_list[i].checked==true){
			   checkedNum+=1;
			   shipperCode+=chk_list[i].alt+",";
			   courierName+=chk_list[i].title+",";
			}
		}
		if(checkedNum==0){
			$("#msgDiv").alertmsg('warn', '至少选择一项');
        	return false;
		}
		$("#courierNameAwaitPut").val(courierName.substring(0,courierName.length-1));
		$("#shipperCodeAwaitPut").val(shipperCode.substring(0,shipperCode.length-1));
		$("#nowDClose").click();
	}
	function getStorageCode(){
      	$('#storageCodeAwaitPut').val($('#storageCodeSelectAwaitPut').val());
    }
    function getShipperCode(){
      	$('#shipperCodeInfoAwaitPut').val($('#shipperCodeSelectAwaitPut').val());
    }
</script>
<div id="msgDiv"></div>
<div class="bjui-pageHeader">
    <form id="pagerForm" data-toggle="ajaxsearch" class="pageForm" action="${request.contextPath}/setting/storage/getStorageCouriersByUserIdAwaitPut"
          data-toggle="validate"
          method="post">
          
          <div style="margin:15px auto 0;">
            <fieldset>
                <table class="table table-condensed table-hover">
                    <tbody>
                    <tr>
                        <td align="center" width="15%"><label class="label-control">仓库名称：</label></td>
                        <td width="15%">
                            <select id="storageCodeSelectAwaitPut" data-toggle="selectpicker" data-width="200"  title="" multiple onchange="getStorageCode()">
				                <#if storages??>
				                    <#list storages as storage>
				                        <option value="${storage.storageCode}" <#if infoWhereAwaitPut.storageCode?? && infoWhereAwaitPut.storageCode?index_of(storage.storageCode)!=-1  >
                                selected="selected"</#if>>${storage.storageName}</option>
				                    </#list>
				                </#if>
				            </select>
				            <input type="hidden" name="storageCode" id="storageCodeAwaitPut" value="${infoWhereAwaitPut.storageCode}">
                        </td>
                        <td align="center" width="15%"><label class="label-control">快递公司：</label></td>
                        <td width="15%">
                            <select id="shipperCodeSelectAwaitPut" data-toggle="selectpicker" data-width="200"  title="" multiple onchange="getShipperCode();">
				                <#if couriers??>
				                    <#list couriers as courier>
				                        <option value="${courier.shipperCode}" 
				                        <#if infoWhereAwaitPut.shipperCode?? && infoWhereAwaitPut.shipperCode?index_of(courier.shipperCode)!=-1 >
				                                selected="selected"</#if>>${courier.courierName}</option>
				                    </#list>
				                </#if>
				            </select>
				            <input type="hidden" name="shipperCodeInfo" id="shipperCodeInfoAwaitPut" value="${infoWhereAwaitPut.shipperCode}">
                        </td>
                        
                        <td> <button type="submit" class="btn-default" data-icon="search">查询</button></td>
                    </tr>
                    </tbody>
                </table>
            </fieldset>
        </div>
    </form>
</div>
<div class="bjui-pageContent">
    <fieldset>
        <table class="table table-bordered table-hover table-striped table-top">
            <thead>
            <tr>
                <th><input type="checkbox"  class="checkboxCtrl"  data-group="idss" data-toggle="icheck"></th>
                <th>仓库名称</th>
                <th>快递名称</th>
                <th>仓库编号</th>
                <th>快递编号</th>
            </tr>
            </thead>
            <tbody>
            <#if storageCourierListAwaitPut??>
                <#list storageCourierListAwaitPut as storageCourier>
                <tr>
                    <td><input type="checkbox" data-toggle="icheck" name="idss" alt="'${storageCourier.storageCode}-${storageCourier.shipperCode}'" title="${storageCourier.storageName}-${storageCourier.courierName}"
                    	<#if shipperCodesAwaitPut?? && shipperCodesAwaitPut?index_of(storageCourier.storageCode+"-"+storageCourier.shipperCode)!=-1>checked</#if>>
                    </td>
                    <td>${storageCourier.storageName}</td>
                    <td>${storageCourier.courierName}</td>
                    <td>${storageCourier.storageCode}</td>
                    <td>${storageCourier.shipperCode}</td>
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
            <button type="button" class="btn-default" onclick="saveStorageCode()">保存</button>
        </li>
    </ul>
</div>