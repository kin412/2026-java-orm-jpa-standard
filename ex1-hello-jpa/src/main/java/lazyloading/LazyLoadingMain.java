package lazyloading;

import hellojpa.Member;
import hellojpa.Team;
import jakarta.persistence.*;

import java.util.List;

public class LazyLoadingMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        //code

        EntityTransaction tx = em.getTransaction();
        tx.begin(); //트랜잭션 시작

        try {

            Team team = new Team();
            team.setName("team1");
            em.persist(team);

            Member member = new Member();
            member.setUsername("hello1");
            member.setTeam(team);
            em.persist(member);

            em.flush();
            em.clear();

            Member m = em.find(Member.class, member.getId());

            System.out.println("m.getTeam().getClass(): " + m.getTeam().getClass());

            System.out.println("======================================");
            System.out.println("m.getTeam().getName(): " + m.getTeam().getName()); // 지연로딩시 db조회 (초기화)
            System.out.println("======================================");

            em.flush();
            em.clear();

            /*Member m1 = em.find(Member.class, member.getId());

            //member내에 team이 즉시로딩 설정되어 있을 경우 team조회하는 쿼리가 같이 나감.
            //근데 이게 실무에서처럼 member내에 즉시로딩 설정되어 있는 테이블이 수백개라면?
            //= member는 한개지만 수많은 테이블이 조회되는 쿼리가 나감 = N+1 문제
            List<Member> members = em.createQuery("select m from Member m", Member.class)
                    .getResultList();*/



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
