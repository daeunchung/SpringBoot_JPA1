package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") // FK명 설정해준 것. 관계의 주인이니 여기로 값을 변경.
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    // Entity당 각각 persist를 호출해야하는데 CascadeAll 설정해주면
    // persist(orderItemA), persist(orderItemB), persist(orderItemC), persist(order) 대신에
    // persist(order) 하나만 해줘도 앞에 아이템들 다 들어간다. delete할때도 다같이 지워버림

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;  // Cascade 해주면 Order저장할때 Delivery객체도 함께 저장해준다.
    // 원래 모든 Entity는 persist를 저장하고 싶으면 각자해줘야하는데 CascadeAll해주면 Delivery에 데이터값만 넣어놓으면 Order가 persist될때 자동으로 같이 persist된다.

    // SpringPhysicalNamingStrategy 에 의해 order_date로 변경됨
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태 [ORDER, CANCEL]

    /*
    OneToOne에서 주의사항
    1대1일때 JPA 에서는 FK를 어느 테이블에 둬도 상관 X
    강사님 : 일반적으로 주문->배송 조회 빈도  > 배송->주문 조회 빈도
    그래서 일반적으로 Order에 FK를 놓는다. Order에도 Delivery 있고, Delivery에도 Order가 있는데
    그렇다면 연관관계의 주인은 ?
    => 연관관계의 주인은 FK와 가까운 곳에 있는 Order에 있는 Delivery를 연관관계 주인으로 잡아준다.
     */

    //==연관관계 편의 메서드==// 양방향 연관관계에서 사용, 두 곳중 핵심Controll 하는 곳에 적어주는 것이 좋다.
    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }
    
    //==생성 메서드==//
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //==비즈니스 로직==//
    /**
     * 주문 취소
     */
    public void cancel(){
        if(delivery.getStatus() == DeliveryStatus.COMP){
            throw new IllegalStateException(("이미 배송완료된 상품은 취소가 불가능합니다."));
        }
        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    //==조회 로직==// : 계산이 필요할 때에
    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice() {
//        return orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();
//        자바8로 바꾸면 위처럼 한줄로.. for문에서 alt+enter -> Replace with sum() 후에 Ctrl+Alt+n Inline처리
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }


}
