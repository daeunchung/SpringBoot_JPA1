package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository // Component scan으로 자동으로 Spring Bean에 등록
@RequiredArgsConstructor
public class MemberRepository {

//    @PersistenceContext         // JPA 표준 Annotation
//    @Autowired
/* 원래는 Persistence Context가 필수인데 Boot 사용시에만 영속성컨텍스트 어노테이션을 오토와이어드로 변경 가능 (Spring Data JPA가 지원해줘서)
 그 이후에 Autowired 떼고 final 붙은 필드만 생성자 주입 만들어주는 RequiredArgsConstructor 로 관리 가능
*/
    private final EntityManager em;
    // Spring 이 생성한 EntityManager 를 ?에 주입해준다. 순수JPA를 사용하면 EntityManagerFactory에서 직접 EntityManager를 꺼내고 사용해야 함
//    public MemberRepository(EntityManager em) {
//        this.em = em;
//    }
    //    @PersistenceUnit            // 원하면 ManagerFactory 직접 주입 가능
//    private EntityManagerFactory emf;

    // 저장 logic
    public void save(Member member){
        em.persist(member);     // 영속성 컨텍스트에 member entity 를 주입 ( 그럼 나중에 transaction이 commit 되는 시점에 DB에 반영이 된다. DB에 Insert query 날라감)
    }

    // 단건 조회 logic
    public Member findOne(Long id){
        return em.find(Member.class, id);
    }

    // 리스트(목록) 조회 logic
    public List<Member> findAll(){
        // createQuery(JPQL, 반환타입) 작성
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();

//      return result;
//      getResultList() 반환값을 List<Member> result에 받아서 Inline처리(Ctrl+Alt+n) 해준 것
        // JPQL은 SQL과 유사하나 다르다. SQL은 Table 대상으로 Query, JPQL은 Entity 대상으로 Query한다. JPQL은 결국 SQL로 번역된다
        // 위의 "select m from Member m" : Member의 entity 객체에 대한 alias(별칭)을 m 으로 준 후 Entity Member를 조회하라
    }

    // 이름으로 회원리스트 조회
    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();   // m.name = :name 이 부분은 parameter binding 한 것
    }



}
