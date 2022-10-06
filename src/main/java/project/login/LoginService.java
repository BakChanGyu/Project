package project.login;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import project.member.Member;
import project.repository.MemberRepository;
import project.repository.MemberRepositoryImpl;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepositoryImpl memberRepository;

    public Member login(String loginId, String password) {
        return memberRepository.findLoginId(loginId)
                .filter(member -> member.getPassword().equals(password))
                .orElse(null);
    }

    public Member verifyCode(String loginId) {
        return memberRepository.findLoginId(loginId)
                .filter(member -> member.getPrivateKey()
                .equals("certified"))
                .orElse(null);
    }
}
