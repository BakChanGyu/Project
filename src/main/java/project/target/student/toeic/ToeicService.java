package project.target.student.toeic;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.repository.target.ToeicRepository;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ToeicService {

    private final ToeicRepository toeicRepository;

    // 수능 응시자 등록
    public void save(Toeic target) {
        Toeic toeic = validateDuplicateMissing(target);
        if (toeic != null) {
            throw new IllegalStateException("응시자 정보가 중복되었습니다.");
        }
        toeicRepository.save(target);
    }

    // 수능 응시자 명단 가져오기
    public List<Toeic> findAll() {
        return toeicRepository.findAll();
    }

    // idCode를 key로 토익 응시자 조회
    public Optional<Toeic> findOne(String idCode) {
        return toeicRepository.findByIdCode(idCode);
    }

    // 수능 응시자 정보 수정
    public Toeic update(Toeic target) {
        return toeicRepository.update(target);
    }

    // 수능 응시자 정보 삭제
    public void delete(String idCode) {
        toeicRepository.delete(idCode);
    }

    // 개인 코드 생성
    public String createIdCode() {
        return createRand();
    }

    private Toeic validateDuplicateMissing(Toeic target) {

        return toeicRepository.findAll().stream()
                .filter(t -> t.getToeicSsn().equals(target.getToeicSsn()))
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
