package project.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.repository.MemberRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // 회원 등록
    public void addMember(Member member) {
        memberRepository.save(member);
    }
    // 회원 정보 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }
    // TODO: 회원 정보 수정

    // 회원 정보 삭제
    public void deleteMember(Long memberId) {
        memberRepository.delete(memberId);
    }
    // 식별번호 갯수 체크
    public int countId(Member member) {
        return memberRepository.countId(member.getMemberId());
    }
    // 로그인 아이디 갯수 체크
    public int countLoginId(Member member) {
        return memberRepository.countLoginID(member.getLoginId());
    }
}
