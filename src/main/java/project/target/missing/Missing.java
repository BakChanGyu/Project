package project.target.missing;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.sql.Date;

@Data
public class Missing {

    @NotEmpty
    private String idCode; // 개인 코드
    @NotEmpty
    private String name; // 이름
    @NotEmpty
    private String ssn; // 주민등록번호
    @NotEmpty
    private String address; // 주소

    private String missingDate; // 실종일자

    private String protectorName; // 보호자 성함

    private String protectorTel; // 보호자 연락처
    @NotEmpty
    private String rgstDate; // 등록일자 - 실종자 등록시 서버에서 자동생성

    private String updateDate; // 수정일자 - 업데이트시 서버에서 자동생성
}
