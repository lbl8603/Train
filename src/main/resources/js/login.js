function start(success,err) {
    getCode(function (e) {

        loadQRCode(e,success,err);
    },function (e) {
        err(e);
    })
}
function getCode(success,err) {
    $.ajax({
        type: "GET",
        url: urlRoot+"/open/getLoginCode",
        dataType: "json",
        success: function(data){
            if(data.code==200){
                console.log(data)
                success(data.data);
            }else{
                console.log(data);
                var e = {};
                e.err='code_err';
                e.msg='客户端唯一标识分配失败';
                err(e)
            }
        }
    });

}
function loadQRCode(data,success,err) {
    var xmlhttp;
    xmlhttp=new XMLHttpRequest();
    xmlhttp.open("GET",urlRoot+"/open/loginQRCodeLoad?code="+data.code+"&expire="+data.expire+"&sign="+data.sign,true);
    xmlhttp.responseType = "blob";
    xmlhttp.onload = function(){
        console.log(this);
        if (this.status == 200) {
            var blob = this.response;
            var img = document.getElementById("qr-code");
            img.onload = function(e) {
                window.URL.revokeObjectURL(img.src);
            };
            img.src = window.URL.createObjectURL(blob);
            document.body.appendChild(img);
            wss(data,success,err);
        }else{
            var e = {};
            e.err='qr_code_load_err';
            e.msg='二维码加载失败';
            err(e);
        }
    }
    xmlhttp.send();
}
var socket;
function closeWss() {
    socket.close();
}
function wss(data,success,err) {
    if(!window.WebSocket){
        window.WebSocket = window.MozWebSocket;
    }
    if(!window.WebSocket){
        var e = {};
        e.err='websocket_err';
        e.msg='该浏览器不支持websocket，需要更换浏览器';
        err(e);
        return;
    }
    if(window.WebSocket){
        socket = new WebSocket(wsUrl+"/receive?code="+data.code+"&expire="+data.expire+"&sign="+data.sign);
        socket.onmessage = function(event){
            success(JSON.parse(event.data));
        };
        socket.onopen = function(event){
            success('load_success');
            console.log('连接开启')
        };
        socket.onclose = function(event){
            console.log('连接关闭');
            var e = {};
            e.err='channel_err';
            e.msg='连接关闭';
            err(e);
        };
    }
}