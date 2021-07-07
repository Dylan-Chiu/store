send_request = function (url, table_id, data, curPage) {//接收两个参数 第一个 表格id  第二个 修改的数据
    layui.use(['form', 'table'], function () {
        var table = layui.table;
        var xhr = new XMLHttpRequest();
        xhr.open("post", url);
        xhr.setRequestHeader("Content-Type", "application/json");
        xhr.setRequestHeader("token", sessionStorage.getItem("token"));
        xhr.send(data);
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4) {
                if (JSON.parse(data).data != undefined && JSON.parse(data).data.length == 0) {
                    alert_reminder("你未添加删除记录");
                }
                if (xhr.status >= 200 && xhr.status <= 400) {
                    curPage = JSON.parse(xhr.response).curPage;
                    console.log(xhr.response);
                    table.reload(table_id, {//再进行一次表格重载
                        page: {
                            curr: curPage,
                        }
                    });
                }
            }
        };
    })

}