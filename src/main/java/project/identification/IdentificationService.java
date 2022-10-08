package project.identification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.repository.IdentificationRepositoryImpl;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IdentificationService {
    private final IdentificationRepositoryImpl memberRepository;

    public List<IdentificationTarget> list() {
        return memberRepository.findAll();
    }
}
