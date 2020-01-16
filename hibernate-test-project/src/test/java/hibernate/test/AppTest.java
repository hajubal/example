package hibernate.test;

import static org.junit.Assert.*;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Test;

import hibernate.test.dto.EmployeeEntity;

public class AppTest {
	@Test
	public void 저장_조회_삭제() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
       
		//Add new Employee object
		EmployeeEntity emp = new EmployeeEntity();
		emp.setEmail("demo-user@mail.com");
		emp.setFirstName("demo");
		emp.setLastName("user");
		
		Long id = (Long) session.save(emp);
		
		Query query = session.createQuery("from EmployeeEntity where id = :id");
		query.setParameter("id", id);
		
		List<EmployeeEntity> resultList = (List<EmployeeEntity>)query.list();
		
		assertNotNull(resultList);
		
		assertEquals(1, resultList.size());
		
		EmployeeEntity newEntity = resultList.iterator().next();
		
		assertEquals(emp.getEmail(), newEntity.getEmail());
		
		session.delete(newEntity);
		
		newEntity = (EmployeeEntity) session.get(EmployeeEntity.class, id);

		assertNull(newEntity);
		
		session.getTransaction().commit();
	}
	
}
