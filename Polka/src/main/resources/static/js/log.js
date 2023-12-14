$(document).ready(function(){
    $('#InputSubmit').click(function(){
        let mail   = $('#mail').val();
        let pass = $('#pass').val();
        $.ajax({
            url: "api/login",
            type: "post",
            data: {
                "mail":   mail,
                "pass":   pass
            },
            error:function(){$("#echo").html("Login error!");},
            beforeSend: function() {
                $("#echo").html("Login...");
            },
            success: function(result){
                if (result === "Вход выполнен!") {
                    location.reload();
                }
                $('#echo').html(result);
            }
        });
    });
});
