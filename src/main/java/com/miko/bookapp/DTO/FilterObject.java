package com.miko.bookapp.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FilterObject {
    private String category;
    private String type;
    private Double prize;
}
