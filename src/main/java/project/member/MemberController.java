package project.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.repository.MemberRepository;


@Slf4j
@RestController
@RequestMapping("/api")
public class MemberController {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberController(@Qualifier("JdbcMemberRepository") MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @GetMapping("/add")
    public String addForm(@ModelAttribute("member") Member member) {
        return "members/addMemberForm";
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody Member member,
                      BindingResult bindingResult) {
        // 에러가 있는 경우 다시 회원가입 폼으로
        if (bindingResult.hasErrors()) {
            log.info("error ={}", bindingResult);
            return new ResponseEntity<>(bindingResult, HttpStatus.BAD_REQUEST);
        }

        // 아이디 중복체크
        int countId = memberRepository.countId(member.getId());
        int countLoginId = memberRepository.countLoginID(member.getLoginId());
        log.info("countId ={}", countId);
        log.info("countLoginId ={}", countLoginId);

        // 식별번호(id)가 중복되면 에러
        if (countId != 0) {
            bindingResult.reject("signupFail", "식별번호 또는 아이디가 중복입니다.");
            return new ResponseEntity<>(bindingResult, HttpStatus.BAD_REQUEST);
        }
        // 로그인아이디가 중복되면 에러
        if (countLoginId != 0) {
            bindingResult.reject("signupFail", "식별번호 또는 아이디가 중복입니다.");
            return new ResponseEntity<>(bindingResult, HttpStatus.BAD_REQUEST);
        }

        log.info("회원가입완료 member ={} ", member);
        memberRepository.save(member);
        return new ResponseEntity<>(member, HttpStatus.CREATED);
    }

    // 로그인하면, 누가 로그인한건지 띄워줌. 마이페이지 느낌으로
    @PostMapping("/login/{id}")
    public ResponseEntity<?> showLoginMember(@ModelAttribute Member member, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            log.info("error ={}", bindingResult);
            log.info("member ={}", member);
            return new ResponseEntity<>(member, HttpStatus.BAD_REQUEST);
        }
        log.info("member ={}", member);
        return new ResponseEntity<>(member, HttpStatus.OK);
    }
}
