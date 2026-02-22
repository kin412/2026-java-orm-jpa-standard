package jpql;

import jakarta.persistence.*;

import java.util.List;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpaMain");
        EntityManager em = emf.createEntityManager();
        //code

        EntityTransaction tx = em.getTransaction();
        tx.begin(); //트랜잭션 시작

        try {

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            //보통 setParameter, getResultList 이런건 메서드 체이닝으로 많이씀
            TypedQuery<Member> query = em.createQuery(
                    "select m from Member m where m.username = :username", Member.class);
            query.setParameter("username", "member1");

            //username만 가져올때. 타입을 지정했으므로 TypedQuery<String>
            TypedQuery<String> query2 = em.createQuery("select m.username from Member m", String.class);

            //username과 age만 가진 클래스는 없으므로 뒤에 타입을 지정할수 없음. 반환타입이 명확하지 않은 경우는 query 사용
            Query query3 = em.createQuery("select m.username, m.age from Member m");

            //getResultList - 컬렉션 반환 시 사용
            List<Member> resultList = query.getResultList();

            //결과 단건만 가져올때. 단일 객체. 한건은 무조건 나올때만 써야함. 안그러면 예외. 그래서 잘 안쓰려함.
            // Member result = query.getSingleResult();

            for (Member member1 : resultList) {
                System.out.println("member1 = " + member1);
            }

            em.flush();
            em.clear();

            Member member2 = new Member();
            member2.setUsername("member2");
            member2.setAge(20);
            em.persist(member2);

            em.flush();
            em.clear();

            List<Member> result = em.createQuery("select m from Member m", Member.class)
                    .getResultList();

            Member findMember = result.get(1);
            findMember.setAge(90);

            /*
            //페이징테스트를 위해
            for (int i=0; i<100; i++){
                Member member3 = new Member();
                member3.setUsername("member" + i);
                member3.setAge(i);
                em.persist(member3);
            }

            em.flush();
            em.clear();

            //페이징
            List<Member> result1 = em.createQuery("select m from Member m order by m.age desc", Member.class)
                    //setFirstResult((currentPage - 1) * pageSize)
                    //setMaxResults(pageSize)
                    .setFirstResult(5)
                    .setMaxResults(10)
                    .getResultList();

            System.out.println("result1.size() = " + result1.size());
            for (Member member1 : result1) {
                System.out.println("member1 = " + member1);
            }

            */

            em.flush();
            em.clear();

            //조인

            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member4 = new Member();
            member4.setUsername("member4");
            member4.setAge(10);
            member4.setTeam(team);

            em.persist(member4);

            em.flush();
            em.clear();

            String teamName = "teamA";

            String queryJoin = "select m from Member m inner join m.team t where t.name = :teamName";
            List<Member> resultJoin = em.createQuery(queryJoin, Member.class)
                    .setParameter("teamName", teamName)
                    .getResultList();

            System.out.println("teamName = " + resultJoin.get(0).getTeam().getName());
            System.out.println("memberUsername = " + resultJoin.get(0).getUsername());

            //서브쿼리는 비슷하나 from절에서 못씀. 인라인뷰 불가.
            // 조인으로 풀어서 해결 하던가 안되면 jdbc로 해결하는 수밖에 없음.

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }finally {
            em.close();
        }
        emf.close();
    }

}
