<#escape x as x?html>
<script type="text/javascript">
	  function dialog_editCourierSign() {
	    $(".bjui-pageContent").dialog({
	        type:'POST',
	        id:'addCourier_dialog',
	        url:'/setting/storage/getStorageCouriersByUserIdSign',
	        data:{"shipperCode":$("#shipperCodeSign").val()},
	        mask:true,
	        title:'选择仓库快递',
	        height:400,
	        width:800,
	        fresh:true
	    })
	} 
</script>
<div class="bjui-pageHeader" xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
    <form id="pagerForm" data-toggle="ajaxsearch" action="${request.contextPath}/monitoInfo/yuantong/abnormalSign"
          method="post">
        <input type="hidden" name="pageSize" value="${model.pageSize}">
        <input type="hidden" name="pageCurrent" value="${model.pageCurrent}">
        <input type="hidden" name="orderField" value="${param.orderField}">
        <input type="hidden" name="orderDirection" value="${param.orderDirection}">
        <div class="bjui-searchBar">
            <label>快递单号：</label><input type="text" name="logisticCode" class="form-control"
                                       value="${infoWhere.logisticCode}" size="18">&nbsp;
            <label>签收超时原因:</label>
            <select name="signReason" data-toggle="selectpicker">
                <option value="">全部</option>
                <#if signReasons??>
                    <#list signReasons as signReason>
                        <option value="${signReason}"
                                <#if infoWhere.signReason==signReason >selected="selected"</#if>>${signReason.desc}</option>
                    </#list>
                </#if>
            </select>&nbsp;
             <input type="checkbox"  <#if infoWhere.signReasonNull=true >checked="checked"</#if>id="j_table_chk_2" name="signReasonNull"
                   value="true" data-toggle="icheck" data-label="签收超时原因为空">&nbsp;
            <button type="button" class="showMoreSearch" data-toggle="moresearch" data-name="custom2"><i
                    class="fa fa-angle-double-down"></i></button>
            <button type="submit" class="btn-default" data-icon="search">查询</button>
            &nbsp;
            <a class="btn btn-orange" href="javascript:;" onclick="$(this).navtab('reloadForm', true);"
               data-icon="undo">清空查询</a>&nbsp;
            <@shiro.hasPermission name="/monitoInfo/exportExcelSet">
           		<button  data-url="${request.contextPath}/monitoInfo/exportExcelSet?type=yuantongSignInfoWhere&&belongTo=yt-3-5"
					 class="btn-green" data-icon="save" data-title="导出字段选择"
                    data-toggle="dialog"
                    data-width="800" data-height="470" data-id="dialog">导出数据
            	</button>
            </@shiro.hasPermission>
        </div>
        <div class="bjui-moreSearch">
            <label class="control-label x90">所属仓库快递：</label>
            <textarea name="courierName" id="courierNameSign" cols="75" rows="4" readonly="true">${infoWhere.courierName}</textarea>
            <input type="hidden" name="shipperCode" id="shipperCodeSign" value="${infoWhere.shipperCode}">
            <a class="btn btn-green"    onclick="dialog_editCourierSign()"  >选择仓库快递</a> 
        	<br>
            <br>
            <label>流转状态:</label>
            <select name="state" data-toggle="selectpicker">
                <option value="">全部</option>
                <#if states??>
                    <#list states as state>
                        <option value="${state}"
                                <#if infoWhere.state==state >selected="selected"</#if>>${state.describe}</option>
                    </#list>
                </#if>
            </select>&nbsp;
            <label>配送状态:</label>
            <select name="orderState" data-toggle="selectpicker">
                <option value="">全部</option>
                <#if orderStatus??>
                    <#list orderStatus as orderState>
                        <option value="${orderState}"
                                <#if infoWhere.orderState==orderState >selected="selected"</#if>>${orderState.desc}</option>
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
            <br>
            <br>
            <label class="label-control">计划签收日期：</label>&nbsp;&nbsp;
            <input type="text" name="planTimeStart" readonly="true"
                   <#if infoWhere.planTimeStart??>value="${infoWhere.planTimeStart?date}"</#if>
                   data-toggle="datepicker" size="12">&nbsp;&nbsp;&nbsp;至&nbsp;&nbsp;&nbsp;
            <input type="text" name="planTimeEnd" readonly="true"
                   <#if infoWhere.planTimeEnd??>value="${infoWhere.planTimeEnd?date}"</#if>
                   data-toggle="datepicker" size="12">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <label class="label-control">签收时间：</label>&nbsp;&nbsp;
            <input type="text" name="signTimeStart" data-toggle="datepicker" readonly="true"
                   <#if infoWhere.signTimeStart??>value="${infoWhere.signTimeStart?string("yyyy-MM-dd HH:mm:ss")}"</#if>
                   data-pattern="yyyy-MM-dd HH:mm:ss">&nbsp;&nbsp;&nbsp;至&nbsp;&nbsp;&nbsp;
            <input type="text" name="signTimeEnd" readonly="true"
                   <#if infoWhere.signTimeEnd??>value="${infoWhere.signTimeEnd?string("yyyy-MM-dd HH:mm:ss")}"</#if>
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
            <th align="center" data-order-field="planTime" title="计划送达日期">计划签收日期</th>
            <th align="center" data-order-field="state" title="流转状态">流转状态</th>
            <th align="center" data-order-field="signTime" title="签收时间">签收时间</th>
            <th align="center" data-order-field="signTime" title="退回签收时间">退回签收时间</th>
            <th align="center" data-order-field="attritDay" title="在途天数">在途天数</th>
            <th align="center" data-order-field="overDay" title="超时天数">超时天数</th>
            <th align="center" title="签收超时原因">签收超时原因</th>
        </tr>
        </thead>
        <tbody>
            <#if pageInfo?? &&pageInfo.list??&&pageInfo.list?size gt 0 >
                <#list pageInfo.list as Info>
                <tr data-id="${Info.infoId}">
                    <td align="center" width="15%">
                        <div class="mall-order-list" onmouseover="showme(this);" onmouseout="hideme(this);">
                            <a href="javascript:;" class="status express-title">
                            ${Info.logisticCode}
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
                    <td align="center"<#if Info.shipmentsTime??>
                        title="${Info.shipmentsTime?string("yyyy-MM-dd HH:mm:ss")}">${Info.shipmentsTime?string("yyyy-MM-dd")}</#if></td>
                   <td align="center" title="${Info.city}">${Info.province}</td>
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
                    <td align="center" title="蓝色标识为判别列">
                        <#if Info.signTime??>
                            <#if Info.distColumn?? && Info.distColumn =="signTime" >
                                <span style="color: #1030ff">系统:</span>${Info.signTime!?string("yyyy-MM-dd HH:mm:ss")}
                            <#else >
                                系统:${Info.signTime!?string("yyyy-MM-dd HH:mm:ss")}
                            </#if>
                        </#if>
                        <br>
                        <#if Info.fileSignTime?? && Info.orderStatus!='退回签收'>
                            <#if Info.distColumn?? && Info.distColumn =="fileSignTime" >
                                <span style="color: #1030ff">人工:</span>${Info.fileSignTime!?string("yyyy-MM-dd HH:mm:ss")}
                            <#else>
                                                                                     人工:${Info.fileSignTime!?string("yyyy-MM-dd HH:mm:ss")}
                            </#if>
                        </#if>
                    </td>
                    <td><#if Info.orderStatus=='退回签收'>退回:${Info.fileSignTime!?string("yyyy-MM-dd HH:mm:ss")}</#if></td>
                    <td align="center">
                        <#if  Info.attritDay??>
                        ${Info.attritDay}天
                        </#if>
                    </td>
                    <#if Info.overDay??>
                        <#if Info.overDay = 0>
                            <td align="center" style="color: #06A601">准时</td>
                        <#elseif Info.overDay gt 0>
                            <td align="center" class="btn-red">超时${Info.overDay}天</td>
                        <#elseif Info.overDay lt 0>
                            <td align="center" class="btn-blue">提前${Info.overDay?abs}天</td>
                        <#else>
                            空
                        </#if>
                    <#else >
                        <td align="center"></td>
                    </#if>
                    <td align="center" width="100">
                    <div class="mall-order-list" onmouseover="showme(this);" onmouseout="hideme(this);">
                            <a href="javascript:;" class="status express-title">
                       			 <#if  Info.signReason??>
                       			 ${Info.signReason}
                        		 </#if>
                                <div class="express-info2 active">
                                    <p class="express-number">快递单号：${Info.logisticCode}</p>
                                    <div class="j_express express-details">
                                        <#if Info.signReasonList?? && Info.signReasonList?size gt 0>
                                            <#list Info.signReasonList as signReasonL>
                                                <div class="status active">
                                                    <i class="express-none-icon icons"></i>
                                                    <div class="express-details-bd">
                                                        <p>${signReasonL.fileSignTime}</p>    
                                                        <p>${signReasonL.signReason}</p> 
                                                        <p>${signReasonL.orderStatus}</p>                                                  
                                                    </div>
                                                    <i class="line"></i>
                                                </div>
                                            </#list>
                                        <#else>
                                            <p>没有超时原因</p>
                                        </#if>
                                    </div>
                                </div>
                            </a>
                        </div>
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