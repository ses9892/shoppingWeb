package com.store.project.application.util;

import com.store.project.application.domain.dto.Role;
import com.store.project.application.domain.entity.Client;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAtt {

    private Map<String,Object> attributes;
    private String nameAttKey;
    private String name;
    private String email;
    private String picture;

    @Builder
    public OAuthAtt(Map<String, Object> attributes,
                           String nameAttributeKey, String name,
                           String email, String picture) {
        this.attributes = attributes;
        this.nameAttKey= nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }
    public static OAuthAtt of(String registrationId,
                              String userNameAttributeName,
                              Map<String, Object> attributes) {
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAtt ofGoogle(String userNameAttributeName,
                                     Map<String, Object> attributes) {
        return OAuthAtt.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }
    public Client toEntity() {
        Client client = new Client();
        client.setUserId(email);
        client.setName(name);
        client.setPassword("");
        client.setRole(Role.USER);
        client.setPhone("Google User");
        client.setAddress("Google User");
        client.setEmail(email);
        return client;
    }
    public void clientUpdate(Client client){
        client.setUserId(email);
        client.setEmail(email);
    }
}
