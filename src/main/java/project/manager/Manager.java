package project.manager;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class Manager {
    @NotNull
    private Long managerId; // 학번, 군번식 ex.202012345
    @NotEmpty
    private String managerPwd; // 로그인시 managerId + managerPwd 입력
    @NotEmpty
    private String managerName; // 관리자 로그인 시 프론트에 출력해주는 용도쯤 생각중
}
