package project.email;

import project.member.Member;

public interface EmailService {
    String sendSimpleMessage(Member member)throws Exception;
}
