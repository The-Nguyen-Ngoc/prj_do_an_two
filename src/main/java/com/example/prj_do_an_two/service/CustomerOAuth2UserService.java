package com.example.prj_do_an_two.service;

import com.example.prj_do_an_two.config.auth.CustomerOAuth2User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomerOAuth2UserService extends DefaultOAuth2UserService {
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String clientName = userRequest.getClientRegistration().getClientName();

        OAuth2User oAuth2User =  super.loadUser(userRequest);

        return new CustomerOAuth2User(oAuth2User, clientName);
    }
}