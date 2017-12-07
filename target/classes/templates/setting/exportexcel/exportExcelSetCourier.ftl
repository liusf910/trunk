<script type="text/javascript">
	
	//导出
	function exportExcelCourier(){
		var expNameList="";
		var expFieldName="";
		var noexpNameList="";
		var noexpFieldName="";
		var chk_list=$("#xTableC input[name='ids']");
		var trueNum=0;
       	for(var i=0;i<chk_list.length;i++){
	        if(chk_list[i].checked==true){
	            expNameList+=chk_list[i].alt+"#";
	            expFieldName+=chk_list[i].value+"#";
	            trueNum=trueNum+1;
	        }else{
	        	noexpNameList+=chk_list[i].alt+"#";
	        	noexpFieldName+=chk_list[i].value+"#";
	        }
         }
        if(trueNum<=0){
        	$("#msgDiv").alertmsg('warn', '请勾选导出字段');
        	return false;
        }
         $("#expNameListC").val(expNameList);
         $("#expFieldNameC").val(expFieldName);
         $("#noexpNameListC").val(noexpNameList);
         $("#noexpFieldNameC").val(noexpFieldName);
         $("#add_export_formC").submit();
	}
</script>
 <form id="add_export_formC" action="${request.contextPath}/setting/exportExcel/saveExportExcelSet" 
  method="post" data-toggle="validate" >
<input type="hidden" name="belongTo" id="belongToC" value='${belongTo}'>
<input type="hidden" name="expNameList" id="expNameListC" >
<input type="hidden" name="expFieldName" id="expFieldNameC" >
<input type="hidden" name="noexpNameList" id="noexpNameListC" >
<input type="hidden" name="noexpFieldName" id="noexpFieldNameC" >
</form>
<div id="msgDiv"></div>
<div class="bjui-pageContent">
    <div style="margin:15px auto 0; width:800px;">
        <fieldset>
            <legend>导出字段设置</legend>
            <table class="table table-condensed table-hover" id="xTableC">
                <tbody>
                <tr>
                	<td colspan='4'><input type="checkbox"  class="checkboxCtrl"  data-group="ids" data-toggle="icheck">全选</td>
                </tr>
                <tr>
                    <td><input type="checkbox" data-toggle="icheck" name="ids" value="shipmentsRq" alt="发货日期" data-label="发货日期" <#if returnMap??&&returnMap.shipmentsRq == "1">checked='checked'</#if>></td>
                    <td><input type="checkbox"  data-toggle="icheck" name="ids" value="extOrderCode" alt="外部单号" data-label="外部单号" <#if returnMap??&&returnMap.extOrderCode == "1">checked='checked'</#if>></td>
    				<td><input type="checkbox"  data-toggle="icheck" name="ids" value="orderCode" alt="网络单号" data-label="网络单号" <#if returnMap??&&returnMap.orderCode == "1">checked='checked'</#if>></td>
    				<td><input type="checkbox" data-toggle="icheck" name="ids" value="linkman" alt="联系人" data-label="联系人" <#if returnMap??&&returnMap.linkman == "1">checked='checked'</#if>></td>
                </tr>
                <tr>
                    <td><input type="checkbox" data-toggle="icheck" name="ids"  value="province" alt="省份" data-label="省份"  <#if returnMap??&&returnMap.province == "1">checked='checked'</#if>></td>
                    <td><input type="checkbox" data-toggle="icheck" name="ids"  value="city" alt="市"  data-label="市"<#if returnMap??&&returnMap.city == "1">checked='checked'</#if>></td>
    				<td><input type="checkbox" data-toggle="icheck" name="ids"  value="cityScale" alt="城市分级" data-label="城市分级" <#if returnMap??&&returnMap.cityScale == "1">checked='checked'</#if>></td>
    				<td><input type="checkbox" data-toggle="icheck" name="ids"  value="addr" alt="地址" data-label="地址" <#if returnMap??&&returnMap.addr == "1">checked='checked'</#if>></td>
                </tr>
                <tr>
                    <td><input type="checkbox" data-toggle="icheck" name="ids"  value="phone" alt="电话" data-label="电话"  <#if returnMap??&&returnMap.phone == "1">checked='checked'</#if>></td>
                    <td><input type="checkbox" data-toggle="icheck" name="ids"  value="courierName" alt="快递公司"  data-label="快递公司"<#if returnMap??&&returnMap.courierName == "1">checked='checked'</#if>></td>
    				<td><input type="checkbox" data-toggle="icheck" name="ids"  value="logisticCode" alt="速递单号" data-label="速递单号" <#if returnMap??&&returnMap.logisticCode == "1">checked='checked'</#if>></td>
    				<td><input type="checkbox" data-toggle="icheck" name="ids"  value="number" alt="数量" data-label="数量" <#if returnMap??&&returnMap.number == "1">checked='checked'</#if>></td>
                </tr>
                <tr>
                    <td><input type="checkbox" data-toggle="icheck" name="ids"  value="weight" alt="重量" data-label="重量"  <#if returnMap??&&returnMap.weight == "1">checked='checked'</#if>></td>
                    <td><input type="checkbox" data-toggle="icheck" name="ids"  value="storage" alt="发货仓库"  data-label="发货仓库"<#if returnMap??&&returnMap.storage == "1">checked='checked'</#if>></td>
    				<td><input type="checkbox" data-toggle="icheck" name="ids"  value="xtTookTime" alt="系统揽件日期" data-label="系统揽件日期" <#if returnMap??&&returnMap.rgTookTime == "1">checked='checked'</#if>></td>
    				<td><input type="checkbox" data-toggle="icheck" name="ids"  value="rgTookTime" alt="人工揽件日期" data-label="人工揽件日期" <#if returnMap??&&returnMap.xtTookTime == "1">checked='checked'</#if>></td>
                </tr>
                <tr>
                    <td><input type="checkbox" data-toggle="icheck" name="ids"  value="tookOut" alt="揽收是否超时" data-label="揽收是否超时"  <#if returnMap??&&returnMap.tookOut == "1">checked='checked'</#if>></td>
                    <td><input type="checkbox" data-toggle="icheck" name="ids"  value="tookOutReason" alt="揽收超时原因"  data-label="揽收超时原因"<#if returnMap??&&returnMap.tookOutReason == "1">checked='checked'</#if>></td>
    				<td><input type="checkbox" data-toggle="icheck" name="ids"  value="tookFlag" alt="人工导入揽收时间标记" data-label="人工导入揽收时间标记" <#if returnMap??&&returnMap.tookFlag == "1">checked='checked'</#if>></td>
    				<td><input type="checkbox" data-toggle="icheck" name="ids"  value="validityDay" alt="时限" data-label="时限" <#if returnMap??&&returnMap.validityDay == "1">checked='checked'</#if>></td>
                </tr>
                <tr>
                    <td><input type="checkbox" data-toggle="icheck" name="ids"  value="planTime" alt="计划到货日期" data-label="计划到货日期"  <#if returnMap??&&returnMap.planTime == "1">checked='checked'</#if>></td>
                    <td><input type="checkbox" data-toggle="icheck" name="ids"  value="pbLine" alt="判别列"  data-label="判别列"<#if returnMap??&&returnMap.pbLine == "1">checked='checked'</#if>></td>
    				<td><input type="checkbox" data-toggle="icheck" name="ids"  value="xtSignTime" alt="系统对接签收日期" data-label="系统对接签收日期" <#if returnMap??&&returnMap.xtSignTime == "1">checked='checked'</#if>></td>
    				<td><input type="checkbox" data-toggle="icheck" name="ids"  value="rgSignTime" alt="人工导入签收日期" data-label="人工导入签收日期" <#if returnMap??&&returnMap.rgSignTime == "1">checked='checked'</#if>></td>
                </tr>
                <tr>
                    <td><input type="checkbox" data-toggle="icheck" name="ids"  value="overDay" alt="超时天数" data-label="超时天数"  <#if returnMap??&&returnMap.overDay == "1">checked='checked'</#if>></td>
                    <td><input type="checkbox" data-toggle="icheck" name="ids"  value="attritDay" alt="在途天数"  data-label="在途天数"<#if returnMap??&&returnMap.attritDay == "1">checked='checked'</#if>></td>
    				<td><input type="checkbox" data-toggle="icheck" name="ids"  value="orderState" alt="配送状态" data-label="配送状态" <#if returnMap??&&returnMap.orderState == "1">checked='checked'</#if>></td>
    				<td><input type="checkbox" data-toggle="icheck" name="ids"  value="reason" alt="签收超时原因" data-label="签收超时原因" <#if returnMap??&&returnMap.reason == "1">checked='checked'</#if>></td>
                </tr>
    			<tr>
                    <td><input type="checkbox" data-toggle="icheck" name="ids"  value="signUser" alt="签收人" data-label="签收人"  <#if returnMap??&&returnMap.signUser == "1">checked='checked'</#if>></td>
                    <td><input type="checkbox" data-toggle="icheck" name="ids"  value="beiuzhuState" alt="流转状态"  data-label="流转状态"<#if returnMap??&&returnMap.beiuzhuState == "1">checked='checked'</#if>></td>
    				<td><input type="checkbox" data-toggle="icheck" name="ids"  value="yckdlzxxOne" alt="延迟第一天快递流转信息" data-label="延迟第一天快递流转信息" <#if returnMap??&&returnMap.yckdlzxxOne == "1">checked='checked'</#if>></td>
    				<td><input type="checkbox" data-toggle="icheck" name="ids"  value="yckdlzxxTwo" alt="延迟第二天快递流转信息" data-label="延迟第二天快递流转信息" <#if returnMap??&&returnMap.yckdlzxxTwo == "1">checked='checked'</#if>></td>
                </tr>
				<tr>
    				<td><input type="checkbox" data-toggle="icheck" name="ids"  value="yckdlzxxThree" alt="延迟第三天快递流转信息" data-label="延迟第三天快递流转信息" <#if returnMap??&&returnMap.yckdlzxxThree == "1">checked='checked'</#if>></td>
    				<td><input type="checkbox" data-toggle="icheck" name="ids"  value="kdlzxx" alt="当前快递流转信息" data-label="当前快递流转信息" <#if returnMap??&&returnMap.kdlzxx == "1">checked='checked'</#if>></td>
                    <td><input type="checkbox" data-toggle="icheck" name="ids"  value="rgxtFlag" alt="人工导入早于系统对接24h 标记" data-label="人工导入早于系统对接24h 标记"  <#if returnMap??&&returnMap.rgxtFlag == "1">checked='checked'</#if>></td>
                    <td><input type="checkbox" data-toggle="icheck" name="ids"  value="fhTookFlag" alt="20点后发货当天揽收 标记"  data-label="20点后发货当天揽收 标记"<#if returnMap??&&returnMap.fhTookFlag == "1">checked='checked'</#if>></td>
                </tr>
                <tr>
                	<td colspan="4"><input type="checkbox" data-toggle="icheck" name="ids"  value="payTime" alt="付款时间" data-label="付款时间" <#if returnMap??&&returnMap.payTime == "1">checked='checked'</#if>></td>
                </tr>
                </tbody>
            </table>
            <br/><br/>
                <div align="center">
                    <button type="button" class="btn-default" onclick="exportExcelCourier()">保存</button>
                    <button type="button" class="btn-close" style="margin-left: 20px;">关闭</button>
                </div>
        </fieldset>
    </div>
</div>