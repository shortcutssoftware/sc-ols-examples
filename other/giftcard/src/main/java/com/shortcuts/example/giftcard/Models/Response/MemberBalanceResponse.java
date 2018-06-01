package com.shortcuts.example.giftcard.Models.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MemberBalanceResponse {

    public enum CardTypeCode {
        giftcard, loyalty_card, membership_card
    }

    @JsonProperty("balance_ex_tax_amount")
    @JsonInclude
    private BigDecimal balanceExTaxAmount;

    @JsonProperty("balance_inc_tax_amount")
    @JsonInclude
    private BigDecimal balanceIncTaxAmount;

    @JsonProperty("expiry_date")
    @JsonInclude
    private String expiryDate;

    @JsonProperty("currency_code")
    @JsonInclude
    private String currencyCode;

    @JsonProperty("program_name")
    @JsonInclude
    private String programName;

    @JsonProperty("member_number")
    @JsonInclude
    private String memberNumber;

    @JsonProperty("pos_card_type_id")
    @JsonInclude
    private String posCardTypeIdentification;

    @JsonProperty("balance_points")
    @JsonInclude
    private String balancePoints;

    @JsonProperty("card_type_code")
    @JsonInclude
    private CardTypeCode cardType;

}
