package entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Kostya Nirchenko.
 *
 * @since 02.11.2016
 */

@Entity
@Table(name = "admin", uniqueConstraints = {
        @UniqueConstraint(columnNames = "id"),
        @UniqueConstraint(columnNames = "login")
})
public class Admin implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    @Column(name = "login", unique = true, nullable = false, length = 16)
    private String login;

    @Column(name = "password", unique = false, nullable = false, length = 16)
    private String password;

    public Admin() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
