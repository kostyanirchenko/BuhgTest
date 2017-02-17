package entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Kostya Nirchenko.
 *
 * @since 12.02.2017.
 */

@Entity
@Table(name = "groups", uniqueConstraints = {
        @UniqueConstraint(columnNames = "id"),
        @UniqueConstraint(columnNames = "groupName")
})
public class Groups implements Serializable {

    private static final long serialVersionUID = -1798070786993154676L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    @Column(name = "groupName", unique = false, nullable = false, length = 6)
    private String groupName;

    public Groups() {

    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public String toString() {
        return this.getGroupName();
    }
}
