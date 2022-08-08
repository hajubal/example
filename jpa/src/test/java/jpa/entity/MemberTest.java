package jpa.entity;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
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
        EntityManager en = emf.createEntityManager();

        EntityTransaction transaction = en.getTransaction();

        transaction.begin();

        Team team = Team.builder().teamName("1team").members(Arrays.asList(Member.builder().memberName("member1").build()
                , Member.builder().memberName("member1").build()
                , Member.builder().memberName("member2").build()
        )).build();

        en.persist(team);

        Team findTeam = en.find(Team.class, team.getTeamId());

        Assertions.assertTrue(findTeam.getMembers().size() == 3);

        transaction.commit();

        en.close();
    }
}
