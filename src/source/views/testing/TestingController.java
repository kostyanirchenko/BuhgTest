package source.views.testing;

import entity.Answers;
import entity.Questions;
import entity.Students;
import entity.Subjects;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import model.Question;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import source.Main;
import util.HibernateUtil;
import util.Messages;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Kostya Nirchenko.
 *
 * @since 03.11.2016
 */
public class TestingController {

    public Label testTypeLabel;
    public RadioButton firstAnswerButton;
    public RadioButton secondAnswerButton;
    public RadioButton thirdAnswerButton;
    public RadioButton fourthAnswerButton;
    public Label questionLabel;
    public Button answerButton;
    public Button backButton;

    private Students students;
    private Subjects currentSubject;

    private Main main;

    private Stage stage;

    private ToggleGroup toggleGroup = new ToggleGroup();

    private List<Questions> questionsList = new ArrayList<>();
    private List<Answers> answersList = new ArrayList<>();
    private List<Question> questions = new ArrayList<>();

    private int wrongAnswer = 0;
    private int rightAnswer = 0;
    private int iterate = 1;

    public void setStudents(Students students) {
        this.students = students;
        testTypeLabel.setText(students.getTest_type());
        testTypeLabel.setTextAlignment(TextAlignment.CENTER);
    }

    public void setMain(Main main, Subjects subject) {
        this.main = main;
        currentSubject = subject;
        firstAnswerButton.setToggleGroup(toggleGroup);
        secondAnswerButton.setToggleGroup(toggleGroup);
        thirdAnswerButton.setToggleGroup(toggleGroup);
        fourthAnswerButton.setToggleGroup(toggleGroup);
        questionLabel.setTextAlignment(TextAlignment.CENTER);
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Query query = session.createQuery("from Questions where subjectId = :subjectId").setParameter("subjectId", currentSubject.getId());
            questionsList = (List<Questions>) query.list();
            int firstQuestion = questionsList.get(0).getId();
            session.getTransaction().commit();
            session.close();
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Query answers = session.createQuery("from Answers where questionId = :questionId").setParameter("questionId", firstQuestion);
            answersList = (List<Answers>) answers.list();
            session.getTransaction().commit();
            session.close();
            for (Questions q : questionsList) {
                questions.addAll(answersList.stream().map(a -> new Question(
                        q.getId(),
                        q.getQuestionText(),
                        a.getId(),
                        a.getFirstAnswer(),
                        a.getSecondAnswer(),
                        a.getThirdAnswer(),
                        a.getFourthAnswer(),
                        a.getRightAnswer()
                )).collect(Collectors.toList()));
            }
        } catch (HibernateException e) {
            Messages.showErrorMessage(e);
        }
        if(!questionsList.isEmpty()) {
            questionLabel.setText(questionsList.get(0).getQuestionText());
            firstAnswerButton.setText(answersList.get(0).getFirstAnswer());
            secondAnswerButton.setText(answersList.get(0).getSecondAnswer());
            thirdAnswerButton.setText(answersList.get(0).getThirdAnswer());
            fourthAnswerButton.setText(answersList.get(0).getFourthAnswer());
        } else {
            questionLabel.setVisible(false);
            firstAnswerButton.setVisible(false);
            secondAnswerButton.setVisible(false);
            thirdAnswerButton.setVisible(false);
            fourthAnswerButton.setVisible(false);
            answerButton.setVisible(false);
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void answerButtonAction(ActionEvent actionEvent) {
        RadioButton currentAnswer = (RadioButton) toggleGroup.getSelectedToggle();

        if (iterate >= questionsList.size()) {
            Messages.showInfoMessage("SUCCESS");
            stage.close();


        } else {
            try {
            /*if (iterate >= questionsList.size()) {
                Messages.showInfoMessage("SUCCESS");
                stage.close();
            }*/
                Session session = HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();
                Query query = session.createQuery("from Answers where questionId = :questionId")
                        .setParameter("questionId", questions.get(iterate).getId());
//                    .setString("rightAnswer", currentAnswer.getText());
                List<Answers> currAnswer = (List<Answers>) query.list();
                session.getTransaction().commit();
                session.close();
                if(currentAnswer.getText().equals(currAnswer.get(0).getRightAnswer())) {
                    rightAnswer++;
                } else {
                    wrongAnswer++;
                }
                iterate++;
            } catch (HibernateException e) {
//            Messages.showErrorMessage(e);
                e.printStackTrace();
            }
            // TODO BLYAT!
            /*if (iterate <= questionsList.size()) {
                questionLabel.setText(questions.get(iterate).getQuestion());
                firstAnswerButton.setText(questions.get(iterate).getFirstAnswer());
                secondAnswerButton.setText(questions.get(iterate).getSecondAnswer());
                thirdAnswerButton.setText(questions.get(iterate).getThirdAnswer());
                fourthAnswerButton.setText(questions.get(iterate).getFourthAnswer());
            }*/

        }
    }

    public void backButtonAction(ActionEvent actionEvent) {
        stage.close();
    }
}
