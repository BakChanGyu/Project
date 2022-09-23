package project.missing;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.sql.Date;

@Data
public class MissingMember {

    @NotEmpty
    private String misscode; // 실종자 코드
    @NotEmpty
    private String name; // 실종자 이름
    @NotEmpty
    private String address; // 실종자 주소
    @NotEmpty
    private String ssn; // 주민등록번호; social security number
    @NotEmpty
    private Date found_date; // 실종일자
    @NotEmpty
    private String found_loc; // 실종장소
    @NotEmpty
    private String protector_name; // 보호자 성함
    @NotEmpty
    private String protector_tel; // 보호자 전화번호

    private Long officer_id; // 등록한 경찰 id
}
