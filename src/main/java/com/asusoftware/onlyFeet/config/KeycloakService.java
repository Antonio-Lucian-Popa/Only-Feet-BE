package com.asusoftware.onlyFeet.config;

import com.asusoftware.onlyFeet.user.model.dto.LoginDto;
import com.asusoftware.onlyFeet.user.model.dto.UserRegisterDto;
import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class KeycloakService {

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${keycloak.admin.username}")
    private String adminUsername;

    @Value("${keycloak.admin.password}")
    private String adminPassword;

    public String createKeycloakUser(UserRegisterDto userDTO) {
        Keycloak keycloak = getKeycloakAdminInstance();

        UserRepresentation user = new UserRepresentation();
        user.setUsername(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setEnabled(true);
        user.setEmailVerified(true);
        user.setRealmRoles(Collections.singletonList(userDTO.getUserRole()));

        CredentialRepresentation password = new CredentialRepresentation();
        password.setType(CredentialRepresentation.PASSWORD);
        password.setTemporary(false);
        password.setValue(userDTO.getPassword());
        user.setCredentials(Collections.singletonList(password));

        Response response = keycloak.realm(realm).users().create(user);
        if (response.getStatus() == 201) {
            String location = response.getHeaderString("Location");
            return location.substring(location.lastIndexOf('/') + 1);
        } else {
            throw new RuntimeException("Failed to create user in Keycloak: " + response.getStatus());
        }
    }

    public AccessTokenResponse loginUser(LoginDto loginDto) {
        try {
            return obtainToken(loginDto.getEmail(), loginDto.getPassword());
        } catch (Exception e) {
            throw new RuntimeException("Failed to login user: " + e.getMessage(), e);
        }
    }

    public void deleteKeycloakUser(String keycloakId) {
        Keycloak keycloak = getKeycloakAdminInstance();
        keycloak.realm(realm).users().delete(keycloakId);
    }

    private AccessTokenResponse obtainToken(String username, String password) {
        return KeycloakBuilder.builder()
                .serverUrl(authServerUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .username(username)
                .password(password)
                .grantType(OAuth2Constants.PASSWORD)
                .build()
                .tokenManager()
                .getAccessToken();
    }

    private Keycloak getKeycloakAdminInstance() {
        return KeycloakBuilder.builder()
                .serverUrl(authServerUrl)
                .realm("master")
                .clientId("admin-cli")
                .username(adminUsername)
                .password(adminPassword)
                .grantType(OAuth2Constants.PASSWORD)
                .build();
    }
}
