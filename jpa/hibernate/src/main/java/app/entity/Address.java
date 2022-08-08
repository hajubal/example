package app.entity;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Embeddable
public class Address {

    private String name;
    private String street;

    @Column(name = "post_code")
    private String postCode;

    @Builder
    public Address(String name, String street, String postCode) {
        this.name = name;
        this.street = street;
        this.postCode = postCode;
    }
}
