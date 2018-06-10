package com.shortcuts.example.giftcard.Models.RequestBody;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ActivateCardRequestBody extends CardServiceRequestBody {

    @JsonProperty("activation_inc_tax_amount")
    private BigDecimal activationIncTaxAmount;
}
