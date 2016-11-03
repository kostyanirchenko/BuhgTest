package source;

import entity.Admin;
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
import util.HibernateUtil;

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

    private Students user;

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
        launchApplication();
    }

    private void launchApplication() {
        FXMLLoader loader = new FXMLLoader();
        AnchorPane anchorPane;
        try {
            anchorPane = loader.load(getClass().getResourceAsStream("views/application.fxml"));
            source.views.Application application = loader.getController();
            application.setMain(this);
            Scene scene = new Scene(anchorPane);
            primaryStage.setScene(scene);

            user = login();

            application.setUser(user);

            primaryStage.show();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Exception: ", e);
        }
    }

    public void launchAdminPanel(List<Admin> adm) {
        try {
            FXMLLoader loader = new FXMLLoader();
            AnchorPane pane = loader.load(getClass().getResourceAsStream("views/admin/admin.fxml"));
            Stage adminStage = new Stage();
            adminStage.setTitle("Администрирование");
            adminStage.initModality(Modality.WINDOW_MODAL);
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

    private Students login() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Данные для тестирования");
        dialog.setHeaderText("Пожалуйста укажите ваши данные");
        dialog.setGraphic(new ImageView(this.getClass().getResource("views/images/login.png").toString()));
        ButtonType loginButtonType = new ButtonType("Далее", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        TextField name = new TextField();
        name.setPromptText("Введите ваше имя");
        TextField surname = new TextField();
        surname.setPromptText("Введите вашу фамилию");
        TextField group = new TextField();
        group.setPromptText("Введите вашу группу");
        grid.add(new Label("Имя :"), 0, 0);
        grid.add(name, 1, 0);
        grid.add(new Label("Фамилия :"), 0, 1);
        grid.add(surname, 1, 1);
        grid.add(new Label("Группа :"), 0, 2);
        grid.add(group, 1, 2);
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);
        group.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });
        dialog.getDialogPane().setContent(grid);
        Platform.runLater(name::requestFocus);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(surname.getText(), name.getText() + "." + group.getText());
            }
            return null;
        });
        Students students = new Students();
        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(student -> {
            students.setName(student.getValue().substring(0, student.getValue().indexOf(".")));
            students.setSurname(student.getKey());
            students.setStudentGroup(student.getValue().substring(student.getValue().indexOf(".") + 1));
        });
        return students;
    }

    public Stage getPrimaryStage() {
        return this.primaryStage;
    }

    public void exit() {
        primaryStage.close();
    }
}
