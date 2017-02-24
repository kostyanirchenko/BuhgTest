package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Kostya Nirchenko.
 *
 * @since 22.02.2017
 */
public class Student {

    private final StringProperty studentName;
    private final StringProperty studentSurname;
    private final StringProperty studentGroup;
    private final StringProperty subject;
    private final IntegerProperty rightAnswer;
    private final IntegerProperty wrongAnswer;
    private final StringProperty testDate;
    private final StringProperty testTime;
    private final StringProperty testType;

    public Student(String studentName, String studentSurname, String studentGroup, String subject,
                   int rightAnswer, int wrongAnswer, String  testDate, String testTime, String testType) {
        this.studentName = new SimpleStringProperty(studentName);
        this.studentSurname = new SimpleStringProperty(studentSurname);
        this.studentGroup = new SimpleStringProperty(studentGroup);
        this.subject = new SimpleStringProperty(subject);
        this.rightAnswer = new SimpleIntegerProperty(rightAnswer);
        this.wrongAnswer = new SimpleIntegerProperty(wrongAnswer);
        this.testDate = new SimpleStringProperty(testDate);
        this.testTime = new SimpleStringProperty(testTime);
        this.testType = new SimpleStringProperty(testType);
    }

    public Student() {
        this(null, null, null, null, 0, 0, null, null, null);
    }

    public String getStudentName() {
        return studentName.get();
    }

    public StringProperty studentNameProperty() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName.set(studentName);
    }

    public String getStudentSurname() {
        return studentSurname.get();
    }

    public StringProperty studentSurnameProperty() {
        return studentSurname;
    }

    public void setStudentSurname(String studentSurname) {
        this.studentSurname.set(studentSurname);
    }

    public String getStudentGroup() {
        return studentGroup.get();
    }

    public StringProperty studentGroupProperty() {
        return studentGroup;
    }

    public void setStudentGroup(String studentGroup) {
        this.studentGroup.set(studentGroup);
    }

    public String getSubject() {
        return subject.get();
    }

    public StringProperty subjectProperty() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject.set(subject);
    }

    public int getRightAnswer() {
        return rightAnswer.get();
    }

    public IntegerProperty rightAnswerProperty() {
        return rightAnswer;
    }

    public void setRightAnswer(int rightAnswer) {
        this.rightAnswer.set(rightAnswer);
    }

    public int getWrongAnswer() {
        return wrongAnswer.get();
    }

    public IntegerProperty wrongAnswerProperty() {
        return wrongAnswer;
    }

    public void setWrongAnswer(int wrongAnswer) {
        this.wrongAnswer.set(wrongAnswer);
    }

    public String getTestDate() {
        return testDate.get();
    }

    public StringProperty testDateProperty() {
        return testDate;
    }

    public void setTestDate(String testDate) {
        this.testDate.set(testDate);
    }

    public String getTestTime() {
        return testTime.get();
    }

    public StringProperty testTimeProperty() {
        return testTime;
    }

    public void setTestTime(String testTime) {
        this.testTime.set(testTime);
    }

    public String getTestType() {
        return testType.get();
    }

    public StringProperty testTypeProperty() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType.set(testType);
    }
}
