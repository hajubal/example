package app;

import app.entity.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class SampleApp {

    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

    public static void main(String[] args) {
        //test1();
        //test2();

        emf.close();
    }

    private static void test1() {
        EntityManager en = emf.createEntityManager();

        EntityTransaction transaction = en.getTransaction();

        transaction.begin();

        en.persist(Member.builder().memberName("박휘영").build());
        en.persist(Member.builder().memberName("하주헌").build());

        transaction.commit();

        en.close();
    }

    private static void test2() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin(); // [트랜잭션] 시작

        // 영속 엔티티 조회
        Member member = em.find(Member.class, 1l);

        System.out.println(member);

        // 영속 엔티티 데이터 수정
        member.setMemberName("박휘영");


        //em.update(member) 이런 코드가 있어야 하지 않을까?

        transaction.commit(); // [트랜잭션] 커밋

        em.close();
    }

}
