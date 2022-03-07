package com.example.prj_do_an_two.controller;

import com.example.prj_do_an_two.config.CustomerUserDetails;
import com.example.prj_do_an_two.config.Utility;
import com.example.prj_do_an_two.config.auth.CustomerOAuth2User;
import com.example.prj_do_an_two.entity.Country;
import com.example.prj_do_an_two.entity.Customer;
import com.example.prj_do_an_two.entity.EmailSettingBag;
import com.example.prj_do_an_two.exception.CustomerNotFoundException;
import com.example.prj_do_an_two.service.CustomerService;
import com.example.prj_do_an_two.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String showRegisterForm(Model model) {
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

    @GetMapping("/account_details")
    public String viewAccountDetails(Model model, HttpServletRequest request) {

        String email = Utility.getEmailOfAuthenticatedCustomer(request);
        Customer customer = customerService.getCustomerByEmail(email);
        customer.setPassword("");
        List<Country> countryList = customerService.listAllCountries();
        model.addAttribute("pageTitle", "Thông Tin Tài Khoản");
        model.addAttribute("customer", customer);
        model.addAttribute("countries", countryList);
        return "customer/account_form";
    }

//    private String getEmailOfAuthenticatedCustomer(HttpServletRequest request) {
//
//        Object principal = request.getUserPrincipal();
//        String customerEmail = null;
//        if (principal instanceof UsernamePasswordAuthenticationToken || principal instanceof RememberMeAuthenticationToken) {
//
//            customerEmail = request.getUserPrincipal().getName();
//        } else if (principal instanceof OAuth2AuthenticationToken) {
//            OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) principal;
//            CustomerOAuth2User oAuth2User = (CustomerOAuth2User) oAuth2AuthenticationToken.getPrincipal();
//            customerEmail = oAuth2User.getEmail();
//        }
//
//        return customerEmail;
//    }

    @PostMapping("/update_account_details")
    public String updateAccountDetails(Customer customer, HttpServletRequest request, RedirectAttributes redirectAttributes,
                                       Model model) {
        customerService.update(customer);

        redirectAttributes.addFlashAttribute("message", "Cập Nhật Thông Tin Thành Công");

        updateNameForAuthenticatedCustomer(request, customer);
        return "redirect:/account_details";
    }

    private void updateNameForAuthenticatedCustomer(HttpServletRequest request, Customer customer) {

        Object principal = request.getUserPrincipal();
        if (principal instanceof UsernamePasswordAuthenticationToken || principal instanceof RememberMeAuthenticationToken) {
            CustomerUserDetails customerUserDetails = getCustomerUserDetailsObject(principal);
            Customer authenticatedCustomer = customerUserDetails.getCustomer();
            authenticatedCustomer.setFirstName(customer.getFirstName());
            authenticatedCustomer.setLastName(customer.getLastName());
        } else if (principal instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) principal;
            CustomerOAuth2User oAuth2User = (CustomerOAuth2User) oAuth2AuthenticationToken.getPrincipal();
            String fullName = customer.getFirstName() + " " + customer.getLastName();
            oAuth2User.setFullName(fullName);
        }
    }

    private CustomerUserDetails getCustomerUserDetailsObject(Object principal) {
        CustomerUserDetails customerUserDetails = null;

        if (principal instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
            customerUserDetails = (CustomerUserDetails) token.getPrincipal();
        }
        else if (principal instanceof RememberMeAuthenticationToken) {
            RememberMeAuthenticationToken token = (RememberMeAuthenticationToken) principal;
            customerUserDetails = (CustomerUserDetails) token.getPrincipal();
        }
        return customerUserDetails;
    }

    @GetMapping("/reset_password")
    public String showResetForm(@RequestParam("token")String token, Model model)  {
        Customer customer = null;
        try {
            customer = customerService.getByResetPasswordToken(token);
        } catch (CustomerNotFoundException e) {
            model.addAttribute("message", "Token không hợp lệ");
            return "customer/message";
        }
        if (customer != null) {
            model.addAttribute("token", token);
        }else{
            model.addAttribute("message", "Token không Hợp Lệ");
            return "customer/message";
        }

        return "customer/reset_password_form";
    }

    @PostMapping("/reset_password")
    public String processResetForm(HttpServletRequest request,  Model model){
        String token = request.getParameter("token");
        String password = request.getParameter("password");

        try {
            customerService.updatePassword(token, password);
            model.addAttribute("message", "Mật khẩu được thay đổi thành công!");

            return "customer/message";
        } catch (CustomerNotFoundException e) {
            model.addAttribute("message", "Token Hợp Lệ");
            model.addAttribute("message", e.getMessage());
            return "customer/message";
        }
    }


}
