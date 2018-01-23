package com.shortcuts.example.java.services.site;

import com.shortcuts.example.java.services.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class GetSitesResponse {

    private List<Site> sites;
    private Paging paging;
    private String href;

}
