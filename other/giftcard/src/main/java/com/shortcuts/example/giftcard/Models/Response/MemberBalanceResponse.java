package com.shortcuts.example.giftcard.Models.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MemberBalanceResponse {

    public enum CardTypeCode {
        giftcard, loyalty_card, membership_card
    }

    @JsonProperty("balance_ex_tax_amount")
    private BigDecimal balanceExTaxAmount;

    @JsonProperty("balance_inc_tax_amount")
    private BigDecimal balanceIncTaxAmount;

    @JsonProperty("expiry_date")
    private String expiryDate;

    @JsonProperty("currency_code")
    private String currencyCode;

    @JsonProperty("program_name")
    private String programName;

    @JsonProperty("member_number")
    private String memberNumber;

    @JsonProperty("pos_card_type_id")
    private String posCardTypeIdentification;

    @JsonProperty("balance_points")
    private String balancePoints;

    @JsonProperty("card_type_code")
    private CardTypeCode cardType;

}
