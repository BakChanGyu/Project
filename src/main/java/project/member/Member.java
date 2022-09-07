package project.member;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class Member {

    @NotNull
    private Long id; // 회원 고유번호
    @NotEmpty
    private String loginId; // 로그인 ID
    @NotEmpty
    private String name; // 사용자 이름
    @NotEmpty
    private String password; // 비밀번호
}
