package jpql.entity;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@EqualsAndHashCode
@Entity
@Table(name = "Orders")
public class Order {

    @Id @GeneratedValue
    private Long id;

    private int orderAmount;

    @Embedded
    private Address address;

    @Builder
    public Order(int orderAmount, Address address) {
        this.orderAmount = orderAmount;
        this.address = address;
    }
}
