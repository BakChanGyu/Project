package project.valid;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class Regex {

    // 아이디 검사기
    public boolean isLoginId(String str) {
        return Pattern.matches("^[a-zA-Z0-9]*$", str);
    }
    // 비밀번호 검사기 (최소 8자, 최소 하나의 문자 및 숫자)
    public boolean isPassword(String str) {
     return Pattern.matches("^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$", str);
    }
    // 이메일 검사기
    public boolean isEmail(String str) {
        return Pattern.matches("^[a-zA-Z0-9._-]*@[a-z0-9A-Z]*.[a-zA-Z.]*$", str);
    }
    // 등록 날짜 검사기
    public boolean isDate(String str) {
        return Pattern.matches("^\\d{4}-\\d{2}-\\d{2}$", str);
    }
    // 전화번호 검사기
    public boolean isPhone(String str) {
        return Pattern.matches("^\\d{2,3}-\\d{3,4}-\\d{4}$", str);
    }
    // 주소 검사기
    public boolean isAddress(String str) {
        return Pattern.matches("^[가-힣]{2,7}\\s[가-힣]{2,4}\\s[가-힣]{2,4}$", str);
    }
    // 주민등록번호 검사기
    public boolean isSsn(String str) {
        // 숫자 두자리, 9701
        return Pattern.matches("^(?:[0-9]{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[1,2][0-9]|3[0,1]))-[1-4][0-9]{6}$", str);
    }
}
