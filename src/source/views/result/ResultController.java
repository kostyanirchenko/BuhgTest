package source.views.result;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.Student;
import source.Main;

/**
 * Created by Kostya Nirchenko.
 *
 * @since 22.02.2017
 */
public class ResultController {

    public TableView<Student> tableView;

    public TableColumn<Student, String> nameColumn;
    public TableColumn<Student, String> surnameColumn;
    public TableColumn<Student, String> groupColumn;

    public Label nameLabel;
    public Label surnameLabel;
    public Label groupLabel;
    public Label subjectLabel;
    public Label rightAnswerLabel;
    public Label wrongAnswerLabel;
    public Label testDateLabel;
    public Label testTimeLabel;

    public Button exitButton;
    public Label testTypeLabel;

    private Stage stage;

    private Main main;

    public ResultController() {

    }

    public void initialize() {
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().studentNameProperty());
        surnameColumn.setCellValueFactory(cellData -> cellData.getValue().studentSurnameProperty());
        groupColumn.setCellValueFactory(cellData -> cellData.getValue().studentGroupProperty());
        showResultDetail(null);
        tableView.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> showResultDetail(newValue)));
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void exitButtonAction(ActionEvent actionEvent) {
        stage.close();
    }

    private void showResultDetail(Student student) {
        if (student != null) {
            nameLabel.setText(student.getStudentName());
            surnameLabel.setText(student.getStudentSurname());
            groupLabel.setText(student.getStudentGroup());
            subjectLabel.setText(student.getSubject());
            rightAnswerLabel.setText(Integer.toString(student.getRightAnswer()));
            wrongAnswerLabel.setText(Integer.toString(student.getWrongAnswer()));
            testDateLabel.setText(student.getTestDate());
            testTimeLabel.setText(student.getTestTime());
            testTypeLabel.setText(student.getTestType());
        } else {
            nameLabel.setText("");
            surnameLabel.setText("");
            groupLabel.setText("");
            subjectLabel.setText("");
            rightAnswerLabel.setText("");
            wrongAnswerLabel.setText("");
            testDateLabel.setText("");
            testTimeLabel.setText("");
            testTypeLabel.setText("");
        }
    }

    public void setMain(Main main) {
        this.main = main;
        tableView.setItems(main.getResultList());
        main.getResultList().clear();
    }
}
