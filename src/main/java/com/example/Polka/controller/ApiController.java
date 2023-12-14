package com.example.Polka.controller;

import com.example.Polka.database.*;
import com.example.Polka.managers.*;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.util.List;
import java.util.Optional;

import static com.example.Polka.functions.func.*;

@Controller
public class ApiController {
    @Autowired
    private UserManager userManager;
    @Autowired
    private BookManager bookManager;
    @Autowired
    private CommentManager commentManager;

    @ResponseBody
    @PostMapping("/api/register")
    public String register(HttpSession session, @RequestParam(name="username", required=false) String username, @RequestParam(name="mail", required=false) String mail, @RequestParam(name="pass", required=false) String pass, @RequestParam(name="usertype", required=false) String userType) {
        String securepass;
        mail = mail.trim();
        pass = pass.trim();
        username = username.trim();

        mail = mail.toLowerCase();

        if (mail == "" || pass == "" || username == "") {
            return "Заполните все поля!";
        }

        if (!isValidEmail(mail)) {
            return "Почта невалидна!";
        }

        Optional<User> userOptional = UserManager.getUserByMail(mail);
        User user = userOptional.orElse(null);
        if (user != null) {
            return "Такая почта уже существует!";
        }

        securepass = md5(pass);
        User registerUser = new User();
        registerUser.setMail(mail);
        registerUser.setPassword(securepass);
        registerUser.setUsername(username);
        registerUser.setTime(getCurrentTime());
        registerUser.setDeleted(false);

        switch (userType) {
            case "writer":
                registerUser.setUserType(1);
                break;
            case "reader":
                registerUser.setUserType(0);
                break;
            default:
                return "Выберите тип аккаунта";
        }
        UserManager.createUser(registerUser);
        session.setAttribute("user", mail);
        return "Аккаунт зарегистрирован!";
    }
    @ResponseBody
    @PostMapping("/api/login")
    public String ApiLogin(HttpSession session,@RequestParam(name="mail", required=false) String mail, @RequestParam(name="pass", required=false) String pass) {
        String response;

        pass = pass.trim();
        mail = mail.trim().toLowerCase();

        if (mail == "" || pass == "") {
            return "Заполните все поля!";
        }

        Optional<User> userOptional = userManager.getUserByMail(mail);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String securePass = md5(pass);

            if (mail.equals(user.getMail()) && securePass.equals(user.getPass())) {
                session.setAttribute("user", mail);
                response = "Вход выполнен!";
            } else {
                response = "Пароль неверный!";
            }
        } else {
            response = "Такого логина не существует!";
        }
        return response;
    }

    @GetMapping("/api/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @RestController
    @RequestMapping("/api/uploadbooks")
    public class FileUploadController {
        private static final String UPLOAD_DIR = "src/main/uploads/";
        @PostMapping
        public String handleFileUpload(@RequestParam(name="name") String name, @RequestParam(name="author") String author, @RequestParam("genre") int genre, @RequestParam("files") List<MultipartFile> files, HttpSession session, @RequestParam("writer") String writer, @RequestParam("aboutbook") String aboutBook, @RequestParam("price") Float price) {
            try {
                if (name.isEmpty() || author.isEmpty() || writer.isEmpty() || aboutBook.isEmpty() || price == null) {return "Заполните все поля!";}
                int Counter = 0;
                boolean isTxtFile = false;
                boolean isImgFile = false;

                for (MultipartFile file : files) {
                    String checkfile = checkFile(file);
                    if (checkfile != "ok") {return checkfile;}
                    String realFileName = file.getOriginalFilename();
                    String fileExtension = getFileExtension(realFileName);
                    if (fileExtension.equals("txt")) {isTxtFile = true;}
                    if (fileExtension.equals("png") || fileExtension.equals("jpeg") || fileExtension.equals("jpg")) {isImgFile = true;}
                    Counter++;
                }
                if (isImgFile == false || isTxtFile == false) {return "Должно быть только 2 файла - обложка и сама книга.";}
                if (Counter != 2) {return "Должно быть только 2 файла - обложка и сама книга.";}
                String mail = (String) session.getAttribute("user");
                Long idUser = userManager.getUserByMail(mail).get().getId();
                Long idBook = createBook(idUser,name,author,genre,writer,aboutBook,price);
                for (MultipartFile file : files) {
                    saveFile(file,idBook,idUser);
                }
                return "Файлы были успешно загружены!";
            } catch (IOException e) {
                e.printStackTrace();
                return "Ошибка при загрузке файлов";
            }
        }
        private void saveFile(MultipartFile file, Long idBook,Long idUser) throws IOException {
            long fileSize = file.getSize();
            String realFileName = file.getOriginalFilename();
            String genFileName = generateUniqueFileName();
            String fileExtension = getFileExtension(realFileName);
            Path filePath = Path.of(UPLOAD_DIR + genFileName + '.' + fileExtension);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            File newFile = new File();
            newFile.setRealName(realFileName);
            newFile.setGenName(genFileName);
            newFile.setTime(getCurrentTime());
            newFile.setType(fileExtension);
            newFile.setSize(fileSize);
            newFile.setIdOwn(idUser);
            newFile.setIdBook(idBook);
            newFile.setDeleted(false);
            FileManager.createFile(newFile);
            Long idFile = FileManager.createFile(newFile).getId();

            Book newbook = bookManager.getBookById(idBook).get();
            if (fileExtension.equals("txt")) {
                newbook.setIdfile(idFile);
            }
            if (fileExtension.equals("png") || fileExtension.equals("jpeg") || fileExtension.equals("jpg")) {
                newbook.setIdimg(idFile);
            }
            BookManager.createBook(newbook);
        }
        private String checkFile(MultipartFile file) throws IOException {
            long fileSize = file.getSize();
            String realFileName = file.getOriginalFilename();
            String fileExtension = getFileExtension(realFileName);
            if (realFileName.length() > 30) {return "Файл " + realFileName + " имеет слишком длинное имя!";}
            if (canUploadFile(fileExtension) == false) {return "Тип файла " + fileExtension + " загружать нельзя!";}
            if (fileSize > 52428800) {return "Файл " + realFileName + " превышает допустимый размер.";}
            return "ok";
        }
        private Long createBook(Long idOwn, String name, String author,int genre, String writer, String aboutBook, Float price) {
            Book newBook = new Book();
            newBook.setAbout(aboutBook);
            newBook.setGenre(genre);
            newBook.setIdfile(0l);
            newBook.setIdimg(0l);
            newBook.setIdOwn(idOwn);
            newBook.setName(name);
            newBook.setPrice(price);
            newBook.setStatus("NEW");
            newBook.setTitle(writer);
            newBook.setAuthor(author);
            BookManager.createBook(newBook);
            return BookManager.createBook(newBook).getId();
        }
    }
    @ResponseBody
    @PostMapping("/api/createcomment")
    public String ApiLogin(HttpSession session,@RequestParam(name="review", required=false) String review, @RequestParam(name="userraiting", required=false) Long userraiting, @RequestParam(name="idbook", required=false) Long idBook) {
        if (review.trim() == "" || userraiting == null) {return "Заполните все поля!";}
        if (session.getAttribute("user") == null) {
            return "Авторизируйтесь или зарегистрируйтесь, чтобы оставить комментарий!";
        }
        Optional<Comment> userOptional = commentManager.getUserCommById(userManager.getUserByMail((String) session.getAttribute("user")).get().getId(),idBook);
        if (userOptional.isPresent()) {
            return "Вы уже оставляли свой комментарий на эту книгу!";
        }
        Comment newcomment = new Comment();
        newcomment.setBody(review);
        newcomment.setRaiting(userraiting);
        newcomment.setTime(getCurrentTime());
        newcomment.setIdOwn(userManager.getUserByMail((String) session.getAttribute("user")).get().getId());
        newcomment.setIdBook(idBook);
        newcomment.setDeleted(false);
        CommentManager.createComment(newcomment);
        return "ok";
    }

    @ResponseBody
    @PostMapping("/api/createsaved")
    public String ApiCreateSaved(HttpSession session,@RequestParam(name="idbook", required=false) Long idBook, @RequestParam(name="boolean", required=false) Boolean resolution, @RequestParam(name="res", required=false) String saved) {
        switch(saved) {
            case "saved":
                if (resolution == true) {
                    if (!(SavedManager.getSaved(userManager.getUserByMail((String) session.getAttribute("user")).get().getId(),idBook,"saved",false).isPresent())) {
                        Saved newsaved = new Saved();
                        newsaved.setIdOwn(userManager.getUserByMail((String) session.getAttribute("user")).get().getId());
                        newsaved.setIdBook(idBook);
                        newsaved.setTime(getCurrentTime());
                        newsaved.setStatus("saved");
                        newsaved.setDeleted(false);
                        SavedManager.createSaved(newsaved);
                    }
                }
                else
                {
                    if (SavedManager.getSaved(userManager.getUserByMail((String) session.getAttribute("user")).get().getId(),idBook,"saved",false).isPresent()) {
                        Saved newsaved = SavedManager.getSaved(userManager.getUserByMail((String) session.getAttribute("user")).get().getId(),idBook,"saved",false).get();
                        newsaved.setDeleted(true);
                        SavedManager.createSaved(newsaved);
                    }
                }
                break;
            case "wasread":
                if (resolution == true) {
                    if (!(SavedManager.getSaved(userManager.getUserByMail((String) session.getAttribute("user")).get().getId(),idBook,"wasread",false).isPresent())) {
                        Saved newsaved = new Saved();
                        newsaved.setIdOwn(userManager.getUserByMail((String) session.getAttribute("user")).get().getId());
                        newsaved.setIdBook(idBook);
                        newsaved.setTime(getCurrentTime());
                        newsaved.setStatus("wasread");
                        newsaved.setDeleted(false);
                        SavedManager.createSaved(newsaved);
                    }
                }
                else
                {
                    if (SavedManager.getSaved(userManager.getUserByMail((String) session.getAttribute("user")).get().getId(),idBook,"wasread",false).isPresent()) {
                        Saved newsaved = SavedManager.getSaved(userManager.getUserByMail((String) session.getAttribute("user")).get().getId(),idBook,"wasread",false).get();
                        newsaved.setDeleted(true);
                        SavedManager.createSaved(newsaved);
                    }
                }
                break;
            case "readitlater":
                if (resolution == true) {
                    if (!(SavedManager.getSaved(userManager.getUserByMail((String) session.getAttribute("user")).get().getId(),idBook,"readitlater",false).isPresent())) {
                        Saved newsaved = new Saved();
                        newsaved.setIdOwn(userManager.getUserByMail((String) session.getAttribute("user")).get().getId());
                        newsaved.setIdBook(idBook);
                        newsaved.setTime(getCurrentTime());
                        newsaved.setStatus("readitlater");
                        newsaved.setDeleted(false);
                        SavedManager.createSaved(newsaved);
                    }
                }
                else
                {
                    if (SavedManager.getSaved(userManager.getUserByMail((String) session.getAttribute("user")).get().getId(),idBook,"readitlater",false).isPresent()) {
                        Saved newsaved = SavedManager.getSaved(userManager.getUserByMail((String) session.getAttribute("user")).get().getId(),idBook,"readitlater",false).get();
                        newsaved.setDeleted(true);
                        SavedManager.createSaved(newsaved);
                    }
                }
                break;
        }
        return "ok";
    }


    @ResponseBody
    @PostMapping("/api/createcomp")
    public String ApiCreateCompilation(HttpSession session,@RequestParam(name="idbook", required=false) Long idBook, @RequestParam(name="create", required=false) String create, @RequestParam(name="compnamegroup", required=false) String compnamegroup, @RequestParam(name="compilationmydel", required=false) String compilationmydel,@RequestParam(name="idcomp", required=false) Long idcomp) {
        switch(create) {
            case "new":
                if (compnamegroup.trim() == "") {return "Заполните все поля!";}
                Compilation compilation = new Compilation();
                compilation.setName(compnamegroup);
                compilation.setTime(getCurrentTime());
                compilation.setIdOwn(userManager.getUserByMail((String) session.getAttribute("user")).get().getId());
                compilation.setDeleted(false);
                CompilationManager.createCompilation(compilation);

                Long idCompil = CompilationManager.createCompilation(compilation).getId();

                CompilationPart compilationPart = new CompilationPart();
                compilationPart.setIdbook(idBook);
                compilationPart.setIdCompilation(idCompil);
                compilationPart.setTime(getCurrentTime());
                compilationPart.setDeleted(false);
                CompilationPartManager.createCompilationPart(compilationPart);
                return "Вы создали подборку \"" +compnamegroup+ "\". Она доступна всем по ссылке: <a target=\"_blank\" href=\"../booklist/"+idCompil+"\">Ссылка на подборку</a>";
            case "current":
                switch (compilationmydel) {
                    case "create":
                        if (CompilationPartManager.getPart(idBook,idcomp,false).isPresent()) {
                            return "Такая книга в этой подборке уже существует!";
                        }
                        else {
                            CompilationPart compilationPartCreate = new CompilationPart();
                            compilationPartCreate.setIdbook(idBook);
                            compilationPartCreate.setIdCompilation(idcomp);
                            compilationPartCreate.setTime(getCurrentTime());
                            compilationPartCreate.setDeleted(false);
                            CompilationPartManager.createCompilationPart(compilationPartCreate);
                            return "Вы успешно добавили книгу в подборку!";
                        }
                    case "delete":
                        if (!(CompilationPartManager.getPart(idBook,idcomp,false).isPresent())) {
                            return "Такой подборки не существует!";
                        }
                        else {
                            CompilationPart compilationPartCreate = CompilationPartManager.getPart(idBook,idcomp,false).get();
                            compilationPartCreate.setDeleted(true);
                            CompilationPartManager.createCompilationPart(compilationPartCreate);
                            return "Книга из подборки успешно удалена!";
                        }
                }
                break;
        }
        return "ok";
    }
    @ResponseBody
    @PostMapping("/api/createquestion")
    public String ApiCreateCompilation(HttpSession session,@RequestParam(name="problem", required=false) String problem) {
        if (problem.trim() == "") {return "Заполните поле для вопроса!";}
        Question question = new Question();
        question.setQuestion(problem);
        question.setTime(getCurrentTime());
        question.setResponse("");
        question.setIdOwn(userManager.getUserByMail((String) session.getAttribute("user")).get().getId());
        question.setStatus("INADMIN");
        QuestionManager.createQuestion(question);
        return "ok";
    }

    @ResponseBody
    @PostMapping("/api/createbuycheck")
    public String ApiCreateBuyCheck(HttpSession session,@RequestParam(name="name", required=false) String name,@RequestParam(name="cardnum", required=false) String cardnum,@RequestParam(name="dateend", required=false) String dateend,@RequestParam(name="cvccvv", required=false) String cvccvv) {
        if (name.trim() == "" || cardnum.trim() == "" || dateend.trim() == "" || cvccvv.trim() == "") {return "2";}
        if (!isValidCreditCardNumber(cardnum)) {return "1";}
        if (!isValidCVV(cvccvv)) {return "4";}
        if (!isValid(dateend)) {return "3";}

        String gencode = generateRandomStringNum(6);

        Order order = new Order();
        order.setIdOwn(userManager.getUserByMail((String) session.getAttribute("user")).get().getId());
        order.setIdBook((Long) session.getAttribute("book"));
        order.setPrice(BookManager.getBookById((Long) session.getAttribute("book")).get().getPrice());
        order.setStatus("INPROGRESS");
        order.setCode(Long.valueOf(gencode));
        OrderManager.createOrder(order);

        session.setAttribute("code", gencode);
        session.setAttribute("time", getCurrentTime());
        session.setAttribute("gencodeAttempts", 0);
        session.setAttribute("orderid", OrderManager.createOrder(order).getId());

        MailController.sendEmail((String) session.getAttribute("user"),"Код оплаты","Ваш код для оплаты покупки: " + gencode);
        return "5";
    }
    @ResponseBody
    @PostMapping("/api/createbuycheckcode")
    public String ApiCreateBuyCheckCode(HttpSession session,@RequestParam(name="code", required=false) String code) {
        if ((int) session.getAttribute("gencodeAttempts") >= 3) {return "Вы исчерпали лимит попыток.";}
        if (code.trim() == "") {return "Заполните поле!";}
        Long currentTime = getCurrentTime();
        Long oldTime = (Long) session.getAttribute("time");

        if (currentTime-oldTime > 300) {return "Время ввода кода вышло!";}

        if (!code.equals((String) session.getAttribute("code"))) {
            session.setAttribute("gencodeAttempts", (int) session.getAttribute("gencodeAttempts")+1);
            int Attempts = 3 - (int) session.getAttribute("gencodeAttempts");
            return "Код неверный! (Осталось " + Attempts + " попытки(а))";
        }
        else {
            Order order = OrderManager.getOrderById((Long) session.getAttribute("orderid")).get();
            order.setStatus("SUCCESS");
            OrderManager.createOrder(order);

            Saved ordersaved = new Saved();
            ordersaved.setIdOwn(userManager.getUserByMail((String) session.getAttribute("user")).get().getId());
            ordersaved.setIdBook((Long) session.getAttribute("book"));
            ordersaved.setTime(getCurrentTime());
            ordersaved.setStatus("buy");
            ordersaved.setDeleted(false);
            SavedManager.createSaved(ordersaved);

            Book redirectbook = BookManager.getBookById((Long) session.getAttribute("book")).get();
            return "Покупка совершена успешно! Редирект на страницу товара...<meta http-equiv=\"refresh\" content=\"0; url=../book/"+redirectbook.getId()+"\" />";
        }
    }
    @ResponseBody
    @GetMapping("/api/deletenonactiveusers")
    public String ApiCreateBuyCheckCode() {
        String resp = "";
        List<User> users = UserManager.findAllUsers();

        for (User user: users) {
            if (getCurrentTime() - user.getTime() > 15638400 && user.getDeleted() == false) {
                resp+="Заморожен пользователь " + user.getUsername() + " за неактивность.<br>";
                user.setDeleted(true);
                UserManager.createUser(user);
            }
        }
        return resp;
    }

    @ResponseBody
    @PostMapping("/api/createbookstatus")
    public String ApiCreateBookStatus(HttpSession session,@RequestParam(name="idbook", required=false) Long idbook,@RequestParam(name="genre", required=false) int genre,@RequestParam(name="booleanbook", required=false) boolean booleanbook) {
        if (userManager.getUserByMail((String) session.getAttribute("user")).get().getUserType() == 2) {
            Book book = BookManager.getBookById(idbook).get();
            if (booleanbook == true) {
                book.setStatus("PUBLISH");
            } else {
                book.setStatus("INWRITTER");
            }
            book.setGenre(genre);
            BookManager.createBook(book);
            return "ok";
        }
        else {
            return "false";
        }
    }

    @ResponseBody
    @PostMapping("/api/deletecomment")
    public String ApiDeleteComment(HttpSession session,@RequestParam(name="idcomm", required=false) Long idcomm) {
        if (userManager.getUserByMail((String) session.getAttribute("user")).get().getUserType() == 2) {
            Comment comment = CommentManager.getUserCommByIdComm(idcomm).get();
            comment.setDeleted(true);
            CommentManager.createComment(comment);
            return "ok";
        }
        else {
            return "false";
        }
    }

    @ResponseBody
    @PostMapping("/api/sendsupportmail")
    public String ApiSendSupportMail(HttpSession session,@RequestParam(name="idarea", required=false) Long idarea,@RequestParam(name="text", required=false) String text) {
        if (userManager.getUserByMail((String) session.getAttribute("user")).get().getUserType() == 2) {
            Question question = QuestionManager.getOrderById(idarea).get();
            question.setResponse(text);
            question.setStatus("FINISH");
            QuestionManager.createQuestion(question);
            User user = UserManager.getUserById(question.getIdOwn()).get();
            MailController.sendEmail(user.getMail(),"Ответ от поддержки",text);
            return "ok";
        }
        else {
            return "false";
        }
    }

    @ResponseBody
    @PostMapping("/api/updatebook")
    public String UpdateBook(@RequestParam(name="idbook") Long idBook,@RequestParam(name="name") String name, @RequestParam(name="author") String author, @RequestParam("genre") int genre, HttpSession session, @RequestParam("writer") String writer, @RequestParam("aboutbook") String aboutBook, @RequestParam("price") Float price) {
        if (userManager.getUserByMail((String) session.getAttribute("user")).get().getUserType() == 1) {
            Book book = BookManager.getBookById(idBook).get();
            book.setName(name);
            book.setGenre(genre);
            book.setAuthor(author);
            book.setTitle(writer);
            book.setStatus("NEW");
            book.setPrice(price);
            book.setAbout(aboutBook);
            BookManager.createBook(book);
            return "Изменения сохранены! Ожидайте одобрения администратора.";
        }
        else {
            return "false";
        }
    }
}
