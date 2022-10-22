package project.target.missing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.file.ItemForm;
import project.file.LearnFileStore;
import project.file.UploadFile;
import project.valid.ValidCheck;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MissingController {

    private final MissingService missingService;
    private final LearnFileStore learnFileStore;
    private final ValidCheck validCheck;

    // 실종자 등록 api
    @PostMapping("/missing/add")
    public ResponseEntity<?> add(@RequestBody Missing target, BindingResult bindingResult) {

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
            String idCode = missingService.createIdCode();
            target.setMissingIdCode(idCode);

            missingService.save(target);

        } catch (IllegalStateException e) {
            return new ResponseEntity<>("error_code: 실종자 정보가 중복되었습니다.", HttpStatus.OK);
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
            return new ResponseEntity<>("error_code: 조회 실패!",HttpStatus.OK);
        }

        // 리스트 조회 성공시 listMember 반환
        log.info("missings ={}", missings);
        return new ResponseEntity<>(missings, HttpStatus.OK);
    }

    // 실종자 정보 수정 api (2단계)
    // 1. idCode를 key로 실종자 정보를 가져온다.
    @GetMapping("/missing/update/form/{missingIdCode}")
    public ResponseEntity<?> updateForm(@PathVariable String missingIdCode) {

        Optional<Missing> target = missingService.findOne(missingIdCode);
        log.info("find target ={}", target);
        return new ResponseEntity<>(target, HttpStatus.OK);
    }

    // 2. 실종자 정보를 업데이트 한다.
    @PostMapping("/missing/update")
    public ResponseEntity<?> update(@RequestBody Missing target) {
        log.info("update missing ={}", target);

        missingService.update(target);
        log.info("실종자 정보 수정 완료");

        return new ResponseEntity<>("success_code: 실종자 정보 수정 완료.", HttpStatus.OK);
    }

    // 실종자 정보 삭제 api
    @GetMapping("/missing/delete/{missingIdCode}")
    public ResponseEntity<?> delete(@PathVariable String missingIdCode) {
        missingService.delete(missingIdCode);
        log.info("실종자 정보 삭제 완료");

        return new ResponseEntity<>("success_code: 실종자 정보 삭제 완료.",HttpStatus.OK);
    }

    // 학습할 이미지 업로드
    @PostMapping("/image/learn/upload/{missingIdCode}")
    public ResponseEntity<?> saveLearnImage(@PathVariable String missingIdCode,
                                            @ModelAttribute ItemForm form, HttpSession session) throws IOException {

        // 서버에 이미지 저장
        List<UploadFile> storeImageFiles = learnFileStore.storeLearnFiles(form.getImageFiles(), missingIdCode, session);
        if (storeImageFiles == null) {
            return new ResponseEntity<>("error_code: 이미지 저장 실패. 비회원입니다.", HttpStatus.OK);
        }
        log.info("storedLearnImageFiles={}", storeImageFiles);

        return new ResponseEntity<>("success_code: 이미지 저장 완료.", HttpStatus.OK);
    }

    private String validAddTarget(Missing target) {

        String errorMessage;
        int info = validCheck.missingInfo(target);
        if (info == 1) {
            errorMessage = "주민등록번호 작성 양식을 지켜주세요. 예)050309-3019242";
            return errorMessage;
        } else if (info == 2) {
            errorMessage = "주소는 도-시(군)-구(면)까지만 작성해주세요. 예)충청북도 수정시 가천구";
            return errorMessage;
        } else if (info == 3) {
            errorMessage = "실종 일자 작성 양식을 지켜주세요. 예)2022-11-04.";
            return errorMessage;
        } else if (info == 4) {
            errorMessage = "휴대폰 번호 작성 양식을 지켜주세요.";
            return errorMessage;
        } else {
            return "ok";
        }
    }
}
