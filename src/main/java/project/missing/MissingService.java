package project.missing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import project.repository.MemberRepository;

import java.util.List;

@Service
public class MissingService {
    private final MemberRepository memberRepository;

    @Autowired
    public MissingService(@Qualifier("MissingMemberRepository")
                                  MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<MissingMember> list() {
        return memberRepository.findAll();
    }
}
