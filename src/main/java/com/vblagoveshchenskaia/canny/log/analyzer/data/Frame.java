package com.vblagoveshchenskaia.canny.log.analyzer.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Frame {
    private long data;
    private long mask;
    private int length;
}
