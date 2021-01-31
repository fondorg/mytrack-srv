package ru.fondorg.mytracksrv.controller;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

//@RestController
public class HelloController {

    //@GetMapping("/hello")
    public ResponseEntity<String> sayHello(KeycloakPrincipal<RefreshableKeycloakSecurityContext> principal) {
        String name = principal.getKeycloakSecurityContext().getToken().getPreferredUsername();
        principal.getKeycloakSecurityContext().getToken().getRealmAccess().getRoles();
        return new ResponseEntity<>(String.format("Hello, %s", name), HttpStatus.OK);
    }
}
