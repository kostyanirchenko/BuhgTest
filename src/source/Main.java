package source;

import entity.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import model.Student;
import org.hibernate.Query;
import org.hibernate.Session;
import source.views.admin.AdminController;
import source.views.admin.add.AddQuestionsController;
import source.views.admin.instructor.InstructorController;
import util.HibernateUtil;
import util.Messages;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
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
    private Instructor instructor;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
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
        /*FXMLLoader loader = new FXMLLoader();
        AnchorPane anchorPane;
        try {
            anchorPane = loader.load(getClass().getResourceAsStream("views/application.fxml"));
            this.application = loader.getController();
            this.application.setMain(this);
            Scene scene = new Scene(anchorPane);
            primaryStage.setScene(scene);
            application.setStage(primaryStage);
//            student = login();
//
//            application.setUser(student);
            login();
//            primaryStage.show();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Exception: ", e);
        }*/
        login();
    }

    public void launchAdminPanel(List<Admin> adm) {
        try {
            FXMLLoader loader = new FXMLLoader();
            AnchorPane pane = loader.load(getClass().getResourceAsStream("views/admin/admin.fxml"));
//            Stage adminStage = new Stage();
            primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("views/images/admin.jpg")));
            primaryStage.setTitle("Администрирование");
//            primaryStage.initModality(Modality.APPLICATION_MODAL);
//            primaryStage.initOwner(primaryStage);
            primaryStage.setScene(new Scene(pane));
            AdminController adminController = loader.getController();
            adminController.setMain(this);
            adminController.setAdmin(adm);
            adminController.setStage(primaryStage);
            primaryStage.show();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Exception: ", e);
        }
    }

    private void launchStudentPanel(Students student) {

        FXMLLoader loader = new FXMLLoader();
        AnchorPane anchorPane;
        try {
            anchorPane = loader.load(getClass().getResourceAsStream("views/application.fxml"));
            this.application = loader.getController();
            this.application.setMain(this);
            this.primaryStage.setTitle("Тестирование");
            this.primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("views/images/testing.png")));
            Scene scene = new Scene(anchorPane);
            primaryStage.setScene(scene);
            application.setStage(primaryStage);
            application.setUser(student);
//            student = login();
//
//            application.setUser(student);
//            login();
            primaryStage.show();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Exception: ", e);
        }
    }

    public void login() {
        primaryStage.getIcons().clear();
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
                    System.out.println(studentsList.size());
                    if (studentsList.size() == 0) {
                        try {
                            Session getInstructors = HibernateUtil.getSessionFactory().openSession();
                            getInstructors.beginTransaction();
                            Query getInstructorsQuery = getInstructors.createQuery("from Instructor where login = :login and password = :password")
                                    .setString("login", person.getKey())
                                    .setString("password", person.getValue());
                            List<Instructor> instructorList = (List<Instructor>) getInstructorsQuery.list();
                            getInstructors.getTransaction().commit();
                            getInstructors.close();
                            launchInstructorPanel(instructorList.get(0));
                        } catch (Exception e) {
//                            Messages.showErrorMessage(e);
                        }
                    } else {
                        launchStudentPanel(studentsList.get(0));
//                        setStudent(studentsList.get(0));

                    }
//                    primaryStage.show();
                } else {
                    launchAdminPanel(admins);
                }
            } catch (Exception e) {
                Messages.showLoginErrorMessage("Введен неверный логин или пароль, попробуйте еще раз.");
                e.printStackTrace();
                login();
            }

        });
    }

    private void launchInstructorPanel(Instructor instructor) {
        try {
            FXMLLoader loader = new FXMLLoader();
            AnchorPane pane = loader.load(getClass().getResourceAsStream("views/admin/instructor/instructor.fxml"));
//            Stage instructorStage = new Stage();
            primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("views/images/admin.jpg")));
            primaryStage.setTitle("Панель преподавателя");
//            primaryStage.initModality(Modality.APPLICATION_MODAL);
//            primaryStage.initOwner(primaryStage);
            primaryStage.setScene(new Scene(pane));
            InstructorController instructorController = loader.getController();
            instructorController.setMain(this);
            instructorController.setInstructor(instructor);
            instructorController.setStage(primaryStage);
            primaryStage.show();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Exception: ", e);
        }
    }

//    private void setStudent(Students student) {
//        this.student = student;
//        application.setUser(student);
//    }

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

    private ObservableList<Student> resultList = FXCollections.observableArrayList();

    public void initSubjectForInstructor(Instructor instructor) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            System.out.println(instructor.getSubjectId());
            Query query = session.createQuery("from Result where subjectId = :subjectId")
                    .setParameter("subjectId", instructor.getSubjectId());
            List<Result> results = (List<Result>) query.list();
            session.getTransaction().commit();
            session.close();
            for (Result result : results) {
                Session students = HibernateUtil.getSessionFactory().openSession();
                students.beginTransaction();
                Query studentsQuery = students.createQuery("from Students where id = :studentId")
                        .setInteger("studentId", result.getStudentId());
                List<Students> stList = (List<Students>) studentsQuery.list();
                students.getTransaction().commit();
                students.close();
                Session sub = HibernateUtil.getSessionFactory().openSession();
                sub.beginTransaction();
                Query subQuery = sub.createQuery("from Subjects where id = :id")
                        .setParameter("id", result.getSubjectId());
                List<Subjects> subList = (List<Subjects>) subQuery.list();
                sub.getTransaction().commit();
                sub.close();
                resultList.add(new Student(
                        stList.get(0).getName(),
                        stList.get(0).getSurname(),
                        stList.get(0).getStudentGroup(),
                        subList.get(0).getSubject(),
                        result.getRightAnswer(),
                        result.getWrongAnswer(),
                        result.getTestDate(),
                        result.getTestTime(),
                        result.getTestType()
                ));
            }
        } catch (Exception e) {
            //            Messages.showErrorMessage(e);
            e.printStackTrace();
            System.out.println(instructor.getSubjectId());
    }}

    public ObservableList<Student> getResultList() {
        return resultList;
    }
    public void exit() {
        primaryStage.close();
    }
}
