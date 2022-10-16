package project.target.student.csat;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.sql.Date;

@Data
public class Csat {

    @NotEmpty
    private String idCode; // 개인코드
    @NotEmpty
    private String name; // 이름
    @NotEmpty
    private String ssn; // 주민등록번호
    @NotEmpty
    private String address; // 주소
    @NotEmpty
    private String examDate; // 시험응시일자
    @NotEmpty
    private String examLoc; // 시험응시장소
    @NotEmpty
    private String rgstDate; // 등록일자 - 대상 등록시 서버에서 자동생성

    private String updateDate; // 수정일자 - 업데이트시 서버에서 자동생성
}
