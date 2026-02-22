package proxyrelationship;

import hellojpa.Member;
import hellojpa.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class ProxyMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        //code

        EntityTransaction tx = em.getTransaction();
        tx.begin(); //트랜잭션 시작

        try {

           /* Member member = em.find(Member.class, 1L);
            printMember(member);
            printMemberAndTeam(member);*/

            Member member = new Member();
            member.setUsername("hello");
            em.persist(member);

            em.flush();
            em.clear();

            //find라 커밋이나 flush전에 조회해 오기때문에 쿼리문 후에 id와 username이 찍힘
            //Member findMember = em.find(Member.class, member.getId());

            //getReference - 데이터베이스 조회를 미루는 가짜(프록시) 엔티티 객체 조회.
            Member findMember = em.getReference(Member.class, member.getId());
            System.out.println("findMember.getClass(): " + findMember.getClass());

            /*em.getReference(Member.class, member.getId());에서 id를 넣어줬으므로
            db까지 조회하지 않고 값 출력.*/
            System.out.println("findMember.id = " + findMember.getId());

            /*첫번째 호출에는 username이 없으므로 em.getReference이기때문에 호출되면
            그제서야 db에서 조회 후 값을 가져옴. -> 프록시를 이용해 지연로딩.
            두번째 호출에서는 이미 1차 캐시에 가져온 값이 있으므로
            db까지 가지 않으므로 쿼리로그가 찍히지 않음
             */
            System.out.println("findMember.username = " + findMember.getUsername());
            System.out.println("findMember.username = " + findMember.getUsername());


            Member member1 = new Member();
            member1.setUsername("hello1");
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("hello2");
            em.persist(member2);

            em.flush();
            em.clear();

            /*Member m1 = em.find(Member.class, member1.getId());
            Member m2 = em.find(Member.class, member2.getId());
            // == 은 타입 비교기 때문에 find의 반환타입은 Member로 같으므로 true
            logic(m1, m2);*/


            Member m1 = em.find(Member.class, member1.getId());
            Member m2 = em.getReference(Member.class, member2.getId());
            //getReference의 반환 타입은 프록시객체이므로 Memeber와 달라서 false
            //그래서 getReference의 타입 비교시에는 instanceof를 써야함
            logic(m1, m2);

            em.flush();
            em.clear();

            Member m3 = em.find(Member.class, member1.getId());
            Member m4 = em.getReference(Member.class, member1.getId());
            //하나의 영속성 컨텍스트라면 true임.
            //jpa는 처음 find를 한 순간 1차 캐시에 이미 member 조회값을 담아두기때문에
            //getReference시에 이미 1차캐시에 담긴 객체의 주소값을 반환함
            System.out.println("m3 == m4 = " + (m3 == m4));

            em.flush();
            em.clear();

            /*지연로딩은 하나의 영속성컨텍스트내에서 작동하므로
            detach - 영속성 컨텍스트 바깥으로 쫒아버림
            close - 영속성 컨텍스트를 닫음. 위와동일
            clear - 영속성 컨텍스를 비웠으므로 참고해야할 getReference 정보가 없음
            시에 작동하지 않음.
             */
            //Member refMember = em.getReference(Member.class, member1.getId());
            //System.out.println("refMember = " + refMember.getClass());//proxy

            //em.detach(refMember);
            //em.close();
            //em.clear();

            //refMember.getUsername(); // 에러

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }finally {
            em.close();
        }
        emf.close();
    }

    private static void logic(Member m1, Member m2) {
        // == 은 타입 비교기 때문에 find의 반환타입은 Member로 같으므로 true
        System.out.println("m1 == m2 = " + (m1.getClass() == m2.getClass()));

        //getReference의 반환 타입은 프록시객체이므로 Memeber와 달라서 false
        //그래서 getReference의 타입 비교시에는 instanceof를 써야함
        System.out.println("m1 instanceof Member = " + (m1 instanceof Member));
        System.out.println("m2 instanceof Member = " + (m2 instanceof Member));
    }

    private static void printMember(Member member) {
        //team 정보는 필요없는 케이스 = 지연로딩과 proxy로 해결
        System.out.println("Member = " + member.getUsername());
    }

    private static void printMemberAndTeam(Member member) {
        //team 정보까지 가져오는 케이스

        String username = member.getUsername();
        System.out.println("username = " + username);

        Team team = member.getTeam();
        System.out.println("team = " + team.getName());

    }
}
