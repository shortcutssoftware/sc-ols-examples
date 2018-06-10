package com.shortcuts.example.giftcard.Models.RequestBody;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReloadCardRequestBody extends CardServiceRequestBody{
    @JsonProperty("reload_amount")
    private BigDecimal reloadAmount;
}
