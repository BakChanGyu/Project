package project.valid;

import org.springframework.stereotype.Component;
import project.member.Member;
import project.target.missing.Missing;
import project.target.student.csat.Csat;
import project.target.student.toeic.Toeic;

@Component
public class ValidCheck {

    private final Regex regex;

    public ValidCheck(Regex regex) {
        this.regex = regex;
    }

    public int memberInfo(Member member) {
        if (!regex.isLoginId(member.getLoginId())) {
            return 1;
        } else if (!regex.isPassword(member.getPassword())) {
            return 2;
        } else if (!regex.isEmail(member.getEmail())) {
            return 3;
        } else {
            return 0;
        }
    }

    public int missingInfo(Missing missing) {
        if (!regex.isSsn(missing.getMissingSsn())) {
            return 1;
        } else if (!regex.isAddress(missing.getMissingAddress())) {
            return 2;
        } else if (!regex.isDate(missing.getMissingDate())) {
            return 3;
        } else if (!regex.isPhone(missing.getProtectorTel())) {
            return 4;
        } else {
            return 0;
        }
    }

    public int csatInfo(Csat csat) {
        if (!regex.isSsn(csat.getCsatSsn())) {
            return 1;
        } else if (!regex.isAddress(csat.getCsatAddress())) {
            return 2;
        } else if (!regex.isDate(csat.getCsatExamDate())) {
            return 3;
        } else {
            return 4;
        }
    }

    public int toeicInfo(Toeic toeic) {
        if (!regex.isSsn(toeic.getToeicSsn())) {
            return 1;
        } else if (!regex.isAddress(toeic.getToeicAddress())) {
            return 2;
        } else if (!regex.isDate(toeic.getToeicExamDate())) {
            return 3;
        } else {
            return 4;
        }
    }
}
