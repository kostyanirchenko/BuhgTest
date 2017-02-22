package source.views.result;

import entity.Instructor;
import entity.Result;
import entity.Students;
import entity.Subjects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.Student;
import org.hibernate.Query;
import org.hibernate.Session;
import util.DateUtil;
import util.HibernateUtil;

import java.util.List;

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

    private Stage stage;

    private Instructor instructor;

    private ObservableList<Student> resultList = FXCollections.observableArrayList();

    public ResultController() {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Query query = session.createQuery("from Result where subjectId = :subjectId")
                    .setInteger("subjectId", instructor.getSubjectId());
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
                        subList.get(0).getId() + "",
                        result.getRightAnswer(),
                        result.getWrongAnswer(),
                        DateUtil.parse(result.getTestDate()),
                        result.getTestTime()
                ));
            }
        } catch (Exception e) {
//            Messages.showErrorMessage(e);
            e.printStackTrace();
            System.out.println(instructor.getSubjectId());
        }
    }

    public void initialize() {
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().studentNameProperty());
        surnameColumn.setCellValueFactory(cellData -> cellData.getValue().studentSurnameProperty());
        groupColumn.setCellValueFactory(cellData -> cellData.getValue().studentGroupProperty());
        tableView.setItems(resultList);
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
            testDateLabel.setText(DateUtil.format(student.getTestDate()));
            testTimeLabel.setText(student.getTestTime());
        } else {
            nameLabel.setText("");
            surnameLabel.setText("");
            groupLabel.setText("");
            subjectLabel.setText("");
            rightAnswerLabel.setText("");
            wrongAnswerLabel.setText("");
            testDateLabel.setText("");
            testTimeLabel.setText("");
        }
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }
}
