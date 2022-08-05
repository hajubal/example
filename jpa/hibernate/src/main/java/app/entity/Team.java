package app.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Team {

    @Id
    @GeneratedValue
    private Long team_id;
    private String name;
    private String description;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Member> members = new ArrayList<>();


    @Builder
    public Team(String name, String description, List<Member> members) {
        this.name = name;
        this.description = description;
        this.members = members;
    }
}
