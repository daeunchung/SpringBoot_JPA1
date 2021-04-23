package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity @Getter @Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;  // READY, COMP
    /* 강사님 강조 : EnumType.ORDINARY 가 숫자로 들어가는데 READY, XXX, COMP 과 같이
    중간에 다른 enum값이 추가되면 망.함. COMP가 2에서 3으로 되어버려 다음에 DB 조회시 기존의 COMP가 XXX로 나옴 */
}
