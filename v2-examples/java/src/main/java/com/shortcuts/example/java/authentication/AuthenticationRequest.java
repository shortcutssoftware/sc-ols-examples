package com.shortcuts.example.java.authentication;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthenticationRequest {

    private String credential_type_code;
    private String serial_number;
    private String site_installation_id;

}
