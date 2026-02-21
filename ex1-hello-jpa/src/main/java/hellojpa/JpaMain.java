package hellojpa;

import jakarta.persistence.*;

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

            //member에 연관관예 manyToOne을 사용한 후
            // 이런식으로 그냥 team을 가져올수있음.
            //위에서 setName을 했고, id의 경우 시퀀스 전략으로 가져왔기 때문에
            // team객체에는 id, name이 존재
            member.setTeam(team);

            em.persist(member);

            //아래 find 쿼리를 직접 보고싶을 경우 일단 flush를 날린 후
            //영속성 컨텍스트를 비워서 1차캐시를 비운다.
            //그럼 find 시 1차캐시가 비워져있기때문에
            //db까지가서 쿼리로 조회 후 가져오기때문에 쿼리가 보인다.
            /*em.flush();
            em.clear();*/

            Member findMember = em.find(Member.class, member.getId());

            Team findTeam = findMember.getTeam(); // 이런식으로 그냥 team을 가져올수있음.
            System.out.println("findTeam = " + findTeam);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();
    }
}
