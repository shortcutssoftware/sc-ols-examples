package com.shortcuts.example.giftcard.Models.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class BaseResponse {

    public enum ERROR_TYPE_CODE {
        validation, system, not_supported, communication, offline, authorization, license
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ERROR_TYPE_CODE error_type_code;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String detailMessage;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String property_name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String data;

}
