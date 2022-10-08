package project.identification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.repository.IdentificationRepository;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class IdentificationController {

    private final IdentificationService identificationService;
    private final IdentificationRepository memberRepository;

    @GetMapping("/list")
    public ResponseEntity<?> list() {

        // 신원확인대상 리스트 반환
        List<IdentificationTarget> identificationTarget = identificationService.list();

        // 리스트 조회 실패시, 에러반환
        if (identificationTarget == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // 리스트 조회 성공시 listMember 반환
        log.info("missingMember={}", identificationTarget);
        return new ResponseEntity<>(identificationTarget, HttpStatus.OK);
    }

    @PostMapping("/idntf_add")
    public ResponseEntity<?> add(@RequestBody IdentificationTarget member, BindingResult bindingResult) {
        // 에러가 있는 경우 다시 회원가입 폼으로
        if (bindingResult.hasErrors()) {
            log.info("error ={}", bindingResult);
            return new ResponseEntity<>(bindingResult, HttpStatus.BAD_REQUEST);
        }
        memberRepository.save(member);
        log.info("회원가입완료 member={}", member);
        return new ResponseEntity<>(member, HttpStatus.CREATED);
    }

    @GetMapping("/idntf_delete")
    public ResponseEntity<?> delete(@RequestBody IdentificationTarget member, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("error ={}", bindingResult);
            return new ResponseEntity<>(bindingResult, HttpStatus.BAD_REQUEST);
        }
        memberRepository.delete(member);
        log.info("회원삭제완료");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/identf_update")
    public ResponseEntity<?> update(@RequestBody IdentificationTarget member, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("error={}", bindingResult);
            return new ResponseEntity<>(bindingResult, HttpStatus.BAD_REQUEST);
        }
        memberRepository.updateByMissingcode(member);
        log.info("업데이트완료 member={}", member);
        return new ResponseEntity<>(member, HttpStatus.OK);
    }

    /**
     * TODO :
     * 예외처리 (중복체크, 검증 등)
     * 신원확인대상 등록시 member_id 같이 넣기.
     */

}
