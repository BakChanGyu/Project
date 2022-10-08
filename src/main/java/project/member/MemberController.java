package project.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.email.EmailService;
import project.repository.MemberRepositoryImpl;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@EnableAsync
public class MemberController {

    private final MemberRepositoryImpl memberRepository;
    private final EmailService emailService;

    @Async
    @PostMapping("/add")
    // 회원가입후, 사용자가 입력한 정보반환.
    public ResponseEntity<?> add(@RequestBody Member member,
                      BindingResult bindingResult) throws Exception {
        // 에러가 있는 경우 다시 회원가입 폼으로
        if (bindingResult.hasErrors()) {
            log.info("error ={}", bindingResult);
            return new ResponseEntity<>(bindingResult, HttpStatus.BAD_REQUEST);
        }

        // 아이디 중복체크
        int countId = memberRepository.countId(member.getMemberId());
        int countLoginId = memberRepository.countLoginID(member.getLoginId());
        log.info("countId ={}", countId);
        log.info("countLoginId ={}", countLoginId);

        // 식별번호(id)가 중복되면 에러
        if (countId != 0) {
            bindingResult.reject("signupFail", "식별번호 또는 아이디가 중복입니다.");
            log.info("id 중복. 회원가입실패");
            return new ResponseEntity<>(bindingResult, HttpStatus.BAD_REQUEST);
        }
        // 로그인아이디가 중복되면 에러
        if (countLoginId != 0) {
            bindingResult.reject("signupFail", "식별번호 또는 아이디가 중복입니다.");
            log.info("loginId 중복. 회원가입실패");
            return new ResponseEntity<>(bindingResult, HttpStatus.BAD_REQUEST);
        }

        memberRepository.save(member);
        log.info("회원가입완료 member ={} ", member);

        // 이메일 발송후 인증코드 반환
        String code = emailService.sendSimpleMessage(member);
        log.info("code ={}", code);
        // 회원 DB에 인증코드 업데이트
        memberRepository.addPrivateKey(member.getMemberId(), code);

        return new ResponseEntity<>(member, HttpStatus.CREATED);
    }

    // 이메일로 보낸 인증코드가 일치하는지 확인하는 api
    @GetMapping("/verifyCode")
    private String confirmEmail(@RequestParam Long id, String private_key) {

        // DB에서 id값으로 인증코드 찾아옴
        String serverKey = memberRepository.findPrivateKeyById(id);
        log.info("serverKey ={}", serverKey);
        log.info("userKey ={}", private_key);
        // 서버의 인증코드와 사용자의 인증코드가 일치할 경우 DB의 private_key값 certified로 변경
        // 이후 로그인할때 private_key값이 certified인지 아닌지 확인.
        if (serverKey.equals(private_key)) {
            memberRepository.addPrivateKey(id, "certified");

            return "인증에 성공했습니다!";
        } else {
            return "인증에 실패했습니다. 다시 시도해주세요.";
        }
    }

//    // 로그인하면, 누가 로그인한건지 띄워줌. 마이페이지 느낌으로
//    @PostMapping("/login/{id}")
//    public ResponseEntity<?> showLoginMember(@ModelAttribute Member member, BindingResult bindingResult) {
//
//        if (bindingResult.hasErrors()) {
//            log.info("error ={}", bindingResult);
//            log.info("member ={}", member);
//            return new ResponseEntity<>(member, HttpStatus.BAD_REQUEST);
//        }
//        log.info("member ={}", member);
//        return new ResponseEntity<>(member, HttpStatus.OK);
//    }
}
