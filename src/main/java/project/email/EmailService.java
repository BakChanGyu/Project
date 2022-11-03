package project.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import project.member.Member;
import project.repository.member.MemberRepository;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Random;

@Service
public class EmailService{

    @Value("${email.url}")
    String emailUrl;

    // Bean등록해둔 EmailConfig를 javaMailSender라는 이름으로 Autowired
    @Autowired
    JavaMailSender javaMailSender;

    private final MemberRepository memberRepository;
    private final String emailPassword = createKey(); // 인증코드

    public EmailService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 메일을 보내는 메서드
    public void sendSimpleMessage(Member member) throws Exception {

        MimeMessage message = createMessage(member);
        try {
            javaMailSender.send(message);
        } catch (MailException e) {
            throw new IllegalArgumentException();
        } finally {
            memberRepository.addPrivateKey(member.getMemberId(), emailPassword);
        }
    }

    // DB에서 id값으로 인증코드 찾아옴
    public String confirmEmail(Long memberId) {
        return memberRepository.findPrivateKeyById(memberId);
    }

    // DB에 인증코드 업데이트
    public void addPrivateKey(Long memberId) {
        memberRepository.addPrivateKey(memberId, "certified");
    }

    // 가입시 사용한 메일로 비밀번호 보내줌.
    public void sendPassword(Member member) throws MessagingException, UnsupportedEncodingException {
        MimeMessage findPwdMessage = createFindPwdMessage(member);

        try {
            javaMailSender.send(findPwdMessage);
        } catch (MailException e) {
            throw new IllegalArgumentException();
        }
    }

    // 메일의 내용을 작성하는 메서드
    private MimeMessage createMessage(Member member) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = javaMailSender.createMimeMessage();

        String to = member.getEmail();
        member.setPrivateKey(emailPassword);

        message.addRecipients(Message.RecipientType.TO, to); // 보내는 대상
        message.setSubject("회원가입 이메일 인증"); // 제목

        String msg = "";
        msg += "<div style='margin:100px;'>";
        msg += "<h2> <strong>" + member.getMemberName() + "</strong> 님 환영합니다!</h2>";
        msg += "<br>";
        msg += "<p>아래 인증 링크를 눌러 회원가입을 완료해주세요!<p>";
        msg += "<br>";
        msg += "<a href='" + emailUrl
                + "/api/verify/code?memberId=" + member.getMemberId()
                + "&privateKey=" + member.getPrivateKey()
                + "'> 인증하기</a>";
        msg += "<br>";
        msg += "</div>";

        message.setText(msg, "utf-8", "html");
        message.setFrom(new InternetAddress("parkcg123123@gmail.com", "Admin"));

        return message;
    }

    // 비밀번호 찾기 시 사용하는 메서드
    private MimeMessage createFindPwdMessage(Member member) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = javaMailSender.createMimeMessage();

        // 이메일을 보내는 대상의 메일 주소
        String to = member.getEmail();

        message.addRecipients(Message.RecipientType.TO, to); // 보내는 대상
        message.setSubject("비밀번호를 전송했습니다."); // 제목

        String msg = "";
        msg += "<div style='margin:100px;'>";
        msg += "<h2> <strong>" + member.getMemberName() + "</strong> 님의 비밀번호입니다.</h2>";
        msg += "<br>";
        msg += "<p> 타인에게 유출되지 않도록 조심하세요.</p>";
        msg += "<br>";
        msg += "<strong>" + member.getPassword() +"</strong>";
        msg += "<br>";
        msg += "</div>";

        message.setText(msg, "utf-8", "html");
        message.setFrom(new InternetAddress("parkcg123123@gmail.com", "Admin"));

        return message;
    }

    // 인증키를 만드는 메서드
    private static String createKey() {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();

        // 8자리 인증코드 생성
        for (int i = 0; i < 8; i++) {
            // 0~2사이 랜덤한 정수 저장
            int index = random.nextInt(3);

            switch (index) {
                case 0:
                    // a~z사이 알파벳 ('a' = 97)
                    sb.append((char) ((random.nextInt(26)) + 97));
                    break;
                case 1:
                    // A~Z사이 알파벳 ('A' = 65)
                    sb.append((char) ((random.nextInt(26)) + 65));
                    break;
                case 2:
                    // 0~9사이 숫자
                    sb.append((random.nextInt(10)));
                    break;
            }
        }

        return sb.toString();
    }

}
