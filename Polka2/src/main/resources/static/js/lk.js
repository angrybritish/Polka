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