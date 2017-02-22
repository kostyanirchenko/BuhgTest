package entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Kostya Nirchenko.
 *
 * @since 22.02.2017
 */

@Entity
@Table(name = "result", uniqueConstraints = {
        @UniqueConstraint(columnNames = "id"),
        @UniqueConstraint(columnNames = "studentId"),
        @UniqueConstraint(columnNames = "subjectId"),
        @UniqueConstraint(columnNames = "rightAnswer"),
        @UniqueConstraint(columnNames = "wrongAnswer"),
        @UniqueConstraint(columnNames = "testDate"),
        @UniqueConstraint(columnNames = "testTime")
})
public class Result implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    @Column(name = "studentId", unique = false, nullable = false, length = 3)
    private int studentId;

    @Column(name = "subjectId", unique = false, nullable = false, length = 3)
    private int subjectId;

    @Column(name = "rightAnswer", unique = false, nullable = false, length = 2)
    private int rightAnswer;

    @Column(name = "wrongAnswer", unique = false, nullable = false, length = 2)
    private int wrongAnswer;

    @Column(name = "testDate", unique = false, nullable = false, length = 11)
    private String testDate;

    @Column(name = "testTime", unique = false, nullable = false, length = 9)
    private String testTime;

    public int getId() {
        return id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(int rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

    public int getWrongAnswer() {
        return wrongAnswer;
    }

    public void setWrongAnswer(int wrongAnswer) {
        this.wrongAnswer = wrongAnswer;
    }

    public String getTestDate() {
        return testDate;
    }

    public void setTestDate(String testDate) {
        this.testDate = testDate;
    }

    public String getTestTime() {
        return testTime;
    }

    public void setTestTime(String testTime) {
        this.testTime = testTime;
    }
}
