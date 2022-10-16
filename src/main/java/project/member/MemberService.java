package project.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.repository.member.MemberRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // 회원 등록
    public void addMember(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
    }

    // 회원 정보 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    // 회원 정보 수정
    public void updateMember(Member member) {
        memberRepository.update(member);
    }

    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }

    // 회원 정보 삭제
    public void deleteMember(Long memberId) {
        memberRepository.delete(memberId);
    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findById(member.getMemberId())
                .ifPresent(m -> {
                    throw new IllegalStateException("식별번호가 중복입니다.");
                });
        memberRepository.findByLoginId(member.getLoginId())
                .ifPresent(m -> {
                    throw new IllegalStateException("로그인 아이디가 중복입니다.");
                });
    }


}
