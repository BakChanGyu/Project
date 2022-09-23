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

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
        return "login/loginForm";
    }

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
        // 세션이 있으면 반환, 없으면 신규 생성
        HttpSession session = request.getSession(true);
        // 세션에 로그인 정보 보관
        session.setAttribute("login-member", loginMember);
        log.info("session ={}", session);

        return new ResponseEntity<>(loginMember, HttpStatus.OK);
    }

    @PostMapping("logout")
    public String logout(HttpServletRequest request) {

        log.info("request ={}", request);
        // 세션이 없어도 새로 생성X
        HttpSession session = request.getSession(false);
        if (session != null) {
            // 세션 무효화
            session.invalidate();
            log.info("session is invalidated");
        }
        log.info("session ={}", session);
        return "redirect:/home";
    }


    /**
     * TODO list
     * 인공지능 -> 파이썬코드를 자바에서 돌리기
     * 필적입력 데이터 받기. -> 이미지 받아와서 인공지능 모델에 input.
     * 시간남을때 -> 로그인 vaild(글자제한 등) -> 리액트에서 처리하는 방법 찾아야.
     */


}

