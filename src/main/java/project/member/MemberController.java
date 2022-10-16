package project.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.email.EmailService;

import java.util.List;
import java.util.Optional;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;
    private final EmailService emailService;

    // 회원가입 API
    @PostMapping("/member/add")
    public ResponseEntity<?> add(@RequestBody Member member,
                                 BindingResult bindingResult) throws Exception {
        // 에러가 있는 경우
        if (bindingResult.hasErrors()) {
            log.error("error ={}", bindingResult);
            return new ResponseEntity<>(bindingResult, HttpStatus.BAD_REQUEST);
        }

        try {
            memberService.addMember(member);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }

        log.info("임시 회원가입 완료 member ={} ", member);

        // 이메일로 인증코드 발송
        try {
            emailService.sendSimpleMessage(member);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("can not send email");
            return new ResponseEntity<>(bindingResult, HttpStatus.OK);
        }
        log.info("인증코드 전송 완료");

        return new ResponseEntity<>(member, HttpStatus.CREATED);
    }

    @GetMapping("/member/list")
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

    // 회원수정을 위한 api (2단계)
    // 1. memberId를 key로 회원 정보를 가져온다.
    @GetMapping("/member/update_form/{memberId}")
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
}
