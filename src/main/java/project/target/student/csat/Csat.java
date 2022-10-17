package project.target.student.csat;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class Csat {

    @NotEmpty
    private String csatIdCode; // 개인코드
    @NotEmpty
    private String csatName; // 이름
    @NotEmpty
    private String csatSsn; // 주민등록번호
    @NotEmpty
    private String csatAddress; // 주소
    @NotEmpty
    private String csatExamDate; // 시험응시일자
    @NotEmpty
    private String csatExamLoc; // 시험응시장소
    @NotEmpty
    private String csatRgstDate; // 등록일자 - 대상 등록시 서버에서 자동생성

    private String csatUpdateDate; // 수정일자 - 업데이트시 서버에서 자동생성
}
