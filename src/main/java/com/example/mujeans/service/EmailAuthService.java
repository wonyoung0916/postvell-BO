package com.example.mujeans.service;

import com.example.mujeans.model.MailDTO;
import com.example.mujeans.model.MemberDTO;
import com.example.mujeans.repository.EmailAuthRepository;
import com.example.mujeans.repository.signIn.SignInRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Random;


@Log4j2
@Service("EmailAuthService")
@RequiredArgsConstructor
public class EmailAuthService {

    @Autowired
    private final SignInRepository signInRepository;

    private final EmailAuthRepository emailAuthRepository;

    private final JavaMailSender javaMailSender;

    //인증번호 생성
    private final String ePw = createKey();

    @Transactional
    public boolean certified(MailDTO mail){
        //변수정리
        boolean success = false;
        MailDTO latestMail;

        List<MailDTO> list = emailAuthRepository.findByEmailAndCertifiedYnOrderByCreatedAtDesc(mail.getEmail(), "N");

        if (list.size() > 0){
            latestMail = list.get(0);
            log.info("이메일 인증 DTO :: "+latestMail);
            latestMail.setCertifiedYn("Y");
            success = true;
        }

        return success;
    }

    @Transactional
    public String sendSimpleMessage(MailDTO mailDto)throws Exception {
        MimeMessage message = createMessage(mailDto);

        try{
            javaMailSender.send(message); // 메일 발송

            mailDto.setCode(ePw);
            emailAuthRepository.save(mailDto);

        }catch(MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
        return ePw; // 메일로 보냈던 인증 코드를 서버로 리턴
    }

    public MimeMessage createMessage(MailDTO mailDto)throws MessagingException, UnsupportedEncodingException {
        log.info("보내는 대상 : "+ mailDto.getEmail());
        log.info("인증 번호 : " + ePw);
        MimeMessage  message = javaMailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, mailDto.getEmail()); // to 보내는 대상
        message.setSubject("[뮤진스] 가입을 위한 이메일 인증"); //메일 제목

        // 메일 내용 메일의 subtype을 html로 지정하여 html문법 사용 가능

        /*
        String msg="";
        msg += "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">이메일 주소 확인</h1>";
        msg += "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">아래 확인 코드를 회원가입 화면에서 입력해주세요.</p>";
        msg += "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">";
        msg += ePw;
        msg += "</td></tr></tbody></table></div>";
         */

        StringBuffer emailcontent = new StringBuffer();
        emailcontent.append("<!DOCTYPE html>");
        emailcontent.append("<html>");
        emailcontent.append("<head>");
        emailcontent.append("</head>");
        emailcontent.append("<body>");
        emailcontent.append(
                " <div" 																																																	+
                        "	style=\"font-family: 'Apple SD Gothic Neo', 'sans-serif' !important; width: 400px; height: 600px; border-top: 4px solid #02b875; margin: 100px auto; padding: 30px 0; box-sizing: border-box;\">"		+
                        "	<h1 style=\"margin: 0; padding: 0 5px; font-size: 28px; font-weight: 400;\">"																															+
                        "		<span style=\"font-size: 15px; margin: 0 0 10px 3px;\">시크릿우편함 벨벨</span><br />"																													+
                        "		<span style=\"color: #02b875\">메일인증</span> 안내입니다."																																				+
                        "	</h1>\n"																																																+
                        "	<p style=\"font-size: 16px; line-height: 26px; margin-top: 50px; padding: 0 5px;\">"																													+
                        mailDto.getEmail()																																																+
                        "		님 안녕하세요.<br />"																																													+
                        "		시크릿우편함 벨벨에 가입해 주셔서 진심으로 감사드립니다.<br />"																																						+
                        "		아래 <b style=\"color: #02b875\">'메일 인증'</b> 버튼을 클릭하여 회원가입을 완료해 주세요.<br />"																													+
                        "		감사합니다."																																															+
                        "	</p>"																																																	+
                        "	<a style=\"color: #FFF; text-decoration: none; text-align: center;\""																																	+
                        "	href=\"http://192.168.1.43:9099/emailAuth/certified?email=" + mailDto.getEmail() + "\" target=\"_blank\">"														+
                        "		<p"																																																	+
                        "			style=\"display: inline-block; width: 210px; height: 45px; margin: 30px 5px 40px; background: #02b875; line-height: 45px; vertical-align: middle; font-size: 16px;\">"							+
                        "			메일 인증</p>"																																														+
                        "	</a>"																																																	+
                        "	<div style=\"border-top: 1px solid #DDD; padding: 5px;\"></div>"																																		+
                        " </div>"
        );
        emailcontent.append("</body>");
        emailcontent.append("</html>");

        message.setText(emailcontent.toString(), "utf-8", "html"); //내용, charset타입, subtype
        message.setFrom(new InternetAddress(mailDto.getEmail(),"뮤진스 테스트")); //보내는 사람의 메일 주소, 보내는 사람 이름

        return message;
    }

    // 인증코드 만들기
    public static String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 6; i++) { // 인증코드 6자리
            key.append((rnd.nextInt(10)));
        }
        return key.toString();
    }
}
