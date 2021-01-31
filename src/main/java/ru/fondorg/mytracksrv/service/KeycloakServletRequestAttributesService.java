package ru.fondorg.mytracksrv.service;

import org.keycloak.KeycloakSecurityContext;
import org.springframework.stereotype.Service;
import ru.fondorg.mytracksrv.domain.User;

import javax.servlet.http.HttpServletRequest;

/**
 * Keycloak related implementation of {@link ServletRequestAttributesService}
 */
@Service
public class KeycloakServletRequestAttributesService implements ServletRequestAttributesService {

    @Override
    public User getUserFromRequest(HttpServletRequest request) {
        KeycloakSecurityContext context = (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
        User user = new User();
        user.setId(context.getToken().getSubject());
        user.setUsername(context.getToken().getPreferredUsername());
        user.setFirstName(context.getToken().getGivenName());
        user.setLastName(context.getToken().getFamilyName());
        return user;
    }
}
