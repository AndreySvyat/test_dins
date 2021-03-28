package com.rincentral.test.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BodyCharacteristics {
    private List<Integer> bodyStyles;
    private int length;
    private int width;
    private int height;
}
