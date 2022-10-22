package project.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.ai.learn.CsatLearnAIService;
import project.ai.learn.MissingLearnAIService;
import project.ai.learn.ToeicLearnAIService;
import project.member.Member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UploadLearnFileController {

    private final LearnFileStore learnFileStore;
    private final CsatLearnAIService csatLearnAIService;
    private final MissingLearnAIService missingLearnAIService;
    private final ToeicLearnAIService toeicLearnAIService;

    // 학습할 필적 업로드
    @PostMapping("/image/learn/upload")
    public ResponseEntity<?> saveImage(@ModelAttribute ItemForm form, HttpServletRequest request) throws IOException {

        HttpSession session = request.getSession(false);
        log.info("회원 session ={}", session);
        // 비회원 비교할 이미지 업로드시
        if(session == null) {
            return new ResponseEntity<>("error_code: 비회원입니다.", HttpStatus.OK);
        }
        // 회원인 경우
        // 서버에 이미지 저장 -> 이때 경로는 이미지캡쳐 api로
        try {
            List<UploadFile> storeImageFiles = learnFileStore.storeLearnFiles(form.getImageFiles(), form.getIdCode(), session);
            log.info("storedLearnImageFiles={}", storeImageFiles);
        } catch (IOException e) {
            return new ResponseEntity<>("error_code: IOException 발생!", HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>("error_code: NullPointException 발생!", HttpStatus.OK);
        }

        return new ResponseEntity<>("success_code: 이미지 업로드 완료.", HttpStatus.OK);
    }

    // 필적 학습
    @GetMapping("/image/learn/start")
    public ResponseEntity<?> startModel(HttpSession session) {

        // 서버내부에서 학습시작
        // 이때 내부 폴더 갯수가 10이 아니면 학습불가
//        int countFolder = countFolder(path);
//        if (countFolder != 10) {
//            return new ResponseEntity<>("error_code: 10인분의 필적을 넣어주세요. 현재: " + countFolder + "인분 입니다.", HttpStatus.OK);
//        }

        Member loginMember = (Member) session.getAttribute("login-member");
        log.info("session에 저장된 member ={}", loginMember);
        String memberType = loginMember.getMemberType();

        switch (memberType) {
            case "police":
                // 경찰 AI모델
                missingLearnAIService.requestToFlask();
                return new ResponseEntity<>("success_code: 실종자 학습 완료.", HttpStatus.OK);
            case "teacher":
                // 수능 감독관
                csatLearnAIService.requestToFlask();
                return new ResponseEntity<>("success_code: 수능 응시자 학습 완료.", HttpStatus.OK);

            default:
                // 토익 감독관
                toeicLearnAIService.requestToFlask();
                return new ResponseEntity<>("success_code: 수능 응시자 학습 완료.", HttpStatus.OK);
        }
    }

    private int countFolder(String path) {
        File dir = new File(path);
        File[] files = dir.listFiles();

        return files.length;
    }
}
