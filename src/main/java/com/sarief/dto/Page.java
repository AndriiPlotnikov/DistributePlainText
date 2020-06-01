package com.sarief.dto;

import com.sarief.entity.Data;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * stab page implementation
 */
@Getter
@Setter
@AllArgsConstructor
public class Page {
    private List<Data> data;
    private String page;
    private int totalElements;
}
