window.onload = function () {


    var option1 = {
        color: ["#046e6e", "#046e6e"],
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data: ['销售额'],
            textStyle: {
                //color: ['red', 'green'],//修改图例颜色
            },
            right: "0%"//修改图例位置
        },
        grid: {
            top: '20%',
            left: '3%',
            right: '4%',
            bottom: '3%',
            show: false,
            borderColor: "rgb(0, 0, 0)",
            containLabel: true
        },
        xAxis: {
            name: '时间',
            nameTextStyle: {         //关键代码
                padding: [-25, -80, 0, -20],

            },
            splitNumber: 9,
            type: 'category',
            boundaryGap: false,
            data: ['0:00', '1:00', '2:00', '3:00', '4:00', '5:00', '6:00', '7:00', '8:00', '9:00', '9:00', '10:00', '11:00', '12:00', '13:00', '14:00', '15:00', '16:00', '17:00', '18:00', '19:00', '20:00', '21:00', '22:00', '23:00'],
            axisTick: {
                show: false
            }//去除刻度
            ,
            axisLine: {
                symbol: ['none', 'path://M250 150 L150 350 L350 350 Z']
            },
            axisLabel: {
                textStyle: {
                    fontSize: '14',

                }
            }

        },
        yAxis: {
            splitNumber: 3,
            name: '元',
            nameTextStyle: {         //关键代码

                padding: [20, -60, -20, -30],

            },
            type: 'value',
            axisTick: {
                show: false
            }//去除刻度

            ,
            axisTick: {
                show: false
            }//去除刻度
            ,
            axisLine: {
                symbol: ['none', 'path://M250 150 L150 350 L350 350 Z']
            },
            splitLine: {
                show: false
            },
            axisLabel: {
                textStyle: {
                    fontSize: '14',

                }
            }

        },
        series: [
            {
                name: '销售额',
                type: 'line',
                stack: '总量',
                smooth: true,
                data: [120, 132, 101, 134, 90, 230, 210, 222, 3332, 200, 15, 131, 200, 600, 155, 666, 999, 200, 366, 200, 333, 232, 266, 200]
            }
        ]
    };
    var option2 = {
        color: ["#046e6e", "#046e6e"],
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data: ['销售额'],
            textStyle: {
                //color: ['red', 'green'],//修改图例颜色
            },
            right: "0%"//修改图例位置
        },
        grid: {
            top: '20%',
            left: '3%',
            right: '4%',
            bottom: '3%',
            show: false,
            borderColor: "rgb(180, 175, 175)",
            containLabel: true
        },
        xAxis: {
            name: '时间',
            nameTextStyle: {         //关键代码

                padding: [-25, -80, 0, -20],

            },
            type: 'category',
            boundaryGap: false,
            data: ['6.21', '6.22', '6.23', '6.24', '6.25', '6.26', '6.27'],
            axisTick: {
                show: false
            }//去除刻度
            ,
            axisLine: {
                symbol: ['none', 'path://M250 150 L150 350 L350 350 Z']
            },
            axisLabel: {
                textStyle: {
                    fontSize: '14',

                }
            }
        },
        yAxis: {
            splitNumber: 3,
            name: '元',
            nameTextStyle: {         //关键代码

                padding: [20, -60, -20, -30],

            },
            type: 'value',
            axisTick: {
                show: false
            }//去除刻度
            , splitLine: {
                show: false
            }//y轴分割线
            , axisTick: {
                show: false
            }//去除刻度
            ,
            axisLine: {
                symbol: ['none', 'path://M250 150 L150 350 L350 350 Z']
            },
            axisLabel: {
                textStyle: {
                    fontSize: '14',

                }
            }
        },
        series: [
            {
                name: '销售额',
                type: 'line',
                stack: '总量',
                smooth: true,
                data: [1236, 1563, 1223, 1445, 1555, 1600, 1750]
            }
        ]
    };
    var option3 = {
        color: ["#046e6e"],
        xAxis: {
            name: '商品名',
            nameTextStyle: {         //关键代码

                padding: [-25, -80, 0, -20],

            },
            type: 'category',
            data: ['老鼠药', '蟑螂药', '鱿鱼丝', '白菜', '青椒', '薯片', '风扇'],
            axisTick: {
                show: false
            }//去除刻度
            ,
            axisLine: {
                symbol: ['none', 'path://M250 150 L150 350 L350 350 Z']
            },
            axisLabel: {
                textStyle: {
                    fontSize: '14',

                }
            }
        },
        yAxis: {
            splitNumber: 3,
            name: '件',
            nameTextStyle: {         //关键代码

                padding: [20, -60, -20, -30],

            },
            axisLine: {
                symbol: ['none', 'path://M250 150 L150 350 L350 350 Z']
            },
            axisTick: {
                show: false
            }//去除刻度
            ,
            type: 'value',
            axisLabel: {
                textStyle: {
                    fontSize: '14',

                }
            }
        },
        tooltip: {
            trigger: 'axis'
        },
        series: [{
            data: [120, 200, 150, 80, 70, 110, 130],
            type: 'bar',
            showBackground: true,
            backgroundStyle: {
                color: 'rgba(180, 180, 180, 0.2)'
            },
            barWidth: 30,
        }]
    };
    var option4 = {
        color: ["#046e6e"],
        xAxis: {
            name: '商品名',
            nameTextStyle: {         //关键代码

                padding: [-25, -80, 0, -20],
            },
            type: 'category',
            data: ['老鼠药', '蟑螂药', '鱿鱼丝', '白菜', '青椒', '薯片', '风扇'],
            axisTick: {
                show: false
            }//去除刻度
            ,
            axisLine: {
                symbol: ['none', 'path://M250 150 L150 350 L350 350 Z']
            },
            axisLabel: {
                textStyle: {
                    fontSize: '14',

                }
            }
        },
        yAxis: {
            splitNumber: 3,
            name: '件',
            nameTextStyle: {         //关键代码

                padding: [20, -60, -20, -30],

            },
            axisLine: {
                symbol: ['none', 'path://M250 150 L150 350 L350 350 Z']
            },
            axisTick: {
                show: false
            }//去除刻度
            ,
            type: 'value',
            axisLabel: {
                textStyle: {
                    fontSize: '14',

                }
            }
        },
        tooltip: {
            trigger: 'axis'
        },
        series: [{
            data: [120, 200, 150, 80, 70, 110, 130],
            type: 'bar',
            showBackground: true,
            backgroundStyle: {
                color: 'rgba(180, 180, 180, 0.2)'
            },
            barWidth: 30,
        }]
    };
    var E1 = echarts.init(document.querySelector('#showData1'));
    var E2 = echarts.init(document.querySelector('#showData2'));
    var E3 = echarts.init(document.querySelector('#showData3'));
    var E4 = echarts.init(document.querySelector('#showData4'));

    var xhr = new XMLHttpRequest();

    xhr.open('get', ip + '/getStatistics');
    xhr.setRequestHeader('token', sessionStorage.getItem('token'));
    xhr.send();
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status >= 200 && xhr.status <= 400) {

                Data = JSON.parse(xhr.response);
                if (Data.code == "0") {
                    Data = Data.data;
                    console.log(option1.yAxis.series);
                    option1.series[0].data = Data[0].y;
                    option2.xAxis.data = Data[1].x;
                    option2.series[0].data = Data[1].y;
                    option3.xAxis.data = Data[2].x;
                    option3.series[0].data = Data[2].y;
                    option4.xAxis.data = Data[3].x;
                    option4.series[0].data = Data[3].y;
                    console.log(Data);
                    E1.setOption(option1);
                    E2.setOption(option2);
                    E3.setOption(option3);
                    E4.setOption(option4);
                }
                else {
                    alert_reminder("未登录");
                }



            }
        }
    }
    /*     layui.use(['form'], function () {
            var form = layui.form;
    
            form.on('select(demo)', function (data) {
                var flag = data.value;
                if (flag == 0) {
                    var Y = [100, 200, 5220, 300, 400, 400, 333];
                    var X = ['周一', '周二', '周三', '周四', '周五', '周六', '周日'];
                    option.series[0].data = Y;
                    option.xAxis.data = X;
                    Echarts.setOption(option);
                }
                else {
                    var Y = [350, 666, 752];
                    var X = ['上午', '下午', '晚上'];
                    option.series[0].data = Y;
                    option.xAxis.data = X;
                    Echarts.setOption(option);
                }
            })
        }) */
}