package com.example.prj_do_an_two.controller;

import com.example.prj_do_an_two.config.Utility;
import com.example.prj_do_an_two.entity.EmailSettingBag;
import com.example.prj_do_an_two.exception.CustomerNotFoundException;
import com.example.prj_do_an_two.service.CustomerService;
import com.example.prj_do_an_two.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Controller
public class ForgotPasswordController {
    @Autowired private CustomerService customerService;
    @Autowired private SettingService settingService;

    @GetMapping("forgot_password")
    public String showRequestForm(){
        return "customer/forgot_password_form";
    }
    @PostMapping("/forgot_password")
    public String showRequestForm(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");
        EmailSettingBag emailSettingBag = settingService.getEmailSettings();
        JavaMailSenderImpl mailSender = (JavaMailSenderImpl) Utility.prepareMailSender(emailSettingBag);


        try {
           String token =  customerService.updateResetPasswordToken(email);
           String link = Utility.getSiteURL(request) + "/reset_password?token=" + token;
           sendEmail(link, email);
           model.addAttribute("message", "Đã gửi đường liên kết đổi mật khẩu đến email của bạn");
        } catch (CustomerNotFoundException e) {
            model.addAttribute("error", e.getMessage());
        } catch (MessagingException |UnsupportedEncodingException e) {
            model.addAttribute("error", "Không gửi được email");

        }
        return "customer/forgot_password_form";
    }

    private void sendEmail(String link, String email) throws MessagingException, UnsupportedEncodingException {
        EmailSettingBag emailSettingBag = settingService.getEmailSettings();
        JavaMailSenderImpl mailSender = (JavaMailSenderImpl) Utility.prepareMailSender(emailSettingBag);

        String toAddress = email;
        String subject = "Liên kết đổi mật khẩu";

        String content = "<p>Xin chào,</p>" +
                "<p>Bạn đã yêu cầu đổi mật khẩu tại website của chúng tôi. " +
                "Vui lòng nhấp vào liên kết bên dưới để hoàn tất quá trình đổi mật khẩu.</p>" +
                link;

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, false, "UTF-8");

        mimeMessageHelper.setFrom(emailSettingBag.getFromAddress(), emailSettingBag.getSenderName());
        mimeMessageHelper.setTo(toAddress);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(content, true);
        mailSender.send(message);
    }
}
