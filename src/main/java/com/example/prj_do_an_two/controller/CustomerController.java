package com.example.prj_do_an_two.controller;

import com.example.prj_do_an_two.config.Utility;
import com.example.prj_do_an_two.entity.Country;
import com.example.prj_do_an_two.entity.Customer;
import com.example.prj_do_an_two.entity.EmailSettingBag;
import com.example.prj_do_an_two.service.CustomerService;
import com.example.prj_do_an_two.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
public class CustomerController {
    @Autowired
    CustomerService customerService;
    @Autowired
    SettingService settingService;

    @GetMapping("/register")
    public String showRegisterForm(Model model){
        List<Country> countryList = customerService.listAllCountries();

        model.addAttribute("countryList", countryList);
        model.addAttribute("pageTitle", "Đăng Ký");
        model.addAttribute("customer", new Customer());

        return "register/register_form";
    }

    @PostMapping("/create_customer")
    public String createCustomer(Customer customer, Model model,
                                 HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        customerService.registerCustomer(customer);
        sendVerifycationEmail(request, customer);

        model.addAttribute("pageTitle", "Đăng Ký Thành Công");
        return "register/register_success";
    }

    private void sendVerifycationEmail(HttpServletRequest request, Customer customer) throws MessagingException, UnsupportedEncodingException {

        EmailSettingBag emailSettingBag = settingService.getEmailSettings();
        JavaMailSenderImpl mailSender = (JavaMailSenderImpl) Utility.prepareMailSender(emailSettingBag);

        String toAddress = customer.getEmail();
        String subject = emailSettingBag.getCustomerVerifySubject();
        String content = emailSettingBag.getCustomerVerifyContent();

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, false, "UTF-8");

        mimeMessageHelper.setFrom(emailSettingBag.getFromAddress(), emailSettingBag.getSenderName());
        mimeMessageHelper.setTo(toAddress);
        mimeMessageHelper.setSubject(subject);


        content = content.replace("[[name]]", customer.getFullName());

        String verifyURL = Utility.getSiteURL(request) + "/verify?code=" + customer.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        mimeMessageHelper.setText(content, true);


        mailSender.send(message);


    }

    @GetMapping("/verify")
    public String verifyCustomer(@RequestParam(name = "code") String code, Model model) {
        boolean verify = customerService.verify(code);

        return "register/" + (verify ? "verify_success" : "verify_fail");
    }
}
