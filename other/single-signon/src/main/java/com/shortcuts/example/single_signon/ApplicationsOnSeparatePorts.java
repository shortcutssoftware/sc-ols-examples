package com.shortcuts.example.single_signon;

import com.shortcuts.example.single_signon.salon.SalonApplication;
import com.shortcuts.example.single_signon.shortcuts.ShortcutsApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

public class ApplicationsOnSeparatePorts {

    public static void main(String[] args) {
        new SpringApplicationBuilder(SalonApplication.class)
                .properties("server.port=8080")
                .run();
        new SpringApplicationBuilder(ShortcutsApplication.class)
                .properties("server.port=9090")
                .run();
    }
}
