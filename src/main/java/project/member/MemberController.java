package project.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.email.EmailService;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;
    private final EmailService emailService;

    // 회원가입 API
    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody Member member,
                                 BindingResult bindingResult,
                                 Model model) throws Exception {
        // 에러가 있는 경우 다시 회원가입 폼으로
        if (bindingResult.hasErrors()) {
            log.error("error ={}", bindingResult);
            return new ResponseEntity<>(bindingResult, HttpStatus.BAD_REQUEST);
        }

        // 아이디 중복체크
        int countId = memberService.countId(member);
        int countLoginId = memberService.countLoginId(member);
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
        // 비밀번호 확인과 일치하지 않으면 에러
        String password = member.getPassword();
        String checkPwd = member.getCheckPwd();
        if (!password.equals(checkPwd)) {
            log.info("비밀번호 불일치. password ={}, checkPwd ={}", password, checkPwd);
            model.addAttribute("error", "비밀번호와 동일하게 입력해주세요");
            return new ResponseEntity<>(bindingResult, HttpStatus.BAD_REQUEST);
        }

        memberService.addMember(member);
        log.info("회원가입완료 member ={} ", member);

        // 이메일로 인증코드 발송
        emailService.sendSimpleMessage(member);
        log.info("인증코드 전송 완료");

        return new ResponseEntity<>(member, HttpStatus.CREATED);
    }

    @GetMapping("/member_list")
    public ResponseEntity<?> list() {
        List<Member> members = memberService.findMembers();

        // 리스트 조회 실패시, 에러반환
        if (members == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // 리스트 조회 성공시, 리스트 반환
        log.info("missingMembers ={}", members);
        return new ResponseEntity<>(members, HttpStatus.OK);
    }

    @GetMapping("/delete")
    public void delete(Long memberId) {
        memberService.deleteMember(memberId);
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
