package jpql.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@EqualsAndHashCode
@Entity
public class Team {

    @Id @GeneratedValue
    private Long id;

    private String name;

    /**
     * 연관관계의 주인 설정
     * mappedBy만 설정할 경우 조회만 가능
     * cascade: ALL 설정 시 객체 업데이트 시 주인 객체도 업데이트
     * orphanRemoval: 객체 삭제시 주인 객체도 삭제
     */
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Member> members = new ArrayList<>();

    /**
     * @Singular: collection type의 객체에 추가 함수를 자동으로 생성한다.
     *
     * @param name
     * @param members
     */
    @Builder
    public Team(String name, @Singular List<Member> members) {
        this.name = name;
        this.members = members;
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", members=" + members.size() +
                '}';
    }
}
