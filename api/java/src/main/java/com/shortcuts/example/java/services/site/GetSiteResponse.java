package com.shortcuts.example.java.services.site;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class GetSiteResponse {

    private Site site;

}
