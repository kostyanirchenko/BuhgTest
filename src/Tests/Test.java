package Tests;

import entity.Students;
import org.hibernate.Session;
import util.HibernateUtil;

/**
 * Created by Kostya Nirchenko.
 *
 * @since 31.10.2016
 */
public class Test {

    public static void main(String[] args) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Students students = new Students();
        students.setName("test");
        students.setSurname("test");
        students.setStudentGroup("test");

        session.save(students);

        session.getTransaction().commit();
        session.close();
    }

}
