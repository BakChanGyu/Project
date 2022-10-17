package project.file;

import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.AI.AIService;
import project.member.Member;
import project.repository.ItemRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UploadFileController {

    private final ItemRepository itemRepository;
    private final FileStore fileStore;
    private final AIService aiService;

    // 파일을 서버에 업로드
    @PostMapping("/upload")
    public ResponseEntity<?> saveImage(@ModelAttribute ItemForm form, HttpServletRequest request) throws IOException {
        try {
            // 서버에 이미지 저장
            List<UploadFile> storeImageFiles = fileStore.storeFiles(form.getImageFiles());
            log.info("storeImageFiles={}", storeImageFiles);

            Item item = new Item();
            item.setImageFiles(storeImageFiles);
            itemRepository.save(item);

            // 세션 없어도 생성 X
            HttpSession session = request.getSession(false);
            // 비회원인 경우
            if(session == null) {
                // TODO: 비회원 들어올 경우 루트.
            } else {
                // 회원인 경우
                Member loginMember = (Member) session.getAttribute("login-member");
                log.info("session.. ={}", loginMember);

                String memberType = loginMember.getMemberType();
                switch (memberType) {
                    case "police":
                        // TODO: 경찰 AI모델
                        return null;
                    case "csatTeacher":
                        // TODO: 수능 감독관 (교사)
                        return null;
                    case "toeicTeacher":
                        // TODO: 토익 감독관
                }
            }

        } catch (IOException e) {
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }

        JsonObject jsonObject = aiService.requestToFlask(form.getItemId());

        return new ResponseEntity<>(jsonObject, HttpStatus.OK);
    }
}
