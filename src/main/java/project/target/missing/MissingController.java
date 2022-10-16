package project.target.missing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MissingController {

    private final MissingService missingService;

    // 실종자 등록 api
    @PostMapping("/missing/add")
    public ResponseEntity<?> add(@RequestBody Missing target, BindingResult bindingResult) {

        log.info("ssn? ={}", target.getSsn());
        log.info("date타입인가? ={}", target.getMissingDate());
        // 에러가 있는 경우 다시 회원가입 폼으로
        if (bindingResult.hasErrors()) {
            log.info("error ={}", bindingResult);
            return new ResponseEntity<>(bindingResult, HttpStatus.BAD_REQUEST);
        }

        try {
            // 난수 생성하여 idCode 주입
            String idCode = missingService.createIdCode();
            target.setIdCode(idCode);

            // 입력받은 문자열을 date 타입으로 변환
//            String missingDate = target.getMissingDate();
//            try {
//                Date date = missingService.converString(missingDate);
//                target.setMissingDate(String.valueOf(date));
//            } catch (ParseException e) {
//                return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
//            }

            missingService.save(target);

        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }

        log.info("실종자 등록 완료 target ={}", target);

        return new ResponseEntity<>(target, HttpStatus.CREATED);
    }

    // 실종자 정보 조회 api
    @GetMapping("/missing/list")
    public ResponseEntity<?> list() {

        // 실종자 리스트 반환
        List<Missing> missings = missingService.findAll();

        // 리스트 조회 실패시, 에러반환
        if (missings == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // 리스트 조회 성공시 listMember 반환
        log.info("missings ={}", missings);
        return new ResponseEntity<>(missings, HttpStatus.OK);
    }

    // 실종자 정보 수정 api (2단계)
    // 1. idCode를 key로 실종자 정보를 가져온다.
    @GetMapping("/missing/update_form/{idCode}")
    public ResponseEntity<?> updateForm(@PathVariable String idCode) {

        Optional<Missing> target = missingService.findOne(idCode);
        log.info("find target ={}", target);
        return new ResponseEntity<>(target, HttpStatus.OK);
    }

    // 2. 실종자 정보를 업데이트 한다.
    @PostMapping("/missing/update")
    public ResponseEntity<?> update(@RequestBody Missing target) {
        log.info("update missing ={}", target);

        missingService.update(target);
        log.info("실종자 정보 수정 완료");

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 실종자 정보 삭제 api
    @GetMapping("/missing/delete/{idCode}")
    public ResponseEntity<?> delete(@PathVariable String idCode) {
        missingService.delete(idCode);
        log.info("실종자 정보 삭제 완료");

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
