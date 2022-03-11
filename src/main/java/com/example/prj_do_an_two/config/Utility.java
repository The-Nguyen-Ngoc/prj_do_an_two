package com.example.prj_do_an_two.config;

import com.example.prj_do_an_two.config.auth.CustomerOAuth2User;
import com.example.prj_do_an_two.entity.CurrencySettingBag;
import com.example.prj_do_an_two.entity.EmailSettingBag;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Properties;

public class Utility {
    public static String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();

        return siteURL.replace(request.getServletPath(), "");
    }

    public static JavaMailSender prepareMailSender(EmailSettingBag emailSettingBag) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(emailSettingBag.getHost());
        mailSender.setPort(emailSettingBag.getPort());
        mailSender.setUsername(emailSettingBag.getUserName());
        mailSender.setPassword(emailSettingBag.getPassword());

        Properties javaMailProperties = new Properties();
        javaMailProperties.setProperty("mail.smtp.auth", emailSettingBag.getSmtpAuth());
        javaMailProperties.setProperty("mail.smtp.starttls.enable", emailSettingBag.getSmtpSecured());

        mailSender.setJavaMailProperties(javaMailProperties);

        return mailSender;
    }

    public static String getEmailOfAuthenticatedCustomer(HttpServletRequest request) {

        Object principal = request.getUserPrincipal();
        if(principal == null) return null;
        String customerEmail = null;
        if (principal instanceof UsernamePasswordAuthenticationToken || principal instanceof RememberMeAuthenticationToken) {

            customerEmail = request.getUserPrincipal().getName();
        } else if (principal instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) principal;
            CustomerOAuth2User oAuth2User = (CustomerOAuth2User) oAuth2AuthenticationToken.getPrincipal();
            customerEmail = oAuth2User.getEmail();
        }

        return customerEmail;
    }

    public static String formatCurrency(float amount, CurrencySettingBag settings) {
        String symbol = settings.getSymbol();
        String symbolPosition = settings.getSymbolPosition();
        String decimalPointType = settings.getDecimalPointType();
        String thousandPointType = settings.getThousandPointType();
        int decimalDigits = settings.getDecimalDigit();

        String pattern = symbolPosition.equals("before") ? symbol : "";
        pattern += "###,###";

        if (decimalDigits > 0) {
            pattern += ".";
            for (int count = 1; count <= decimalDigits; count++) pattern += "#";
        }

        pattern += symbolPosition.equals("after") ? symbol : "";

        char thousandSeparator = thousandPointType.equals("POINT") ? '.' : ',';
        char decimalSeparator = decimalPointType.equals("POINT") ? '.' : ',';

        DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance();
        decimalFormatSymbols.setDecimalSeparator(decimalSeparator);
        decimalFormatSymbols.setGroupingSeparator(thousandSeparator);

        DecimalFormat formatter = new DecimalFormat(pattern, decimalFormatSymbols);

        return formatter.format(amount);
    }

}
