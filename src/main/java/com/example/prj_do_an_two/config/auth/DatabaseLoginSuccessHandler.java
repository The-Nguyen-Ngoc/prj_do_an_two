package com.example.prj_do_an_two.config.auth;

import com.example.prj_do_an_two.config.CustomerUserDetails;
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
public class DatabaseLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Autowired private CustomerService customerService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        CustomerUserDetails customerUserDetails = (CustomerUserDetails) authentication.getPrincipal();
        Customer customer = customerUserDetails.getCustomer();
        customerService.updateAuthentication(customer, AuthenticationType.DATABASE);

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
