package hellojpa;

import jakarta.persistence.*;

import java.util.List;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        //code

        EntityTransaction tx = em.getTransaction();
        tx.begin(); //트랜잭션 시작

        try {

            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");

            //member1을 팀A에 소속시키고 싶다면
            //em.persist(team); 이시점에 시퀀스전략으로 db에 붙어서 시퀀스만 가져옴. 저장은 안함
            //근데 이게 객체 스럽지 못함. 아래의 과정으로 팀을 또 찾아야함.
            /*member.setTeamId(team.getId());
            em.persist(member);

            Member findMember = em.find(Member.class, member.getId());

            Long findTeamId = findMember.getTeamId();
            Team findTeam = em.find(Team.class, findTeamId);*/

            //member에 연관관계 manyToOne을 사용한 후
            // 이런식으로 그냥 team을 가져올수있음.
            //위에서 setName을 했고, id의 경우 시퀀스 전략으로 가져왔기 때문에
            // team객체에는 id, name이 존재
            member.setTeam(team);

            //member를 1차캐시에 저장 및 쓰기 지연 저장소에 쿼리생성
            em.persist(member);

            //아래 find 쿼리를 직접 보고싶을 경우 일단 flush를 날린 후
            //영속성 컨텍스트를 비워서 1차캐시를 비운다.
            //그럼 find 시 1차캐시가 비워져있기때문에
            //db까지가서 쿼리로 조회 후 가져오기때문에 아래 find 쿼리가 보인다.
            // clear하지 않는다면 아래의 findmember는 그냥 위에서 em.persist(member);
            //한 member가 1차캐시에 저장되어 있으므로 db까지 가지않고 그냥 저 member를 반환

            /*양방향의 경우
            member.setTeam(team); 을 통해서 member의 team은 set함.
            반대로 team의 List<Member> members 는 set하지 않았음.
            아래 find쿼리는 member가 1차 캐시에 있는 것을 보고 실행되지 않고
            그냥 1차캐시에 있는 member를 return 함
            db에서 데이터를 가져오지 않았고 team의 List<Member> members는
            set하지 않았기 때문에 아래의 findMember.getTeam().getMembers();
            이 구문은 member가 없어서 size가 0.
            flush() 후 clear()하면 위에서 만든 team과 member가 저장된 후
             1차 캐시와 쓰기지연 저장소가 비기때문에
            아래의 find시 db에서 데이터를 가져오므로 연관관계가 매핑되어 size가 1
            */
            /*em.flush();
            em.clear();*/

            Member findMember = em.find(Member.class, member.getId());

            //단방향
            Team findTeam = findMember.getTeam(); // 이런식으로 그냥 team을 가져올수있음.
            System.out.println("findTeam = " + findTeam);
            //

            //양방향
            List<Member> members = findMember.getTeam().getMembers();

            System.out.println("members.size() = " + members.size());

            for (Member m : members) {
                System.out.println("Member m = " + m.getUsername());
            }
            //

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();
    }
}
