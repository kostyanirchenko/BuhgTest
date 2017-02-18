package entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by MarinaGagloeva.
 *
 * @since 17.02.2017.
 */

@Entity
@Table(name = "subjects", uniqueConstraints = {
        @UniqueConstraint(columnNames = "id"),
        @UniqueConstraint(columnNames = "subject"),
        @UniqueConstraint(columnNames = "courseId"),
        @UniqueConstraint(columnNames = "groupId")
})
public class Subjects implements Serializable {

    private static final long serialVersionUID = -1798070786993154676L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    @Column(name = "subject", unique = false, nullable = false, length = 30)
    private String subject;

    @Column(name = "courseId", unique = false, length = 2)
    private int courseId;

    @Column(name = "groupId", unique = false, length = 2)
    private int groupId;

    public Subjects() {
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return getSubject();
    }
}
