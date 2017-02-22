package source.views.testing;

import entity.Students;
import entity.Subjects;
import javafx.scene.control.Label;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import source.Main;

/**
 * Created by Kostya Nirchenko.
 *
 * @since 03.11.2016
 */
public class TestingController {

    public Label testTypeLabel;

    private Students students;
    private Subjects currentSubject;

    private Main main;

    private Stage stage;

    public void setStudents(Students students) {
        this.students = students;
        testTypeLabel.setText(students.getTest_type());
        testTypeLabel.setTextAlignment(TextAlignment.CENTER);
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
