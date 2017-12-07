<script type="text/javascript">    
	function saveStorageCode(){
	    var chk_list=$("input[name='idss']");
	    var shipperCode="";
	    var courierName="";
	    for(var i=0;i<chk_list.length;i++){
			if(chk_list[i].checked==true){
			   shipperCode+=chk_list[i].alt+",";
			   courierName+=chk_list[i].title+",";
			}
		}
		$("#courierName").val(courierName.substring(0,courierName.length-1));
		$("#shipperCode").val(shipperCode.substring(0,shipperCode.length-1));
		$("#nowDClose").click();
	}
	function getStorageCode(){
      	$('#storageCode').val($('#storageCodeSelect').val());
    }
    function getShipperCode(){
      	$('#shipperCodeInfo').val($('#shipperCodeSelect').val());
    }
</script>
<div class="bjui-pageHeader">
    <form id="pagerForm" data-toggle="ajaxsearch" class="pageForm" action="${request.contextPath}/setting/storage/getAllStorageCourier"
          data-toggle="validate"
          method="post">
          
          <div style="margin:15px auto 0;">
            <fieldset>
                <table class="table table-condensed table-hover">
                    <tbody>
                    <tr>
                        <td align="center" width="15%"><label class="label-control">仓库名称：</label></td>
                        <td width="15%">
                            <select id="storageCodeSelect" data-toggle="selectpicker" data-width="200"  title="" multiple onchange="getStorageCode()">
				                <#if storages??>
				                    <#list storages as storage>
				                        <option value="${storage.storageCode}" <#if infoWhere.storageCode?? && infoWhere.storageCode?index_of(storage.storageCode)!=-1  >
                                selected="selected"</#if>>${storage.storageName}</option>
				                    </#list>
				                </#if>
				            </select>
				            <input type="hidden" name="storageCode" id="storageCode" value="${infoWhere.storageCode}">
                        </td>
                        <td align="center" width="15%"><label class="label-control">快递公司：</label></td>
                        <td width="15%">
                            <select id="shipperCodeSelect" data-toggle="selectpicker" data-width="200"  title="" multiple onchange="getShipperCode();">
				                <#if couriers??>
				                    <#list couriers as courier>
				                        <option value="${courier.shipperCode}" 
				                        <#if infoWhere.shipperCode?? && infoWhere.shipperCode?index_of(courier.shipperCode)!=-1 >
				                                selected="selected"</#if>>${courier.courierName}</option>
				                    </#list>
				                </#if>
				            </select>
				            <input type="hidden" name="shipperCodeInfo" id="shipperCodeInfo" value="${infoWhere.shipperCode}">
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
            <#if storageCourierList??>
                <#list storageCourierList as storageCourier>
                <tr>
                    <td><input type="checkbox" data-toggle="icheck" name="idss" alt="${storageCourier.storageCode}-${storageCourier.shipperCode}" title="${storageCourier.storageName}-${storageCourier.courierName}"
                    	<#if shipperCodes?? && shipperCodes?index_of(storageCourier.storageCode+"-"+storageCourier.shipperCode)!=-1>checked</#if>>
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