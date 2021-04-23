package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(name = "category_item",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<Item> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    //==연관관계 메서드==/
    public void addChileCategory(Category child){
        this.child.add(child);
        child.setParent(this);
    }
    /*

    ManyToMany 는 RDB(관계형DB)로 표현불가 => 1대n n대1로 중간테이블 맵핑 필요

    객체는 양쪽 모두 Collection, Collection 으로 이루어져 다대다 관계가 가능하지만
    RDB는 Collection 관계를 양쪽에 가질 수 없기 때문에 중간테이블 필요 (정형화된 모습으로 맵핑)

    실무에서 쓰지말아야할 이유 : 중간테이블에 필드를 더 추가할 수 없어 딱 한가지 형태 (지금 예제)로만 나오기 때문
    실무에서는 이렇게 단순한 맵핑이 없기 때문

    @JoinTable(name = "category_item",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")) 이부분 설명 :
            중간 테이블로 category_item 을 만들고
            내쪽(Category entity)으로 category_item 테이블의 category_id를 FK로 맵핑,
            쟤쪽(Item entity)    으로 category_item 테이블의 item_id    를 FK로 맵핑
    */

}
