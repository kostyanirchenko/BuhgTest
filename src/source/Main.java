package source;

import entity.Admin;
import entity.Questions;
import entity.Students;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.hibernate.Query;
import org.hibernate.Session;
import source.views.admin.AdminController;
import source.views.admin.add.AddQuestionsController;
import util.HibernateUtil;
import util.Messages;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Kostya Nirchenko.
 *
 * @since 31.10.2016
 */
public class Main extends Application {

    private static Logger logger = Logger.getLogger(Main.class.getName());

    private Stage primaryStage;

    private Students student;
    private Admin admin;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Тестирование");
        this.primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("views/images/database.png")));
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Query query = session.createQuery("from Admin");
        List<Admin> admins = (List<Admin>) query.list();
        session.getTransaction().commit();
        session.close();
        if (admins.size() == 0) {
            Session session1 = HibernateUtil.getSessionFactory().openSession();
            session1.beginTransaction();
            Admin admin = new Admin();
            admin.setLogin("admin");
            admin.setPassword("admin");
            session1.save(admin);
            session1.getTransaction().commit();
            session1.close();
        }
        Session sess = HibernateUtil.getSessionFactory().openSession();
        sess.beginTransaction();
        Query qu = sess.createQuery("from Students");
        List<Students> st = (List<Students>) qu.list();
        sess.getTransaction().commit();
        sess.close();
        if (st.size() == 0) {
            Session session2 = HibernateUtil.getSessionFactory().openSession();
            session2.beginTransaction();
            Students students = new Students();
            students.setName("test");
            students.setStudentGroup("test");
            students.setSurname("test");
            students.setLogin("test");
            students.setPassword("test");
            session2.save(students);
            session2.getTransaction().commit();
            session2.close();
        }
        launchApplication();
    }

    private source.views.Application application;

    private void launchApplication() {
        FXMLLoader loader = new FXMLLoader();
        AnchorPane anchorPane;
        try {
            anchorPane = loader.load(getClass().getResourceAsStream("views/application.fxml"));
            this.application = loader.getController();
            this.application.setMain(this);
            Scene scene = new Scene(anchorPane);
            primaryStage.setScene(scene);

//            student = login();
//
//            application.setUser(student);
            login();
//            primaryStage.show();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Exception: ", e);
        }
    }

    public void launchAdminPanel(List<Admin> adm) {
        try {
            FXMLLoader loader = new FXMLLoader();
            AnchorPane pane = loader.load(getClass().getResourceAsStream("views/admin/admin.fxml"));
            Stage adminStage = new Stage();
            adminStage.getIcons().add(new Image(this.getClass().getResourceAsStream("views/images/admin.jpg")));
            adminStage.setTitle("Администрирование");
            adminStage.initModality(Modality.APPLICATION_MODAL);
            adminStage.initOwner(primaryStage);
            adminStage.setScene(new Scene(pane));
            AdminController adminController = loader.getController();
            adminController.setMain(this);
            adminController.setAdmin(adm);
            adminController.setStage(adminStage);
            adminStage.showAndWait();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Exception: ", e);
        }
    }

    private void login() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Вход");
        dialog.setHeaderText("Пожалуйста, авторизируйтесь под\nвашей учетной записью");
        dialog.setGraphic(new ImageView(this.getClass().getResource("views/images/login.png").toString()));
        ButtonType loginButtonType = new ButtonType("Далее", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        TextField login = new TextField();
        login.setPromptText("Введите ваш логин");
        PasswordField password = new PasswordField();
        password.setPromptText("Введите ваш пароль");
        grid.add(new Label("Логин :"), 0, 0);
        grid.add(login, 1, 0);
        grid.add(new Label("Пароль :"), 0, 1);
        grid.add(password, 1, 1);
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);
        password.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });
        dialog.getDialogPane().setContent(grid);
        Platform.runLater(login::requestFocus);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(login.getText(), password.getText());
            }
            return null;
        });
        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(person -> {
            Session getAdmin = HibernateUtil.getSessionFactory().openSession();
            getAdmin.beginTransaction();
            Query query = getAdmin.createQuery("from Admin where login = :login and password = :password")
                    .setString("login", person.getKey())
                    .setString("password", person.getValue());
            List<Admin> admins = (List<Admin>) query.list();
            getAdmin.getTransaction().commit();
            getAdmin.close();
            try {
                if (admins.size() == 0) {
                    Session getStudents = HibernateUtil.getSessionFactory().openSession();
                    getStudents.beginTransaction();
                    Query query1 = getStudents.createQuery("from Students where login = :login and password = :password")
                            .setString("login", person.getKey())
                            .setString("password", person.getValue());
                    List<Students> studentsList = (List<Students>) query1.list();
                    getStudents.getTransaction().commit();
                    getStudents.close();
                    setStudent(studentsList.get(0));
                    primaryStage.show();
                } else {
                    try {
                        Session session = HibernateUtil.getSessionFactory().openSession();
                        session.beginTransaction();
                    } catch (Exception e) {
                        Messages.showErrorMessage(e);
                    }
                    launchAdminPanel(admins);
                }
            } catch (Exception e) {
                Messages.showLoginErrorMessage("Введен неверный логин или пароль, попробуйте еще раз.");
                login();
            }

        });
    }

//    private void setAdmin(Admin admin) {
//        this.admin = admin;
//        application.setUser(admin);
//    }

    private void setStudent(Students student) {
        this.student = student;
        application.setUser(student);
    }

    public boolean addNewQuestion(/*Questions question*/) {
        try {
            FXMLLoader loader = new FXMLLoader();
            AnchorPane newStage = loader.load(getClass().getResourceAsStream("views/admin/add/add.fxml"));
            Stage newQuestionStage = new Stage();
            newQuestionStage.setTitle("Добавление");
            newQuestionStage.getIcons().add(new Image(this.getClass().getResourceAsStream("views/images/new.png")));
            newQuestionStage.initModality(Modality.APPLICATION_MODAL);
            newQuestionStage.initOwner(primaryStage);
            Scene scene = new Scene(newStage);
            newQuestionStage.setScene(scene);
            AddQuestionsController addQuestionsController = loader.getController();
            addQuestionsController.setStage(newQuestionStage);
            newQuestionStage.showAndWait();
            return addQuestionsController.isOkClicked();
        } catch (IOException e) {
           Messages.showErrorMessage(e);
           return false;
        }
    }

    public Stage getPrimaryStage() {
        return this.primaryStage;
    }

    public void exit() {
        primaryStage.close();
    }
}
