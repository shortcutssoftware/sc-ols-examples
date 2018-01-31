package com.shortcuts.example.java.services;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Link {

    private String rel;
    private String href;

}
