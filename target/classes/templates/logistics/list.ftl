<div class="bjui-pageHeader">
    <form id="pagerForm" data-toggle="ajaxsearch" class="pageForm"
          action="${request.contextPath}/logistics/wl/list"
          data-toggle="validate" method="post">
        <fieldset>
            <legend>查询条件</legend>
            <table class="table table-condensed table-hover">
                <tbody>
                <tr>
                    <td align="center" width="15%"><label class="label-control">单号：</label></td>
                    <td width="35%">
                            <input type="text" name="oddNum" value="${logisticsAppointment.oddNum!""}" size="20"/>
                    </td>
                    <td align="center" width="15%"><label class="label-control">承运商：</label></td>
                    <td width="35%">
		                 <select name="carrier" data-toggle="selectpicker" data-width="200">
		                            <option value="">------------请选择----------</option>
		                            <#if carriers??>
		                                <#list  carriers as carriers>
		                                    <option value="${carriers.carrierName}" <#if logisticsAppointment.carrier??><#if logisticsAppointment.carrier == carriers.carrierName>selected</#if></#if>>${carriers.carrierName}</option>
		                                </#list>
		                            </#if>
		                 </select>
                 </td>
                 </tr>
                 <tr>
                    <td align="center" width="15%"><label class="label-control">预约送达日期：</label></td>
                    <td width="35%">
                        <input type="text" name="startDate" value="${logisticsAppointment.startDate!""}" data-toggle="datepicker"
                               size="15">
                        &nbsp;至&nbsp;
                        <input type="text" name="endDate" value="${logisticsAppointment.endDate!""}" data-toggle="datepicker"
                               size="15">
                    </td>
                     <td align="center" width="15%"><label class="label-control">仓库 ：</label></td>
                    <td width="35%">
                       <select name="warehouse" data-toggle="selectpicker" data-width="200">
                            <option value="">------------请选择----------</option>
                            <option value="经销仓" <#if logisticsAppointment.warehouse??><#if logisticsAppointment.warehouse == "经销仓">selected</#if></#if>>
                                经销仓
                            </option>
                            <option value="直营仓" <#if logisticsAppointment.warehouse??><#if logisticsAppointment.warehouse == "直营仓">selected</#if></#if>>
                                直营仓
                            </option>
                        </select>      
                    </td>
                </tr>
                <tr>
                    <td align="center" width="15%"><label class="label-control">受理日期：</label></td>
                    <td width="35%">
                        <input type="text" name="lnceStartDate" value="${logisticsAppointment.lnceStartDate!""}" data-toggle="datepicker"
                               size="15">
                        &nbsp;至&nbsp;
                        <input type="text" name="lnceEndDate" value="${logisticsAppointment.lnceEndDate!""}" data-toggle="datepicker"
                               size="15">
                    </td>
                     <td align="center" width="15%"><label class="label-control">品项 ：</label></td>
                    <td width="35%">
                            <input type="text" name="item" value="${logisticsAppointment.item!""}" size="20"/>
                    </td>
                </tr>
                </tbody>
            </table>
            <br/>
            <div align="center">
                <button type="submit" class="btn-default" data-icon="search">查询</button>
                &nbsp;
                <a class="btn btn-orange" href="javascript:;" data-toggle="reloadsearch" data- clear-query="true"
                   data-icon="undo">清空查询</a>
                <@shiro.hasPermission name="/logistics/exportExcelSet">
                <button  data-url="${request.contextPath}/logistics/exportExcelSet?type=logisticsAppointment&&belongTo=wl-4-1"
					 class="btn-green" data-icon="save" data-title="导出字段选择"
                    data-toggle="dialog"
                    data-width="800" data-height="470" data-id="dialog">导出数据
               </button>
                </@shiro.hasPermission>
                <@shiro.hasPermission name="/loginstics/updateView" >
                <a id="updDateBtn" class="btn btn-red" onClick="updateDate();">批量修改日期
                </a>
                </@shiro.hasPermission>
            </div>
	        </fieldset>
    </form>
</div>
<div class="bjui-pageContent">
    <fieldset>
        <legend>查询结果</legend>
        <table class="table table-bordered table-hover table-striped table-top">
            <thead>
            <tr>
                <th width="5%"><input type="checkbox" class="checkboxCtrl" data-group="ids" data-toggle="icheck"></th>
                <th width="10%">80或180单号</th>
                <th width="15%">预约送达日期</th>
                <th width="5%">发货方</th>
                <th width="5%">承运商</th>
                <th width="10%">预约人所属公司</th>
                <th width="10%">预约人联系方式</th>
                <th width="5%">仓库</th>
                <th width="15%">lnec入库预约号</th>
                <th width="15%">lnec受理送达日期</th>
            </tr>
            </thead>
            <tbody>
            <#if lists??>
                <#list lists as logisticsAppointment>
                <tr data-id="1">
                    <td><input type="checkbox" name="ids" data-toggle="icheck" value="${logisticsAppointment.appointmentId}"/></td>
                    <td><a href="${request.contextPath}/logistics/${logisticsAppointment.oddNum}/show_detail"
                           class="btn-red"          
                           data-toggle="navtab" data-id="courNavtab"
                           data-reload-warn="本页已有打开的内容，确定将刷新本页内容，是否继续？" data-title="物流预约详情">${logisticsAppointment.oddNum}</a>
                    </td>
                    <td>${logisticsAppointment.dateAppoint?string("yyyy-MM-dd HH:mm:ss")}</td>
                    <td>${logisticsAppointment.shipper}</td>
                    <td>${logisticsAppointment.carrier}</td>
                    <td>${logisticsAppointment.appointCompany}</td>
                    <td>${logisticsAppointment.phone}</td>
                    <td>${logisticsAppointment.warehouse}</td>
                    <td>${logisticsAppointment.lnecStorageReservatnum}</td>
                    <td><#if logisticsAppointment.lnecAcceptanceDate??>${logisticsAppointment.lnecAcceptanceDate?string("yyyy-MM-dd HH:mm:ss")}</#if></td>
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
            <@shiro.hasPermission name="/logistics/logistics_upload">
            <button href="${request.contextPath}/logistics/logistics_upload" type="submit" class="btn-red" data-icon="save"
                    data-toggle="dialog"
                    data-width="500" data-height="300" data-id="dialog">物流预约导入
            </button>
            </@shiro.hasPermission>
        </li>
    </ul>
    <div class="pages">
        <span>每页&nbsp;</span>
        <div class="selectPagesize">
            <select data-toggle="selectpicker" data-toggle-change="changepagesize">
                <option value="10">10</option>
                <option value="20">20</option>
                <option value="30">30</option>
                <option value="40">40</option>
            </select>
        </div>
        <span>&nbsp;条，共 ${total?string('#')} 条</span>
    </div>
    <div class="pagination-box" data-toggle="pagination" data-total="${total?string('#')}"
         data-page-size="${logisticsAppointment.pageSize}" data-page-current="${logisticsAppointment.pageCurrent}">
    </div>
</div>
<script type="text/javaScript">
function updateDate(){
	var ids = $("input:checkbox[name='ids']:checked").val();
	if(ids==null){
		$("#updDateBtn").alertmsg("warn","请选择要修改项");
		return;
	}
	$("#updDateBtn").dialog({
		id:"updateView",
		title : "批量修改受理时间",
		url:"${request.contextPath}/logistics/updateView"
	});
}
</script>