package project.target.missing;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.repository.target.MissingRepository;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MissingService {

    private final MissingRepository missingRepository;

    // 실종자 등록
    public void save(Missing target) {
        Missing missing = validateDuplicateMissing(target);
        if (missing != null) {
            throw new IllegalStateException("실종자 정보가 중복되었습니다.");
        }
        missingRepository.save(target);
    }

    // 실종자 명단 가져오기
    public List<Missing> findAll() {
        return missingRepository.findAll();
    }

    // idCode를 key로 실종자 조회
    public Optional<Missing> findOne(String missingIdCode) {
        return missingRepository.findByIdCode(missingIdCode);
    }


    // 실종자 정보 수정
    public Missing update(Missing target) {
        return missingRepository.update(target);
    }

    // 실종자 정보 삭제
    public void delete(String missingIdCode) {
        missingRepository.delete(missingIdCode);
    }

    // 개인 코드 생성
    public String createIdCode() {
        return createRand();
    }

    private Missing validateDuplicateMissing(Missing target) {

        return missingRepository.findAll().stream()
                .filter(t -> t.getMissingSsn().equals(target.getMissingSsn()))
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
