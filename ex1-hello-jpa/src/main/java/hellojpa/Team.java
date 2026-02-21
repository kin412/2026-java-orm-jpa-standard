package hellojpa;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Team extends BaseEntity{
    @Id
    @GeneratedValue
    @Column(name = "TEAM_ID")
    private Long id;
    private String name;

    //양방향 연관관계
    // 이 "team"은 Member객체 내부의 private Team team; 이 team.
    //mappedBy - 반대방향의 주인의 team 멤버변수에 매핑된다.
    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<Member>();

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
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
