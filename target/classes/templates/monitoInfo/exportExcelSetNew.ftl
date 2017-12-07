<script type="text/javascript">	
	//导出
	function exportExcelNew(){
		var expNameList="";
		var expFieldName="";
		var noexpNameList="";
		var noexpFieldName="";
		var chk_list=$("#xTable input[name='ids']");
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
         $.ajax({
             type: "POST",
             url: "/monitoInfo/exportExcel",
             data: {belongTo:$("#belongToNew").val(), 
                    type:$("#type").val(),
                    expNameList:expNameList,
                    expFieldName:expFieldName,
                    noexpNameList:noexpNameList,
                    noexpFieldName:noexpFieldName
                    },
             dataType: "text",
             success: function(data){
             	window.location.href='/file/download?fileName='+data;
             	$(".btn-close").click();
           	 }
         });
	}
</script>
    <input tyep="hidden" name="belongTo" id="belongToNew" value='${belongTo}'>
    <input tyep="hidden" name="type" id="type" value='${type}'>
<div id="msgDiv"></div>
<div class="bjui-pageContent">
    <div style="margin:15px auto 0; width:750px;">
        <fieldset>
            <table class="table table-condensed table-hover" id="xTable">
                <tbody>
                <tr>
                	<td colspan='4'><input type="checkbox"  class="checkboxCtrl"  data-group="ids" data-toggle="icheck">全选</td>
                </tr>
                
                <#if seeExportFieldList??>
                		<tr>
                	<#list seeExportFieldList as seeExportField>
	                		<#if seeExportField_index%4==0></tr><tr></#if>
	                    	<td><input type="checkbox" data-toggle="icheck" name="ids" value="${seeExportField.expFieldName}" alt="${seeExportField.expName}" data-label="${seeExportField.expName}" 
	                    	<#if seeExportField.isSelect==1>checked="checked"</#if> ></td>
 					</#list>
                    	</tr>
                </#if>
                </tbody>
            </table>
        </fieldset>
    </div>
</div>
<div class="bjui-pageFooter">
    <ul>
       <li>
            <button type="button" class="btn-close">关闭</button>
        </li>
        <li>
            <button type="button" class="btn-default" onclick="exportExcelNew()">导出</button>
        </li>
    </ul>
</div>