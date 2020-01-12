package hibernate.test;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hibernate.test.dto.EmployeeEntity;

public class TestHibernate {
	
	static final Logger logger = LoggerFactory.getLogger(TestHibernate.class);
	
	public static void main(String[] args) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
       
		//Add new Employee object
		EmployeeEntity emp = new EmployeeEntity();
		emp.setEmail("demo-user@mail.com");
		emp.setFirstName("demo");
		emp.setLastName("user");
		
		logger.debug(emp.toString());
		
		session.save(emp);
		
		session.getTransaction().commit();
		
		
		Session session2 = HibernateUtil.getSessionFactory().openSession();
		session2.beginTransaction();
       
		//Add new Employee object
		emp.setEmail("demo-user@mail.com");
		emp.setFirstName("demo");
		emp.setLastName("user");
		
		session2.save(emp);
		
		session2.getTransaction().commit();
		
		
		HibernateUtil.shutdown();
	}

}
