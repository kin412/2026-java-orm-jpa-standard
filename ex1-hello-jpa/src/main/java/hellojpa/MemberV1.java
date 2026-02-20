package hellojpa;

import jakarta.persistence.Id;

//@Entity
//@Table(name = "MBR")
public class MemberV1 {

    @Id
    private Long id;
    private String name;

    //엔티티는 기본생성자가 반드시 있어야함. 아예 오버로딩한 생성자가 없을경우는 생략할수 있지만
    //오버로딩한 아래같은 생성자가 있을경우는 기본생성자가 없어지므로 이렇게 명시 해주어야함.
    public MemberV1() {
    }

    public MemberV1(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
