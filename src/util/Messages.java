package util;

import javafx.scene.control.Alert;

/**
 * Created by MarinaGagloeva.
 * @since 12.02.2017
 */
public class Messages {

    public static void showErrorMessage(String message) {
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle("Ошибка");
        error.setHeaderText("Упс, что-то пошло не так!");
        error.setContentText(message);
        error.showAndWait();
    }

    public static void showInfoMessage(String message) {
        Alert error = new Alert(Alert.AlertType.INFORMATION);
        error.setTitle("Информация");
        error.setContentText(message);
        error.showAndWait();
    }
}
