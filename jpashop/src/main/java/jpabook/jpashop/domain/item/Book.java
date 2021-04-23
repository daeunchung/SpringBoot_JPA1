package jpabook.jpashop.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("B")    // 싱글테이블에 저장될때 구분해주는 식별자 (상속관계 맵핑을 위해 특별히)
@Getter
@Setter
public class Book extends Item {

    private String author;
    private String isbn;
}
