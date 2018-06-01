package com.shortcuts.example.giftcard.Models.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper=false)
public class CardServiceResponse extends BaseResponse {
    @JsonProperty("transaction_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String transactionId;

    @JsonProperty("transaction_ex_tax_amount")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal transactionExTaxAmount;

    @JsonProperty("transaction_inc_tax_amount")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal transactionIncTaxAmount;

    @JsonProperty("member_balance")
    @JsonInclude
    private MemberBalanceResponse memberBalanceResponse;
}
