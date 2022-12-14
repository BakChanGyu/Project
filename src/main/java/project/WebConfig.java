package project;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import project.interceptor.LogInterceptor;
import project.interceptor.LoginCheckInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    // 인터셉터 등록
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/*.ico", "/error");

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
//                .excludePathPatterns("/**");
                .excludePathPatterns("/", "/api", "/api/member/add", "/api/login", "/api/logout",
                        "/api/manager/login", "/api/login/find/id", "/api/login/find/password",
                        "/api/verify/code", "/css/**", "/*.ico", "/error");
    }

}
