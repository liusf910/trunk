<div class="bjui-pageContent">
    <div style="margin:15px 0 0 10px; width:800px;">
        <fieldset>
            <legend>基本信息</legend>
            <table class="table table-condensed table-hover">
                <tbody>
                <tr>
                    <td align="right"><label class="label-control" style="width: 190px;">速递单号：</label></td>
                    <td><label class="label-control" style="width: 100px;">${monito.logisticCode!""}</label></td>
                    <td align="right"><label class="label-control" style="width: 150px;">快递公司：</label></td>
                    <td><label class="label-control" style="width: 100px;">${monito.courierName!""}</label></td>
                    <td align="right"><label class="label-control" style="width: 150px;">网络单号：</label></td>
                    <td><label class="label-control" style="width: 100px;">${monito.orderCode!""}</label></td>
                    <td align="right"><label class="label-control" style="width: 130px;">外部单号：</label></td>
                    <td><label class="label-control" style="width: 140px;">${monito.extOrderCode!""}</label></td>
                </tr>
                <tr>
                    <td align="right"><label class="label-control">发货日期：</label></td>
                    <td><label class="label-control"><#if monito.shipmentsTime??>${monito.shipmentsTime?string('yyyy/MM/dd')}</#if></label></td>
                    <td align="right"><label class="label-control">发货时间：</label></td>
                    <td ><label class="label-control"><#if monito.shipmentsTime??>${monito.shipmentsTime?string('yyyy/MM/dd HH:mm:ss')}</#if></label></td>
                    <td align="right"><label class="label-control">联系人：</label></td>
                    <td><label class="label-control">${monito.linkman!""}</label></td>
                    <td align="right"><label class="label-control">电话：</label></td>
                    <td><label class="label-control">${monito.phone!""}</label></td>
                </tr>
                <tr>
                    <td align="right"><label class="label-control">城市分级：</label></td>
                    <td><label class="label-control">${monito.cityScale!""}</label></td>
                    <td align="right"><label class="label-control">省份：</label></td>
                    <td><label class="label-control">${monito.province!""}</label></td>
                    <td align="right"><label class="label-control">发货仓库代码：</label></td>
                    <td><label class="label-control">${monito.storageCode!""}</label></td>
                    <td align="right"><label class="label-control">发货仓库：</label></td>
                    <td><label class="label-control">${monito.storage!""}</label></td>
                </tr>
                <tr>
                    <td align="right"><label class="label-control">市：</label></td>
                    <td colspan="3"><label class="label-control">${monito.city!""}</label></td>
                    <td align="right"><label class="label-control">数量：</label></td>
                    <td colspan="3"><label class="label-control">${monito.number!""}</label></td>
                </tr>
                <tr>
                    <td align="right"><label class="label-control">地址：</label></td>
                    <td colspan="3"><label class="label-control">${monito.addr!""}</label></td>
                    <td align="right"><label class="label-control">重量：</label></td>
                    <td colspan="3"><label class="label-control">${monito.weight!""}</label></td>
                </tr>
                </tbody>
            </table>
        </fieldset>
        <fieldset>
        <legend>揽收信息</legend>
            <table class="table table-condensed table-hover">
                <tbody>
                <tr>
                    <td align="right"><label class="label-control" style="width: 190px;">系统揽件日期：</label></td>
                    <td><label class="label-control" style="width: 100px;"><#if monito.xtTookTime??>${monito.xtTookTime?string('yyyy/MM/dd HH:mm:ss')}</#if></label></td>
                    <td align="right"><label class="label-control" style="width: 150px;">人工揽收日期：</label></td>
                    <td><label class="label-control" style="width: 100px;"><#if monito.rgTookTime??>${monito.rgTookTime?string('yyyy/MM/dd HH:mm:ss')}</#if></label></td>
                    <td align="right"><label class="label-control" style="width: 150px;">揽收是否超时：</label></td>
                    <td><label class="label-control" style="width: 100px;">${monito.tookOut!""}</label></td>  
                    <td align="right"><label class="label-control" style="width: 150px;">揽收超时原因：</label></td>
                    <td><label class="label-control" style="width: 120px;">${monito.tookOutReason!""}</label></td>
                </tr>
                </tbody>
            </table>
          </fieldset>
          <fieldset>
            <legend>签收信息</legend>
            <table class="table table-condensed table-hover">
                <tbody>
                <tr>
                    <td align="right"><label class="label-control" style="width: 190px;">时限：</label></td>
                    <td><label class="label-control" style="width: 100px;"> <#if monito.validityDay??>
                    	${monito.validityDay}天
                        </#if></label>
                    </td>
                    <td align="right"><label class="label-control" style="width: 150px;">计划到货日期：</label></td>
                    <td><label class="label-control" style="width: 100px;"><#if monito.planTime??>${monito.planTime?string('yyyy/MM/dd')}</#if></label></td>
                    <td align="right"><label class="label-control" style="width: 150px;">超时天数：</label></td>
                     <#if monito.overDay??>
                        <#if monito.overDay = 0>
                            <td><label class="label-control" style="width: 110px;">准时</label></td>
                        <#elseif monito.overDay gt 0>
                            <td><label class="label-control" style="width: 110px;">超时${monito.overDay}天</label></td>
                        <#elseif monito.overDay lt 0>
                            <td><label class="label-control" style="width: 110px;">提前${monito.overDay?abs}天</label></td>
                        </#if>
                    <#else >
                        <td><label class="label-control" style="width: 110px;"></label></td>
                    </#if>
                    <td align="right"><label class="label-control" style="width: 150px;">在途天数：</label></td>
                    <td><label class="label-control" style="width: 110px;"><#if  monito.attritDay??>
                        ${monito.attritDay}天
                        </#if></label>
                     </td>
                </tr>
                <tr>
                    <td align="right"><label class="label-control">判别列：</label></td>
                    <td><label class="label-control">${monito.pbLine!""}</label></td>
                    <td align="right"><label class="label-control">签收人：</label></td>
                    <td><label class="label-control">${monito.signUser!""}</label></td>
                    <td align="right"><label class="label-control">系统对接签收时间：</label></td>
                    <td><label class="label-control"><#if monito.xtSignTime??>${monito.xtSignTime?string('yyyy/MM/dd HH:mm:ss')}</#if></label></label></td>
                    <td align="right"><label class="label-control">人工导入签收时间：</label></td>
                    <td><label class="label-control"><#if monito.rgSignTime??>${monito.rgSignTime?string('yyyy/MM/dd HH:mm:ss')}</#if></label></td>
                </tr>
                <tr>
                    <td align="right"><label class="label-control">配送状态：</label></td>
                    <td><label class="label-control">${monito.orderState!""}</label></td>
                    <td align="right"><label class="label-control">签收超时原因：</label></td>
                    <td><label class="label-control">${monito.reason!""}</label></td>
                    <td align="right"><label class="label-control">流转状态：</label></td>
                    <td colspan="3"><label class="label-control">${monito.beiuzhuState!""}</label></td>
                </tr>
                <tr>
                	<td align="right"><label class="label-control">当前快递流转信息：</label></td>
                    <td colspan="3"><label class="label-control"><#if monito.kdlzxx??>${monito.kdlzxx!""}</#if></label></td>
                    <td align="right"><label class="label-control">延迟第一天快递流转信息：</label></td>
                    <td colspan="3"><label class="label-control"><#if monito.overDay?? && monito.overDay=='1' && monito.kdlzxx??>${monito.kdlzxx!""}</#if></label></td>
                </tr>
                <tr>
                    <td align="right"><label class="label-control">延迟第二天快递流转信息：</label></td>
                    <td colspan="3"><label class="label-control"><#if monito.overDay?? && monito.overDay=='2' && monito.kdlzxx??>${monito.kdlzxx!""}</#if></label></td>
                    <td align="right"><label class="label-control">延迟第三天快递流转信息：</label></td>
                    <td colspan="3"><label class="label-control"><#if monito.overDay?? && monito.overDay=='3' && monito.kdlzxx??>${monito.kdlzxx!""}</#if></label></td>
                </tr>
                <tr>
                    <td align="right"><label class="label-control">人工导入早于系统对接24h标记：</label></td>
                    <td colspan="3"><label class="label-control">${monito.rgxtFlag!""}</label></td>
                    <td align="right"><label class="label-control">20点后发货当天揽收标记：</label></td>
                    <td colspan="3"><label class="label-control">${monito.fhTookFlag!""}</label></td>
                </tr>
                </tbody>
            </table>
          </fieldset>
    </div>
</div>
<div class="bjui-pageFooter">
    <ul>
        <li>
            <button type="button" class="btn-close" data-icon="close">关闭</button>
        </li>
    </ul>
</div>