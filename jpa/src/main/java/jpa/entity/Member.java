package jpa.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity(name = "member")
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "member_name")
    private String memberName;

    /**
     * 연관관계 주인 객체
     */
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    /**
     * 값 타입.
     */
    @Embedded
    private Address address;

    /**
     * 값 타입 컬랙션
     */
    @ElementCollection
    @CollectionTable(name = "member_role", joinColumns =
        @JoinColumn(name = "member_id"))
    @Column(name = "roles")
    private Set<String> userRoles = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "member_address_history", joinColumns =
        @JoinColumn(name = "member_id"))
    private List<Address> addressHistory = new ArrayList<>();

    @Builder
    public Member(String memberName, Team team, Address address, Set<String> userRoles, List<Address> addressHistory) {
        this.memberName = memberName;
        this.team = team;
        this.address = address;
        this.userRoles = userRoles;
        this.addressHistory = addressHistory;
    }
}
