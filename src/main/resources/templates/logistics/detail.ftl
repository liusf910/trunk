<div class="bjui-pageContent">
    <div style="margin:15px auto 0; width:800px;">
        <fieldset>
            <legend>列表详细信息</legend>
            <table class="table table-condensed table-hover">
                <tbody>
                <tr>
                    <td align="right"><label class="label-control">80或180单号：</label></td>
                    <td><label class="label-control">${logisticsAppointment.oddNum}</label></td>
                    <td align="right"><label class="label-control">品项：</label></td>
                    <td><label class="label-control">${logisticsAppointment.item}</label></td>
                </tr>
                <tr>
                    <td align="right"><label class="label-control">数量：</label></td>
                    <td><label class="label-control">${logisticsAppointment.number}</label></td>
                    <td align="right"><label class="label-control">件数：</label></td>
                    <td><label class="label-control">${logisticsAppointment.numberPackage}</label></td>
                </tr>
                <tr>
                    <td align="right"><label class="label-control">预约送达日期：</label></td>
                    <td><label class="label-control">${logisticsAppointment.dateAppoint?string("yyyy-MM-dd HH:mm:ss")}</label></td>
                    <td align="right"><label class="label-control">发货方：</label></td>
                    <td><label class="label-control">${logisticsAppointment.shipper}</label></td>
                </tr>
                <tr>
                    <td align="right"><label class="label-control">发货城市：</label></td>
                    <td>
                        <label class="label-control">${logisticsAppointment.shipperCity}</label>
                    </td>
                    <td align="right"><label class="label-control">承运商：</label></td>
                    <td>
                        <label class="label-control">${logisticsAppointment.carrier}</label>
                    </td>
                </tr>
                <tr>
                    <td align="right"><label class="label-control">预约人：</label></td>
                    <td>
                        <label class="label-control">${logisticsAppointment.reservatePerson}</label>
                    </td>
                    <td align="right"><label class="label-control">预约人所属公司：</label></td>
                    <td>
                        <label class="label-control">${logisticsAppointment.appointCompany}</label>
                    </td>
                </tr>
                <tr>
                    <td align="right"><label class="label-control">预约人联系方式：</label></td>
                    <td>
                        <label class="label-control">${logisticsAppointment.phone}</label>
                    </td>
                    <td align="right"><label class="label-control">仓库：</label></td>
                    <td>
                        <label class="label-control">${logisticsAppointment.warehouse}</label>
                    </td>
                </tr>
                <tr>
                    <td align="right"><label class="label-control">备注：</label></td>
                    <td>
                        <label class="label-control">${logisticsAppointment.remark}</label>
                    </td>
                    <td align="right"><label class="label-control">lnec入库预约号：</label></td>
                    <td>
                        <label class="label-control">${logisticsAppointment.lnecStorageReservatnum}</label>
                    </td>
                </tr>
                 <tr>
                    <td align="right"><label class="label-control">lnec受理送达日期：</label></td>
                    <td>
                        <label class="label-control"><#if logisticsAppointment.lnecAcceptanceDate??>${logisticsAppointment.lnecAcceptanceDate?string("yyyy-MM-dd HH:mm:ss")}</#if></label>
                    </td>
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