package app.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "team")
public class Team {

    @Id
    @GeneratedValue
    @Column(name = "team_id")
    private Long teamId;

    @Column(name = "team_name")
    private String teamName;

    private String description;

    /**
     * 연관관계 주인 객체 참조 mappedBy
     *
     * cascade(종속): 영속성 전이. 부모의 객체에서 데이터 변경 시 컨텍스트에 적용됨.
     * 예) mmappedby가 선언되어 있으므로 연관관계의 주인인 아니라서 Team 객체에서 members list에 데이터를 추가해도 컨텍스트에
     * 추가되지 않으나 cascade를 설정하면 추가됨
     *
     * orphanRemoval(고아): 부모의 연관관계가 끊겼을 때 컨텍스트에서 삭제됨.
     * 예) Team 객체의 members list에서 객체를 삭제하면 컨텍스트에서 삭제됨.
     */
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Member> members = new ArrayList<>();

    @Builder
    public Team(String teamName, String description, List<Member> members) {
        this.teamName = teamName;
        this.description = description;
        this.members = members;
    }
}
