package project.repository.member;

import project.member.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    Member save(Member member);
    List<Member> findAll();
    Optional<Member> findById(Long id);
    Optional<Member> findByLoginId(String loginId);
    void update(Member member);
    void delete(Long memberId);
    void addPrivateKey(Long id, String code);
    String findPrivateKeyById(Long id);
}
