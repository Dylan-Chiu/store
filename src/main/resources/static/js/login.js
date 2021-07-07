
window.onload = function () {
    var btn = new Vue({
        el: ".login-card",
        data: {
            isShow_login: true,
            isShow_regist: false,
            isShow_count: false,
            isShow_password: false,
            isSuccess: [false, false, true, true],
            count_pattern: /^[a-zA-Z0-9_-]{4,16}$/,
            password_pattern: /^(\w){6,20}$/,
            email_pattern: /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/,
            phone_pattern: /^1[3456789]\d{9}$/

        },
        methods: {
            returnLogin: function () {
                this.isShow_login = !this.isShow_login;
                this.isShow_regist = !this.isShow_regist;
                var div = document.getElementById("checkIdentity");
                div.style.display = 'block';
                document.getElementById("return").style.display = 'none';
            },
            regist: function () {//控制登录界面和注册界面显示
                this.isShow_login = !this.isShow_login;
                this.isShow_regist = !this.isShow_regist;
                var div = document.getElementById("checkIdentity");
                div.style.display = 'none';
                document.getElementById("return").style.display = 'block';
            },
            get_login_message: function () {//得到登录信息
                var count = document.querySelectorAll("#login #count")[0].value
                var password = document.querySelectorAll("#login #password")[0].value;
                var message = {};
                message.username = count;
                message.password = password;
                message.identity = parseInt($('form input[name="identity"]:checked ').val());
                if (Test(count, password, this))
                    send_login_message(message, this);
            },
            get_regist_message: function () {//得到注册信息
                var login_message = {};
                login_message.username = document.querySelectorAll("#regist #count")[0].value
                if (!is_legal(login_message.username, this.count_pattern) || login_message.username == "")
                    return

                login_message.password = document.querySelectorAll("#regist #password")[0].value;
                if (!is_legal(login_message.password, this.password_pattern) || login_message.password == "")
                    return

                login_message.email = document.querySelectorAll("#regist #email")[0].value;
                if (!is_legal(login_message.email, this.email_pattern) && login_message.email != "")
                    return;
                login_message.phone = document.querySelectorAll("#regist #phone")[0].value;
                if (!is_legal(login_message.phone, this.phone_pattern) && login_message.phone != "")
                    return;

                send_regist_message(login_message, this);//发送注册请求
            },
            vetify_count: function (obj) {//移除事件

                var type = obj.path[0].id;
                var t;
                if (type == "count") {
                    this.isShow_count = false;
                    t = document.querySelectorAll("#regist #count")[0];
                    var uPattern = /^[a-zA-Z0-9_-]{4,16}$/;
                    if (t.value == "") {
                        t.placeholder = "请输入账号";
                        t.style.borderColor = "red";
                        t.style.borderWidth = "2px";
                    }
                    else if (!is_legal(t.value, this.count_pattern)) {
                        t.value = "";
                        t.placeholder = "账号不符合规范!";
                        t.style.borderColor = "red";
                        t.style.borderWidth = "2px";
                    }

                } else if (type == "password") {
                    this.isShow_password = false;
                    t = document.querySelectorAll("#regist #password")[0];
                    var uPattern = /^(\w){6,20}$/;
                    if (t.value == "") {
                        t.placeholder = "请输入密码";
                        t.style.borderColor = "red";
                        t.style.borderWidth = "2px";
                    }
                    else if (!is_legal(t.value, this.password_pattern)) {
                        t.value = "";
                        t.placeholder = "密码不符合规范!";
                        t.style.borderColor = "red";
                        t.style.borderWidth = "2px";
                    }

                }
                else if (type == "email") {
                    t = document.querySelectorAll("#regist #email")[0];
                    var uPattern = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
                    if (!is_legal(t.value, this.email_pattern) && t.value != "") {
                        t.value = "";
                        t.placeholder = "邮箱格式不符合规范!";
                        t.style.borderColor = "red";
                        t.style.borderWidth = "2px";
                    }
                }
                else if (type == "phone") {
                    this.isShow_count = false;
                    t = document.querySelectorAll("#regist #phone")[0];
                    var uPattern = /^1[3456789]\d{9}$/;
                    if (!is_legal(t.value, this.phone_pattern) && t.value != "") {
                        t.value = "";
                        t.placeholder = "手机号格式不符合规范!";
                        t.style.borderColor = "red";
                        t.style.borderWidth = "2px";
                    }
                }


            },
            get_blue: function (obj) {//点击事件让框框变回原来的蓝
                var border;
                if (obj == "count") {//账号

                    this.isShow_count = true;
                    border = document.querySelectorAll(".login-card #regist #count")[0];
                    border.placeholder = "用户名";
                    border.style.borderColor = "";
                    border.style.borderWidth = "";
                }
                else if (obj == "password") {
                    this.isShow_password = true;
                    border = document.querySelectorAll(".login-card #regist #password")[0];
                    border.placeholder = "密码";
                    border.style.borderColor = "";
                    border.style.borderWidth = "";
                }
                else if (obj == "email") {
                    border = document.querySelectorAll(".login-card #regist #email")[0];
                    border.placeholder = "邮箱";
                    border.style.borderColor = "";
                    border.style.borderWidth = "";
                }
                else if (obj == "phone") {
                    border = document.querySelectorAll(".login-card #regist #phone")[0];
                    border.placeholder = "电话";
                    border.style.borderColor = "";
                    border.style.borderWidth = "";
                }
                else if (obj == "logincount") {
                    this.isShow_count = true;
                    border = document.querySelectorAll(".login-card #login #count")[0];
                    border.placeholder = "用户名";
                    border.style.borderColor = "";
                    border.style.borderWidth = "";
                } else if (obj == "loginpassword") {
                    this.isShow_password = true;
                    border = document.querySelectorAll(".login-card #login #password")[0];
                    border.placeholder = "密码";
                    border.style.borderColor = "";
                    border.style.borderWidth = "";
                }


            }

        }
    })
}
is_legal = function (obj, pattern) {
    return pattern.test(obj);
}
Test = function (count, password, This) {//验证是否用户在登陆时是否没输入就登陆了
    var t1 = document.querySelectorAll("#login #count")[0];
    var t2 = document.querySelectorAll("#login #password")[0];
    var identity = $('form input[name="identity"]:checked ').val();
    var flag = true;
    if (identity == undefined) {
        alert_reminder("请选择身份登录！");
        flag = false;
    }
    if (count == "") {
        t1.placeholder = "请输入账号";
        t1.style.borderColor = "red";
        t1.style.borderWidth = "2px";
    }
    if (password == "") {
        t2.placeholder = "请输入密码";
        t2.style.borderColor = "red";
        t2.style.borderWidth = "2px";
    }
    if (count == "" || password == "" || flag == false)
        return false;
    else return true;

}
send_regist_message = function (message, This) {//发送注册信息并回调处理函数
    message = JSON.stringify(message);
    var xhr = new XMLHttpRequest();
    xhr.open('post', ip + '/addConsumer');
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send(message);

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status >= 200 && xhr.status <= 400) {
                var status = JSON.parse(xhr.response).code;
                if (status == "1") {
                    alert_reminder("注册成功!");
                    This.isShow_login = !This.isShow_login;
                    This.isShow_regist = !This.isShow_regist;
                    document.getElementById("return").style.display = 'none';
                    document.getElementById("checkIdentity").style.display = 'block';
                } else if (status == "-1") {
                    t = document.querySelectorAll("#regist #count")[0];
                    t.value = "";
                    t.placeholder = "用户名重复";
                    t.style.borderColor = "red";
                    t.style.borderWidth = "2px";
                }
            }
        }
    }
}
send_login_message = function (message, This) {//发送登录信息 并做出相应判断
    message = JSON.stringify(message);
    var xhr = new XMLHttpRequest();
    xhr.open('post', ip + '/login');
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send(message);
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status >= 200 && xhr.status <= 400) {
                var res = JSON.parse(xhr.response);
                console.log(res.code);
                if (res.code == "1") {
                    sessionStorage.setItem("token", res.token);
                    if ($('form input[name="identity"]:checked ').val() == 1)
                        window.location.href = 'http://127.0.0.1:8080//purchase.html';
                    else
                        window.location.href = 'http://127.0.0.1:8080//houtai//index.html';
                }
                if (res.code == "-1") {
                    alert_reminder("用户名不存在！");
                }
                if (res.code == "-2") {
                    alert_reminder("密码错误！");
                }
            }
        }
    }
}



