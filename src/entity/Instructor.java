package entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by MarinaGagloeva.
 *
 * @since 18.02.2017.
 */
@Entity
@Table(name = "instructor", uniqueConstraints = {
        @UniqueConstraint(columnNames = "id"),
        @UniqueConstraint(columnNames = "login"),
        @UniqueConstraint(columnNames = "password"),
        @UniqueConstraint(columnNames = "name"),
        @UniqueConstraint(columnNames = "surname"),
        @UniqueConstraint(columnNames = "subjectId")
})
public class Instructor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    @Column(name = "login", unique = true, nullable = false, length = 16)
    private String login;

    @Column(name = "password", unique = false, nullable = false, length = 16)
    private String password;

    @Column(name = "name", unique = false, nullable = false, length = 20)
    private String name;

    @Column(name = "surname", unique = false, nullable = false, length = 20)
    private String surname;

    @Column(name = "subjectId", unique = false, nullable = false, length = 3)
    private int subjectId;

    public Instructor(String login, String password, String name, String surname, int subjectId) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.subjectId = subjectId;
    }

    public Instructor() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }
}
