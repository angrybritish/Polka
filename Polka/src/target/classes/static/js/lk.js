function onDragOver(event) {
    event.preventDefault(); // Предотвращаем стандартное поведение браузера
}

function onDrop(event) {
    event.preventDefault(); // Предотвращаем стандартное поведение браузера
    const files = event.dataTransfer.files; // Получаем перетаскиваемые файлы
    const id = event.target.dataset.myValue;
    const fileInput = document.getElementById('fileUpload' + id);
    fileInput.files = files; // Устанавливаем файлы в элемент input
    updateLabel(files,id); // Обновляем текст лейбла
}

function onFileSelect(event) {
    const id = event.target.dataset.myValue;
    updateLabel(event.target.files,id); // Обновляем текст лейбла
}

function updateLabel(files, id) {
    const label = document.getElementById('fileUpload' + id).nextElementSibling;
    const labelDefaultText = 'Кликните или перетащите файлы сюда для загрузки';
    label.textContent = files.length ? Array.from(files).map(f => f.name).join(', ') : labelDefaultText;
}

$(document).ready(function () {
    $("#InputSubmit").on("click", function () {
        var fileInput = $("#fileUpload1")[0];
        var files = fileInput.files;
        if (files.length == 2) {
            uploadFiles(files);
        } else {
            $("#echo").text("Выберите два файла для загрузки!");
        }
    });
    $("#InputSubmitProblem").on("click", function () {
        var problem = $('#userbroblem').val();
        $.ajax({
            url: "../api/createquestion",
            type: "post",
            data: {
                "problem":   problem
            },
            error:function(){},
            beforeSend: function() {
            },
            success: function(result){
                if (result === "ok") {
                    $('#userbroblem').val("");
                    $("#echo").text("Ваш вопрос отправлен администратору. Ожидайте ответ на почту, которую указывали при регистрации.");
                }
                else {
                    $("#echo").text(result);
                }
            }
        });
    });
});

function uploadFiles(files) {
    var formData = new FormData();

    $.each(files, function(index, file) {
        formData.append("files", file);
    });

    var name = $("#name").val();
    formData.append("name", name);

    var author = $("#author").val();
    formData.append("author", author);

    var genre = $("#genre").val();
    formData.append("genre", genre);

    var writer = $("#writer").val();
    formData.append("writer", writer);

    var aboutbook = $("#aboutbook").val();
    formData.append("aboutbook", aboutbook);

    var price = $("#price").val();
    formData.append("price", price);

    $.ajax({
        url: "/api/uploadbooks",
        type: "POST",
        data: formData,
        processData: false,
        contentType: false,
        xhr: function () {
            var xhr = new window.XMLHttpRequest();
            xhr.upload.addEventListener("progress", function (evt) {
                if (evt.lengthComputable) {
                    var percentComplete = (evt.loaded / evt.total) * 100;
                    $("#echo").text(percentComplete.toFixed(2) + "%");
                }
            }, false);
            return xhr;
        },
        success: function (data) {
            if (data == "Файлы были успешно загружены!") {
                $('#name').val("");
                $('#author').val("");
                $('#genre').val("");
                $('#writer').val("");
                $('#aboutbook').val("");
                $('#price').val("");

                const label = document.getElementById('fileUpload1').nextElementSibling;
                const labelDefaultText = 'Кликните или перетащите файлы сюда для загрузки';
                label.textContent = labelDefaultText;
                $("#fileUpload1").val(null);
            }
            $("#echo").text(data);
        },
        error: function () {
            $("#echo").text("Ошибка загрузки файла.");
        }
    });
}

function uploadComment(idbook) {
    let userraiting = $('input[name="userraiting"]:checked').val();
    var review = $('#review').val();
    $.ajax({
        url: "../api/createcomment",
        type: "post",
        data: {
            "userraiting":   userraiting,
            "review":   review,
            "idbook": idbook
        },
        error:function(){$("#echo").html("Ошибка отправки комментария");},
        beforeSend: function() {
            $("#echo").html("Отправка...");
        },
        success: function(result){
            if (result === "ok") {
                location.reload();
            }
            $('#echo').html(result);
        }
    });
}

function mypolka(idbook,boolean,res) {
    $.ajax({
        url: "../api/createsaved",
        type: "post",
        data: {
            "boolean":   boolean,
            "idbook": idbook,
            "res": res
        },
        error:function(){},
        beforeSend: function() {
        },
        success: function(result){
            if (result === "ok") {
                location.reload();
            }
        }
    });
}

function createcomp(idbook) {
    let create = $('#compilation').val();
    let compname = $('#compname').val();
    let idcomp = $('#compilationmy').val();
    let compilationmydel = $('#compilationmydel').val();
    $.ajax({
        url: "../api/createcomp",
        type: "post",
        data: {
            "create":  create,
            "idbook": idbook,
            "compnamegroup": compname,
            "idcomp": idcomp,
            "compilationmydel": compilationmydel
        },
        error:function(){},
        beforeSend: function() {
        },
        success: function(result){
            if (result === "ok") {
                location.reload();
            }
            $('#exampleModalLabel').html(result);
        }
    });
}

function regbook(id,booleanbook) {
    let genre = $('#genre').val();
    $.ajax({
        url: "../api/createbookstatus",
        type: "post",
        data: {
            "idbook":  id,
            "genre": genre,
            "booleanbook": booleanbook
        },
        error:function(){},
        beforeSend: function() {
        },
        success: function(result){
            if (result === "ok") {
                location.reload();
            }
        }
    });
}

function deletecomment(id) {
    let genre = $('#genre').val();
    $.ajax({
        url: "../api/deletecomment",
        type: "post",
        data: {
            "idcomm":  id,
        },
        error:function(){},
        beforeSend: function() {
        },
        success: function(result){
            if (result === "ok") {
                location.reload();
            }
        }
    });
}

function supportq(idarea) {
    let text = $('#userquestionmy' + idarea).val();
    $.ajax({
        url: "../api/sendsupportmail",
        type: "post",
        data: {
            "text":  text,
            "idarea": idarea
        },
        error:function(){},
        beforeSend: function() {
            $('#echo').html("Отправка...");
        },
        success: function(result){
            if (result === "ok") {
                location.reload();
            }
        }
    });
}

function savebook(idbook) {
    let name = $('#name' + idbook).val();
    let author = $('#author' + idbook).val();
    let genre = $('#genre' + idbook).val();
    let writer = $('#writer' + idbook).val();
    let aboutbook = $('#aboutbook' + idbook).val();
    let price = $('#price' + idbook).val();
    $.ajax({
        url: "../api/updatebook",
        type: "post",
        data: {
            "name":  name,
            "genre": genre,
            "writer": writer,
            "price": price,
            "aboutbook": aboutbook,
            "author": author,
            "idbook":idbook
        },
        error:function(){
            $('#echo'+ idbook).html("Ошибка сохранения");
        },
        beforeSend: function() {
            $('#echo'+ idbook).html("Отправка...");
        },
        success: function(result){
            $('#echo'+ idbook).html(result);
        }
    });
}