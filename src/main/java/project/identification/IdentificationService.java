package project.identification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.repository.IdentificationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IdentificationService {
    private final IdentificationRepository identificationRepository;

    // 신원확인대상 명단 가져오기
    public List<IdentificationTarget> list() {
        return identificationRepository.findAll();
    }
    public void save(IdentificationTarget target) {
        identificationRepository.save(target);
    }
    public IdentificationTarget update(IdentificationTarget target) {
        return identificationRepository.updateTable(target);
    }
    public void delete(IdentificationTarget target) {
        identificationRepository.delete(target);
    }


}
