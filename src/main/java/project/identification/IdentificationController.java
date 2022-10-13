package project.identification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class IdentificationController {

    private final IdentificationService identificationService;

    @GetMapping("/idntf_list")
    public ResponseEntity<?> list() {

        // 신원확인대상 리스트 반환
        List<IdentificationTarget> identificationTargets = identificationService.list();

        // 리스트 조회 실패시, 에러반환
        if (identificationTargets == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // 리스트 조회 성공시 listMember 반환
        log.info("identificationTargets ={}", identificationTargets);
        return new ResponseEntity<>(identificationTargets, HttpStatus.OK);
    }

    @PostMapping("/idntf_add")
    public ResponseEntity<?> add(@RequestBody IdentificationTarget target, BindingResult bindingResult) {
        // 에러가 있는 경우 다시 회원가입 폼으로
        if (bindingResult.hasErrors()) {
            log.info("error ={}", bindingResult);
            return new ResponseEntity<>(bindingResult, HttpStatus.BAD_REQUEST);
        }

        identificationService.save(target);
        log.info("신원확인대상 등록 완료 target ={}", target);

        return new ResponseEntity<>(target, HttpStatus.CREATED);
    }

    @GetMapping("/idntf_delete")
    public ResponseEntity<?> delete(@RequestBody IdentificationTarget target, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("error ={}", bindingResult);
            return new ResponseEntity<>(bindingResult, HttpStatus.BAD_REQUEST);
        }

        identificationService.delete(target);
        log.info("회원삭제완료");

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/identf_update")
    public ResponseEntity<?> update(@RequestBody IdentificationTarget target, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("error={}", bindingResult);
            return new ResponseEntity<>(bindingResult, HttpStatus.BAD_REQUEST);
        }
        IdentificationTarget updatedTarget = identificationService.update(target);
        log.info("업데이트완료 target ={}", updatedTarget);

        return new ResponseEntity<>(updatedTarget, HttpStatus.OK);
    }

    /**
     * TODO :
     * 예외처리 (중복체크, 검증 등)
     * 신원확인대상 등록시 member_id 같이 넣기.
     */

}
