package com.shortcuts.example.single_signon.shortcuts;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthenticateCustomerBody {

    private String credentialTypeCode;
    private String tokenType;
    private String accessToken;

    @JsonProperty("credential_type_code")
    public String getCredentialTypeCode() {
        return credentialTypeCode;
    }

    public void setCredentialTypeCode(String credentialTypeCode) {
        this.credentialTypeCode = credentialTypeCode;
    }

    @JsonProperty("token_type")
    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    @JsonProperty("access_token")
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
