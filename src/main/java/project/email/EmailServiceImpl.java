package project.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import project.member.Member;
import project.repository.MemberRepositoryImpl;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{

    // Bean등록해둔 EmailConfig를 javaMailSender라는 이름으로 Autowired
    @Autowired
    JavaMailSender javaMailSender;
    private final String emailPassword = createKey(); // 인증코드

    @Override
    public String sendSimpleMessage(Member member) throws Exception {

        MimeMessage message = createMessage(member);
        try {
            javaMailSender.send(message);

        } catch (MailException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
        return emailPassword;
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
        msg += "<a href='http://localhost:8080"
                + "/api/verifyCode?id=" + member.getMemberId()
                + "&private_key=" + emailPassword
                + "'> 인증하기</a>";
        msg += "<br>";
        msg += "</div>";

        message.setText(msg, "utf-8", "html");
        message.setFrom(new InternetAddress("parkcg123123@gmail.com", "Admin"));

        return message;
    }

    // 인증키를 만드는 메서드
    public static String createKey() {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();

        // 8자리 인증코드 생성
        for (int i = 0; i < 8; i++) {
            // 0~2사이 랜덤한 정수 저장
            int index = random.nextInt(3);

            switch (index) {
                case 0:
                    // a~z사이 알파벳 ('a' = 97)
                    sb.append((char) ((int) (random.nextInt(26)) + 97));
                    break;
                case 1:
                    // A~Z사이 알파벳 ('A' = 65)
                    sb.append((char) ((int) (random.nextInt(26)) + 65));
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
