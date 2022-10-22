package project.file;

import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.ai.compare.CsatAIService;
import project.ai.compare.MissingAIService;
import project.ai.compare.ToeicAIService;
import project.member.Member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UploadFileController {
    private final FileStore fileStore;
    private final MissingAIService missingAIService;
    private final CsatAIService csatAIService;
    private final ToeicAIService toeicAIService;

    // 비교할 이미지 업로드
    @PostMapping("/image/missing/upload")
    public ResponseEntity<?> saveMissingImage(@ModelAttribute ItemForm form, HttpServletRequest request) throws IOException {

        HttpSession session = request.getSession(false);
        log.info("회원 session ={}", session);
        // 비회원 비교할 이미지 업로드시
        if(session == null) {
            return new ResponseEntity<>("error_code: 비회원입니다.", HttpStatus.OK);
        }

        // 서버에 이미지 저장
        List<UploadFile> storeImageFiles = fileStore.storeFiles(form.getImageFiles());
        log.info("storedCompareImageFiles={}", storeImageFiles);

        // AI를 통해 필적 비교
        try {
            JsonObject jsonObject = compareControl(session);
            log.info("반환값 ={}", jsonObject);
            return new ResponseEntity<>(jsonObject, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("error_code: 해당 값이 없습니다.", HttpStatus.OK);
        }
    }

    @PostMapping("/image/csat/upload")
    public ResponseEntity<?> saveCsatImage(@ModelAttribute ItemForm form, HttpServletRequest request) throws IOException {

        HttpSession session = request.getSession(false);
        log.info("회원 session ={}", session);
        // 비회원 비교할 이미지 업로드시
        if(session == null) {
            return new ResponseEntity<>("error_code: 비회원입니다.", HttpStatus.OK);
        }

        // 서버에 이미지 저장
        List<UploadFile> storeImageFiles = fileStore.storeFiles(form.getImageFiles());
        log.info("storedCompareImageFiles={}", storeImageFiles);

        // AI를 통해 필적 비교
        try {
            JsonObject jsonObject = compareControl(session);
            log.info("반환값 ={}", jsonObject);
            return new ResponseEntity<>(jsonObject, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("error_code: 해당 값이 없습니다.", HttpStatus.OK);
        }
    }

    @PostMapping("/image/toeic/upload")
    public ResponseEntity<?> saveToeicImage(@ModelAttribute ItemForm form, HttpServletRequest request) throws IOException {

        HttpSession session = request.getSession(false);
        log.info("회원 session ={}", session);
        // 비회원 비교할 이미지 업로드시
        if(session == null) {
            return new ResponseEntity<>("error_code: 비회원입니다.", HttpStatus.OK);
        }

        // 서버에 이미지 저장
        List<UploadFile> storeImageFiles = fileStore.storeFiles(form.getImageFiles());
        log.info("storedCompareImageFiles={}", storeImageFiles);

        // AI를 통해 필적 비교
        try {
            JsonObject jsonObject = compareControl(session);
            log.info("반환값 ={}", jsonObject);
            return new ResponseEntity<>(jsonObject, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("error_code: 해당 값이 없습니다.", HttpStatus.OK);
        }
    }

    private JsonObject compareControl(HttpSession session) {

        Member loginMember = (Member) session.getAttribute("login-member");
        log.info("session에 저장된 member ={}", loginMember);
        String memberType = loginMember.getMemberType();

        switch (memberType) {
            case "police":
                // 경찰 AI모델
                return missingAIService.requestToFlask();
            case "teacher":
                // 수능 감독관
                return csatAIService.requestToFlask();
            default:
                // 토익 감독관
                return toeicAIService.requestToFlask();
        }
    }
}
