package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)    // Junit 실행시에 Spring 과 Integration 해서 test 진행
@SpringBootTest                 // SpringBoot를 띄워놓은 상태로 (Spring Container 안에서) test 실행 : 오토와이어드 같은 어노테이션 사용하라면 필요
@Transactional                  // Test 에 붙여놓을 때'만' default Rollback true
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;
    
    @Test   // @Rollback(false) // 달아주면 Insert 쿼리문 볼 수 있음 (@Transactional이 default Rollback(true)이기 때문)
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("kim");
        
        //when
        Long savedId = memberService.join(member);

        //then
//        em.flush(); // 이렇게 해주면 실제로 쿼리날리고 위에 Transactional 에서 Rollback 해줌
        assertEquals(member, memberRepository.findOne(savedId));
    }
    
    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("kim");
        Member member2 = new Member();
        member2.setName("kim");
        
        //when
        memberService.join(member1);
        memberService.join(member2);
//        try{
//            memberService.join(member2);    // 예외가 발생해야 한다!!
//        } catch (IllegalStateException e){
//            return;
//        }

        //then
        fail("예외가 발생해야 한다.");    // Assert.fail 함수 : 코드가 돌다가 여기 오면 안되는 것. 즉, 여기 왔다는건 뭔가 잘못됐다는 것 => fail 떨궈버림
    }
}

/*
우리 Test는 실제 외부 DB를 사용했음 (따로 설치가 필요함)
이런 Test를 병렬로 여러개 돌리거나
Test는 종료된 후에 롤백되어 데이터가 초기화되는 것이 바람직함
=> 테스트를 완전히 격리된 환경, 자바 띄울때 자바 "안에" 살짝 DB 새로 만들어서 띄우는 방법 => 메모리DB 활용 (스프링부트에서 무료활용 가능)
방법 : Test>java>resources 폴더 생성 후에 application.yml을 생성 => test 실행시 이 폴더 안의 yml 설정파일이 우선권을 가진다
application.yml 에 datasource url을 H2DB In-Memory 로 작성 => 외부 DB 내리고 test 돌려도 실행o

더 나아가 test yml 파일 지금처럼 모두 주석처리해도 됨. 왜냐하면 별도의 설정이 없으면 SpringBoot가 memoryMode로 test를 실행
 결과 로그 : #1617419965254 | took 0ms | statement | connection 6| url jdbc:h2:mem:85f60bb3-4d59-4ffc-a745-203c0318983c
 
 SpringBoot는 기본적으로 ddl-auto : create-drop 으로 실행된다.
 create란 ? 내가 가지고 있는 엔티티 먼저 다 drop 시킨 후에 create 하고 애플리케이션 실행
 create-drop 이란 ? 위와 동일 + 마지막에 커넥션 종료 시점에 drop 쿼리 날려 완전 초기화

 DB memoryMode는 WAS가 내려가면 종료되어 버린다.
 */