package com.shortcuts.example.java.authentication;

import com.shortcuts.example.java.BaseUrlAware;
import com.shortcuts.example.java.util.RestTemplateCallingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JWTSerialNumberAuthenticationService extends BaseUrlAware implements JWTAuthentication {

    @Autowired
    private RestTemplateCallingUtil restTemplateCallingUtil;

    @Value("${authentication.onPremise.serialNumber}")
    private String serialNumber;

    @Value("${authentication.onPremise.siteInstallationId}")
    private String siteInstallationId;

    public RestTemplateCallingUtil getRestTemplateCallingUtil() {
        return restTemplateCallingUtil;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getSiteInstallationId() {
        return siteInstallationId;
    }

    @Override
    public String authenticate() {

        // set up authentication request object with
        // serial number and site installation id
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setCredential_type_code("serial");
        authenticationRequest.setSerial_number(getSerialNumber());
        authenticationRequest.setSite_installation_id(getSiteInstallationId());

        // call the api
        AuthenticationResponse authenticationResponse = restTemplateCallingUtil.postForObject(
                getEndpoint("authenticate"),
                new HttpHeaders(),
                authenticationRequest,
                AuthenticationResponse.class);

        String jwtToken = authenticationResponse.getAccess_token();
        log.info("authentication result: {}", jwtToken);

        return jwtToken;
    }

}
