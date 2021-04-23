package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)  // JPA 의 모든 데이터변경이나 로직들은 가급적 트랜젝션 안에서 실행되어야 한다.
@RequiredArgsConstructor        // (롬복기능) final 붙은 필드만 생성자 자동생성 // @AllArgsConstructor: 모든 필드 생성자 만들어주기
public class MemberService {

    // @Autowired  // 스프링이 스프링빈에 등록되어있던 memRepo를 Injection 해준다. field 주입
    private final MemberRepository memberRepository;
//    @Autowired  // setter Injection 장점: test 시에 mock 직접주입 가능, 단점: 실제 runtime에 누가 바꿔치기 가능
//    public void setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }
//    @Autowired  // 생성자 Injection 권장 (최신 스프링 : 생성자가 딱 한개 있을경우 오토와이어드 생략가능)
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }
    /**
     * 회원 가입
     */
    @Transactional
    public Long join(Member member){
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    // 중복회원 검증 로직 (실무에서는 동시에 2명가입되어 문제발생가능 => 최후의 방어 : DB member name을 UNIQUE 제약 걸기를 권장)
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 회원 전체 조회
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }

}



