package project.repository;

import project.member.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    Member save(Member member);
    List<Member> findAll();
    Optional<Member> findById(Long id);
    Optional<Member> findLoginId(String loginId);
    void delete(Long memberId);
    void addPrivateKey(Long id, String code);
    String findPrivateKeyById(Long id);

    int countId(Long id);
    int countLoginID(String loginId);
}
