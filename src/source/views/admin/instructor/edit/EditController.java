package source.views.admin.instructor.edit;

import entity.Subjects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Question;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import util.HibernateUtil;
import util.Messages;

import java.util.List;

/**
 * Created by MarinaGagloeva.
 *
 * @since 23.02.2017.
 */
public class EditController {

    public TextField questionField;
    public TextField firstAnswerField;
    public TextField secondAnswerField;
    public TextField thirdAnswerField;
    public TextField fourthAnswerField;
    public Button editButton;
    public Button backButton;
    public RadioButton firstAnswerRadioButton;
    public RadioButton secondAnswerRadioButton;
    public RadioButton thirdAnswerRadioButton;
    public RadioButton fourthAnswerRadioButton;
    private ToggleGroup rightAnswerField = new ToggleGroup();

    private Stage stage;

    private Question question;

    private ObservableList<Subjects> subjectsObservableList = FXCollections.observableArrayList();

    private Session session;
    private Query query;

    public void initialize() {
        firstAnswerRadioButton.setToggleGroup(rightAnswerField);
        secondAnswerRadioButton.setToggleGroup(rightAnswerField);
        thirdAnswerRadioButton.setToggleGroup(rightAnswerField);
        fourthAnswerRadioButton.setToggleGroup(rightAnswerField);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setQuestion(Question question) {
        this.question = question;
        questionField.setText(question.getQuestion());
        firstAnswerField.setText(question.getFirstAnswer());
        secondAnswerField.setText(question.getSecondAnswer());
        thirdAnswerField.setText(question.getThirdAnswer());
        fourthAnswerField.setText(question.getFourthAnswer());
    }

    public void editButtonAction(ActionEvent actionEvent) {
        try {
            Session session;
            Query query;
            if (!questionField.getText().isEmpty() && !firstAnswerField.getText().isEmpty() && !secondAnswerField.getText().isEmpty()
                    && !thirdAnswerField.getText().isEmpty() && !fourthAnswerField.getText().isEmpty()
                    && rightAnswerField.getSelectedToggle() != null) {
                session = HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();
                query = session.createQuery("update Questions set questionText = :questionText where id = :id")
                        .setParameter("questionText", questionField.getText())
                        .setParameter("id", question.getId());
                query.executeUpdate();
                session.getTransaction().commit();
                session.close();
                session = HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();
                query = session.createQuery("update Answers set firstAnswer = :firstAnswer, secondAnswer = :secondAnswer," +
                        "thirdAnswer = :thirdAnswer, fourthAnswer = :fourthAnswer, rightAnswer = :rightAnswer where id = :id")
                        .setParameter("firstAnswer", firstAnswerField.getText())
                        .setParameter("secondAnswer", secondAnswerField.getText())
                        .setParameter("thirdAnswer", thirdAnswerField.getText())
                        .setParameter("fourthAnswer", fourthAnswerField.getText())
                        .setParameter("rightAnswer", getRightAnswerFromToggle( (RadioButton) rightAnswerField.getSelectedToggle() ))
                        .setParameter("id", question.getAnswerId());
                query.executeUpdate();
                session.getTransaction().commit();
                session.close();
            } else {
                Messages.showLoginErrorMessage("Поля не заполнены!!!");
            }
        } catch (HibernateException e) {

        }
    }

    private String getRightAnswerFromToggle(RadioButton id) {
        String rightAnswerFromToggle = "";
        int selectId = Integer.parseInt(id.getText());
        switch (selectId) {
            case 1: rightAnswerFromToggle = firstAnswerField.getText().trim();
                break;
            case 2: rightAnswerFromToggle =  secondAnswerField.getText().trim();
                break;
            case 3: rightAnswerFromToggle =  thirdAnswerField.getText().trim();
                break;
            case 4: rightAnswerFromToggle =  fourthAnswerField.getText().trim();
                break;
        }
        return rightAnswerFromToggle;
    }

    public void backButtonAction(ActionEvent actionEvent) {
        stage.close();
    }
}
