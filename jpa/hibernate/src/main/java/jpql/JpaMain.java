package jpql;

import jpql.entity.Member;
import jpql.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction transaction = em.getTransaction();

        transaction.begin();

        Team team = Team.builder().name("team").build();

        em.persist(team);

        System.out.println("team = " + team);

        Member member = Member.builder().username("member").age(20).team(team).build();

        em.persist(member);

        Member memberResult = em.find(Member.class, member.getId());

        System.out.println("memberResult = " + memberResult);

        transaction.commit();

        memberResult = em.find(Member.class, member.getId());

        System.out.println("memberResult = " + memberResult);

        System.out.println("memberResult.getTeam() = " + memberResult.getTeam().getMembers());

        System.out.println("em.find(Team.class, team.getId()).getMembers() = " + em.find(Team.class, team.getId()).getMembers());

        em.close();

        emf.close();

    }
}
