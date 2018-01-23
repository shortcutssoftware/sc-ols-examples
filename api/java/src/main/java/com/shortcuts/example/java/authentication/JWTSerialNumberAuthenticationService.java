package com.shortcuts.example.java.authentication;

import com.shortcuts.example.java.BaseUrlAware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class JWTSerialNumberAuthenticationService extends BaseUrlAware implements JWTAuthentication {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${authentication.onPremise.serialNumber}")
    private String serialNumber;

    @Value("${authentication.onPremise.siteInstallationId}")
    private String siteInstallationId;

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getSiteInstallationId() {
        return siteInstallationId;
    }

    @Override
    public String authenticate() {

        // set up an authentication request object
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setCredential_type_code("serial");
        authenticationRequest.setSerial_number(getSerialNumber());
        authenticationRequest.setSite_installation_id(getSiteInstallationId());

        // call the api
        ResponseEntity<AuthenticationResponse> responseEntity = restTemplate.postForEntity(
                String.format("%s/authenticate", getBaseUrl()),
                authenticationRequest,
                AuthenticationResponse.class);

        String jwtToken = responseEntity.getBody().getAccess_token();
        log.info("authentication result: {}", jwtToken);

        return jwtToken;
    }
}
