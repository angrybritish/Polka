package com.example.Polka.functions;

import com.example.Polka.database.*;
import com.example.Polka.managers.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class func {
    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final String CHARACTERSNUM = "0123456789";
    private static final Set<String> imgFormats = Set.of(".png", ".jpeg", ".jpg");
    private static final Set<String> booksFormats = Set.of(".txt");
    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);

    public static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static boolean isValidEmail(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static String getLkMenu(int UserType) {
        switch (UserType) {
            case 0:
                return "<li><a href=\"../my/saved\">Избранное</a></li><li><a href=\"../lk/support\">Поддержка</a></li><li><a href=\"../api/logout\">Выход</a></li>";
            case 1:
                return "<li><a href=\"../lk/publish\">Загрузить книгу</a></li><li><a href=\"../lk/books\">Мои книги</a></li><li><a href=\"../my/saved\">Избранное</a></li><li><a href=\"../lk/support\">Поддержка</a></li><li><a href=\"../api/logout\">Выход</a></li>";
            case 2:
                return "<li><a href=\"../admin/books\">Проверка книг</a></li><li><a href=\"../admin/support\">Поддержка пользователей</a></li><li><a href=\"../my/saved\">Избранное</a></li><li><a href=\"../lk/support\">Поддержка</a></li><li><a href=\"../api/logout\">Выход</a></li>";
        }
        return "null";
    }

    public static String getLkRecomendations() {
        List<Book> books = BookManager.readAllByRandom10();
        String respBook = "";
        String htmlBook = "<h2>Рекомендации</h2><div class=\"card-container\">\n";
        int Count = 0;
        for (Book book : books) {
            if (book.getStatus().equals("PUBLISH")) {
                File fileImg = FileManager.getFileById(book.getIdimg()).get();
                respBook += "    <a href=\"/book/" + book.getId() + "\" class=\"card\">\n" +
                        "        <img src=\"/uploads/" + fileImg.getGenName() + "." + fileImg.getType() + "\" alt=\"img\">\n" +
                        "        <div class=\"card-info\">\n" +
                        "            <h3>" + book.getName() + "</h3>\n" +
                        "            <p>" + book.getAuthor() + "</p>\n" +
                        "        </div>\n" +
                        "    </a>\n";
                Count++;
            }
        }
        respBook = htmlBook + respBook + "</div>";
        return respBook;
    }

    public static String getLkPublish(Long user) {
        return "<div class = \"container reg_form2\" id = \"form2\">\n" +
                "    <div class=\"row justify-content-center\">\n" +
                "        <h2 id=\"echo\">Загрузить новую книгу</h2>\n" +
                "        <div class=\"mb-3 col-12 col-md-4\">\n" +
                "            <p for=\"InputUserName\" class=\"form-label\">Название</p>\n" +
                "            <input type=\"text\" id=\"name\" class=\"form-control\" name=\"InputUserName\">\n" +
                "        </div>\n" +
                "        <div class =\"w-100\"></div>\n" +
                "        <div class=\"mb-3 col-12 col-md-4\">\n" +
                "            <p for=\"InputPassword1\" class=\"form-label\">Автор</p>\n" +
                "            <input type=\"text\" id=\"author\" class=\"form-control\" name=\"InputPassword1\">\n" +
                "        </div>\n" +
                "        <div class =\"w-100\"></div>\n" +
                "        <div class=\"mb-3 col-12 col-md-4\">\n" +
                "            <p for=\"InputPassword1\" class=\"form-label\">Жанр</p>\n" +
                "<select id=\"genre\" class=\"form-select\">\n" +
                "  <option value=\"1\">Ужасы</option>\n" +
                "  <option value=\"2\">Мистика</option>\n" +
                "  <option value=\"3\">Фантастика</option>\n" +
                "  <option selected value=\"4\">Классика</option>\n" +
                "</select>" +
                "        </div>\n" +
                "        <div class =\"w-100\"></div>\n" +
                "        <div class=\"mb-3 col-12 col-md-4\">\n" +
                "            <p for=\"InputPassword1\" class=\"form-label\">Издательство</p>\n" +
                "            <input type=\"text\" id=\"writer\" class=\"form-control\" name=\"InputPassword1\">\n" +
                "        </div>\n" +
                "        <div class =\"w-100\"></div>\n" +
                "        <div class=\"mb-3 col-12 col-md-4\">\n" +
                "            <p for=\"InputPassword1\" class=\"form-label\">Описание</p>\n" +
                "            <textarea type=\"text\" id=\"aboutbook\" class=\"form-control\" name=\"InputPassword1\"></textarea>\n" +
                "        </div>\n" +
                "        <div class =\"w-100\"></div>\n" +
                "        <div class=\"mb-3 col-12 col-md-4\">\n" +
                "            <p for=\"InputPassword1\" class=\"form-label\">Цена</p>\n" +
                "            <input type=\"number\" id=\"price\" class=\"form-control\" name=\"InputPassword1\">\n" +
                "        </div>\n" +
                "        <div class =\"w-100\"></div>\n" +
                "        <div class=\"mb-3 col-12 col-md-4\">\n" +
                "      <div class=\"form-group\">\n" +
                "<p for=\"InputPassword1\" class=\"form-label\">Прикрепите 2 файла: обложку (png, jpg) и книгу (txt)</p>\n" +
                "           <label for=\"fileUpload1\" class=\"dropzone\" data-my-value=\"1\" ondragover=\"onDragOver(event)\" ondrop=\"onDrop(event)\">\n" +
                "               <input id=\"fileUpload1\" type=\"file\" name=\"files\" data-my-value=\"1\" multiple=\"multiple\" style=\"display: none;\" onchange=\"onFileSelect(event)\">\n" +
                "               <span>Кликните или перетащите файлы сюда для загрузки</span>\n" +
                "           </label>\n" +
                "      </div>\n" +
                "        </div>\n" +
                "        <div class =\"w-100\"></div>\n" +
                "        <div class=\"mb-3 col-12 col-md-4\">\n" +
                "            <button type=\"login \" class=\"btn btn-warning\" id=\"InputSubmit\">Опубликовать книгу!</button>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</div>";
    }

    public static String generateUniqueFileName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = dateFormat.format(new Date());
        String newFileName = timestamp + "_" + generateRandomString(10);
        return newFileName;
    }

    public static String generateRandomString(int length) {
        StringBuilder randomString = new StringBuilder();
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            randomString.append(randomChar);
        }

        return randomString.toString();
    }

    public static String generateRandomStringNum(int length) {
        StringBuilder randomString = new StringBuilder();
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERSNUM.length());
            char randomChar = CHARACTERSNUM.charAt(randomIndex);
            randomString.append(randomChar);
        }

        return randomString.toString();
    }

    public static String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0) {
            return fileName.substring(dotIndex + 1);
        }
        return "";
    }

    public static Long getCurrentTime() {
        long currentTimeMillis = System.currentTimeMillis();
        long unixTimestamp = currentTimeMillis / 1000;
        return unixTimestamp;
    }

    public static boolean canUploadFile(String fileName) {

        String fileExtension = '.' + fileName.toLowerCase();

        if (booksFormats.contains(fileExtension)) {
            return true;
        } else if (imgFormats.contains(fileExtension)) {
            return true;
        } else {
            return false;
        }
    }

    public static String getSearch(String query,int option) {
        if (query.trim().equals("")) {return "Пустой запрос!";}
        List<Book> books = BookManager.readAllByNA(query);
        String respBook = "";
        String htmlBook = "<div class=\"card-container\">\n";
        int Count = 0;
        for (Book book : books) {
            if (book.getStatus().equals("PUBLISH")) {
                if (!(option != book.getGenre() && option != 0)) {
                    File fileImg = FileManager.getFileById(book.getIdimg()).get();
                    respBook += "    <a href=\"/book/" + book.getId() + "\" class=\"card\">\n" +
                            "        <img src=\"/uploads/" + fileImg.getGenName() + "." + fileImg.getType() + "\" alt=\"img\">\n" +
                            "        <div class=\"card-info\">\n" +
                            "            <h3>" + book.getName() + "</h3>\n" +
                            "            <p>" + book.getAuthor() + "</p>\n" +
                            "        </div>\n" +
                            "    </a>\n";
                    Count++;
                }
            }
        }
        respBook = htmlBook + respBook + "</div>";
        if (Count == 0) {respBook = "Ничего не найдено!";}
        return respBook;
    }

    public static String getBook(Book book,Boolean auto,User user) {
        String adminDel = "";
        if (!book.getStatus().equals("PUBLISH")) {return "Ошибка просмотра книги. Пожалуйста, попробуйте позже.";}
        File fileImg = FileManager.getFileById(book.getIdimg()).get();
        List<Comment> comments = CommentManager.getCommByIdBook(book.getId());
        int count = 0;
        float raiting = 0F;
        String commentshtml = "";
        String myPolkaBTN = "";
        for (Comment comment : comments) {
            if (comment.isDeleted() == false) {
                if (user.getUserType() == 2) {
                    adminDel = "<a onclick=\"deletecomment('" + comment.getId() + "');\">(Удалить комментарий)</a>";
                }
                User userComm = UserManager.getUserById(comment.getIdOwn()).get();
                commentshtml += "<li>" + userComm.getUsername() + " - <i class=\"fa-solid fa-star\" style=\"color: #d49953;\" aria-hidden=\"true\"></i>" + comment.getRaiting() + " - " + comment.getBody() + " " + adminDel + " </li>\n";
                raiting += comment.getRaiting();
                count++;
            }
        }
        if (count != 0) {raiting = raiting/count;} else {commentshtml="<li>Комментариев нет</li>";}
        if (auto == true) {
            if (SavedManager.getSaved(user.getId(),book.getId(),"saved",false).isPresent()) {
                myPolkaBTN += "<button type=\"submit\" onclick=\"mypolka(" + book.getId() + ",false,'saved');\" class=\"btn btn-danger\">Удалить из избранного</button>&nbsp;";
            }
            else {
                myPolkaBTN += "<button type=\"submit\" onclick=\"mypolka(" + book.getId() + ",true,'saved');\" class=\"btn btn-warning\">В избранное</button>&nbsp;";
            }

            if (SavedManager.getSaved(user.getId(),book.getId(),"wasread",false).isPresent()) {
                myPolkaBTN += "<button type=\"submit\" onclick=\"mypolka(" + book.getId() + ",false,'wasread');\" class=\"btn btn-danger\">Удалить из прочитанного</button>&nbsp;";
            }
            else {
                myPolkaBTN += "<button type=\"submit\" onclick=\"mypolka(" + book.getId() + ",true,'wasread');\" class=\"btn btn-warning\">Прочитано</button>&nbsp;";
            }

            if (SavedManager.getSaved(user.getId(),book.getId(),"readitlater",false).isPresent()) {
                myPolkaBTN += "<button type=\"submit\" onclick=\"mypolka(" + book.getId() + ",false,'readitlater');\" class=\"btn btn-danger\">Удалить из прочитать позже</button>&nbsp;";
            }
            else {
                myPolkaBTN += "<button type=\"submit\" onclick=\"mypolka(" + book.getId() + ",true,'readitlater');\" class=\"btn btn-warning\">Прочитать позже</button>&nbsp;";
            }
            if (SavedManager.getSaved(user.getId(),book.getId(),"buy",false).isPresent()) {
                myPolkaBTN += "<button type=\"submit\" onclick=\"window.location.href='../readbook/"+book.getId()+"&1';\" class=\"btn btn-success\">Читать книгу</button>&nbsp;";
            }
            else {
                myPolkaBTN += "<button type=\"submit\" onclick=\"window.location.href='../buybook/"+book.getId()+"';\" class=\"btn btn-warning\">Купить за "+book.getPrice()+"</button>&nbsp;";
            }
            String listOption = "";
            List<Compilation> compilations = CompilationManager.getCompByIdOwn(user.getId());
            for (Compilation compilation : compilations) {
                listOption += "<option value=\""+compilation.getId()+"\">"+compilation.getName()+"</option>\n";
            }
            myPolkaBTN += "<button type=\"button\" data-toggle=\"modal\" data-target=\"#compilationmodal\" class=\"btn btn-warning\">Добавить в подборку</button>";
            myPolkaBTN += "<div class=\"modal fade\" id=\"compilationmodal\" tabindex=\"-1\" role=\"dialog\" aria-hidden=\"true\">\n" +
                    "  <div class=\"modal-dialog\" role=\"document\">\n" +
                    "    <div class=\"modal-content\">\n" +
                    "      <div class=\"modal-header\">\n" +
                    "        <h5 class=\"modal-title\" id=\"exampleModalLabel\">Добавление книги в подборку</h5>\n" +
                    "        <button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-label=\"Close\">\n" +
                    "          <span aria-hidden=\"true\">&times;</span>\n" +
                    "        </button>\n" +
                    "      </div>\n" +
                    "      <div class=\"modal-body\">\n" +
                    "<div class=\"form-group\">\n" +
                    "    <label for=\"exampleFormControlSelect1\">Выбор подборки</label>\n" +
                    "    <select onchange=\"if (this.value == 'new') {document.getElementById('compnamegroup').style.display='';document.getElementById('compilationmygroup').style.display='none';document.getElementById('compilationmydelgroup').style.display='none';}if (this.value == 'current') {document.getElementById('compnamegroup').style.display='none';document.getElementById('compilationmygroup').style.display='';document.getElementById('compilationmydelgroup').style.display='';}\" class=\"form-control\" id=\"compilation\">\n" +
                    "      <option selected disabled value=\"none\">Выбрать...</option>\n" +
                    "      <option value=\"new\">Создать новую</option>\n" +
                    "      <option value=\"current\">Выбрать существующую</option>\n" +
                    "    </select>\n" +
                    "  </div>" +
                    "<div style=\"display:none;\" id=\"compnamegroup\" class=\"form-group\">\n" +
                    "    <label for=\"exampleFormControlInput1\">Имя подборки</label>\n" +
                    "    <input type=\"text\" class=\"form-control\" id=\"compname\">\n" +
                    "  </div>" +
                    "<div style=\"display:none;\" id=\"compilationmygroup\" class=\"form-group\">\n" +
                    "    <label for=\"exampleFormControlSelect1\">Выбор вашей подборки</label>\n" +
                    "    <select class=\"form-control\" id=\"compilationmy\">\n" +
                    "      <option selected disabled value=\"none\">Выбрать...</option>\n" + listOption +
                    "    </select>\n" +
                    "  </div>" +
                    "<div style=\"display:none;\" id=\"compilationmydelgroup\" class=\"form-group\">\n" +
                    "    <label for=\"exampleFormControlSelect1\">Выбор действия</label>\n" +
                    "    <select class=\"form-control\" id=\"compilationmydel\">\n" +
                    "      <option selected disabled value=\"none\">Выбрать...</option>\n" +
                    "      <option value=\"delete\">Удалить</option>\n" +
                    "      <option value=\"create\">Добавить</option>\n" +
                    "    </select>\n" +
                    "  </div>" +
                    "      </div>\n" +
                    "      <div class=\"modal-footer\">\n" +
                    "        <button type=\"button\" class=\"btn btn-secondary\" data-dismiss=\"modal\">Закрыть</button>\n" +
                    "        <button type=\"button\" onclick=\"createcomp('"+book.getId()+"');\" class=\"btn btn-warning\">Добавить книгу</button>\n" +
                    "      </div>\n" +
                    "    </div>\n" +
                    "  </div>\n" +
                    "</div>";
        }
        return "<div class=\"row p-3\">\n" +
                "            <div class=\"col-md-4\">\n" +
                "                <img src=\"/uploads/" + fileImg.getGenName() + "." + fileImg.getType() + "\" class=\"img-fluid rounded-start\" alt=\"Book Cover\">\n" +
                "            </div>\n" +
                "            <div class=\"col-md-8\">\n" +
                "                <div class=\"card-body\">\n" +
                "                    <h5 class=\"card-title\">"+book.getName()+"</h5>\n" +
                "                    <p class=\"card-text\"><strong>Автор:</strong> "+book.getAuthor()+"</p>\n" +
                "                    <p class=\"card-text\"><strong>Издательство:</strong> "+book.getTitle()+"</p>\n" +
                "                    <p class=\"card-text\"><strong>Цена:</strong> "+book.getPrice()+"</p>\n" +
                "                    \n" +
                "                    <div class=\"my-3\">\n" +
                "                        <span>Рейтинг книги:</span>\n" + getRating(raiting) + " (" + raiting + ")" +
                "                    </div>\n" + myPolkaBTN +
                "                    <!-- Форма для оставления отзыва -->\n" +
                "                        <div class=\"mb-3\">\n" +
                "                            <label for=\"review\" id=\"echo\" class=\"form-label\">Оставьте ваш отзыв</label>\n" +
                "                            <textarea class=\"form-control\" id=\"review\" name=\"review\" rows=\"3\"></textarea>\n" +
                "                        </div>\n" +
                "<label class=\"form-label\">Оцените книгу:</label>" +
                "<div class=\"d-flex flex-row justify-content-between\">" +
                "<div class=\"form-check\">\n" +
                "            <input class=\"form-check-input\" type=\"radio\" name=\"userraiting\" id=\"userraiting1\" value=\"1\">\n" +
                "            <label class=\"form-check-label\" for=\"flexRadioUser\">\n" +
                "                1\n" +
                "            </label>\n" +
                "        </div>" +
                "<div class=\"form-check\">\n" +
                "            <input class=\"form-check-input\" type=\"radio\" name=\"userraiting\" id=\"userraiting2\" value=\"2\">\n" +
                "            <label class=\"form-check-label\" for=\"flexRadioUser\">\n" +
                "                2\n" +
                "            </label>\n" +
                "        </div>" +
                "<div class=\"form-check\">\n" +
                "            <input class=\"form-check-input\" type=\"radio\" name=\"userraiting\" id=\"userraiting3\" value=\"3\">\n" +
                "            <label class=\"form-check-label\" for=\"flexRadioUser\">\n" +
                "                3\n" +
                "            </label>\n" +
                "        </div>" +
                "<div class=\"form-check\">\n" +
                "            <input class=\"form-check-input\" type=\"radio\" name=\"userraiting\" id=\"userraiting4\"  value=\"4\">\n" +
                "            <label class=\"form-check-label\" for=\"flexRadioUser\">\n" +
                "                4\n" +
                "            </label>\n" +
                "        </div>" +
                "<div class=\"form-check\">\n" +
                "            <input class=\"form-check-input\" type=\"radio\" name=\"userraiting\" id=\"userraiting5\" value=\"5\">\n" +
                "            <label class=\"form-check-label\" for=\"flexRadioUser\">\n" +
                "                5\n" +
                "            </label>\n" +
                "        </div>" +
                "        </div>" +
                "                        <button type=\"submit\" onclick=\"uploadComment("+book.getId()+");\" class=\"btn btn-warning\">Отправить</button>\n" +
                "                    <!-- Список оценок -->\n" +
                "                    <div class=\"my-3\">\n" +
                "                        <h5>Оценки этой книги:</h5>\n" +
                "                        <ul id=\"rewpole\">\n" + commentshtml +
                "                        </ul>\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "        </div>";
    }
    public static String getRating(Float rating) {
        // Определяем количество полных звезд
        int fullStars = rating.intValue();

        // Проверяем, нужна ли полузвезда
        int halfStars = (rating - fullStars >= 0.5) ? 1 : 0;

        // Подсчитываем количество пустых звезд
        int emptyStars = 5 - fullStars - halfStars;

        // Создаем объект StringBuilder для эффективной работы со строками
        StringBuilder ratingStars = new StringBuilder();

        // Добавляем полные звезды
        for(int i = 0; i < fullStars; i++) {
            ratingStars.append("<i class=\"fa-solid fa-star\" style=\"color: #d49953;\"></i>");
        }

        // Добавляем полузвезду, если нужно
        if (halfStars == 1) {
            ratingStars.append("<i class=\"fa-solid fa-star-half-stroke\" style=\"color: #d49953;\"></i>");
        }

        // Добавляем пустые звезды
        for(int i = 0; i < emptyStars; i++) {
            ratingStars.append("<i class=\"fa-regular fa-star\" style=\"color: #d49953;\"></i>");
        }

        return ratingStars.toString();
    }

    public static String getLkSaved(Long idOwn, String saved) {
        String respBook = "";
        List<Saved> savedbooks = SavedManager.getMyBooks(idOwn,saved,false);
        switch(saved) {
            case "saved":
                for (Saved savedb : savedbooks) {
                    Book book = BookManager.getBookById(savedb.getIdBook()).get();
                    if (book.getStatus().equals("PUBLISH")) {
                        File fileImg = FileManager.getFileById(book.getIdimg()).get();
                        respBook += "    <a href=\"../book/" + book.getId() + "\" class=\"card\">\n" +
                                "        <img src=\"../uploads/" + fileImg.getGenName() + "." + fileImg.getType() + "\" alt=\"img\">\n" +
                                "        <div class=\"card-info\">\n" +
                                "            <h3>" + book.getName() + "</h3>\n" +
                                "            <p>" + book.getAuthor() + "</p>\n" +
                                "        </div>\n" +
                                "    </a>\n";
                    }
                }
                return "<select onchange=\"window.location.href = '../my/' + this.value\" name=\"option\">\n" +
                        "    <option selected=\"\" value=\"saved\">Избранное</option>\n" +
                        "    <option value=\"wasread\">Прочитано</option>\n" +
                        "    <option value=\"readitlater\">Прочитать позже</option>\n" +
                        "    <option value=\"buy\">Купленные книги</option>\n" +
                        "</select><br>" + respBook;
            case "wasread":
                for (Saved savedb : savedbooks) {
                    Book book = BookManager.getBookById(savedb.getIdBook()).get();
                    if (book.getStatus().equals("PUBLISH")) {
                        File fileImg = FileManager.getFileById(book.getIdimg()).get();
                        respBook += "    <a href=\"../book/" + book.getId() + "\" class=\"card\">\n" +
                                "        <img src=\"../uploads/" + fileImg.getGenName() + "." + fileImg.getType() + "\" alt=\"img\">\n" +
                                "        <div class=\"card-info\">\n" +
                                "            <h3>" + book.getName() + "</h3>\n" +
                                "            <p>" + book.getAuthor() + "</p>\n" +
                                "        </div>\n" +
                                "    </a>\n";
                    }
                }
                return "<select onchange=\"window.location.href = '../my/' + this.value\" name=\"option\">\n" +
                        "    <option value=\"saved\">Избранное</option>\n" +
                        "    <option selected=\"\" value=\"wasread\">Прочитано</option>\n" +
                        "    <option value=\"readitlater\">Прочитать позже</option>\n" +
                        "    <option value=\"buy\">Купленные книги</option>\n" +
                        "</select><br>" + respBook;
            case "readitlater":
                for (Saved savedb : savedbooks) {
                    Book book = BookManager.getBookById(savedb.getIdBook()).get();
                    if (book.getStatus().equals("PUBLISH")) {
                        File fileImg = FileManager.getFileById(book.getIdimg()).get();
                        respBook += "    <a href=\"../book/" + book.getId() + "\" class=\"card\">\n" +
                                "        <img src=\"../uploads/" + fileImg.getGenName() + "." + fileImg.getType() + "\" alt=\"img\">\n" +
                                "        <div class=\"card-info\">\n" +
                                "            <h3>" + book.getName() + "</h3>\n" +
                                "            <p>" + book.getAuthor() + "</p>\n" +
                                "        </div>\n" +
                                "    </a>\n";
                    }
                }
                return "<select onchange=\"window.location.href = '../my/' + this.value\" name=\"option\">\n" +
                        "    <option value=\"saved\">Избранное</option>\n" +
                        "    <option value=\"wasread\">Прочитано</option>\n" +
                        "    <option selected=\"\" value=\"readitlater\">Прочитать позже</option>\n" +
                        "    <option value=\"buy\">Купленные книги</option>\n" +
                        "</select><br>" + respBook;
            case "buy":
                for (Saved savedb : savedbooks) {
                    Book book = BookManager.getBookById(savedb.getIdBook()).get();
                    if (book.getStatus().equals("PUBLISH")) {
                        File fileImg = FileManager.getFileById(book.getIdimg()).get();
                        respBook += "    <a href=\"../book/" + book.getId() + "\" class=\"card\">\n" +
                                "        <img src=\"../uploads/" + fileImg.getGenName() + "." + fileImg.getType() + "\" alt=\"img\">\n" +
                                "        <div class=\"card-info\">\n" +
                                "            <h3>" + book.getName() + "</h3>\n" +
                                "            <p>" + book.getAuthor() + "</p>\n" +
                                "        </div>\n" +
                                "    </a>\n";
                    }
                }
                return "<select onchange=\"window.location.href = '../my/' + this.value\" name=\"option\">\n" +
                        "    <option value=\"saved\">Избранное</option>\n" +
                        "    <option value=\"wasread\">Прочитано</option>\n" +
                        "    <option value=\"readitlater\">Прочитать позже</option>\n" +
                        "    <option selected=\"\" value=\"buy\">Купленные книги</option>\n" +
                        "</select><br>" + respBook;
            default:
                break;
        }
        return "ok";
    }

    public static String getLkBooklist(Long idBookList) {
        String respBook = "";
        respBook += "<h2>"+CompilationManager.getCompById(idBookList).get().getName()+"</h2><br>";
        List<CompilationPart> compilationParts = CompilationPartManager.getByIdComp(idBookList);
        for (CompilationPart compilationPart : compilationParts) {
            if (compilationPart.isDeleted() == false) {
                Book book = BookManager.getBookById(compilationPart.getIdbook()).get();
                if (book.getStatus().equals("PUBLISH")) {
                    File fileImg = FileManager.getFileById(book.getIdimg()).get();
                    respBook += "    <a href=\"../book/" + book.getId() + "\" class=\"card\">\n" +
                            "        <img src=\"../uploads/" + fileImg.getGenName() + "." + fileImg.getType() + "\" alt=\"img\">\n" +
                            "        <div class=\"card-info\">\n" +
                            "            <h3>" + book.getName() + "</h3>\n" +
                            "            <p>" + book.getAuthor() + "</p>\n" +
                            "        </div>\n" +
                            "    </a>\n";
                }
            }
        }
        return respBook;
    }

    public static String getLkSupport(User user) {
        return "<div class = \"container reg_form2\" id = \"form2\">\n" +
                "    <div class=\"row justify-content-center\">\n" +
                "        <h2 id=\"echo\">Отправить письмо в поддержку</h2>\n" +
                "        <div class =\"w-100\"></div>\n" +
                "        <div class=\"mb-3 col-12 col-md-4\">\n" +
                "            <p for=\"InputUserName\" class=\"form-label\">Ваша проблема:</p>\n" +
                "            <textarea type=\"text\" id=\"userbroblem\" class=\"form-control\" name=\"InputPassword1\"></textarea>\n" +
                "        </div>\n" +
                "        <div class =\"w-100\"></div>\n" +
                "        <div class=\"mb-3 col-12 col-md-4\">\n" +
                "            <button type=\"login \" class=\"btn btn-warning\" id=\"InputSubmitProblem\">Написать в поддержку!</button>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</div>";
    }

    public static boolean isValidCreditCardNumber(String cardNumber) {
        if (cardNumber == null) {
            return false;
        }

        if (!cardNumber.matches("\\d{16}")) {
            return false; // Проверяем, что в строке ровно 16 цифр
        }

        int sum = 0;
        boolean alternate = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(cardNumber.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }
    public static boolean isValidCVV(String cvv) {
            return cvv.matches("\\d{3}");
    }

    public static boolean isValid(String date) {
        if (date == null || date.length() != 5) {
            return false;
        }

        String monthString = date.substring(0, 2);
        String yearString = date.substring(3);

        if (!isInteger(monthString) || !isInteger(yearString)) {
            return false;
        }

        int month = Integer.parseInt(monthString);
        int year = Integer.parseInt(yearString);

        if (month < 1 || month > 12 || year < 0) {
            return false;
        }

        return true;
    }

    private static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String getLkAdminBooks() {
        List<Book> books = BookManager.readAllByStatus("NEW");
        String respBook = "";
        String respBookModal = "";
        String htmlBook = "<div class=\"card-container\">\n";
        int Count = 0;
        for (Book book : books) {
            String dsbl1 = "";
            String dsbl2 = "";
            String dsbl3 = "";
            String dsbl4 = "";
            switch (book.getGenre()) {
                case 1:
                    dsbl1 = "selected";
                    break;
                case 2:
                    dsbl2 = "selected";
                    break;
                case 3:
                    dsbl3 = "selected";
                    break;
                case 4:
                    dsbl4 = "selected";
                    break;
            }
            File fileImg = FileManager.getFileById(book.getIdimg()).get();
            respBook += "    <a data-toggle=\"modal\" data-target=\"#modalbook"+book.getId()+"\" class=\"card\">\n" +
                    "        <img src=\"/uploads/" + fileImg.getGenName() + "." + fileImg.getType() + "\" alt=\"img\">\n" +
                    "        <div class=\"card-info\">\n" +
                    "            <h3>" + book.getName() + "</h3>\n" +
                    "            <p>" + book.getAuthor() + "</p>\n" +
                    "        </div>\n" +
                    "    </a>\n";
            respBookModal+="<div class=\"modal fade\" id=\"modalbook"+book.getId()+"\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"exampleModalLabel\" aria-hidden=\"true\">\n" +
                    "  <div class=\"modal-dialog\" role=\"document\">\n" +
                    "    <div class=\"modal-content\">\n" +
                    "      <div class=\"modal-header\">\n" +
                    "        <h5 class=\"modal-title\" id=\"exampleModalLabel\">Проверка книги</h5>\n" +
                    "        <button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-label=\"Close\">\n" +
                    "          <span aria-hidden=\"true\">&times;</span>\n" +
                    "        </button>\n" +
                    "      </div>\n" +
                    "      <div class=\"modal-body\">\n" +
                    "<div class = \"container reg_form2\" id = \"form2\">\n" +
                    "    <div class=\"row justify-content-center\">\n" +
                    "        <div class=\"col\">\n" +
                    "            <p for=\"InputUserName\" class=\"form-label\">Название</p>\n" +
                    "            <input type=\"text\" id=\"name\" class=\"form-control\" value=\""+book.getName()+"\" disabled name=\"InputUserName\">\n" +
                    "        </div>\n" +
                    "        <div class =\"w-100\"></div>\n" +
                    "        <div class=\"col\">\n" +
                    "            <p for=\"InputPassword1\" class=\"form-label\">Автор</p>\n" +
                    "            <input type=\"text\" id=\"author\" class=\"form-control\" disabled value=\""+book.getAuthor()+"\" name=\"InputPassword1\">\n" +
                    "        </div>\n" +
                    "        <div class =\"w-100\"></div>\n" +
                    "        <div class=\"col\">\n" +
                    "            <p for=\"InputPassword1\" class=\"form-label\">Жанр</p>\n" +
                    "<select id=\"genre\" class=\"form-select\">\n" +
                    "  <option "+dsbl1+" value=\"1\">Ужасы</option>\n" +
                    "  <option "+dsbl2+" value=\"2\">Мистика</option>\n" +
                    "  <option "+dsbl3+" value=\"3\">Фантастика</option>\n" +
                    "  <option "+dsbl4+" value=\"4\">Классика</option>\n" +
                    "</select>" +
                    "        </div>\n" +
                    "        <div class =\"w-100\"></div>\n" +
                    "        <div class=\"col\">\n" +
                    "            <p for=\"InputPassword1\" class=\"form-label\">Издательство</p>\n" +
                    "            <input type=\"text\" id=\"writer\" value=\""+book.getTitle()+"\" class=\"form-control\" name=\"InputPassword1\" disabled>\n" +
                    "        </div>\n" +
                    "        <div class =\"w-100\"></div>\n" +
                    "        <div class=\"col\">\n" +
                    "            <p for=\"InputPassword1\" class=\"form-label\">Описание</p>\n" +
                    "            <textarea type=\"text\" id=\"aboutbook\" class=\"form-control\" name=\"InputPassword1\" disabled>"+book.getAbout()+"</textarea>\n" +
                    "        </div>\n" +
                    "        <div class =\"w-100\"></div>\n" +
                    "        <div class=\"col\">\n" +
                    "            <p for=\"InputPassword1\" class=\"form-label\">Цена</p>\n" +
                    "            <input type=\"number\" id=\"price\" class=\"form-control\" value=\""+book.getPrice()+"\" name=\"InputPassword1\" disabled>\n" +
                    "        </div>\n" +
                    "    </div>\n" +
                    "</div>" +
                    "      </div>\n" +
                    "      <div class=\"modal-footer\">\n" +
                    "        <button type=\"button\" onclick=\"regbook("+book.getId()+",false);\" class=\"btn btn-secondary\">Отклонить</button>\n" +
                    "        <button type=\"button\" onclick=\"regbook("+book.getId()+",true);\" class=\"btn btn-warning\">Принять</button>\n" +
                    "      </div>\n" +
                    "    </div>\n" +
                    "  </div>\n" +
                    "</div>";
            Count++;
        }
        respBook = htmlBook + respBook + respBookModal + "</div>";
        if (Count == 0) {respBook = "Нет книг на проверке.";}
        return respBook;
    }

    public static String getLkAdminSupport() {
        List<Question> questions = QuestionManager.getUserCommByIdComm("INADMIN");
        String respBook = "";
        String respBookModal = "";
        String htmlBook = "<div class=\"card-container\">\n";
        int Count = 0;
        for (Question question : questions) {
            User userquestion = UserManager.getUserById(question.getIdOwn()).get();
            respBook += "    <a data-toggle=\"modal\" data-target=\"#modalbook"+question.getId()+"\" class=\"card\">\n" +
                    "        <img src=\"/img/question.jpg\" alt=\"img\">\n" +
                    "        <div class=\"card-info\">\n" +
                    "            <p>Нажмите, чтобы написать пользоввателю</p>\n" +
                    "        </div>\n" +
                    "    </a>\n";
            respBookModal+="<div class=\"modal fade\" id=\"modalbook"+question.getId()+"\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"exampleModalLabel\" aria-hidden=\"true\">\n" +
                    "  <div class=\"modal-dialog\" role=\"document\">\n" +
                    "    <div class=\"modal-content\">\n" +
                    "      <div class=\"modal-header\">\n" +
                    "        <h5 class=\"modal-title\" id=\"echo\">Поддержка пользователей</h5>\n" +
                    "        <button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-label=\"Close\">\n" +
                    "          <span aria-hidden=\"true\">&times;</span>\n" +
                    "        </button>\n" +
                    "      </div>\n" +
                    "      <div class=\"modal-body\">\n" +
                    "<div class = \"container reg_form2\" id = \"form2\">\n" +
                    "    <div class=\"row justify-content-center\">\n" +
                    "        <div class=\"col\">\n" +
                    "            <p for=\"InputUserName\" class=\"form-label\">Имя пользователя</p>\n" +
                    "            <input type=\"text\" class=\"form-control\" value=\""+userquestion.getUsername()+"\" disabled name=\"InputUserName\">\n" +
                    "        </div>\n" +
                    "        <div class =\"w-100\"></div>\n" +
                    "        <div class=\"col\">\n" +
                    "            <p for=\"InputPassword1\" class=\"form-label\">Почта пользователя</p>\n" +
                    "            <input type=\"text\" class=\"form-control\" disabled value=\""+userquestion.getMail()+"\" name=\"InputPassword1\">\n" +
                    "        </div>\n" +
                    "        <div class =\"w-100\"></div>\n" +
                    "        <div class=\"col\">\n" +
                    "            <p for=\"InputPassword1\" class=\"form-label\">Описание ситуации</p>\n" +
                    "            <textarea type=\"text\" class=\"form-control\" name=\"InputPassword1\" disabled>"+question.getQuestion()+"</textarea>\n" +
                    "        </div>\n" +
                    "        <div class =\"w-100\"></div>\n" +
                    "        <div class=\"col\">\n" +
                    "            <p for=\"InputPassword1\" class=\"form-label\">Ваш ответ</p>\n" +
                    "            <textarea type=\"text\" id=\"userquestionmy"+question.getId()+"\" class=\"form-control\" name=\"InputPassword1\"></textarea>\n" +
                    "        </div>\n" +
                    "    </div>\n" +
                    "</div>" +
                    "      </div>\n" +
                    "      <div class=\"modal-footer\">\n" +
                    "        <button type=\"button\" onclick=\"supportq("+question.getId()+");\" class=\"btn btn-warning\">Отправить ответ</button>\n" +
                    "      </div>\n" +
                    "    </div>\n" +
                    "  </div>\n" +
                    "</div>";
            Count++;
        }
        respBook = htmlBook + respBook + respBookModal + "</div>";
        if (Count == 0) {respBook = "Нет книг на проверке.";}
        return respBook;
    }

    public static String getLkBooksWritter(Long idOwn) {
        List<Book> books = BookManager.readAllByIdOwn(idOwn);
        String respBook = "";
        String respBookModal = "";
        String htmlBook = "<div class=\"card-container\">\n";
        int Count = 0;
        for (Book book : books) {
            String dsbl1 = "";
            String dsbl2 = "";
            String dsbl3 = "";
            String dsbl4 = "";
            switch (book.getGenre()) {
                case 1:
                    dsbl1 = "selected";
                    break;
                case 2:
                    dsbl2 = "selected";
                    break;
                case 3:
                    dsbl3 = "selected";
                    break;
                case 4:
                    dsbl4 = "selected";
                    break;
            }
            File fileImg = FileManager.getFileById(book.getIdimg()).get();
            respBook += "    <a data-toggle=\"modal\" data-target=\"#modalbook"+book.getId()+"\" class=\"card\">\n" +
                    "        <img src=\"/uploads/" + fileImg.getGenName() + "." + fileImg.getType() + "\" alt=\"img\">\n" +
                    "        <div class=\"card-info\">\n" +
                    "            <h3>" + book.getName() + "</h3>\n" +
                    "            <p>" + book.getAuthor() + "</p>\n" +
                    "        </div>\n" +
                    "    </a>\n";
            respBookModal+="<div class=\"modal fade\" id=\"modalbook"+book.getId()+"\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"exampleModalLabel\" aria-hidden=\"true\">\n" +
                    "  <div class=\"modal-dialog\" role=\"document\">\n" +
                    "    <div class=\"modal-content\">\n" +
                    "      <div class=\"modal-header\">\n" +
                    "        <h5 class=\"modal-title\" id=\"echo"+book.getId()+"\">Редактирование книги</h5>\n" +
                    "        <button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-label=\"Close\">\n" +
                    "          <span aria-hidden=\"true\">&times;</span>\n" +
                    "        </button>\n" +
                    "      </div>\n" +
                    "      <div class=\"modal-body\">\n" +
                    "<div class = \"container reg_form2\" id = \"form2\">\n" +
                    "    <div class=\"row justify-content-center\">\n" +
                    "        <div class=\"col\">\n" +
                    "            <p for=\"InputUserName\" class=\"form-label\">Название</p>\n" +
                    "            <input type=\"text\" id=\"name"+book.getId()+"\" class=\"form-control\" value=\""+book.getName()+"\" name=\"InputUserName\">\n" +
                    "        </div>\n" +
                    "        <div class =\"w-100\"></div>\n" +
                    "        <div class=\"col\">\n" +
                    "            <p for=\"InputPassword1\" class=\"form-label\">Автор</p>\n" +
                    "            <input type=\"text\" id=\"author"+book.getId()+"\" class=\"form-control\" value=\""+book.getAuthor()+"\" name=\"InputPassword1\">\n" +
                    "        </div>\n" +
                    "        <div class =\"w-100\"></div>\n" +
                    "        <div class=\"col\">\n" +
                    "            <p for=\"InputPassword1\" class=\"form-label\">Жанр</p>\n" +
                    "<select id=\"genre"+book.getId()+"\" class=\"form-select\">\n" +
                    "  <option "+dsbl1+" value=\"1\">Ужасы</option>\n" +
                    "  <option "+dsbl2+" value=\"2\">Мистика</option>\n" +
                    "  <option "+dsbl3+" value=\"3\">Фантастика</option>\n" +
                    "  <option "+dsbl4+" value=\"4\">Классика</option>\n" +
                    "</select>" +
                    "        </div>\n" +
                    "        <div class =\"w-100\"></div>\n" +
                    "        <div class=\"col\">\n" +
                    "            <p for=\"InputPassword1\" class=\"form-label\">Издательство</p>\n" +
                    "            <input type=\"text\" id=\"writer"+book.getId()+"\" value=\""+book.getTitle()+"\" class=\"form-control\" name=\"InputPassword1\">\n" +
                    "        </div>\n" +
                    "        <div class =\"w-100\"></div>\n" +
                    "        <div class=\"col\">\n" +
                    "            <p for=\"InputPassword1\" class=\"form-label\">Описание</p>\n" +
                    "            <textarea type=\"text\" id=\"aboutbook"+book.getId()+"\" class=\"form-control\" name=\"InputPassword1\">"+book.getAbout()+"</textarea>\n" +
                    "        </div>\n" +
                    "        <div class =\"w-100\"></div>\n" +
                    "        <div class=\"col\">\n" +
                    "            <p for=\"InputPassword1\" class=\"form-label\">Цена</p>\n" +
                    "            <input type=\"number\" id=\"price"+book.getId()+"\" class=\"form-control\" value=\""+book.getPrice()+"\" name=\"InputPassword1\">\n" +
                    "        </div>\n" +
                    "    </div>\n" +
                    "</div>" +
                    "      </div>\n" +
                    "      <div class=\"modal-footer\">\n" +
                    "        <button type=\"button\" onclick=\"savebook("+book.getId()+");\" class=\"btn btn-warning\">Сохранить</button>\n" +
                    "      </div>\n" +
                    "    </div>\n" +
                    "  </div>\n" +
                    "</div>";
            Count++;
        }
        respBook = htmlBook + respBook + respBookModal + "</div>";
        if (Count == 0) {respBook = "Вы ещё не загружали свои книги";}
        return respBook;
    }
}
