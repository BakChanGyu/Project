package project.target.student.csat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.valid.ValidCheck;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CsatController {

    private final CsatService csatService;
    private final ValidCheck validCheck;

    // 수능 응시자 등록 api
    @PostMapping("/csat/add")
    public ResponseEntity<?> add(@RequestBody Csat target, BindingResult bindingResult) {

        log.info("target ={}", target);
        // 에러가 있는 경우 다시 회원가입 폼으로
        if (bindingResult.hasErrors()) {
            log.info("error ={}", bindingResult);
            return new ResponseEntity<>("error_code: 등록 실패!", HttpStatus.OK);
        }

        String validAddTarget = validAddTarget(target);
        if (!validAddTarget.equals("ok")) {
            return new ResponseEntity<>("error_code: " + validAddTarget, HttpStatus.OK);
        }

        try {
            // 난수 생성하여 idCode 주입
            String idCode = csatService.createIdCode();
            target.setCsatIdCode(idCode);

            csatService.save(target);

        } catch (IllegalStateException e) {
            return new ResponseEntity<>("error_code: 응시자 정보가 중복되었습니다.", HttpStatus.OK);
        }

        log.info("수능 응시자 등록 완료 target ={}", target);

        return new ResponseEntity<>(target, HttpStatus.CREATED);
    }

    // 실종자 정보 조회 api
    @GetMapping("/csat/list")
    public ResponseEntity<?> list() {

        // 수능 응시자 리스트 반환
        List<Csat> csats = csatService.findAll();

        // 리스트 조회 실패시, 에러반환
        if (csats == null) {
            return new ResponseEntity<>("error_code: 조회 실패!", HttpStatus.OK);
        }

        // 리스트 조회 성공시 listMember 반환
        log.info("csats ={}", csats);
        return new ResponseEntity<>(csats, HttpStatus.OK);
    }

    // 수능 응시자 정보 수정 api (2단계)
    // 1. idCode를 key로 응시자 정보를 가져온다.
    @GetMapping("/csat/update/form/{csatIdCode}")
    public ResponseEntity<?> updateForm(@PathVariable String csatIdCode) {

        Optional<Csat> target = csatService.findOne(csatIdCode);
        log.info("find target ={}", target);
        return new ResponseEntity<>(target, HttpStatus.OK);
    }

    // 2. 수능 응시자 정보를 업데이트 한다.
    @PostMapping("/csat/update")
    public ResponseEntity<?> update(@RequestBody Csat target) {
        log.info("update missing ={}", target);

        csatService.update(target);
        log.info("수능 응시자 정보 수정 완료");

        return new ResponseEntity<>("success_code: 응시자 정보 수정 완료.", HttpStatus.OK);
    }

    // 수능 응시자 정보 삭제 api
    @GetMapping("/csat/delete/{csatIdCode}")
    public ResponseEntity<?> delete(@PathVariable String csatIdCode) {
        csatService.delete(csatIdCode);
        log.info("수능 응시자 정보 삭제 완료");

        return new ResponseEntity<>("success_code: 응시자 정보 수정 완료.", HttpStatus.OK);
    }

    private String validAddTarget(Csat target) {

        String errorMessage;
        int info = validCheck.csatInfo(target);
        if (info == 1) {
            errorMessage = "주민등록번호 작성 양식을 지켜주세요.";
            return errorMessage;
        } else if (info == 2) {
            errorMessage = "주소는 도-시(군)-구(면)까지만 작성해주세요. 예)충청북도 수정시 가천구";
            return errorMessage;
        } else if (info == 3) {
            errorMessage = "시험 일자 작성 양식을 지켜주세요. 예)2022-11-04.";
            return errorMessage;
        } else {
            return "ok";
        }
    }
}
