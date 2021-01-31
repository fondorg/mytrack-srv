package ru.fondorg.mytracksrv.service;

import lombok.RequiredArgsConstructor;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.fondorg.mytracksrv.domain.User;

import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
public class KeycloakService {

    private final KeycloakSpringBootProperties properties;

    private final KeycloakRestTemplate keycloakRestTemplate;

    private String usersEndpoint;

    @PostConstruct
    public void init() {
        usersEndpoint = properties.getAuthServerUrl() + "/admin/realms/" + properties.getRealm() + "/users/";
    }

    public User getUserById(String id) {
        ResponseEntity<User> response = keycloakRestTemplate.exchange("http://keycloak:9999/auth/admin/realms/mytrack/users/" + id,
                HttpMethod.GET, null, User.class);
        return response.getBody();
    }
}
