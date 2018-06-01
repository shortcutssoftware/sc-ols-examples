package com.shortcuts.example.giftcard.Models.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse {

    public enum ERROR_TYPE_CODE {
        validation, system, not_supported, communication, offline, authorization, license
    }

    private ERROR_TYPE_CODE error_type_code;

    private String message;

    private String detailMessage;

    private String property_name;

    private String data;

}
