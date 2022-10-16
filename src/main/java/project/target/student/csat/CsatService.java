package project.target.student.csat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.repository.target.CsatRepository;
import project.target.missing.Missing;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CsatService {

    private final CsatRepository csatRepository;

    // 수능 응시자 등록
    public void save(Csat target) {
        Csat csat = validateDuplicateMissing(target);
        if (csat != null) {
            throw new IllegalStateException("응시자 정보가 중복되었습니다.");
        }
        csatRepository.save(target);
    }

    // 수능 응시자 명단 가져오기
    public List<Csat> findAll() {
        return csatRepository.findAll();
    }

    // idCode를 key로 수능 응시자 조회
    public Optional<Csat> findOne(String idCode) {
        return csatRepository.findByIdCode(idCode);
    }

    // 수능 응시자 정보 수정
    public Csat update(Csat target) {
        return csatRepository.update(target);
    }

    // 수능 응시자 정보 삭제
    public void delete(String idCode) {
        csatRepository.delete(idCode);
    }

    // 개인 코드 생성
    public String createIdCode() {
        return createRand();
    }

    private Csat validateDuplicateMissing(Csat target) {

        return csatRepository.findAll().stream()
                .filter(t -> t.getSsn().equals(target.getSsn()))
                .findAny()
                .orElse(null);
    }

    // 개인코드생성
    private String createRand() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        // 8자리 개인코드 생성
        for (int i = 0; i < 8; i++) {
            if (i == 2 || i == 5) {
                sb.append((random.nextInt(10)));
            } else {
                // a~z사이 알파벳 ('a' = 97)
                sb.append((char) ((random.nextInt(26)) + 97));
            }
        }

        return sb.toString();
    }
}
