package com.shortcuts.example.giftcard.Models.RequestBody;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RedeemCardRequestBody extends CardServiceRequestBody{
    @JsonProperty("redemption_amount")
    private BigDecimal redemptionAmount;
}
