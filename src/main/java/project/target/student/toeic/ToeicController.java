package project.target.student.toeic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.target.student.csat.Csat;
import project.target.student.csat.CsatService;

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

    // 토익 응시자 등록 api
    @PostMapping("/toeic/add")
    public ResponseEntity<?> add(@RequestBody Toeic target, BindingResult bindingResult) {

        // 에러가 있는 경우 다시 회원가입 폼으로
        if (bindingResult.hasErrors()) {
            log.info("error ={}", bindingResult);
            return new ResponseEntity<>(bindingResult, HttpStatus.BAD_REQUEST);
        }

        try {
            // 난수 생성하여 idCode 주입
            String idCode = toeicService.createIdCode();
            target.setIdCode(idCode);

            toeicService.save(target);

        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
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
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // 리스트 조회 성공시 listMember 반환
        log.info("toeics ={}", toeics);
        return new ResponseEntity<>(toeics, HttpStatus.OK);
    }

    // 토익 응시자 정보 수정 api (2단계)
    // 1. idCode를 key로 응시자 정보를 가져온다.
    @GetMapping("/toeic/update_form/{idCode}")
    public ResponseEntity<?> updateForm(@PathVariable String idCode) {

        Optional<Toeic> target = toeicService.findOne(idCode);
        log.info("find target ={}", target);
        return new ResponseEntity<>(target, HttpStatus.OK);
    }

    // 2. 수능 응시자 정보를 업데이트 한다.
    @PostMapping("/toeic/update")
    public ResponseEntity<?> update(@RequestBody Toeic target) {
        log.info("update missing ={}", target);

        toeicService.update(target);
        log.info("토익 응시자 정보 수정 완료");

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 수능 응시자 정보 삭제 api
    @GetMapping("/toeic/delete/{idCode}")
    public ResponseEntity<?> delete(@PathVariable String idCode) {
        toeicService.delete(idCode);
        log.info("토익 응시자 정보 삭제 완료");

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
