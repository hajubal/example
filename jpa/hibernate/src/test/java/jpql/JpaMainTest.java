package jpql;


import jpql.dto.MemberDTO;
import jpql.entity.Member;
import jpql.entity.Team;
import jpql.type.UserType;
import org.junit.jupiter.api.*;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

public class JpaMainTest {

    static EntityManagerFactory emf;

    @BeforeAll
    static void setup() {
        emf = Persistence.createEntityManagerFactory("hello");

        EntityManager en = emf.createEntityManager();

        EntityTransaction transaction = en.getTransaction();
        try {

            transaction.begin();

            Team team = Team.builder().name("team").build();
            en.persist(team);

            en.persist(Member.builder().username("박휘영").age(20).team(team).userType(UserType.USER).build());
            en.persist(Member.builder().username("하주헌").age(20).team(team).userType(UserType.ADMIN).build());

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            en.close();
        }
    }

    @AfterAll
    static void shutdown() {
        emf.close();
    }

    @Test
    public void insert() {
        EntityManager en = emf.createEntityManager();

        EntityTransaction transaction = en.getTransaction();

        try {

            transaction.begin();

//        Team team = Team.builder().name("1team").members(Arrays.asList(Member.builder().username("member1").build()
//                , Member.builder().username("member1").build()
//                , Member.builder().username("member2").build()
//        )).build();
//


//        en.persist(team);

            Team.builder().build();


            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            en.close();
        }
    }

    @Test
    public void search1() {
        EntityManager en = emf.createEntityManager();

        EntityTransaction transaction = en.getTransaction();

        transaction.begin();

        try {

            TypedQuery<Member> query = en.createQuery("select m from Member as m", Member.class);
            List<Member> resultList = query.getResultList();

            Assertions.assertEquals(2, resultList.size());

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            en.close();
        }
    }

    @Test
    public void search2() {
        EntityManager en = emf.createEntityManager();

        EntityTransaction transaction = en.getTransaction();

        transaction.begin();

        try {
            TypedQuery<Member> query = en.createQuery("select m from Member as m where username = :username"
                    , Member.class);
            query.setParameter("username", "박휘영");

            List<Member> resultList = query.getResultList();

            System.out.println("resultList = " + resultList);

            Assertions.assertEquals("박휘영", resultList.get(0).getUsername());

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            en.close();
        }
    }

    @Test
    @DisplayName("연관관계 테스트")
    public void search3() {
        EntityManager en = emf.createEntityManager();

        EntityTransaction transaction = en.getTransaction();

        transaction.begin();

        try {

            Team team = Team.builder().name("team1").build();
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
             * 이런게 번거롭다면 연관관계 주인객체에 연관관계 편의성 method를 만들어야 한다
             */
            en.clear();

            Team teamResult = en.find(Team.class, team.getId());

            Assertions.assertEquals(2, teamResult.getMembers().size());


            /**
             * 연관관계 편의성 method 사용
             */
            Team team2 = Team.builder().name("team2").build();
            en.persist(team2);

            Member member3 = Member.builder().username("박휘영").age(20).build();
            member3.changTeam(team2);
            en.persist(member3);

            Member member4 = Member.builder().username("하주헌").age(20).build();
            member4.changTeam(team2);
            en.persist(member4);

            teamResult = en.find(Team.class, team2.getId());

            Assertions.assertEquals(2, teamResult.getMembers().size());

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            en.close();
        }
    }

    @Test
    @DisplayName("결과조회 객체 영속성")
    public void search4() {
        EntityManager en = emf.createEntityManager();

        EntityTransaction transaction = en.getTransaction();

        transaction.begin();

        try {

            /**
             * 결과로 나온 객체들은 영속성 관리 대상임
             */
            List<Member> result = en.createQuery("select m from Member as m", Member.class).getResultList();

            Assertions.assertEquals(result.size(), 2);

            Member resultMember = result.get(0);

            /**
             * 객체를 업데이트
             */
            resultMember = Member.builder().username("clone").age(10).build();

            en.persist(resultMember);

            result = en.createQuery("select m from Member as m", Member.class).getResultList();

            /**
             * 결과 확인
             */
            result.stream().forEach(System.out::println);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            en.close();
        }
    }

    @Test
    @DisplayName("묵시적 조인, 명시적 조인")
    public void search5() {
        EntityManager en = emf.createEntityManager();

        EntityTransaction transaction = en.getTransaction();

        transaction.begin();

        try {
            /**
             * 묵시적 조인, 사용하지 말것. 코드상에 DB에서 실행될 쿼리가 보이지 않아 예측이 안됨. 튜닝하는데 어려움이 있음.
             */
            TypedQuery<Team> query = en.createQuery("select m.team from Member as m where username = :username"
                    , Team.class);
            query.setParameter("username", "박휘영");

            List<Team> resultList = query.getResultList();

            System.out.println("resultList = " + resultList);

            Assertions.assertEquals("team", resultList.get(0).getName());

            /**
             * 명시적 조인, 권장됨
             */
            query = en.createQuery("select t from Member as m join m.team t where username = :username"
                    , Team.class);
            query.setParameter("username", "박휘영");

            resultList = query.getResultList();

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            en.close();
        }
    }

    @Test
    @DisplayName("스칼라 타입")
    public void search6() {
        EntityManager en = emf.createEntityManager();

        EntityTransaction transaction = en.getTransaction();

        transaction.begin();

        try {
            /**
             * 배열로 리턴
             * enum type은 전체 페키지 경로를 다 입력해야함
             */
            List<Object[]> result = en.createQuery("select m.id, m.username, 'DATA', true, m.userType " +
                                    " from Member m where m.userType = jpql.type.UserType.ADMIN"
                    , Object[].class)
                    .getResultList();

            result.stream().forEach(obj -> Arrays.asList(obj).stream().forEach(System.out::println));

            /**
             * new DTO로 리턴
             */
            List<MemberDTO> dtoResult = en.createQuery("select new jpql.dto.MemberDTO(m.id, m.username) from Member m"
                    , MemberDTO.class).getResultList();

            dtoResult.stream().forEach(System.out::println);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            en.close();
        }
    }

    @Test
    @DisplayName("페이징")
    public void search7() {
        EntityManager en = emf.createEntityManager();

        EntityTransaction transaction = en.getTransaction();

        transaction.begin();

        try {
            for (int i = 0; i < 100; i++) {
                en.persist(Member.builder().username("user" + i).age(i).build());
            }

            en.flush();
            en.clear();

            List<Member> result = en.createQuery("select m from Member m order by m.age asc", Member.class)
                    .setFirstResult(0)
                    .setMaxResults(10)
                    .getResultList();

            result.stream().forEach(System.out::println);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            en.close();
        }
    }

    @Test
    @DisplayName("조인")
    public void search8() {
        EntityManager en = emf.createEntityManager();

        EntityTransaction transaction = en.getTransaction();

        transaction.begin();

        try {
            Team team1 = Team.builder().name("team1").build();
            Team team2 = Team.builder().name("team2").build();

            for (int i = 0; i < 100; i++) {
                if(i % 2 == 0)
                    en.persist(Member.builder().username("user" + i).age(i).build().changTeam(team1));
                else
                    en.persist(Member.builder().username("user" + i).age(i).build().changTeam(team2));
            }

            en.flush();
            en.clear();

            /**
             * inner join
             */
            List<Member> result = en.createQuery("select m from Member m inner join Team.t where t.name = :teamName", Member.class)
                    .setParameter("teamName", "team1")
                    .getResultList();

            Assertions.assertEquals(50, result.size());

            /**
             * left outer join
             */
            result = en.createQuery("select m from Member m inner left outer join Team.t where t.name = :teamName", Member.class)
                    .setParameter("teamName", "team1")
                    .getResultList();

            Assertions.assertEquals(50, result.size());

            /**
             * seta join
             */
            result = en.createQuery("select m from Member m, Team t where m.name = t.name", Member.class)
                    .getResultList();

            Assertions.assertEquals(0, result.size());

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            en.close();
        }
    }

    @Test
    @DisplayName("조인 필터링")
    public void search9() {
        EntityManager en = emf.createEntityManager();

        EntityTransaction transaction = en.getTransaction();

        transaction.begin();

        try {
            Team team1 = Team.builder().name("team1").build();
            Team team2 = Team.builder().name("team2").build();

            for (int i = 0; i < 100; i++) {
                if(i % 2 == 0)
                    en.persist(Member.builder().username("user" + i).age(i).build().changTeam(team1));
                else
                    en.persist(Member.builder().username("user" + i).age(i).build().changTeam(team2));
            }

            en.flush();
            en.clear();

            List<Member> result = en.createQuery("select m from Member m " +
                            "left join Team t on t.name = 'team1' where m.username = :username", Member.class)
                    .setParameter("username", "user1")
                    .getResultList();

            Assertions.assertEquals(1, result.size());

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            en.close();
        }
    }

    @Test
    @DisplayName("연관관계가 없는 엔티티 외부 조인")
    public void search10() {
        EntityManager en = emf.createEntityManager();

        EntityTransaction transaction = en.getTransaction();

        transaction.begin();

        try {
            Team team1 = Team.builder().name("team1").build();
            Team team2 = Team.builder().name("team2").build();

            for (int i = 0; i < 100; i++) {
                if(i % 2 == 0)
                    en.persist(Member.builder().username("user" + i).age(i).build().changTeam(team1));
                else
                    en.persist(Member.builder().username("user" + i).age(i).build().changTeam(team2));
            }

            en.flush();
            en.clear();

            List<Member> result = en.createQuery("select m from Member m " +
                            "left join Product p on m.username = p.name", Member.class)
                    .getResultList();

            Assertions.assertEquals(0, result.size());

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            en.close();
        }
    }

    @Test
    @DisplayName("case")
    public void search11() {
        EntityManager en = emf.createEntityManager();

        EntityTransaction transaction = en.getTransaction();

        transaction.begin();

        TypedQuery<String>
                query = en.createQuery("select " +
                        "case when m.age > 18 then '성인' " +
                             "when m.age < 18 then '미성년' " +
                             "else '인간이 아님' " +
                        "end " +
                        "from Member as m"
                , String.class);
        try {
            List<String> resultList = query.getResultList();

            System.out.println("resultList = " + resultList);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            en.close();
        }
    }

    @Test
    @DisplayName("fetch join")
    public void search12() {
        EntityManager en = emf.createEntityManager();

        EntityTransaction transaction = en.getTransaction();

        transaction.begin();

        try {
            Team team2 = Team.builder().name("team2").build();
            Team team3 = Team.builder().name("team3").build();

            en.persist(team2);
            en.persist(team3);

            Member member2 = Member.builder().username("member2").age(23).team(team2).build();

            en.persist(member2);

            en.flush();
            en.clear();

            TypedQuery<Member> query = en.createQuery("select m from Member as m", Member.class);
            List<Member> resultList = query.getResultList();

            /**
             * lazy, eager 든 쿼리가 여러번 날아가는건 똑같음. 호출 시점의 문제일 뿐. team 갯 수 만큼 쿼리 날아감. 해결은 fetch join
             */
            resultList.stream().forEach(result -> {
                System.out.println("result.getTeam().getName() = " + result.getTeam().getName());
            });

            System.out.println("================================");
            en.clear();

            /**
             * join으로 한방에 데이터 조회
             */
            query = en.createQuery("select m from Member as m join fetch m.team", Member.class);
            resultList = query.getResultList();

            resultList.stream().forEach(result -> {
                System.out.println("result.getTeam().getName() = " + result.getTeam().getName());
            });

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            en.close();
        }
    }

    @Test
    @DisplayName("collection fetch join")
    public void search13() {
        EntityManager en = emf.createEntityManager();

        EntityTransaction transaction = en.getTransaction();

        transaction.begin();

        try {
            Team team2 = Team.builder().name("team2").build();
            Team team3 = Team.builder().name("team3").build();

            en.persist(team2);
            en.persist(team3);

            Member member2 = Member.builder().username("member2").age(23).team(team2).build();

            en.persist(member2);

            en.flush();
            en.clear();

            /**
             * one to many는 데이터 중복으로 동일한 데이터가 여러개 나옴. 하지만 중복된 데이터는 컨텍스트에 동일한 객체이다.
             */
            TypedQuery<Team> query = en.createQuery("select t from Team as t join fetch t.members", Team.class);
            List<Team> resultList = query.getResultList();

            resultList.stream().forEach(result -> {
                System.out.print("Team = " + result.getName());
                result.getMembers().stream().forEach(
                        member -> System.out.print(", Member = " + member.getUsername())
                );
                System.out.println("");
            });

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            en.close();
        }
    }

    @Test
    @DisplayName("collection fetch join distinct")
    public void search14() {
        EntityManager en = emf.createEntityManager();

        EntityTransaction transaction = en.getTransaction();

        transaction.begin();

        try {
            Team team2 = Team.builder().name("team2").build();
            Team team3 = Team.builder().name("team3").build();

            en.persist(team2);
            en.persist(team3);

            Member member2 = Member.builder().username("member2").age(23).team(team2).build();

            en.persist(member2);

            en.flush();
            en.clear();

            /**
             * jpa의 distinct는 같은 식별자의 중복 데이터를 제거해줌.
             */
            TypedQuery<Team> query = en.createQuery("select distinct t from Team as t join fetch t.members", Team.class);
            List<Team> resultList = query.getResultList();

            resultList.stream().forEach(result -> {
                System.out.print("Team = " + result.getName());
                result.getMembers().stream().forEach(
                        member -> System.out.print(", Member = " + member.getUsername())
                );
                System.out.println("");
            });

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            en.close();
        }
    }
}
