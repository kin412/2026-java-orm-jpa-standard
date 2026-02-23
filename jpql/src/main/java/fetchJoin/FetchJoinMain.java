package fetchJoin;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jpql.Member;
import jpql.Team;

import java.util.List;

public class FetchJoinMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpaMain");
        EntityManager em = emf.createEntityManager();
        //code

        EntityTransaction tx = em.getTransaction();
        tx.begin(); //트랜잭션 시작

        try {

            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member = new Member();
            member.setUsername("회원1");
            member.setAge(20);
            member.setTeam(teamA);
            em.persist(member);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setAge(20);
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setAge(20);
            member3.setTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();

            //fetch 조인 사용 전
            //String query = "select m from Member m";

            //fetch 조인 사용 후
            String query = "select m from Member m join fetch m.team";
            List<Member> result = em.createQuery( query, Member.class)
                    .getResultList();

            //fetch 조인 일대다 문제
            /*
            String query = "select t from Team t join fetch t.members";

            //일대다에서 fetch join의 문제를 해결하기 위해 jpa에서 distinct를 쓰면 됨.
            //하이버네이트6이상에선 이걸 자동으로 해줌
            String query = "select distinct t from Team t join fetch t.members";
            List<Team> result = em.createQuery( query, Team.class)
                    .getResultList();

            for (Team team : result) {
                System.out.println("team.getName: " + team.getName() + ", team.getMember: " + team.getMembers());
                for (Member mem : team.getMembers()) {
                    System.out.println("mem = " + mem);
                }
            }*/

            for (Member member1 : result) {
                //System.out.println("member: " + member1); //멤버만 잘나옴


                /*fetch 조인 사용 전 - proxy
                팀정보도 나오지만 새로운 팀을 호출 할때마다 팀정보를 그때그때 db에 가서 가져옴
                그래서 회원1을 조회했을때 팀A에 대한 정보를 이미 가져오기때문에
                회원2 조회 시에는 팀A에 대한 정보가 영속성컨텍스트(정확히는 1차캐시)에 이미 있어서 더 db에 가지 않지만
                회원3 조회 시에는 팀B에 대한 정보가 영속성컨텍스트(정확히는 1차캐시)에 없기 때문에 db에 가서 가져오는것.

                N+1 문제는 즉시로딩만의 문제다. 즉시로딩은 N+1을 컨트롤 할수 없다.
                지연로딩도 결국 조인한 엔티티를 가져오려면 DB접근 수는 N+1 로 즉시로딩과 동일하다.
                하지만 지연로딩은 이것이 문제되지 않는다.
                왜냐하면 지연로딩은 그 사용의도 자체가 내가 지금당장 필요한것만 로딩하고 나중에 필요한건 그때가서 로딩하겠다는 것이기 때문.

                fetch 조인 사용 후
                fetch 조인은 쿼리를 짜는 시점부터 조인을 명시함.
                그렇기 때문에 db에 한번더 가지않고, 그냥 애초에 갈때 그냥 한번에 조인해서 member랑 team을 다들고옴.
                그래서 영속성컨텍스트(1차 캐시)에 이미 다 있기 때문에 db에 더가는 N+1 문제가 발생하지 않음.*/


                System.out.println("member: " + member1 + " || teamName: " + member1.getTeam().getName());
            }


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
