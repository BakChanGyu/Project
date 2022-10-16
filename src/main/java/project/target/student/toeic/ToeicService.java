package project.target.student.toeic;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.repository.target.ToeicRepository;
import project.target.student.csat.Csat;

import java.util.List;
import java.util.Optional;

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

    private Toeic validateDuplicateMissing(Toeic target) {

        return toeicRepository.findAll().stream()
                .filter(t -> t.getSsn().equals(target.getSsn()))
                .findAny()
                .orElse(null);
    }
}
