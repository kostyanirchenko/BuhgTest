package source.views.admin;

import entity.Admin;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.hibernate.Session;
import source.Main;
import source.views.admin.add.AddQuestionsController;
import util.HibernateUtil;

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

    private List<Admin> admin;

    private Main main;

    private Stage stage;

    public void addQuestionsButtonAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader();
            AnchorPane pane = loader.load(getClass().getResourceAsStream("add/add.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Добавление");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(this.stage);
            stage.setScene(new Scene(pane));
            AddQuestionsController addQuestionsController = loader.getController();
            addQuestionsController.setStage(stage);
            stage.showAndWait();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Exception: ", e);
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
}
