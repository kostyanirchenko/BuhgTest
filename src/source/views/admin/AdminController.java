package source.views.admin;

import entity.Admin;
import entity.Groups;
import entity.Students;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import source.Main;
import source.views.admin.add.AddQuestionsController;
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
 * @since 02.11.2016
 */
public class AdminController {

    private static  Logger logger = Logger.getLogger(AdminController.class.getName());

    public Button backButton;
    public Label helloLabel;
    public Button addQuestionsButton;
    public Button addAdminButton;
    public Button addSubjectButton;
    public Button addStudentButton;
    public Button addGroupButton;

    private List<Admin> admin;

    private Main main;

    private Stage stage;

    public void addQuestionsButtonAction(ActionEvent actionEvent) {
        boolean addClicked = main.addNewQuestion();
        if (addClicked) {
            Messages.showInfoMessage("Операция прошла успешно!");
        }
    }

    public void addAdminButtonAction(ActionEvent actionEvent) {
        Dialog<Pair<String, String>> addAdminDialog = new Dialog<>();
        addAdminDialog.setTitle("Добавление администратора");
        addAdminDialog.setHeaderText("Пожалуйста укажите данные нового администратора");
        ButtonType addButtonType = new ButtonType("Добавить", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Назад", ButtonBar.ButtonData.CANCEL_CLOSE);
        addAdminDialog.getDialogPane().getButtonTypes().addAll(addButtonType, cancelButtonType);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        TextField login = new TextField();
        login.setPromptText("Введите новый логин");
        PasswordField password = new PasswordField();
        password.setPromptText("Введите пароль");
        grid.add(new Label("Логин :"), 0, 0);
        grid.add(login, 1, 0);
        grid.add(new Label("Пароль :"), 0, 1);
        grid.add(password, 1, 1);
        Node addButton = addAdminDialog.getDialogPane().lookupButton(addButtonType);
        addButton.setDisable(true);
        password.textProperty().addListener(((observable, oldValue, newValue) -> {
            addButton.setDisable(newValue.trim().isEmpty());
        }));
        addAdminDialog.getDialogPane().setContent(grid);
        Platform.runLater(login::requestFocus);
        addAdminDialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return new Pair<>(login.getText(), password.getText());
            }
            return null;
        });
        Optional<Pair<String, String>> result = addAdminDialog.showAndWait();
        result.ifPresent(newAdmin -> {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Admin admin = new Admin();
            admin.setLogin(newAdmin.getKey());
            admin.setPassword(newAdmin.getValue());
            session.save(admin);
            session.getTransaction().commit();
            session.close();
        });
    }

    public void backButtonAction(ActionEvent actionEvent) {
        stage.close();
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setAdmin(List<Admin> admin) {
        this.admin = admin;
        helloLabel.setText("Здравствуйте, " + this.admin.get(0).getLogin());
    }

    public void addSubjectButtonAction(ActionEvent actionEvent) {

    }

    private ObservableList<Groups> groupsObservableList = FXCollections.observableArrayList();

    public void addStudentButtonAction(ActionEvent actionEvent) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        try {
            Query query = session.createQuery("from Groups");
            List<Groups> tmp = (List<Groups>) query.list();
            session.getTransaction().commit();
            session.close();
            for (Groups g : tmp) {
                groupsObservableList.add(g);
            }
            tmp = null;
        } catch (HibernateException e) {
            Messages.showErrorMessage(e);
        }
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Добавление студента");
        dialog.setHeaderText("Укажите данные для новой записи");
        ButtonType loginButtonType = new ButtonType("Добавить", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Назад", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, cancelButtonType);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        TextField username = new TextField();
        username.setPromptText("Введите логин");
        TextField studentName = new TextField();
        studentName.setPromptText("Введите имя студента");
        PasswordField password = new PasswordField();
        password.setPromptText("Введите пароль");
        ComboBox<Groups> groups = new ComboBox<>();
        groups.setItems(groupsObservableList);
        groups.setValue(groupsObservableList.get(0));
        TextField studentSurname = new TextField();
        studentSurname.setPromptText("Введите фамилию");
        grid.add(new Label("Логин :"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Пароль :"), 0, 1);
        grid.add(password, 1, 1);
        grid.add(new Label("Имя :"), 0, 2);
        grid.add(studentName, 1, 2);
        grid.add(new Label("Фамилия :"), 0, 3);
        grid.add(studentSurname, 1, 3);
        grid.add(new Label("Группа :"), 0, 4);
        grid.add(groups, 1, 4);
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);
        username.textProperty().addListener((observable, oldValue, newValue) -> {
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
            Session saveStudent = HibernateUtil.getSessionFactory().openSession();
            saveStudent.beginTransaction();
            Students students = new Students();
            students.setLogin(usernamePassword.getKey());
            students.setPassword(usernamePassword.getValue());
            students.setName(studentName.getText());
            students.setSurname(studentSurname.getText());
            students.setStudentGroup(groups.getSelectionModel().getSelectedItem().toString());
            saveStudent.save(students);
            saveStudent.getTransaction().commit();
            saveStudent.close();
        });
    }

    public void addGroupButtonAction(ActionEvent actionEvent) {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Добавление группы");
        dialog.setHeaderText("Укажите название группы");
        ButtonType loginButtonType = new ButtonType("Далее", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Назад", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, cancelButtonType);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        TextField login = new TextField();
        login.setPromptText("Введите название группы");
        grid.add(new Label("Название :"), 0, 0);
        grid.add(login, 1, 0);
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);
        login.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });
        dialog.getDialogPane().setContent(grid);
        Platform.runLater(login::requestFocus);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(login.getText(), "");
            }
            return null;
        });
        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(person -> {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Groups gr = new Groups();
            gr.setGroupName(person.getKey());
            session.save(gr);
            session.getTransaction().commit();
            session.close();
        });
    }
}
