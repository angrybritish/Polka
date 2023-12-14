function govalid() {
    var name = $('#name').val();
    var cardnum = $('#cardnum').val();
    var dateend = $('#dateend').val();
    var cvccvv = $('#cvccvv').val();
    $.ajax({
        url: "../api/createbuycheck",
        type: "post",
        data: {
            "name": name,
            "cardnum": cardnum,
            "dateend": dateend,
            "cvccvv": cvccvv
        },
        error: function () {
        },
        beforeSend: function () {
            $("#echo").html("Ожидание ответа от сервера...");
        },
        success: function (result) {
            switch (result) {
                case "1":
                    $("#echo").html("Невалидный номер карты");
                    break;
                case "2":
                    $("#echo").html("Заполните все поля");
                    break;
                case "3":
                    $("#echo").html("Заполните корректно дату");
                    break;
                case "4":
                    $("#echo").html("Заполните корректно cvc/cvv код");
                    break;
                case "5":
                    $('#codeOrder').modal('show');
                    break;
            }
        }
    });
}

function govalidcode() {
    var code = $('#codemodal').val();
    $.ajax({
        url: "../api/createbuycheckcode",
        type: "post",
        data: {
            "code": code
        },
        error: function () {
        },
        beforeSend: function () {
            $("#echomodal").html("Ожидание ответа от сервера...");
        },
        success: function (result) {
            $("#echomodal").html(result);
        }
    });
}