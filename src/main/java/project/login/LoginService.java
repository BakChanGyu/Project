package project.login;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.manager.Manager;
import project.member.Member;
import project.repository.manager.ManagerRepository;
import project.repository.member.MemberRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;
    private final ManagerRepository managerRepository;

    public Member login(String loginId, String password) {
        return memberRepository.findByLoginId(loginId)
                .filter(member -> member.getPassword().equals(password))
                .orElse(null);
    }

    public Optional<Member> verifyCode(String loginId) {
        return Optional.ofNullable(memberRepository.findByLoginId(loginId)
                .filter(member -> member.getPrivateKey().equals("certified"))
                .orElse(null));
    }

    public Manager loginManager(Long id, String managerPwd) {
        return managerRepository.findById(id)
                .filter(manager -> manager.getManagerPwd().equals(managerPwd))
                .orElse(null);
    }

    // 아이디 찾기
    public String findLoginId(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NoSuchElementException::new);
        return member.getLoginId();
    }

    // 비밀번호 찾기
    public Member findPassword(String loginId) {
        return memberRepository.findByLoginId(loginId)
                .orElseThrow(NoSuchElementException::new);

    }
}
