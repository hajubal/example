package jpql.entity;


import jpql.type.UserType;
import lombok.*;
import org.hibernate.annotations.ColumnTransformer;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@EqualsAndHashCode
@Entity
public class Member {

    @Id @GeneratedValue
    private Long id;

    private String username;

    private int age;

    /**
     * Entity column 이 enum type인 경우 반드시 @Enumerated(EnumType.STRING)으로 지정해야 한다.
     * default가 EnumType.ORDINAL 이기 때문데 DB에 순서로 들어간다.
     * 그럴 경우 enum type의 순서가 나중에 변경되게 되면 데이터가 다 꼬여 버린다.
     */
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @ColumnTransformer(
            read = "decrypt( 'AES', '00', password  )",
            write = "encrypt('AES', '00', ?)"
    )
    private String password;

    @ElementCollection
    @CollectionTable(name = "member_address_history", joinColumns =
    @JoinColumn(name = "member_id"))
    private List<Address> addressHistories = new ArrayList<>();

    /**
     * 연관관계의 주인 객체
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Builder
    public Member(String username, int age, String password, @Singular List<Address> addressHistories
            , Team team, UserType userType) {
        this.username = username;
        this.age = age;
        this.password = password;
        this.addressHistories = addressHistories;
        this.team = team;
        this.userType = userType;

        /**
         * 연관관계 편의성 method
         */
        //this.team.getMembers().add(this);
    }

    /**
     * 연관관계 편의성 method
     * @param team
     */
    public Member changTeam(Team team) {
        this.team = team;
        this.team.getMembers().add(this);
        return this;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", age=" + age +
                ", password='" + password + '\'' +
                '}';
    }
}
