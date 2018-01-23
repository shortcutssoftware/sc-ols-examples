package com.shortcuts.example.java.services.site;

import com.shortcuts.example.java.services.BaseShortcutsAPIService;
import com.shortcuts.example.java.util.ShortcutsAPIException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GetSitesService extends BaseShortcutsAPIService<GetSitesRequest, GetSitesResponse> {

    public GetSitesService() {
        super(HttpMethod.GET);
    }

    @Override
    public GetSitesResponse call(
            String jwtToken,
            HttpHeaders httpHeaders,
            GetSitesRequest requestObject) throws ShortcutsAPIException {
        String endpoint = getEndpoint("sites");
        HttpHeaders headers = setupAuthorizationHeader(httpHeaders, jwtToken);
        GetSitesResponse getSitesResponse = restTemplateCallingUtil.getForObject(
                endpoint,
                headers,
                GetSitesResponse.class);
        log.info("response: {}", getSitesResponse);
        return getSitesResponse;
    }
}
