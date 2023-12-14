package com.example.Polka.functions;

import com.example.Polka.database.Book;
import com.example.Polka.database.Comment;
import com.example.Polka.database.File;
import com.example.Polka.database.User;
import com.example.Polka.managers.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class func {
    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
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
                return "12";
            case 1:
                return "<li><a href=\"../lk/publish\">Загрузить книгу</a></li><li>Мои книги</li><li><a href=\"../lk/my\">Избранное</a></li><li><a href=\"../api/logout\">Выход</a></li>";

        }
        return "null";
    }

    public static String getLkRecomendations() {
        List<Book> books = BookManager.readAllByRandom10();
        String respBook = "";
        String htmlBook = "<h2>Рекомендации</h2><div class=\"card-container\">\n";
        int Count = 0;
        for (Book book : books) {
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
        respBook = htmlBook + respBook + "</div>";
        if (Count == 0) {respBook = "Ничего не найдено!";}
        return respBook;
    }

    public static String getBook(Book book,Boolean auto) {
        File fileImg = FileManager.getFileById(book.getIdimg()).get();
        List<Comment> comments = CommentManager.getCommByIdBook(book.getId());
        int count = 0;
        float raiting = 0F;
        String commentshtml = "";
        String myPolkaBTN = "";
        for (Comment comment : comments) {
            User userComm = UserManager.getUserById(comment.getIdOwn()).get();
            commentshtml+="<li>"+userComm.getUsername()+" - <i class=\"fa-solid fa-star\" style=\"color: #d49953;\" aria-hidden=\"true\"></i>"+comment.getRaiting()+" - "+comment.getBody()+"</li>\n";
            raiting+=comment.getRaiting();
            count++;
        }
        if (count != 0) {raiting = raiting/count;} else {commentshtml="<li>Комментариев нет</li>";}
        if (auto == true) {
            if (SavedManager.getSaved(UserManager.getUserById(book.getIdOwn()).get().getId(),book.getId(),"saved",false).isPresent()) {
                myPolkaBTN += "<button type=\"submit\" onclick=\"mypolka(" + book.getId() + ",false,'saved');\" class=\"btn btn-danger\">Удалить из избранного</button>&nbsp;";
            }
            else {
                myPolkaBTN += "<button type=\"submit\" onclick=\"mypolka(" + book.getId() + ",true,'saved');\" class=\"btn btn-warning\">В избранное</button>&nbsp;";
            }

            if (SavedManager.getSaved(UserManager.getUserById(book.getIdOwn()).get().getId(),book.getId(),"wasread",false).isPresent()) {
                myPolkaBTN += "<button type=\"submit\" onclick=\"mypolka(" + book.getId() + ",false,'wasread');\" class=\"btn btn-danger\">Удалить из прочитанного</button>&nbsp;";
            }
            else {
                myPolkaBTN += "<button type=\"submit\" onclick=\"mypolka(" + book.getId() + ",true,'wasread');\" class=\"btn btn-warning\">Прочитано</button>&nbsp;";
            }

            if (SavedManager.getSaved(UserManager.getUserById(book.getIdOwn()).get().getId(),book.getId(),"readitlater",false).isPresent()) {
                myPolkaBTN += "<button type=\"submit\" onclick=\"mypolka(" + book.getId() + ",false,'readitlater');\" class=\"btn btn-danger\">Удалить из прочитать позже</button>";
            }
            else {
                myPolkaBTN += "<button type=\"submit\" onclick=\"mypolka(" + book.getId() + ",true,'readitlater');\" class=\"btn btn-warning\">Прочитать позже</button>";
            }
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
}
