package project.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.email.EmailService;
import project.valid.ValidCheck;

import java.util.List;
import java.util.Optional;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;
    private final EmailService emailService;
    private final ValidCheck validCheck;

    // 회원가입 API
    @PostMapping("/member/add")
    public ResponseEntity<?> add(@RequestBody Member member,
                                 BindingResult bindingResult) throws Exception {
        // 에러가 있는 경우
        if (bindingResult.hasErrors()) {
            log.error("error ={}", bindingResult);
            return new ResponseEntity<>("error_code: 회원가입 실패!", HttpStatus.OK);
        }

        String validAddMember = validAddMember(member);
        if (!validAddMember.equals("ok")) {
            return new ResponseEntity<>("error_code: " + validAddMember, HttpStatus.OK);
        }

        try {
            memberService.addMember(member);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>("error_code: memberID 또는 loginID가 중복입니다.", HttpStatus.OK);
        }

        log.info("임시 회원가입 완료 member ={} ", member);

        // 이메일로 인증코드 발송
        try {
            emailService.sendSimpleMessage(member);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("error_code: 이메일 발송 실패!", HttpStatus.OK);
        }
        log.info("인증코드 전송 완료");

        return new ResponseEntity<>(member, HttpStatus.CREATED);
    }

    @GetMapping("/member/list")
    public ResponseEntity<?> list() {
        List<Member> members = memberService.findMembers();

        // 리스트 조회 실패시, 에러반환
        if (members == null) {
            return new ResponseEntity<>("error_code: 조회 실패!", HttpStatus.OK);
        }

        // 리스트 조회 성공시, 리스트 반환
        log.info("missingMembers ={}", members);
        return new ResponseEntity<>(members, HttpStatus.OK);
    }

    // 회원수정을 위한 api (2단계)
    // 1. memberId를 key로 회원 정보를 가져온다.
    @GetMapping("/member/update/form/{memberId}")
    public ResponseEntity<?> updateForm(@PathVariable Long memberId) {

        Optional<Member> member = memberService.findOne(memberId);
        log.info("find member ={}", member);
        return new ResponseEntity<>(member, HttpStatus.OK);
    }

    // 2. 회원 정보를 업데이트 한다.
    @PostMapping("/member/update")
    public ResponseEntity<?> update(@RequestBody Member member) {
        log.info("update member ={}", member);

        memberService.updateMember(member);
        log.info("회원 정보 업데이트 완료");

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 회원삭제를 위한 api
    @GetMapping("/member/delete/{memberId}")
    public ResponseEntity<?> delete(@PathVariable Long memberId) {
        memberService.deleteMember(memberId);
        log.info("회원 삭제 완료");

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private String validAddMember(Member member) {

        String errorMessage;
        int info = validCheck.memberInfo(member);
        if (info == 1) {
            errorMessage = "아이디는 영문 또는 숫자만 가능합니다.";
            return errorMessage;
        } else if (info == 2) {
            errorMessage = "비밀번호는 최소 8자, 최소 하나의 문자 및 숫자가 포함되어야 합니다.";
            return errorMessage;
        } else if (info == 3) {
            errorMessage = "이메일 양식이 잘못되었습니다.";
            return errorMessage;
        } else {
            return "ok";
        }
    }
}
