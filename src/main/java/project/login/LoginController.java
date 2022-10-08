package project.login;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.member.Member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginForm form, BindingResult bindingResult,
                        HttpServletRequest request) {
        log.info("loginForm ={}", form);

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult, HttpStatus.BAD_REQUEST);
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
        log.info("login? {}", loginMember);
        if (loginMember == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        // 로그인 성공 처리
        // 인증코드 대조하여 "certified"면 로그인 허용
        Member verifyCode = loginService.verifyCode(form.getLoginId());
        log.info("verifyCode ={}", verifyCode);
        if (verifyCode == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        // 세션이 있으면 반환, 없으면 신규 생성
        HttpSession session = request.getSession(true);
        // 세션에 로그인 정보 보관
        session.setAttribute("login-member", loginMember);
        log.info("session ={}", session);

        return new ResponseEntity<>(loginMember, HttpStatus.OK);
    }

    @GetMapping("logout")
    public void logout(HttpServletRequest request) {

        log.info("request ={}", request);
        // 세션이 없어도 새로 생성X
        HttpSession session = request.getSession(false);
        if (session != null) {
            // 세션 무효화
            session.invalidate();
            log.info("session is invalidated");
        }
        log.info("session ={}", session);
    }


    /**
     * TODO list=
     * 로그인 vaild(글자제한 등) -> 리액트에서 처리하는 방법 찾아야.
     */


}

