package com.shortcuts.example.giftcard.Models.RequestBody;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CardServiceRequestBody {

    @JsonProperty("site_transaction_id")
    private String siteTransactionId;

    @JsonProperty("site_transaction_date_time")
    private String siteTransactionDateTime;
}
