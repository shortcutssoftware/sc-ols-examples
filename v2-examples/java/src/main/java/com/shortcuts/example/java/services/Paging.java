package com.shortcuts.example.java.services;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Paging {

    private Integer page;
    private Integer number_of_pages;

}
