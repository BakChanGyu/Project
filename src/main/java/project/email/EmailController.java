package project.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class EmailController {
    private final EmailService emailService;

    // 이메일로 보낸 인증코드가 일치하는지 확인하는 api
    @GetMapping("/verify_code")
    public String verifyCode(@RequestParam Long memberId, String privateKey) {
        String serverKey = emailService.confirmEmail(memberId);
        log.info("serverKey ={}, userKey ={}", serverKey, privateKey);

        // 서버와 유저의 인증코드가 일치할 경우 DB에서 private_key값 certified로 변경
        if (serverKey.equals(privateKey)) {
            emailService.addPrivateKey(memberId);

            return "인증에 성공했습니다!";
        } else {
            return "인증에 실패했습니다. 다시 시도해주세요.";
        }
    }
}
