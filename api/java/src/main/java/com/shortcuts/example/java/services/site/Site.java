package com.shortcuts.example.java.services.site;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
public class Site {

    private String display_name;
    private LocalDateTime updated_utc_date_time;
    private LocalDateTime created_utc_date_time;
    private String version;
    private String href;

}
