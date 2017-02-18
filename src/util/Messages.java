package util;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import source.Main;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by MarinaGagloeva.
 * @since 12.02.2017
 */
public class Messages {

    public static void showErrorMessage(Exception e) {
        /*Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle("Ошибка");
        error.setHeaderText("Упс, что-то пошло не так!");
        error.setContentText(message);
        Stage errorStage = (Stage) error.getDialogPane().getScene().getWindow();
        errorStage.getIcons().add(new Image(Main.class.getResourceAsStream("views/images/error.png")));
        errorStage.showAndWait();*/
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Упс, что-то пошло не так");
        alert.setContentText("Пожалуйста, отправте отчет об ошибке на сайт разработчика.");
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String exceptionText = sw.toString();
        Label label = new Label("Стек ошибки: ");
        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);
        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);
        alert.getDialogPane().setExpandableContent(expContent);
        Stage errorStage = (Stage) alert.getDialogPane().getScene().getWindow();
        errorStage.getIcons().add(new Image(Main.class.getResourceAsStream("views/images/error.png")));
        errorStage.showAndWait();
    }

    public static void showLoginErrorMessage(String message) {
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle("Ошибка");
        error.setHeaderText("Упс, что-то пошло не так!");
        error.setContentText(message);
        Stage errorStage = (Stage) error.getDialogPane().getScene().getWindow();
        errorStage.getIcons().add(new Image(Main.class.getResourceAsStream("views/images/error.png")));
        errorStage.showAndWait();
    }

    public static void showInfoMessage(String message) {
        Alert information = new Alert(Alert.AlertType.INFORMATION);
        information.setTitle("Информация");
        information.setContentText(message);
        Stage informationStage = (Stage) information.getDialogPane().getScene().getWindow();
        informationStage.getIcons().add(new Image(Main.class.getResourceAsStream("views/images/confirm.png")));
        informationStage.showAndWait();
    }
}
