package com.miko.bookapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FilterObject {
    private String category;
    private String type;
    private Double prize;
}
