package project.login;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.manager.Manager;
import project.member.Member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form) { return "login/loginForm"; }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginForm form, BindingResult bindingResult,
                        HttpServletRequest request) {
        
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
        Optional<Member> verifyCode = loginService.verifyCode(form.getLoginId());
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

    @PostMapping("/manager_login")
    public ResponseEntity<?> managerLogin(@RequestBody Manager manager, BindingResult bindingResult,
                                          HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult, HttpStatus.BAD_REQUEST);
        }

        // 로그인 성공 처리
        Manager loginManager = loginService.loginManager(manager.getManagerId(), manager.getManagerPwd());
        log.info("loginManager ={}", loginManager);
        // TODO: session 줄지 말지 생각

        // 세션이 없으면 신규 생성
        HttpSession session = request.getSession(true);
        // 세션에 로그인 정보 보관
        session.setAttribute("login-manager", loginManager);
        log.info("session ={}", session);

        return new ResponseEntity<>(loginManager, HttpStatus.OK);
    }

    @GetMapping("/logout")
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



}

