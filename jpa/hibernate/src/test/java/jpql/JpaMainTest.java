package jpql;


import jpql.entity.Member;
import jpql.entity.Team;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.*;
import java.util.List;

public class JpaMainTest {

    static EntityManagerFactory emf;

    @BeforeAll
    static void setup() {
        emf = Persistence.createEntityManagerFactory("hello");

        EntityManager en = emf.createEntityManager();

        EntityTransaction transaction = en.getTransaction();

        transaction.begin();

        Team team = Team.builder().name("team").build();
        en.persist(team);

        en.persist(Member.builder().username("박휘영").age(20).team(team).build());
        en.persist(Member.builder().username("하주헌").age(20).team(team).build());

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

//        Team team = Team.builder().name("1team").members(Arrays.asList(Member.builder().username("member1").build()
//                , Member.builder().username("member1").build()
//                , Member.builder().username("member2").build()
//        )).build();
//


//        en.persist(team);

        Team.builder().build();


        transaction.commit();

        en.close();
    }

    @Test
    public void search1() {
        EntityManager en = emf.createEntityManager();

        TypedQuery<Member> query = en.createQuery("select m from Member as m", Member.class);
        List<Member> resultList = query.getResultList();

        Assertions.assertEquals(resultList.size(), 2);

        en.close();
    }

    @Test
    public void search2() {
        EntityManager en = emf.createEntityManager();

        TypedQuery<Member> query = en.createQuery("select m from Member as m where username = :username"
                , Member.class);
        query.setParameter("username", "박휘영");

        List<Member> resultList = query.getResultList();

        System.out.println("resultList = " + resultList);

        Assertions.assertEquals(resultList.get(0).getUsername(), "박휘영");

        en.close();
    }

    @Test
    public void search3() {
        EntityManager en = emf.createEntityManager();

        EntityTransaction transaction = en.getTransaction();

        transaction.begin();

        Team team = Team.builder().name("team2").build();
        en.persist(team);

        Member member1 = Member.builder().username("박휘영").age(20).team(team).build();
        en.persist(member1);
        Member member2 = Member.builder().username("하주헌").age(20).team(team).build();
        en.persist(member2);

        en.flush();

        /**
         * clear를 해서 컨텍스트를 비워야 연관관계 데이터를 DB로 부터 가져온다.
         * clear를 하지 않으면 team.getId()로 컨텍스트로 부터 데이터로 가져오면 위에 Team객체 생성 시 빈값으로
         * 들어가있는 member 객체가 리턴된다.
         */
        en.clear();

        Team team1 = en.find(Team.class, team.getId());

        Assertions.assertEquals(team1.getMembers().size(), 2);

        transaction.commit();

        en.close();
    }
}
