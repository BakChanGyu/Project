package project.target.student.toeic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.target.student.csat.Csat;
import project.target.student.csat.CsatService;
import project.valid.ValidCheck;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ToeicController {

    private final ToeicService toeicService;
    private final ValidCheck validCheck;

    // 토익 응시자 등록 api
    @PostMapping("/toeic/add")
    public ResponseEntity<?> add(@RequestBody Toeic target, BindingResult bindingResult) {
        log.info("target ={}", target);
        // 에러가 있는 경우 다시 회원가입 폼으로
        if (bindingResult.hasErrors()) {
            log.info("error ={}", bindingResult);
            return new ResponseEntity<>("error_code: 등록 실패!", HttpStatus.OK);
        }

        String validAddTarget = validAddTarget(target);
        if (!validAddTarget.equals("ok")) {
            return new ResponseEntity<>("error_code:" + validAddTarget, HttpStatus.OK);
        }

        try {
            // 난수 생성하여 idCode 주입
            String idCode = toeicService.createIdCode();
            target.setToeicIdCode(idCode);
            toeicService.save(target);

        } catch (IllegalStateException e) {
            return new ResponseEntity<>("응시자 정보가 중복되었습니다.", HttpStatus.OK);
        }

        log.info("토익 응시자 등록 완료 target ={}", target);

        return new ResponseEntity<>(target, HttpStatus.CREATED);
    }

    // 토익 응시자 정보 조회 api
    @GetMapping("/toeic/list")
    public ResponseEntity<?> list() {

        // 토익 응시자 리스트 반환
        List<Toeic> toeics = toeicService.findAll();

        // 리스트 조회 실패시, 에러반환
        if (toeics == null) {
            return new ResponseEntity<>("조회 실패!", HttpStatus.OK);
        }

        // 리스트 조회 성공시 listMember 반환
        log.info("toeics ={}", toeics);
        return new ResponseEntity<>(toeics, HttpStatus.OK);
    }

    // 토익 응시자 정보 수정 api (2단계)
    // 1. idCode를 key로 응시자 정보를 가져온다.
    @GetMapping("/toeic/update/form/{toeicIdCode}")
    public ResponseEntity<?> updateForm(@PathVariable String toeicIdCode) {

        Optional<Toeic> target = toeicService.findOne(toeicIdCode);
        log.info("find target ={}", target);
        return new ResponseEntity<>(target, HttpStatus.OK);
    }

    // 2. 수능 응시자 정보를 업데이트 한다.
    @PostMapping("/toeic/update")
    public ResponseEntity<?> update(@RequestBody Toeic target) {
        log.info("update missing ={}", target);

        toeicService.update(target);
        log.info("토익 응시자 정보 수정 완료");

        return new ResponseEntity<>("success_code: 응시자 정보 수정 완료.", HttpStatus.OK);
    }

    // 수능 응시자 정보 삭제 api
    @GetMapping("/toeic/delete/{toeicIdCode}")
    public ResponseEntity<?> delete(@PathVariable String toeicIdCode) {
        toeicService.delete(toeicIdCode);
        log.info("토익 응시자 정보 삭제 완료");

        return new ResponseEntity<>("success_code: 응시자 정보 수정 완료.", HttpStatus.OK);
    }

    private String validAddTarget(Toeic target) {

        String errorMessage;
        int info = validCheck.toeicInfo(target);
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
