package project.login;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.manager.Manager;
import project.member.Member;
import project.repository.manager.ManagerRepository;
import project.repository.member.MemberRepository;

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
}
