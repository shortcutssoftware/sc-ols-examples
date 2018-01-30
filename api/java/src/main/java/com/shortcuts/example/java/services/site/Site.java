package com.shortcuts.example.java.services.site;

import com.shortcuts.example.java.services.Link;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class Site {

    private String display_name;
    private LocalDateTime updated_utc_date_time;
    private LocalDateTime created_utc_date_time;
    private String version;
    private String href;
    private Boolean is_active;
    private Boolean is_subscribed;
    private Boolean is_connected;
    private List<Link> links;
}
