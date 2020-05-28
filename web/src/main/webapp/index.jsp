
<html>
<meta http-equiv=”Content-Type” content=”text/html; charset=utf-8″>
<%@page contentType="text/html;charset=utf-8" language="java" %>
<link href="statics/css/indexNew.css" rel="stylesheet" type="text/css"/>
<script src="statics/libs/jquery-2.1.1/jquery.min.js"></script>
<script src="statics/libs/jquery.base64.js/yckart-jquery.base64.js-4784906/jquery.base64.js" type="text/javascript"></script>
<div class="zhanwei">
    <img src="statics/img/1.jpg" width="100%" height="auto">
</div>
<div class="container">
    <div class="inputdiv">
        <input id="yonghuming" class="inputBtn" type="text" placeholder="用户名"/>
    </div>
    <div class="passdiv">
        <input id="mima" class="passBtn" type="password" placeholder="密码"/>
    </div>
    <div class="tele">
        <input id="dianhua" class="teleBtn" type="tel" placeholder="手机号码"/>
    </div>
    <div>
        <button class="Tijiaobtn" id="anniu" type="button">提交</button>
    </div>
</div>

<script>
    $("document").ready(function () {

        $(".Tijiaobtn").click(function () {

            // alert("是否确认提交？");

            var username=$(".inputBtn").val();
            var Oldpassword=$(".passBtn").val();

            var telenumber=$(".teleBtn").val();

            if (username==""){
                alert("请输入用户名");
            }
            if (Oldpassword==""){
                alert("请输入密码");
            }
            if (telenumber==""){
                alert("请输入手机号码");
            }

            var password = $.base64().encode(Oldpassword);
            var entity={
                userName:username,
                passWord:password,
                teleNumber:telenumber
            }

            $.ajax({
                //这里使用一个绝对定位
                url:'http://localhost:8080/web_war_exploded/user/save',
                type:"post",
                dataType : "json",
                contentType:'application/json',
                data: JSON.stringify(entity),
                success:function (json) {
                    console.log(json);
                }
            })
            resetBtn();
        })
    })
    function resetBtn() {
        $("#yonghuming").val("");
        $("#mima").val("");
        $("#dianhua").val("");
        $("#anniu").attr("disabled",true);
       // $(window).attr('location',"http://localhost:8080/web_war_exploded/page/test.html");
        window.open("http://localhost:8080/web_war_exploded/page/test.html");
    }
</script>

</html>