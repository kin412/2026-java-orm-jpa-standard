package value;

import hellojpa.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;
import java.util.Set;

public class ValueMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        //code

        EntityTransaction tx = em.getTransaction();
        tx.begin(); //트랜잭션 시작

        try {

           MemberV3 member = new MemberV3();
           member.setUsername("hello");
           member.setHomeAddress(new Address("city", "street", "zipcode"));
           member.setPeriod(new Period());

           em.persist(member);

           em.flush();
           em.clear();

           Address address = new Address("city", "street", "10000");

           MemberV3 member2 = new MemberV3();
           member2.setUsername("member1");
           member2.setHomeAddress(address);
           em.persist(member2);

           //값타입에 객체같이 참조하는 것을 넣을땐 이렇게 복사를해서 써야함. 안 그럼 값타입을 이곳저곳에서 공유참조 하게됨.
            // 공유참조한다는 것은 값타입 사용에 위배됨. 공유참조 할거면 엔티티로 관리해야함
            //그래서 불변객체는 setter가 없고 생성자에서 값을 세팅해주므로, 다른값을 넣어줄거면 새로 만들어줘야한다.
            //이런 작은 제약 조건을 검으로써 공유참조의 공포에서 벗아날수 있다.
           Address copyAddress = new Address(address.getCity(), address.getStreet(), address.getZipcode());

           MemberV3 member3 = new MemberV3();
           member3.setUsername("member2");
           member3.setHomeAddress(copyAddress);
           em.persist(member3);

           //그냥 이렇게 하면 다바뀜. 값타입은 이렇게 사용하면 안됨.
            member2.getHomeAddress().setCity("newCity");

            em.flush();
            em.clear();

            MemberV3 member4 = new MemberV3();
            member4.setUsername("member4");
            member4.setHomeAddress(new Address("homeCity", "street22", "10000"));

            member4.getFavoriteFoods().add("치킨");
            member4.getFavoriteFoods().add("족발");
            member4.getFavoriteFoods().add("피자");

            member4.getAddressesHistory().add(new Address("old1", "street22", "10000"));
            member4.getAddressesHistory().add(new Address("old2", "street22", "10000"));

            em.persist(member4);

            em.flush();
            em.clear();

            System.out.println("===============START========================");
            MemberV3 findMember = em.find(MemberV3.class, member4.getId());

            List<Address> addressesHistory = findMember.getAddressesHistory();
            for (Address address1 : addressesHistory) {
                System.out.println("address1: " + address1.getCity() + " " + address1.getStreet() + " " + address1.getZipcode());
            }

            Set<String> favoriteFoods = findMember.getFavoriteFoods();
            for (String favoriteFood : favoriteFoods) {
                System.out.println("favoriteFood: " + favoriteFood);
            }

            em.flush();
            em.clear();

            //값타입 수정
            System.out.println("===============값 타입 수정========================");
            MemberV3 findMember1 = em.find(MemberV3.class, member4.getId());

            // 이렇게 하면 사이드이펙트가 생길수 있음. 불변객체를 사용하는 방식으로 바꿔야함.
            //findMember1.getHomeAddress().setCity("newCity");

            Address a = findMember1.getHomeAddress();
            //불변 객체 - 그냥 새로 생성해서 갈아끼워야함.
            findMember1.setHomeAddress(new Address("newCity4", a.getStreet(), a.getZipcode()));

            //치킨 -> 한식
            findMember1.getFavoriteFoods().remove("치킨");
            findMember1.getFavoriteFoods().add("한식");

            // equals와 hashcode가 중요한 이유
            findMember1.getAddressesHistory().remove(new Address("old1", "street22", "10000"));
            findMember1.getAddressesHistory().add(new Address("seoulland", "go_street", "9999"));

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
