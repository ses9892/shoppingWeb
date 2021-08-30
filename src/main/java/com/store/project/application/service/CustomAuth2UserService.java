package com.store.project.application.service;

import com.store.project.application.domain.dao.ClientRepository;
import com.store.project.application.domain.entity.Client;
import com.store.project.application.util.OAuthAtt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    @Autowired
    ClientRepository clientRepository;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("OAuth2User LoadUser....");
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        log.info("login Id : "+registrationId);
        String userNameAttribute = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        if(userNameAttribute.equals("sub")){
            log.info("google Login Att : "+ userNameAttribute);
        }
        OAuthAtt att = OAuthAtt.of(registrationId,userNameAttribute,oAuth2User.getAttributes());
        Client client = saveOrUpdate(att);
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(client.getRole().key)),
                att.getAttributes(),
                att.getNameAttKey());
    }

    private Client saveOrUpdate(OAuthAtt att){

        Optional<Client> client = clientRepository.findById(att.getEmail());
        Client ClientEntity = null;
        if(client.isPresent()){
            log.info("Client Not Empty");
            ClientEntity =client.get();
            att.clientUpdate(ClientEntity);
            log.info("Client Update Ok");
        }else{
            log.info("Client Empty");
            ClientEntity = att.toEntity();
            log.info("Client Save Ok");
        }
        return clientRepository.save(ClientEntity);
    }
}
