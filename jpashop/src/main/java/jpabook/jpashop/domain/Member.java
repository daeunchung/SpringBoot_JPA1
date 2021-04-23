package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;
    
    @Embedded   // 내장타입
    private Address address;

    // ORDERS Table의 Member 필드에 Mapping"되는" ReadOnly 거울
    // mappedBy에 값을 등록해도 변경되지 않는다. 조회만 가능하게 해놓은 태그
    @OneToMany(mappedBy = "member")  
    private List<Order> orders = new ArrayList<>();

}
