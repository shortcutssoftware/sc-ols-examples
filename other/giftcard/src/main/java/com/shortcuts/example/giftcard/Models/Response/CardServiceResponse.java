package com.shortcuts.example.giftcard.Models.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper=false)
public class CardServiceResponse extends BaseResponse {

    @JsonProperty("transaction_id")
    private String transactionId;

    @JsonProperty("transaction_ex_tax_amount")
    private BigDecimal transactionExTaxAmount;

    @JsonProperty("transaction_inc_tax_amount")
    private BigDecimal transactionIncTaxAmount;

    @JsonProperty("member_balance")
    private MemberBalanceResponse memberBalanceResponse;
}
