package source.views.testing;

import entity.*;
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

import java.text.SimpleDateFormat;
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

    public void setStudents(Students students, String testType) {
        this.students = students;
        testTypeLabel.setText(testType);
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
        Session session;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Query query = session.createQuery("from Questions where subjectId = :subjectId").setParameter("subjectId", currentSubject.getId());
            questionsList = (List<Questions>) query.list();
            session.getTransaction().commit();
            session.close();
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            for (Questions questionss : questionsList) {
                Query answers = session.createQuery("from Answers where questionId = :questionId").setParameter("questionId", questionss.getId());
                answersList = (List<Answers>) answers.list();
                questions.add(new Question(
                        questionss.getId(),
                        questionss.getQuestionText(),
                        answersList.get(0).getId(),
                        answersList.get(0).getFirstAnswer(),
                        answersList.get(0).getSecondAnswer(),
                        answersList.get(0).getThirdAnswer(),
                        answersList.get(0).getFourthAnswer(),
                        answersList.get(0).getRightAnswer()
                ));
                if (questionsList.indexOf(questionss) % 20 == 0) {
                    session.flush();
                    session.clear();
                }
            }
            session.getTransaction().commit();
            session.close();
//            questionsList.clear();
            answersList.clear();
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
            questionLabel.setText(questions.get(0).getQuestion());
            firstAnswerButton.setText(questions.get(0).getFirstAnswer());
            secondAnswerButton.setText(questions.get(0).getSecondAnswer());
            thirdAnswerButton.setText(questions.get(0).getThirdAnswer());
            fourthAnswerButton.setText(questions.get(0).getFourthAnswer());
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
        if (currentAnswer != null) {
            try {
                Session session = HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();
                Query query = session.createQuery("from Answers where questionId = :questionId")
                        .setParameter("questionId", questions.get(iterate - 1).getId());
                List<Answers> currAnswer = (List<Answers>) query.list();
                session.getTransaction().commit();
                session.close();
                System.out.println(currentAnswer.getText() +
                        " " + currAnswer.get(0).getRightAnswer());
                if(currentAnswer.getText().equals(currAnswer.get(0).getRightAnswer())) {
                    rightAnswer++;
                } else {
                    wrongAnswer++;
                }
            } catch (HibernateException e) {
                Messages.showErrorMessage(e);
            }
            if (iterate >= questionsList.size()) {
                Session session = HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();
                String date = new SimpleDateFormat("dd.MM.yyyy").format(System.currentTimeMillis());
                String time = new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis());
                Result result = new Result();
                result.setStudentId(students.getId());
                result.setSubjectId(currentSubject.getId());
                result.setRightAnswer(rightAnswer);
                result.setWrongAnswer(wrongAnswer);
                result.setTestDate(date);
                result.setTestTime(time);
                result.setTestType(testTypeLabel.getText());
                session.save(result);
                session.getTransaction().commit();
                session.close();
                Messages.showInfoMessage(
                        "Ура, вы ответили на все вопросы!\n" +
                                "Студент: " + students.getName() + " " + students.getSurname()
                                + "\nГруппа: " + students.getStudentGroup()
                                + "\nДисциплина: " + currentSubject.getSubject()
                                + "\nТип теста: " + testTypeLabel.getText()
                                + "\nКоличество правильных ответов: " + rightAnswer
                                + "\nКоличество неправильных ответов: " + wrongAnswer
                                + "\nВремя окончания тестирования: " + time
                );
                stage.close();
            } else {
//                if (iterate <= questionsList.size()) {
                    questionLabel.setText(questions.get(iterate).getQuestion());
                    firstAnswerButton.setText(questions.get(iterate).getFirstAnswer());
                    secondAnswerButton.setText(questions.get(iterate).getSecondAnswer());
                    thirdAnswerButton.setText(questions.get(iterate).getThirdAnswer());
                    fourthAnswerButton.setText(questions.get(iterate).getFourthAnswer());
//                }
                iterate++;
                toggleGroup.selectToggle(null);
            }
        } else {
            Messages.showLoginErrorMessage("Не выбран не один из ответов");
        }
    }

    public void backButtonAction(ActionEvent actionEvent) {
        stage.close();
    }
}
