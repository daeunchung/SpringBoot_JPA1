package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item {
    // 상속관계 있어서 abstract class로,
    // 상속관계 맵핑 필요 -> 상속관계 전략을 부모클래스에 지정 : SINGLE_TABLE 전략
    // InheritanceType : 1. SingleTable 한테이블에 때려넣기  
    // 2. TablePerClass ex) Album, Book, Movie 3개의 테이블로 생성   3. Joined 가장 정규화

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //==비즈니스 로직== : 도메인 주도 설계시 엔티티 안에 비즈니스 로직을 넣는 것이 객체지향적. 데이터를 가지고 있는 쪽에 비즈니스 메서드 있는것이 바람직//
    // 비록 Setter 달아줬지만 외부에서 Setter로 조종 하지 않고, 내부에 있는 비즈니스 로직들로 값을 변경하는 것이 객체지향적으로 바람직
    /**
     * 재고 (stock) 수량 증가
     */
    public void addStock(int quantity){
        this.stockQuantity += quantity;
    }
    /**
     * stock 감소
     */
    public void removeStock(int quantity){
        int restStock = this.stockQuantity - quantity;
        if(restStock < 0){
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
    
}
