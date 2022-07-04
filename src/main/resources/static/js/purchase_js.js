var purchase_list = {};//存的是购买记录
var listName = [];//存的是购买记录的商品名
var listNum = [];//存的是购买记录商品数
var listPrice = [];//存的是购买记录的商品价格
var listID = [];//存的是购买记录商品ID
var introduce = [];//存的是当前页商品介绍
var price = [];//存的是当前页商品价格
var num = [];//存的是当前商品数量
var names = [];//存的是当前页的商品名
var id = [];//存的是当前页的id
var img_url = [];//存的是当前页照片的url
var btn = [];
var cur_page = [];//记录当前页码
var total_page;
var username;//保存当前用户名

window.onload = function () {
    if (sessionStorage.getItem("token") != null) {
        alert_reminder("登陆成功！");
    }
    ;
    var url = ip + "/Goods/getGoods?page=1&limit=12";//先得到一次页数请求
    var xhr = new XMLHttpRequest();
    xhr.open("get", url);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.setRequestHeader("token", sessionStorage.getItem("token"));
    xhr.send();
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status >= 200 && xhr.status <= 400) {
                total_page = JSON.parse(xhr.response).count;
                username = JSON.parse(xhr.response).username;
                show_username();
                get_img(xhr.response);
                get_btn();
                bind_page();
            }
        }
    }
    viewPurchase();//绑定查看购买记录事件
    Bind();//绑定购物车显示事件;


    document.addEventListener("scroll", function () {//导航栏始终固定的函数
        var nav = document.getElementsByTagName("nav")[0];
        if (window.pageYOffset >= 270) {
            nav.style.position = 'fixed';
            nav.style.top = '0px';
        } else {
            nav.style.position = 'absolute';
            nav.style.top = '270px';
        }
    })


}
get_img = function (response) {
    img_url = [];
    var res = JSON.parse(response);
    res = res.data;
    for (var i = 0; i < res.length; i++) {
        img_url.push(ip + res[i].img);
    }
}
get_btn = function () {//绑定获取li商品信息 只要绑定一次即可
    var get_li = document.querySelectorAll(".w ul li");
    btn = new Object(get_li.length);
    for (var i = 0; i < get_li.length; i++) {//获取每个li标签，为每个li标签设置相应的响应函数
        btn[i] = new Vue({
            el: get_li[i],
            data: {
                li_show: false,
                isShow: false,//减号标签显示
                introduce: introduce[i],
                price: price[i],
                name: names[i],
                purch_num: 0,
                remain_num: num[i],
                id: id[i],
                img_url: img_url[i]
            },
            /*computed: {
                imgUrl: function () {
                    return this.img_url;
                }
            },*/
            methods: {
                add: function () {
                    this.isShow = true;
                    if (this.remain_num == 0)
                        this.isShow = false;
                    if (this.purch_num >= this.remain_num)
                        alert_reminder("超出库存数量！");
                    else
                        this.purch_num += 1;
                    get_num(this.name, this.purch_num, this.price, this.id);
                },
                sub: function () {
                    this.purch_num -= 1;
                    if (this.purch_num <= 0)
                        this.isShow = false;
                    get_num(this.name, this.purch_num, this.price, this.id);
                }
            }
        })
    }
}
update_cur_message = function (page) {//请求更新当前商品信息函数
    var url = ip + "/Goods/getGoods?page=" + page + "&limit=12";
    var xhr = new XMLHttpRequest();
    xhr.open("get", url);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.setRequestHeader("token", sessionStorage.getItem("token"));
    xhr.send();
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status >= 200 && xhr.status <= 400) {
                clear_vari();//记得清空当前页的商品那些保存的变量
                apply(xhr.response);//先把购物信息提取出来
                shows_message()//渲染商品信息
            }
        }
    }
}
Bind = function () {//绑定购物车函数 即触发进入与移除事件

    var car = new Vue({
        el: "#elnav",
        data: {
            isShow: false,
            listName: listName,
            listNum: listNum
        },
        methods: {
            enter: function () {
                //这一部分逻辑代码是显示购物栏中购物列表
                var ul = document.querySelectorAll("#purchase_list ul")[0];
                get_list();//获取用户购买了多少的东西
                var len = listNum.length + 1;
                if (len * 30 <= 200)//超过一定的数量 让其中的界面高度固定 触发滚轮条
                    ul.style.height = (len * 30) + 'px';
                this.listName = listName;//将对应商品名渲染到html界面中
                this.listNum = listNum;
                this.isShow = true;//购物栏界面显示
            },
            leave: function () {//鼠标离开购物车界面 isShow置为False 即让他消失
                this.isShow = false;
            },
            clicks: function () {//点击购买的逻辑代码 即传给后端
                var r = [];
                for (var key in purchase_list) {
                    var t = {};
                    t.name = key;
                    t.num = purchase_list[key][0];
                    t.id = purchase_list[key][2];
                    t.price = purchase_list[key][1];
                    t.total_price = purchase_list[key][0] * purchase_list[key][1];
                    if (purchase_list[key][0] != 0)
                        r.push(t);
                }
                if (r.length == 0) {
                    alert_reminder("您未购买任何东西！");
                    return;
                }
                var res = {};
                res.goodsList = r;
                res = JSON.stringify(res);
                console.log(res);
                var url = ip + "/Order/createOrder";
                var xhr = new XMLHttpRequest();
                xhr.open("post", url);
                xhr.setRequestHeader("Content-Type", "application/json");
                xhr.setRequestHeader("token", sessionStorage.getItem("token"));
                xhr.send(res);
                xhr.onreadystatechange = function () {
                    if (xhr.readyState === 4) {
                        if (xhr.status >= 200 && xhr.status <= 400) {
                            if (JSON.parse(xhr.response).code == 1) {
                                alert_reminder("购买成功！");
                                purchase_list = {};
                                listName = [];
                                listNum = [];//将购买清单记录清空
                                listPrice = [];
                                listID = [];
                                //更新当前页购物信息
                                update_cur_message(cur_page);
                            } else {
                                layui.use('layer', function () {
                                    var layer = layui.layer;
                                    layer.msg('您还未登录');
                                    setTimeout(function () {//设置1s后跳回到登录页
                                        window.location.href = 'http://127.0.0.1:8080//login.html';
                                    }, 1000)
                                });
                            }

                        }
                    }
                }
            },
            scroll: function () {//此函数解决购物栏 名称与数量那个li始终固定位置的代码 因为scroll是在div里的 不能用fixed解决
                //换一种思路 如果滚轮动了 我们就获取它的位置 让li的top发生变化 始终与scroll滚动的距离相等即可
                var ul = document.querySelectorAll("#purchase_list ul")[0];
                var li = document.querySelectorAll("#purchase_list ul li")[0];
                li.style.top = ul.scrollTop + "px";//获取当前滚轮的位置 并赋给Top
            }
        }
    })
}
get_list = function () {//获取当前页面用户购买的所有物品记录数据，遍历一遍purchase_list（记录了用户购买的东西）即可
    listName = [];
    listNum = [];
    listPrice = [];
    listID = [];
    for (var key in purchase_list) {
        if (purchase_list[key][0] != 0) {
            listName.push(key);
            listNum.push(purchase_list[key][0]);
            listPrice.push(purchase_list[key][1]);
            listID.push(purchase_list[key][2]);
        }

    }
}
get_num = function (name, num, price, id) {//将当前用户购买的数据存入变量中
    purchase_list[name] = [num, price, id];
}
apply = function (response) {//获取处理后端商品信息
    img_url = [];
    var res = JSON.parse(response);
    res = res.data;
    for (var i = 0; i < res.length; i++) {
        names.push(res[i].name);
        num.push(res[i].stock);
        price.push(res[i].price);
        introduce.push(res[i].introduction);
        id.push(res[i].id);
        img_url.push(ip + res[i].img);
    }

}
shows_message = function () {
    for (var i = 0; i < names.length; i++)//将有商品信息的li渲染上去
    {
        btn[i].li_show = true;
        btn[i].name = names[i];
        btn[i].price = price[i];
        btn[i].introduce = introduce[i];
        btn[i].id = id[i];
        btn[i].remain_num = num[i];
        btn[i].img_url = img_url[i];
        if (purchase_list[names[i]] == undefined || purchase_list[names[i]][0] <= 0) {
            btn[i].purch_num = 0;//页面上的购买数得清0
            btn[i].isShow = false;//购买数为0 减号也记得隐藏!!!
        } else {
            btn[i].isShow = true;//购买数不为0 说明有购买量 不能隐藏!!!
            btn[i].purch_num = purchase_list[names[i]][0];
        }


    }
}
bind_page = function () {
    layui.use('laypage', function () {
        var laypage = layui.laypage;

        laypage.render({
            elem: 'page'
            , count: total_page
            , first: false
            , last: false
            , theme: '#e61c1c'
            , limit: 12
            , jump: function (obj, first) {
                cur_page = obj.curr;
                for (var i = 0; i < 12; i++) {
                    btn[i].li_show = false;
                }//先将页面li全部置为false
                update_cur_message(obj.curr);//跳转页面 拿到那页面购物信息
            }
        });
    });
}
clear_vari = function () {//清空当前页面商品信息函数
    introduce = [];
    price = [];
    num = [];
    names = [];
    id = [];
    img_url = [];
}
viewPurchase = function () {
    layui.use(['table', 'layer'], function () {
        var layer = layui.layer;
        var $ = layui.jquery;
        $("#viewPurchase").click(function () {
            layer.open({//订单记录弹出层
                type: 2,
                title: "购买记录",
                toolbar: '#toolbarDemo',
                area: ['700px', '500px'],
                content: 'viewPurchase.html',
                success: function (layero, index) {//弹出后渲染表格显示在弹出层上
                    var table = layui.table;
                    var body = layer.getChildFrame('body', index);
                    body = body.find('#currentTableId');
                    table.render({
                        elem: body,
                        //url: '../houtai/layuimini/api/table.json',
                        url: ip + '/Order/getLimitByUsername',
                        area: ['500px', '300px'],
                        cols: [[
                            {field: 'order_id', width: 150, title: '订单号', align: "center"},
                            {
                                field: 'order_time', width: 150, align: "center", title: '下单时间', templet: function (d) {
                                    var time = new Date(d.order_time);
                                    return time.toLocaleDateString().replace(/\//g, "-") + " " + time.toTimeString().substr(0, 8)
                                }, sort: true
                            },
                            {
                                field: 'status', width: 100, title: '订单状态', align: "center", templet: function (d) {
                                    if (d.status == 1)
                                        return '未发货';
                                    else
                                        return '已发货';
                                }
                            },
                            {
                                field: 'totalPrice', width: 120, title: '总金额', align: "center"
                            },
                            {
                                title: '操作', minWidth: 80, align: "center",
                                templet: function (d) {
                                    return '<button type="button" class="layui-btn layui-btn-sm" lay-event="viewDetail">查看详情</button>'
                                }
                            }
                        ]],
                        limit: 9999,
                        headers: {token: sessionStorage.getItem("token")},
                        parseData: function (d) {
                            console.log(d);
                        }//拿到返回后的renponse数据

                    });
                    var table = layui.table;
                    table.on('tool(currentTableFilter)', function (objs) {//绑定查看订单事件
                        var layers = layui.layer;
                        console.log(objs);
                        layers.open({//订单详情弹出层
                            type: 2,
                            title: '订单详情',
                            area: ['400px', '400px'],
                            content: 'orderDetail.html',
                            success: function (layero, index) {//弹出成功后渲染表格
                                var table = layui.table;
                                console.log(objs.data.order_id);
                                var body = layer.getChildFrame('body', index);
                                body = body.find('#currentTableIdTwo');
                                table.render({//用来渲染订单页详情的表格
                                    elem: body //指定原始表格元素选择器（推荐id选择器）
                                    , url: ip + '/Order/getDetailById'
                                    , cols: [[{field: 'goodsName', width: 150, title: '商品名'},
                                        {field: 'amount', width: 95, title: '购买数量'},
                                        {field: 'totalPrice', width: 150, title: '总金额'}] //设置表头
                                    ],
                                    where: {orderId: objs.data.order_id},
                                    page: false
                                });
                            }
                        })
                    })
                } //对sucess
            })//对open
        })//对 clik
    })
}
show_username = function () {
    var div = document.querySelector("#user div");
    var a = document.querySelector("#user #tuichu");
    if (username == null) {
        div.innerHTML = "您还未登录";
        a.innerHTML = "登录";
    } else {
        div.innerHTML = "用户：" + username;
        a.innerHTML = "退出";
    }
}
quit = function () {
    sessionStorage.removeItem("token");
    if (username != null)
        alert_reminder("退出成功");
    else
        alert_reminder("前往登录");
    setTimeout(function () {//设置1s后跳回到登录页
        window.location.href = 'http://127.0.0.1:8080//login.html';
    }, 100)

}
