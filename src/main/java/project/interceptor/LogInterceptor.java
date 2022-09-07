package project.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    // preHandle에서 지정한 값을 postHandle, afterCompletion에서 사용하기위한 변수
    public static final String LOG_ID = "logId";

    // 컨트롤러 호출 전 (핸들러 어댑터 호출 전) 응답값 true면 진행.
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();

        // 요청로그 구분을 위한 uuid
        String uuid = UUID.randomUUID().toString();
        request.setAttribute(LOG_ID, uuid);

        // @RequestMapping을 사용한 핸드러 매핑일 경우 HandlerMethod가 넘어옴.
        // 정적 리소스가 호출되는경우 ResourceHttpRequestHandler가 호출되어 별도 처리가 필요함.
        if (handler instanceof HandlerMethod) {
            // 호출할 컨트롤러 메서드의 모든 정보가 들어있음.
            HandlerMethod hm = (HandlerMethod) handler;
        }
        log.info("REQUEST [{}][{}][{}]", uuid, requestURI, handler);
        return true;
    }

    // 컨트롤러 호출 후. 컨트롤러에서 예외 발생하면 호출되지 않음.
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandler [{}]", modelAndView);
    }

    // 요청 완료 이후 (뷰 렌더링 된 이후에 호출됨). 항상 호출됨.
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI = request.getRequestURI();
        String logId = (String) request.getAttribute(LOG_ID);
        log.info("RESPONSE [{}][{}]", logId, requestURI);

        // 오류 발생시 로그찍음.
        if (ex != null) {
            log.error("afterCompletion error", ex);
        }
    }
}
