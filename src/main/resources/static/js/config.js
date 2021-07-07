var ip = "http://localhost:8080";
alert_reminder = function (message) {
    layui.use('layer', function () {
        var layer = layui.layer;
        layer.msg(message);
    });
}
function showPreview(source) {
    var file = source.files[0];
    if (window.FileReader) {
        var fr = new FileReader();
        fr.onloadend = function (e) {
            document.getElementById("img").src = this.result;
        };
        fr.readAsDataURL(file);
    }
}//图片预览函数
