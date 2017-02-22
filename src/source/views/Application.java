package source.views;

import entity.Admin;
import entity.Students;
import entity.Subjects;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.hibernate.Query;
import org.hibernate.Session;
import source.Main;
import source.views.testing.TestingController;
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
public class Application {

    private static Logger logger = Logger.getLogger(Application.class.getName());

    private Students user;

    public Label helloLabel;
    public Button firstModuleButton;
    public Button secondModuleButton;
    public Button examButton;
    public Button adminButton;
    public Button exitButton;

    private Main main;

    public void setUser(Students user) {
        this.user = user;
        helloLabel.setText("Здравствуйте, " + user.getName() + " " + user.getSurname() +
                " , группа " + user.getStudentGroup());
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void firstModuleButtonAction(ActionEvent actionEvent) {
        startTest("Первый модуль");
    }

    public void secondModuleButtonAction(ActionEvent actionEvent) {
        startTest("Второй модуль");
    }

    public void examButtonAction(ActionEvent actionEvent) {
        startTest("Комплексная контрольная работа");
    }

    private void startTest(String test_type) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Query query = session.createQuery("from Subjects");
        List<Subjects> subjectsList = (List<Subjects>) query.list();
        session.getTransaction().commit();
        session.close();
        ChoiceDialog<Subjects> dialog = new ChoiceDialog<>(null, subjectsList);
        dialog.setTitle("Тестирование");
        dialog.setHeaderText("Выберите дисциплину");
        dialog.setContentText("Дисциплина");
        Optional<Subjects> result = dialog.showAndWait();
        Subjects subject = result.get();
        try {
            FXMLLoader loader = new FXMLLoader();
            AnchorPane pane = loader.load(main.getClass().getResourceAsStream("views/testing/testing.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Тестирование");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(main.getPrimaryStage());
            stage.setScene(new Scene(pane));
            TestingController testingController = loader.getController();
            testingController.setMain(main, subject);
            user.setTest_type(test_type);
            saveStudent(user);
            testingController.setStudents(user);
            testingController.setStage(stage);
            stage.showAndWait();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Exception: ", e);
            Messages.showErrorMessage(e);
        }
    }

    private void saveStudent(Students students) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(students);
        session.getTransaction().commit();
        session.close();
    }

    public void adminButtonAction(ActionEvent actionEvent) {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Вход администратора");
        dialog.setHeaderText("Пожалуйста, выполните вход администратора");
        dialog.setGraphic(new ImageView(main.getClass().getResource("views/images/login.png").toString()));
        ButtonType loginButtonType = new ButtonType("Вход", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Назад", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, cancelButtonType);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        TextField username = new TextField();
        username.setPromptText("Введите ваш логин");
        PasswordField password = new PasswordField();
        password.setPromptText("Введите ваш пароль");
        grid.add(new Label("Логин :"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Пароль :"), 0, 1);
        grid.add(password, 1, 1);
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);
        password.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });
        dialog.getDialogPane().setContent(grid);
        Platform.runLater(() -> username.requestFocus());
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(username.getText(), password.getText());
            }
            return null;
        });
        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(usernamePassword -> {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            try {
                Query query = session.createQuery("from Admin where login = :login").setString("login", usernamePassword.getKey());
                List<Admin> adminList = (List<Admin>) query.list();
                session.getTransaction().commit();
                session.close();
                if (adminList.size() == 0) {
                    Messages.showLoginErrorMessage("Введен неверный логин или пароль, попробуйте еще раз.");
                    return;
                }
                for (Admin adm : adminList) {
                    if (adm.getPassword().equals(usernamePassword.getValue())) {
                        System.out.println("complete");
                        main.launchAdminPanel(adminList);
                    }
                }
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Exception: ", e);
                Messages.showErrorMessage(e);
            }
        });
    }

    public void exitButtonAction(ActionEvent actionEvent) {
        main.exit();
    }
}
