package project.member;

import lombok.Data;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class Member {

    @NotNull
    private Long memberId; // 회원 고유번호
    @NotEmpty
    private String loginId; // 로그인 ID
    @NotEmpty
    private String password; // 비밀번호
    @NotEmpty
    private String memberName; // 사용자 이름
    @NotEmpty
    private String email; // 이메일
    @NotEmpty
    private String memberType; // 회원의 종류 (police, teacher, others)

    private String privateKey; // 이메일 인증키
}
