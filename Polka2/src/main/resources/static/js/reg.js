$(document).ready(function(){
    $('#InputSubmit').click(function(){
        let mail   = $('#mail').val();
        let pass = $('#pass').val();
        let username = $('#username').val();
        let usertype = $('input[name="flexRadioDefault"]:checked').val();
        $.ajax({
            url: "api/register",
            type: "post",
            data: {
                "mail":   mail,
                "pass":   pass,
                "username":   username,
                "usertype": usertype
            },
            error:function(){$("#echo").html("Registration error!");},
            beforeSend: function() {
                $("#echo").html("Registration...");
            },
            success: function(result){
                if (result === "Аccount is registered!") {
                    location.reload();
                }
                $('#echo').html(result);
            }
        });
    });
});
