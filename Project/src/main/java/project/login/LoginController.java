package project.login;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import project.member.Member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute("loginForm") LoginForm form, BindingResult bindingResult,
                        HttpServletRequest request) {
        log.info("loginForm ={}", form);

        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
        log.info("login? {}", loginMember);
        if (loginMember == null) {
            return "/home";
        }

        // 로그인 성공 처리
        // 세션이 있으면 반환, 없으면 신규 생성
        HttpSession session = request.getSession(true);
        // 세션에 로그인 정보 보관
        session.setAttribute("login-member", loginMember);
        log.info("session ={}", session);

        return "/handwriting/handwriting";

    }

    /**
     * TODO list
     * 로그아웃 (세션)
     * 로그인 후 멤버 정보 뿌리기 (findById)
     * 인공지능 -> 파이썬코드를 자바에서 돌리기
     * 필적입력 데이터 받기. -> 이미지 받아와서 인공지능 모델에 input.
     * 노인정보 가져오기 (findById)
     * 시간남을때 -> 로그인 vaild, interceptor
     */

    @PostMapping("logout")
    public String logout(HttpServletRequest request) {
        log.info("request ={}", request);
        // 세션이 없어도 새로 생성X
        HttpSession session = request.getSession(false);
        if (session != null) {
            // 세션 무효화
            session.invalidate();
        }
        log.info("session ={}", session);
        return "redirect:/";
    }
}

