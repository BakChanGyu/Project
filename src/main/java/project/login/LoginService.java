package project.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import project.member.Member;
import project.repository.MemberRepository;

@Service
public class LoginService {

    private final MemberRepository memberRepository;

    @Autowired
    public LoginService(@Qualifier("JdbcMemberRepository")
                        MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member login(String loginId, String password) {
        return memberRepository.findLoginId(loginId)
                .filter(member -> member.getPassword().equals(password))
                .orElse(null);
    }

}
