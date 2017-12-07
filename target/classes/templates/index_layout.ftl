<div class="bjui-pageContent">
    <div style="margin:15px auto 0; width:96%;">
        <div class="row" style="padding: 0 8px;">
            <div class="col-md-12">
                <div class="panel panel-default">
                    <div class="panel-heading"><h3 class="panel-title"><i class="fa fa-bar-chart-o fa-fw"></i>柱状/拆线图
                    </h3></div>
                    <div class="panel-body">
                    <#-- <div style="mini-width:400px;height:350px" data-toggle="echarts" data-type="bar,line"
                          data-url="echarts-barData.html"></div>-->
                        <div style="margin: 0 0 20px 0;">
                            <table class="table table-condensed table-hover">
                                <tbody>
                                <tr>
                                    <td align="center" width="10%"><label class="label-control">选择仓库：</label></td>
                                    <td width="20%">
                                        <select id="took_storage" style="width: 150px;" multiple="multiple">
                                        </select>
                                    </td>
                                    <td align="center" width="10%"><label class="label-control">发货日期：</label></td>
                                    <td width="40%">
                                        <input type="text" id="took_startDate" data-toggle="datepicker"
                                               size="18" value="">&nbsp;至&nbsp;
                                        <input type="text" id="took_endDate" data-toggle="datepicker"
                                               size="18" value="${.now?string("yyyy-MM-dd")}">
                                        <button type="button" id="took_search" class="btn-default"
                                                data-icon="search">查询
                                        </button>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                        <div id="took_echart" style="mini-width:400px;height:450px"></div>
                        <div style="padding:0 0 20px 50px;">
                            <p class="write">
                                注意:(1)、图表初始化统计前天登录用户绑定仓库下快递公司的发货总数和揽收及时率,每天凌晨3点统计前一天数据,统计规则(按发货时间的20点到20点)</p>
                            <p class="write left">(2)、选择仓库，统计前天仓库下快递公司发货总数和揽件及时率</p>
                            <p class="write left">(3)、选择日期，统计某天的快递公司发货总数和揽件及时率</p>
                            <p class="write left">(4)、选择仓库、日期，统计某天仓库下快递公司发货总数和揽件及时率</p>
                        </div>
                    </div>
                </div>
            </div>
            <script type="text/javascript" language="javascript">
                $(function () {
                    var nowDate = new Date();
                    nowDate.setDate(nowDate.getDate() - 1);
                    var current = nowDate.getFullYear() + "-" + (nowDate.getMonth() + 1) + "-" + nowDate.getDate();
                    $('#took_startDate').val(current);

                    $.ajax({
                        type: 'post',
                        async: false,
                        url: '${request.contextPath}/count/queryBindUserStorgae',
                        dataType: 'JSON',
                        success: function (data) {
                            if (data != "") {
                                for (var i = 0; i < data.length; i++) {
                                    $("#took_storage").append("<option  value=" + data[i].storageCode + ">" + data[i].storageName + "</option>");
                                }
                            }
                        },
                        error: function () {
                            alert('连接服务器异常');
                        }
                    });


                    $('#took_storage').multipleSelect({});
                    $("#took_storage").multipleSelect("checkAll");
                });

                // 路径配置
                require.config({
                    paths: {
                        echarts: '${request.contextPath}/static/BJUI/plugins/echarts'
                    }
                });
                var tookChart;
                // 使用
                require(
                        [
                            'echarts',
                            'echarts/chart/bar',
                            'echarts/chart/line'
                        ],
                        function (ec) {
                            tookChart = ec.init(document.getElementById('took_echart'));

                            var took_option = {
                                title: {
                                    text: '快递揽收及时率'
                                },
                                tooltip: {
                                    trigger: 'axis'
                                },
                                legend: {
                                    data: ['发货总数', '揽收及时率']
                                },
                                toolbox: {
                                    show: true,
                                    feature: {
                                        mark: {show: true},
                                        dataView: {show: true, readOnly: false},
                                        magicType: {show: true, type: ['line', 'bar']},
                                        restore: {show: true},
                                        saveAsImage: {show: true}
                                    }
                                },
                                calculable: true,
                                xAxis: [
                                    {
                                        type: 'category',
                                        boundaryGap: false,
                                        data: ['']
                                    }
                                ],
                                yAxis: [
                                    {
                                        type: 'value',
                                        name: '发货总数',
                                        position: 'right',
                                        axisLine: {
                                            lineStyle: {
                                                color: '#d14a61'
                                            }
                                        }
                                    }, {
                                        type: 'value',
                                        name: '揽收及时率(百分比)',
                                        min: 0,
                                        max: 100,
                                        axisLabel: {
                                            formatter: '{value}%'
                                        }
                                    }
                                ],
                                series: [
                                    {
                                        name: '发货总数',
                                        type: 'line',
                                        yAxisIndex: 0,
                                        smooth: true,
                                        barWidth: 35,
                                        data: [],
                                        itemStyle: {
                                            normal: {
                                                label: {
                                                    show: true, position: 'top', textStyle: {
                                                        fontSize: '15',
                                                        fontFamily: '微软雅黑',
                                                        fontWeight: 'bold',
                                                        color: 'black'
                                                    }
                                                },
                                                areaStyle: {type: 'default'}
                                            }
                                        }
                                    }, {
                                        name: '揽收及时率',
                                        type: 'line',
                                        yAxisIndex: 1,
                                        smooth: true,
                                        itemStyle: {normal: {areaStyle: {type: 'default'}}},
                                        barWidth: 35,
                                        data: [''],
                                        markPoint: {
                                            data: [
                                                {type: 'max', name: '最大值'},
                                                {type: 'min', name: '最小值'}
                                            ]
                                        }
                                    }
                                ]
                            };

                            tookChart.setOption(took_option);
                            loadTookOption("", "", "");
                        });

                //ajax 异步加载配置数据项
                function loadTookOption(storageCode, startDate, endDate) {
                    $.ajax({
                        type: 'post',
                        async: false,
                        url: '${request.contextPath}/count/took/rate',
                        dataType: 'JSON',
                        data: {storageCode: storageCode, province: '', startDate: startDate, endDate: endDate},
                        success: function (result) {
                            if ($("#took_storage").multipleSelect("getSelects") == '') {
                                $("#took_storage").multipleSelect("checkAll");
                            }
                            var xAxis = [];
                            var series_current = [];
                            var shipment_total = [];
                            if (result != "") {
                                if (result == 'loginOut') {
                                    alert("登录超时,请重新登录!");
                                    window.location.href = "${request.contextPath}/page/login";
                                } else {
                                    for (var i = 0; i < result.length; i++) {
                                        xAxis.push(result[i].courierName);
                                        series_current.push(result[i].sentRate);
                                        shipment_total.push(Number(result[i].shouldNum));
                                    }
                                }
                            } else {
                                xAxis.push('');
                                series_current.push('');
                            }
                            var option = tookChart.getOption();
                            option.xAxis[0].data = xAxis;
                            option.series[0].data = shipment_total;
                            option.series[1].data = series_current;
                            tookChart.setOption(option, true);
                        },
                        error: function () {
                            alert('连接服务器异常');
                        }
                    });
                }

                $('#took_search').click(function () {
                    var storage = $("#took_storage").multipleSelect("getSelects").toString();
                    var startDate = $('#took_startDate').val();
                    var endDate = $('#took_endDate').val();
                    loadTookOption(storage, startDate, endDate);
                });
            </script>
        </div>
    </div>
</div>