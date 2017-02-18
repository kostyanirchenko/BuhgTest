package entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by MarinaGagloeva.
 *
 * @since 18.02.2017.
 */

@Entity
@Table(name = "answers", uniqueConstraints = {
        @UniqueConstraint(columnNames = "id"),
        @UniqueConstraint(columnNames = "firstAnswer"),
        @UniqueConstraint(columnNames = "secondAnswer"),
        @UniqueConstraint(columnNames = "thirdAnswer"),
        @UniqueConstraint(columnNames = "fourthAnswer"),
        @UniqueConstraint(columnNames = "rightAnswer"),
        @UniqueConstraint(columnNames = "questionId")
})
public class Answers implements Serializable {

    private static final long serialVersionUID = -1798070786993154676L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    @Column(name = "firstAnswer", unique = false, nullable = false, length = 30)
    private String firstAnswer;

    @Column(name = "secondAnswer", unique = false, nullable = false, length = 30)
    private String secondAnswer;

    @Column(name = "thirdAnswer", unique = false, nullable = false, length = 30)
    private String thirdAnswer;

    @Column(name = "fourthAnswer", unique = false, nullable = false, length = 30)
    private String fourthAnswer;

    @Column(name = "rightAnswer", unique = false, nullable = false, length = 30)
    private String rightAnswer;

    @Column(name = "questionId", unique = false, nullable = false, length = 3)
    private int questionId;

    public Answers(String firstAnswer, String secondAnswer, String thirdAnswer, String fourthAnswer, String rightAnswer, int questionId) {
        this.firstAnswer = firstAnswer;
        this.secondAnswer = secondAnswer;
        this.thirdAnswer = thirdAnswer;
        this.fourthAnswer = fourthAnswer;
        this.rightAnswer = rightAnswer;
        this.questionId = questionId;
    }

    public Answers() {
    }

    public String getFirstAnswer() {
        return firstAnswer;
    }

    public void setFirstAnswer(String firstAnswer) {
        this.firstAnswer = firstAnswer;
    }

    public String getSecondAnswer() {
        return secondAnswer;
    }

    public void setSecondAnswer(String secondAnswer) {
        this.secondAnswer = secondAnswer;
    }

    public String getThirdAnswer() {
        return thirdAnswer;
    }

    public void setThirdAnswer(String thirdAnswer) {
        this.thirdAnswer = thirdAnswer;
    }

    public String getFourthAnswer() {
        return fourthAnswer;
    }

    public void setFourthAnswer(String fourthAnswer) {
        this.fourthAnswer = fourthAnswer;
    }

    public String getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(String rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }
}

