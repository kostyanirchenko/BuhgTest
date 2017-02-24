package entity;


import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Kostya Nirchenko.
 *
 * @since 31.10.2016
 */

@Entity
@Table(name = "students", uniqueConstraints = {
        @UniqueConstraint(columnNames = "id"),
        @UniqueConstraint(columnNames = "name"),
        @UniqueConstraint(columnNames = "surname"),
        @UniqueConstraint(columnNames = "student_group"),
        @UniqueConstraint(columnNames = "login"),
        @UniqueConstraint(columnNames = "password")
})
public class Students implements Serializable {

    private static final long serialVersionUID = -1798070786993154676L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    @Column(name = "name", unique = false, nullable = false, length = 16)
    private String name;

    @Column(name = "surname", unique = false, nullable = false, length = 16)
    private String surname;

    @Column(name = "student_group", unique = false, nullable = false, length = 10)
    private String student_group;

    @Column(name = "login", unique = true, nullable = false, length = 16)
    private String login;

    @Column(name = "password", unique = true, nullable = false, length = 16)
    private String password;

    public Students() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getStudentGroup() {
        return student_group;
    }

    public void setStudentGroup(String student_group) {
        this.student_group = student_group;
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
}
