package jpql.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberDTO {
    private String username;
    private Long id;

    @Builder
    public MemberDTO(Long id, String username) {
        this.username = username;
        this.id = id;
    }
}
