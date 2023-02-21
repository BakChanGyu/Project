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
import project.repository.target.CsatRepository;
import project.repository.target.MissingRepository;
import project.repository.target.ToeicRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Queue;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UploadLearnFileController {

    private final LearnFileStore learnFileStore;
    private final CsatLearnAIService csatLearnAIService;
    private final CsatRepository csatRepository;
    private final MissingLearnAIService missingLearnAIService;
    private final MissingRepository missingRepository;
    private final ToeicLearnAIService toeicLearnAIService;
    private final ToeicRepository toeicRepository;
    private final Queue<Member> queue;


    // 학습할 필적 업로드
    @PostMapping("/image/learn/upload")
    public ResponseEntity<?> saveImage(@ModelAttribute ItemForm form, HttpServletRequest request) throws IOException {

        log.info("들어온 idcode ={}, images ={}", form.getIdCode(), form.getImageFiles());
        HttpSession session = request.getSession(false);
        log.info("회원 session ={}", session);
        // 비회원 비교할 이미지 업로드시
        if(session == null) {
            return new ResponseEntity<>("비회원입니다.", HttpStatus.BAD_REQUEST);
        }
        if(form.getIdCode() == null) {
            return new ResponseEntity<>("idCode값이 null입니다.", HttpStatus.BAD_REQUEST);
        }

        if(form.getImageFiles() ==null) {
            return new ResponseEntity<>("이미지파일 전송 실패! 들어온 파일이 null입니다.", HttpStatus.BAD_REQUEST);
        }

        if(form.getImageFiles().size() != 3) {
            return new ResponseEntity<>("3장의 필적을 넣어주세요!", HttpStatus.BAD_REQUEST);
        }

        // 회원인 경우
        // 서버에 이미지 저장
        try {
            learnFileStore.storeLearnFiles(form.getImageFiles(), form.getIdCode(), session);

            // isUploaded update. 신원확인대상의 필적이 업로드 되었음을 나타냄.
            Member member = (Member) session.getAttribute("login-member");
            String memberType = member.getMemberType();

            if (memberType.equals("police")) {
                // 학습할 이미지 캡쳐 api 호출
                missingLearnAIService.imgCap();
                log.info("학습할 이미지 캡쳐 완료");

                missingRepository.setIsUpdated(form.getIdCode());
                log.info("isUploaded 업데이트 완료");
            } else if (memberType.equals("teacher")) {
                // 학습할 이미지 캡쳐 api 호출
                csatLearnAIService.imgCap();
                log.info("학습할 이미지 캡쳐 완료");

                csatRepository.setIsUpdated(form.getIdCode());
                log.info("isUploaded 업데이트 완료");
            } else {
                // 학습할 이미지 캡쳐 api 호출
                toeicLearnAIService.imgCap();
                log.info("학습할 이미지 캡쳐 완료");

                toeicRepository.setIsUpdated(form.getIdCode());
                log.info("isUploaded 업데이트 완료");
            }

            // 캡쳐 전 이미지 삭제
//            String path;
//            path = delPath + fullPath;
//            DeleteFile deleteFile = new DeleteFile();
//            deleteFile.deleteFile(path);

            return new ResponseEntity<>("이미지 업로드 완료.", HttpStatus.OK);

        } catch (IOException e) {
            return new ResponseEntity<>("IOException 발생!" + e, HttpStatus.BAD_REQUEST);
        } catch (NullPointerException e) {
            return new ResponseEntity<>("NullPointException 발생!", HttpStatus.BAD_REQUEST);
        }
    }

    // 필적 학습
    @GetMapping("/image/learn/start")
    public ResponseEntity<?> startModel(HttpSession session) {

        Member loginMember = (Member) session.getAttribute("login-member");
        log.info("session에 저장된 member ={}", loginMember);
        String memberType = loginMember.getMemberType();

        // 회원요청 들어올때 이미 모델을 사용하고있는 회원이 있을 경우 끝날때까지 대기
        queue.add(loginMember);
        Member member = queue.poll();

        if (loginMember.equals(member)) {
            if (queue.isEmpty()) {
                queue.add(loginMember);
                queue.poll();

                String aiResult = null;
                if (memberType.equals("police")) {
                    // 경찰 AI모델
                    aiResult = missingLearnAIService.requestToFlask();
                    return new ResponseEntity<>(aiResult, HttpStatus.OK);
                } else if (memberType.equals("teacher")) {
                    // 수능 감독관
                    aiResult = csatLearnAIService.requestToFlask();
                    return new ResponseEntity<>(aiResult, HttpStatus.OK);
                } else {
                    // 토익 감독관
                    aiResult = toeicLearnAIService.requestToFlask();
                    return new ResponseEntity<>(aiResult, HttpStatus.OK);
                }
        } else {
                return new ResponseEntity<>("이미 모델을 학습중인 회원이 있습니다.", HttpStatus.BAD_REQUEST);
        }
    }
}
