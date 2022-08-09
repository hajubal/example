package jpql.entity;


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

    @ColumnTransformer(
            read = "decrypt( 'AES', '00', password  )",
            write = "encrypt('AES', '00', ?)"
    )
    private String password;

    @ElementCollection
    @CollectionTable(name = "member_address_history", joinColumns =
    @JoinColumn(name = "member_id"))
    private List<Address> addressHistories = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Builder
    public Member(String username, int age, String password, @Singular List<Address> addressHistories, Team team) {
        this.username = username;
        this.age = age;
        this.password = password;
        this.addressHistories = addressHistories;
        this.team = team;
    }
}
