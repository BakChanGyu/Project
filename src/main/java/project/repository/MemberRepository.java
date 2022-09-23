package project.repository;

import org.springframework.stereotype.Repository;
import project.member.Member;
import project.missing.MissingMember;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository {

    Member save(Member member);
    MissingMember save(MissingMember member);
    Optional<Member> findById(Long id);
    Optional<Member> findLoginId(String loginId);
    List<MissingMember> findAll();
    void delete(MissingMember member);
    MissingMember updateByMissingcode(MissingMember member);

    int countId(Long id);
    int countLoginID(String loginId);



//    private static Map<Long, Member> store = new ConcurrentHashMap<>();
//    private static long sequence = 0L;
//
//    public Member findById(Long id) {
//        return store.get(id);
//    }
//
//    public Member save(Member member) {
//        member.setId(++sequence);
//        log.info("save: member={}", member);
//        store.put(member.getId(), member);
//
//        return member;
//    }
//
//    public Optional<Member> findLoginId(String loginId) {
//        return findAll().stream()
//                .filter(member -> member.getLoginId().equals(loginId))
//                .findFirst();
//    }
//
//    public List<Member> findAll() {
//        return new ArrayList<>(store.values());
//    }
//
//    // 테스트시 초기화를 위해
//    public void clearStore() {
//        store.clear();
//    }
}
