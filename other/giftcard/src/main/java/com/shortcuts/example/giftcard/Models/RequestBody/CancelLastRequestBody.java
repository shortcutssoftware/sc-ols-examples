package com.shortcuts.example.giftcard.Models.RequestBody;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CancelLastRequestBody extends CardServiceRequestBody{
    @JsonProperty("original_site_transaction_id")
    private String originalSiteTransactionId;

    @JsonProperty("original_transaction_amount")
    private BigDecimal originalTransactionAmount;
}
