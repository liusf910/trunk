<#escape x as x?html>
<script type="text/javascript">
	  function dialog_editCourierTook() {
	    $(".bjui-pageContent").dialog({
	        type:'POST',
	        id:'addCourier_dialog',
	        url:'/setting/storage/getStorageCouriersByUserIdTook',
	        data:{"shipperCode":$("#shipperCodeTook").val()},
	        mask:true,
	        title:'选择仓库快递',
	        height:400,
	        width:800,
	        fresh:true
	    })
	} 
</script>
<div class="bjui-pageHeader" xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
    <form id="pagerForm" data-toggle="ajaxsearch" action="${request.contextPath}/monitoInfo/yuantong/abnormalTaking"
          method="post">
        <input type="hidden" name="pageSize" value="${model.pageSize}">
        <input type="hidden" name="pageCurrent" value="${model.pageCurrent}">
        <input type="hidden" name="orderField" value="${param.orderField}">
        <input type="hidden" name="orderDirection" value="${param.orderDirection}">
        <div class="bjui-searchBar">
            <label>快递单号：</label><input type="text" name="logisticCode" class="form-control"
                                       value="${infoWhere.logisticCode}" size="18">&nbsp;
            <label>揽收超时原因:</label>
            <select name="tookReason" data-toggle="selectpicker">
                <option value="">全部</option>
                <#if tookReasons??>
                    <#list tookReasons as tookReason>
                        <option value="${tookReason}"
                                <#if infoWhere.tookReason==tookReason >selected="selected"</#if>>${tookReason.desc}</option>
                    </#list>
                </#if>
            </select>&nbsp;
            <input type="checkbox"  <#if infoWhere.signReasonNull=true >checked="checked"</#if>id="j_table_chk_2" name="signReasonNull"
                   value="true" data-toggle="icheck" data-label="揽收超时原因为空">&nbsp;
            <button type="button" class="showMoreSearch" data-toggle="moresearch" data-name="custom2"><i
                    class="fa fa-angle-double-down"></i></button>
            <button type="submit" class="btn-default" data-icon="search">查询</button>
            &nbsp;
            <a class="btn btn-orange" href="javascript:;" onclick="$(this).navtab('reloadForm', true);"
               data-icon="undo">清空查询</a>&nbsp;
            <@shiro.hasPermission name="/monitoInfo/exportExcelSet">
            	<button  data-url="${request.contextPath}/monitoInfo/exportExcelSet?type=yuantongTookInfoWhere&&belongTo=yt-3-3"
					 class="btn-green" data-icon="save" data-title="导出字段选择"
                    data-toggle="dialog"
                    data-width="800" data-height="470" data-id="dialog">导出数据
            	</button> 
            </@shiro.hasPermission>
        </div>
        <div class="bjui-moreSearch">
            <label class="control-label x90">所属仓库快递：</label>
            <textarea name="courierName" id="courierNameTook" cols="75" rows="4" readonly="true">${infoWhere.courierName}</textarea>
            <input type="hidden" name="shipperCode" id="shipperCodeTook" value="${infoWhere.shipperCode}">
            <a class="btn btn-green"    onclick="dialog_editCourierTook()"  >选择仓库快递</a> 
        	<br><br>
            <label class="label-control">发货日期：</label>&nbsp;&nbsp;
            <input type="text" name="shipmentsTimeStart" readonly="true"
                   <#if infoWhere.shipmentsTimeStart??>value="${infoWhere.shipmentsTimeStart?date}"</#if>
                   data-toggle="datepicker" size="12">&nbsp;&nbsp;&nbsp;至&nbsp;&nbsp;&nbsp;
            <input type="text" name="shipmentsTimeEnd" readonly="true"
                   <#if infoWhere.shipmentsTimeEnd??>value="${infoWhere.shipmentsTimeEnd?date}"</#if>
                   data-toggle="datepicker" size="12">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <label class="label-control">揽收时间：</label>&nbsp;&nbsp;
            <input type="text" name="tookTimeStart" data-toggle="datepicker" readonly="true"
                   <#if infoWhere.tookTimeStart??>value="${infoWhere.tookTimeStart?string("yyyy-MM-dd HH:mm:ss")}"</#if>
                   data-pattern="yyyy-MM-dd HH:mm:ss">&nbsp;&nbsp;&nbsp;至&nbsp;&nbsp;&nbsp;
            <input type="text" name="tookTimeEnd" readonly="true"
                   <#if infoWhere.tookTimeEnd??>value="${infoWhere.tookTimeEnd?string("yyyy-MM-dd HH:mm:ss")}"</#if>
                   data-toggle="datepicker" data-pattern="yyyy-MM-dd HH:mm:ss">


        </div>
    </form>
</div>
<div class="bjui-pageContent">
    <table data-toggle="tablefixed" data-width="100%" data-nowrap="true">
        <thead>
        <tr>
        	<th align="center" data-order-field="logisticCode" title="快递单号">快递单号</th>
            <th align="center" data-order-field="storage" title="发货仓库">发货仓库</th>
            <th align="center" data-order-field="shipperCode" title="快递公司">快递公司</th>
            <th align="center" data-order-field="shipmentsTime" title="发货日期">发货日期</th>
            <th align="center" data-order-field="province" title="目的地省">目的地省</th>
            <th align="center" data-order-field="validityDay" title="时限">时限</th>
            <th align="center" data-order-field="planTime" title="计划送达日期">计划签收日期</th>
            <th align="center" data-order-field="state" title="流转状态">流转状态</th>
            <th align="center" data-order-field="tookTime" title="揽收时间(系统)">揽收时间(系统)</th>
            <th align="center" data-order-field="fileTookTime" title="揽收时间(文件)">揽收时间(人工)</th>
            <th align="center" title="揽收超时原因">揽收超时原因</th>
        </tr>
        </thead>
        <tbody>
            <#if pageInfo?? &&pageInfo.list??&&pageInfo.list?size gt 0 >
                <#list pageInfo.list as Info>
                <tr data-id="${Info.infoId}">
                    <td align="center">${Info.logisticCode}</td>
                    <td align="center" title="${Info.storageCode}">${Info.storage}</td>
                    <td align="center">${Info.courierName}</td>
                    <td align="center"<#if Info.shipmentsTime??>
                        title="${Info.shipmentsTime?string("yyyy-MM-dd HH:mm:ss")}">${Info.shipmentsTime?string("yyyy-MM-dd")}</#if></td>
                    <td align="center" title="${Info.city}">${Info.province}</td>
                    <td align="center">${Info.validityDay}天</td>
                    <#if  Info.planTime??>
                        <#if Info.systemTime?string("yyyy-MM-dd")==Info.planTime?string("yyyy-MM-dd") >
                            <td align="center" class="btn-red">${Info.planTime?string("yyyy-MM-dd")}</td>
                        <#else >
                            <td align="center">${Info.planTime?string("yyyy-MM-dd")}</td>
                        </#if>
                    <#else >
                        <td align="center"></td>
                    </#if>
                    <#if Info.state.describe=='已签收'>
                        <td align="center" style="color: #06A601">${Info.state.describe}</td>
                    <#elseif Info.state.describe=='待揽件' >
                        <td align="center" class="btn-red">${Info.state.describe}</td>
                    <#elseif Info.state.describe=='问题件' >
                        <td align="center" style="color: #FF5C02">${Info.state.describe}</td>
                    <#else >
                        <td align="center" title="${Info.state.describe}">
                            <#if Info.state.describe?length gt 5>${Info.state.describe?substring(0,5)}...
                            <#else >
                            ${Info.state.describe}
                            </#if>
                        </td>
                    </#if>
                    <td align="center"><#if Info.tookTime??>${Info.tookTime?string("yyyy-MM-dd HH:mm:ss")}</#if></td>
                    <td align="center"><#if Info.fileTookTime??>${Info.fileTookTime?string("yyyy-MM-dd HH:mm:ss")}</#if></td>
                    <td align="center">
                       	<#if  Info.tookReason??>
                       		${Info.tookReason}
                        </#if>
                    </td>
                </tr>
                </#list>
            </#if>
        </tbody>
    </table>
</div>
<div class="bjui-pageFooter">
    <div class="pages">
        <span>每页&nbsp;</span>
        <div class="selectPagesize">
            <select data-toggle="selectpicker" data-toggle-change="changepagesize">
                <option value="15">15</option>
                <option value="20">20</option>
                <option value="30">30</option>
                <option value="40">40</option>
            </select>
        </div>
        <span>&nbsp;条，共 ${pageInfo.total?string('#')} 条</span>
    </div>
    <div class="pagination-box" data-toggle="pagination" data-total="${pageInfo.total?string('#')}"
         data-page-size="${pageInfo.pageSize}" data-page-current="${pageInfo.pageNum}">
    </div>
</div>
</#escape>