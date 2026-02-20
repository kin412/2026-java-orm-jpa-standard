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
            /*
            Member member = new Member();
            member.setId(2L);
            member.setName("HelloB"); // 객체 생성부터 여기까지가 비영속 상태

            // 엔티티 매니저 안의 영속성 컨텍스트로 관리되는 영속 상태
            em.persist(member);
            em.detach(member); // 회원 엔티티를 영속성 컨텍스트에서 분리, 준영속 상태
            em.remove(member); 객체를 삭제한 상태
            */

            //jpql 예시 - 객체 대상 조회 Member가 테이블이 아니라 객체 Member임
            //List<Member> result = em.createQuery("select m from Member", Member.class).getResultList();

            /*Member findMember = em.find(Member.class, 1L);

            findMember.setName("HelloJPA");

            //업데이트시에는 setName으로 변경해두면 변경된 채로 1차캐시에 두고
            //트랜잭션 커밋시 엔티티와 스냅샷을 비교해 업데이트쿼리가 실행되므로 persist를 하지않는게 맞음
            em.persist(findMember);*/

            //영속성 컨텍스트에서 같은 트랜잭션 안이라면 동일성 보장.
            //자바컬렉션에서 같은 객체를 꺼냈을때 유지되는 것처럼.
            //Member findMember2 = em.find(Member.class, 1L);
            //Member findMember3 = em.find(Member.class, 1L);

            //영속성 컨텍스트는 최초 findMember2 조회시 member, 1L 조건에 대한 데이터를 영속성 컨텍스트안의
            //1차캐시에 보관함. 그리고 findMember3 조회 시 그 값을 그대로 줌.
            // 그렇기에 db에서 불러오는 쿼리로그는 한번만 찍히고 둘이 같은 객체를 가지게 되므로 true
            //System.out.println(findMember2 == findMember3);

            //영속
            /*MemberV1 memberV11 = new MemberV1(150L, "A");
            MemberV1 memberV12 = new MemberV1(160L, "B");

            em.persist(memberV11);
            em.persist(memberV12);*/

            //em.flush(); // 트랜잭션 커밋전에 강제반영

            //System.out.println("=======실행확인선============");

            // 트랜잭션 커밋. 커밋하는 순간 flush()를 통해 cud를 실행함.
            // r은 트랜잭션이 아니어도 되지만 동일성 보장을 위해 트랜색션 안에서 하는게 좋음
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();
    }
}
