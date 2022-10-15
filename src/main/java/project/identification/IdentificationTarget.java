package project.identification;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.sql.Date;

@Data
public class IdentificationTarget {

    @NotEmpty
    private String idCode; // 실종자 코드
    @NotEmpty
    private String name; // 실종자 이름
    @NotEmpty
    private String ssn; // 주민등록번호
    @NotEmpty
    private String address; // 주소
    @NotEmpty
    private Date rgstDate; // 등록일자

    private Date updateDate; // 수정일자

    private Long memberId; // 등록한 회원 id
}
