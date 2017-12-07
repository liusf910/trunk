<script type="text/javascript">
    $('#jobType').change(function () {
        var jt = $(this).children('option:selected').val();
        if (jt == "took_sync") {
            $('#dateBlock').show();
        } else {
            $('#dateBlock').hide();
        }
    });
</script>
<div class="bjui-pageContent">
    <div style="margin:15px auto 0; width:600px;">
        <fieldset>
            <legend>Job任务手动触发</legend>
            <form action="${request.contextPath}/setting/trigger/tookRateJob" class="pageForm" method="post"
                  data-toggle="validate">
                <table class="table table-condensed table-hover">
                    <tbody>
                    <tr>
                        <td align="right">
                            <label class="label-control">任务类型：</label>
                        </td>
                        <td>
                            <select id="jobType" name="jobType" data-toggle="selectpicker" data-width="200">
                            <#if jobTypes??>
                                <#list jobTypes as jobType>
                                    <option value="${jobType}">${jobType.desc}</option>
                                </#list>
                            </#if>
                            </select>
                        </td>
                    </tr>
                    <tr id="dateBlock" style="display: none;">
                        <td align="right">
                            <label class="label-control">统计日期：</label>
                        </td>
                        <td>
                            <input type="text" name="date" data-toggle="datepicker" size="20">
                        </td>
                    </tr>
                    </tbody>
                </table>
                <br/><br/>
                <div align="center">
                    <button type="submit" class="btn-default">开始统计</button>
                    <button type="button" class="btn-close" style="margin-left: 20px;">关闭</button>
                </div>
            </form>
        </fieldset>
    </div>
</div>