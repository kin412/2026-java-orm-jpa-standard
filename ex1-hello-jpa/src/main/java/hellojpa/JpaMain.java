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
            /*Member member = new Member();
            member.setId(2L);
            member.setName("HelloB");
            em.persist(member);*/

            //jpql 예시 - 객체 대상 조회 Member가 테이블이 아니라 객체 Member임
            //List<Member> result = em.createQuery("select m from Member", Member.class).getResultList();

            Member findMember = em.find(Member.class, 1L);

            findMember.setName("HelloJPA");

            em.persist(findMember);

            tx.commit(); // 트랜잭션 커밋
        } catch (Exception e) {
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();
    }
}
