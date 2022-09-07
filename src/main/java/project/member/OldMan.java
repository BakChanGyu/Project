package project.member;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class OldMan {

    @NotEmpty
    private Long id; // 식별 id
    @NotEmpty
    private String name; // 이름
    @NotEmpty
    private String address; // 주소
}
