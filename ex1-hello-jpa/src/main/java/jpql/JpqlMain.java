package jpql;

import cascade.Child;
import cascade.Parent;
import hellojpa.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import value.MemberV3;

import java.util.List;

public class JpqlMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        //code

        EntityTransaction tx = em.getTransaction();
        tx.begin(); //트랜잭션 시작

        try {

            /*jpql 기본형
            List<Member> result = em.createQuery(
                    "select m from Member m" //여기서의 Member는 테이블이 아닌 엔티티임.
                            + " where m.username like '%kim%'",
                    Member.class
            ).getResultList();

            for (Member member : result) {
                System.out.println("member: " + member);

            }*/

            //Criteria - 실무에서 안씀.
            /*CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<MemberV3> query = cb.createQuery(MemberV3.class);

            Root<MemberV3> m = query.from(MemberV3.class);

            CriteriaQuery<MemberV3> cq = query.select(m);

            String username = "asdd";
            if(username != null) {
                cq = cq.where(cb.equal(m.get("username"), "kim"));
            }

            em.createQuery(cq).getResultList();*/

            //QueryDSL - 복잡한 설정을 먼저 해줘야함. 결국 이걸 편하게 쓰려면 jpql을 알아야함.

            //네이티브sql
            /*em.createNativeQuery("select MEMBER_ID, CITY, STREET, ZIPCODE from MEMBERV3")
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
