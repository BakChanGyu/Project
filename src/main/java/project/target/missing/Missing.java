package project.target.missing;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.sql.Date;

@Data
public class Missing {

    @NotEmpty
    private String missingIdCode; // 개인 코드
    @NotEmpty
    private String missingName; // 이름
    @NotEmpty
    private String missingSsn; // 주민등록번호
    @NotEmpty
    private String missingAddress; // 주소

    private String missingDate; // 실종일자

    private String protectorName; // 보호자 성함

    private String protectorTel; // 보호자 연락처
    @NotEmpty
    private String missingRgstDate; // 등록일자 - 실종자 등록시 서버에서 자동생성

    private String missingUpdateDate; // 수정일자 - 업데이트시 서버에서 자동생성
}
