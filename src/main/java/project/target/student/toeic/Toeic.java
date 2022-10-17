package project.target.student.toeic;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class Toeic {

    @NotEmpty
    private String toeicIdCode; // 개인코드
    @NotEmpty
    private String toeicName; // 이름
    @NotEmpty
    private String toeicSsn; // 주민등록번호
    @NotEmpty
    private String toeicAddress; // 주소
    @NotEmpty
    private String toeicExamDate; // 시험응시일자
    @NotEmpty
    private String toeicExamLoc; // 시험응시장소
    @NotEmpty
    private String toeicRgstDate; // 등록일자 - 대상 등록시 서버에서 자동생성

    private String toeicUpdateDate; // 수정일자 - 업데이트시 서버에서 자동생성
}
