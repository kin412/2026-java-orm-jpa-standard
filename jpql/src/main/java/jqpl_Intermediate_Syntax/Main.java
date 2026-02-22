package jqpl_Intermediate_Syntax;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jpql.Member;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpaMain");
        EntityManager em = emf.createEntityManager();
        //code

        EntityTransaction tx = em.getTransaction();
        tx.begin(); //트랜잭션 시작

        try {

            Member member = new Member();
            member.setUsername("관리자1");
            member.setAge(20);
            em.persist(member);

            Member member2 = new Member();
            member2.setUsername("관리자200");
            member2.setAge(20);
            em.persist(member2);

            em.flush();
            em.clear();

            String query = "select m from Member m"; //상태필드
            //아래 두개는 그냥 쓰지마라
            //String query2 = "select m.team.블라블라 from Member m"; //단일값 연관경로
            //String query3 = "select t.members.size from Team t"; // 컬렉션 값 연관경로
            List<Member> result = em.createQuery( query, Member.class)
                    .getResultList();

            for (Member member1 : result) {
                System.out.println("member1: " + member1);
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
