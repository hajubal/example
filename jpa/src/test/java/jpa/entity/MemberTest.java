package jpa.entity;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Arrays;

public class MemberTest {

    static EntityManagerFactory emf;

    @BeforeAll
    static void tearup() {
        emf = Persistence.createEntityManagerFactory("hello");
    }

    @AfterAll
    static void shutdown() {
        emf.close();
    }

    @Test
    public void insert() {
        EntityManager entityManager = emf.createEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();

        Team team = Team.builder().name("1team").members(Arrays.asList(Member.builder().name("member1").build()
                , Member.builder().name("member1").build()
                , Member.builder().name("member2").build()
        )).build();

        entityManager.persist(team);

        transaction.commit();

        entityManager.close();
    }
}
