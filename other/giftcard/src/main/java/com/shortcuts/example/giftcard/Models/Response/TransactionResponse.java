package com.shortcuts.example.giftcard.Models.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper=false)
public class TransactionResponse extends BaseResponse{

    @JsonProperty("transaction_id")
    private String transactionId;

    @JsonProperty("transaction_ex_tax_amount")
    private BigDecimal transactionExTaxAmount;

    @JsonProperty("transaction_inc_tax_amount")
    private BigDecimal transactionIncTaxAmount;

    @JsonProperty("giftcard_ex_tax_balance")
    private BigDecimal giftcardExTaxBalance;

    @JsonProperty("giftcard_inc_tax_balance")
    private BigDecimal giftcardIncTaxBalance;

    @JsonProperty("giftcard_expiry_date")
    private String giftcardExpiryDate;

    @JsonProperty("giftcard_currency_code")
    private String giftcardCurrencyCode;

}
