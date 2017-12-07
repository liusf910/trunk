<#escape x as x?html>
 <script type="text/javascript">
      function getStorageCode(){
      	$('#storageCodeAllTook').val($('#storageCodeSelectAllTook').val());
      }
      
      function getShipperCode(){
      	$('#shipperCodeAllTook').val($('#shipperCodeSelectAllTook').val());
      }
    </script>
<div class="bjui-pageHeader" xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
    <form id="pagerForm" data-toggle="ajaxsearch" action="${request.contextPath}/monito/allTook/list"
          method="post">
        <input type="hidden" name="pageSize" value="${model.pageSize}">
        <input type="hidden" name="pageCurrent" value="${model.pageCurrent}">
        <input type="hidden" name="orderField" value="${param.orderField}">
        <input type="hidden" name="orderDirection" value="${param.orderDirection}">
        <div class="bjui-searchBar"> 
            <label>快递单号：</label>&nbsp;&nbsp;<input type="text" name="logisticCode" class="form-control"
                                       value="${infoWhere.logisticCode}" size="18">&nbsp;
            <label>揽收超时原因：</label>&nbsp;&nbsp;
            <select name="tookReason" data-toggle="selectpicker">
                <option value="">全部</option>
                <#if tookReasons??>
                    <#list tookReasons as tookReason>
                        <option value="${tookReason}"
                                <#if infoWhere.tookReason==tookReason >selected="selected"</#if>>${tookReason.desc}</option>
                    </#list>
                </#if>
            </select>&nbsp;
            <input type="checkbox"  <#if infoWhere.tookReasonNull=true >checked="checked"</#if>id="j_table_chk_2" name="tookReasonNull"
                   value="true" data-toggle="icheck" data-label="揽收超时原因为空">&nbsp;
            <button type="button" class="showMoreSearch" data-toggle="moresearch" data-name="custom2"><i
                    class="fa fa-angle-double-down"></i></button>
            <button type="submit" class="btn-default" data-icon="search">查询</button>
            &nbsp;
            <a class="btn btn-orange" href="javascript:;" onclick="$(this).navtab('reloadForm', true);"
               data-icon="undo">清空查询</a>
            &nbsp;
            <@shiro.hasPermission name="/admin/monito/exportExcelSet">
            <button  data-url="${request.contextPath}/monito/exportExcelSet?type=took&&belongTo=3-4"
					 class="btn-green" data-icon="save" data-title="导出字段选择"
                    data-toggle="dialog"
                    data-width="800" data-height="470" data-id="dialog">导出数据
            </button>
            </@shiro.hasPermission>
        </div>
        <div class="bjui-moreSearch">
        <label>发货仓库：</label>&nbsp;&nbsp;
            <select id="storageCodeSelectAllTook" data-toggle="selectpicker" data-width="200"  title="" multiple onchange="getStorageCode();">
                <#if storages??>
                    <#list storages as storage>
                        <option value="${storage.storageCode}"  
                        <#if infoWhere.storageCode?? && infoWhere.storageCode?index_of(storage.storageCode)!=-1 >
                                selected="selected"</#if>>${storage.storageName}</option>
                    </#list>
                </#if>
            </select>&nbsp;
            <input type="hidden" name="storageCode" id="storageCodeAllTook" value="${infoWhere.storageCode}">
            <label>快递公司：</label>&nbsp;&nbsp;
            <select id="shipperCodeSelectAllTook" data-toggle="selectpicker" data-width="200"  title="" multiple onchange="getShipperCode();">
                <#if couriers??>
                    <#list couriers as courier>
                        <option value="${courier.shipperCode}" 
                        <#if infoWhere.shipperCode?? && infoWhere.shipperCode?index_of(courier.shipperCode)!=-1 >
                                selected="selected"</#if>>${courier.courierName}</option>
                    </#list>
                </#if>
            </select>&nbsp;
            <input type="hidden" name="shipperCode" id="shipperCodeAllTook" value="${infoWhere.shipperCode}">
            
            <label>流转状态：</label>&nbsp;&nbsp;
            <select name="state" data-toggle="selectpicker">
                <option value="">全部</option>
                <#if states??>
                    <#list states as state>
                        <option value="${state}"
                                <#if infoWhere.state==state >selected="selected"</#if>>${state.describe}</option>
                    </#list>
                </#if>
            </select>&nbsp;
            <br>
            <br>
            <label class="label-control">发货日期：</label>&nbsp;&nbsp;
            <input type="text" name="shipmentsTimeStart" readonly="true"
                   <#if infoWhere.shipmentsTimeStart??>value="${infoWhere.shipmentsTimeStart?date}"</#if>
                   data-toggle="datepicker" size="12">&nbsp;&nbsp;&nbsp;至&nbsp;&nbsp;&nbsp;
            <input type="text" name="shipmentsTimeEnd" readonly="true"
                   <#if infoWhere.shipmentsTimeEnd??>value="${infoWhere.shipmentsTimeEnd?date}"</#if>
                   data-toggle="datepicker" size="12">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                   
            <label class="label-control">揽件时间：</label>&nbsp;&nbsp;
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
            <th align="center" data-order-field="tookTime" title="揽件时间">揽件时间</th>
            <th align="center"  title="揽件超时原因">揽件超时原因</th>
        </tr>
        </thead>
        <tbody>
            <#if pageInfo?? &&pageInfo.list??&&pageInfo.list?size gt 0 >
                <#list pageInfo.list as Info>
                <tr data-id="${Info.infoId}">
                    <td align="center">
                    <div class="mall-order-list" onmouseover="showme(this);" onmouseout="hideme(this);">
                            <a href="javascript:;" class="status express-title">
                                <a href="${request.contextPath}/monito/${Info.infoId}/monito_detail" class="btn-red"
                                   data-toggle="navtab" data-id="monitoNavtab"
                                   data-reload-warn="本页已有打开的内容，确定将刷新本页内容，是否继续？" data-title="快递监测详情">${Info.logisticCode}</a>
                                <div class="express-info active">
                                    <p class="express-number">快递单号：${Info.logisticCode}</p>
                                    <div class="j_express express-details">
                                        <#if Info.traces?? && Info.traces?size gt 0>
                                            <#list Info.traces?reverse as traces>
                                                <div class="status active">
                                                    <i class="express-none-icon icons"></i>
                                                    <div class="express-details-bd">
                                                        <p>${traces.acceptTime}</p>
                                                        <p>${traces.acceptStation}</p>
                                                        <p>${traces.action}</p>
                                                    </div>
                                                    <i class="line"></i>
                                                </div>
                                            </#list>
                                        <#else>
                                            <p>没有物流信息</p>
                                        </#if>
                                    </div>
                                </div>
                            </a>
                        </div>
                    </td>
                    <td align="center" title="${Info.storageCode}">${Info.storage}</td>
                    <td align="center">${Info.courierName}</td>
                    <td align="center" 
                        title="${Info.shipmentsTime?string("yyyy-MM-dd HH:mm:ss")}"><#if Info.shipmentsTime??>${Info.shipmentsTime?string("yyyy-MM-dd")}</#if></td>
                    <td align="center" title="${Info.city}">${Info.province}</td>
                     <td align="center">
                        <#if Info.validityDay??>
                    	${Info.validityDay}天
                        </#if>
                    </td>
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
                    <td align="center"><#if Info.tookTime??>系统:${Info.tookTime!?string("yyyy-MM-dd HH:mm:ss")}</#if><br>
                        <#if Info.fileTookTime??>人工:${Info.fileTookTime!?string("yyyy-MM-dd HH:mm:ss")}</#if></td>
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