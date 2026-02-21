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
            //근데 이게 객체 스럽지 못함. 아래의 긴과정으로 팀을 또 찾아야함.
            /*member.setTeamId(team.getId());
            em.persist(member);

            Member findMember = em.find(Member.class, member.getId());

            Long findTeamId = findMember.getTeamId();
            Team findTeam = em.find(Team.class, findTeamId);*/

            //member에 연관관예 manyToOne을 사용한 후
            member.setTeam(team); // 이런식으로 그냥 team을 가져올수있음.

            em.persist(member);

            //아래 find 쿼리를 직접 보고싶을 경우 일단 flush를 날린 후 영속성 컨텍스트를 비워서 1차캐시를 비운다.
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
