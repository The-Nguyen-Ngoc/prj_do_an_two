package com.example.prj_do_an_two.config.auth;

import com.example.prj_do_an_two.entity.AuthenticationType;
import com.example.prj_do_an_two.entity.Customer;
import com.example.prj_do_an_two.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private CustomerService customerService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        CustomerOAuth2User oAuth2User = (CustomerOAuth2User) authentication.getPrincipal();

        String name = oAuth2User.getName();
        String email = oAuth2User.getEmail();
        String countryCode = request.getLocale().getCountry();
        String clientName = oAuth2User.getClientName();


        AuthenticationType authenticationType = getAuthenticationType(clientName);
        Customer customer = customerService.getCustomerByEmail(email);

        if(customer==null){
            customerService.addNewCustomerUponOAuthLogin(name,email,countryCode, authenticationType);
        }else {
            oAuth2User.setFullName(customer.getFullName());
            customerService.updateAuthentication(customer, authenticationType);
        }


        super.onAuthenticationSuccess(request, response, authentication);
    }

    private AuthenticationType getAuthenticationType(String client) {
        if (client.equals("Google")) {
            return AuthenticationType.GOOGLE;
        } else if (client.equals("Facebook")) {
            return AuthenticationType.FACEBOOK;
        } else return AuthenticationType.DATABASE;
    }
}
