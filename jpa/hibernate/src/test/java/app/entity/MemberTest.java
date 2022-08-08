package app.entity;


import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Arrays;

public class MemberTest {

    static EntityManagerFactory emf;

    @BeforeAll
    static void setup() {
        emf = Persistence.createEntityManagerFactory("hello");

        EntityManager en = emf.createEntityManager();

        EntityTransaction transaction = en.getTransaction();

        transaction.begin();

        en.persist(Member.builder().memberName("박휘영").build());
        en.persist(Member.builder().memberName("하주헌").build());

        transaction.commit();

        en.close();
    }

    @AfterAll
    static void shutdown() {
        emf.close();
    }

    @Test
    public void insert() {
        EntityManager en = emf.createEntityManager();

        EntityTransaction transaction = en.getTransaction();

        transaction.begin();

        Team team = Team.builder().teamName("1team").members(Arrays.asList(Member.builder().memberName("member1").build()
                , Member.builder().memberName("member1").build()
                , Member.builder().memberName("member2").build()
        )).build();

        en.persist(team);



        transaction.commit();

        en.close();
    }

    @Test
    public void search() {
        EntityManager en = emf.createEntityManager();

        Session session = (Session) en.getDelegate();
        Member member = Member.builder().memberName("박휘영").build();

        Example memberExample = Example.create(member);
//        Criteria criteria = session.createCriteria(Member.class).add(memberExample);



        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Member> criteria = builder.createQuery(Member.class);
        Root<Member> from = criteria.from(Member.class);

        criteria.select(from).where(builder.equal(from.get("name"), "박휘영"));

        //from.fetch("dd").


        //System.out.println("criteria = " + criteria.list());

        //criteria.list().stream().forEach(System.out::println);
    }
}
