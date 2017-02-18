package entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by MarinaGagloeva.
 *
 * @since 18.02.2017.
 */

@Entity
@Table(name = "questions", uniqueConstraints = {
        @UniqueConstraint(columnNames = "id"),
        @UniqueConstraint(columnNames = "questionText")
})
public class Questions implements Serializable {

    private static final long serialVersionUID = -1798070786993154676L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    @Column(name = "questionText", unique = true, nullable = false, length = 100)
    private String questionText;

    public Questions() {
    }

    public Questions(String questionText) {
        this.questionText = questionText;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public int getId() {
        return id;
    }
}
