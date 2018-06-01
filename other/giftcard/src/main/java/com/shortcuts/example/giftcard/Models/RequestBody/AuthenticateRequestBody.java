package com.shortcuts.example.giftcard.Models.RequestBody;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticateRequestBody {

    @JsonProperty("credential_type_code")
    private String credentialTypeCode;

    @JsonProperty("serial_number")
    private String serialNumber;

    @JsonProperty("site_installation_id")
    private String siteInstallationId;
}
