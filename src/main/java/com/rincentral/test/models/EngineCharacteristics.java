package com.rincentral.test.models;

import com.rincentral.test.models.enums.EngineType;
import com.rincentral.test.models.enums.FuelType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder(toBuilder = true)
@AllArgsConstructor
public class EngineCharacteristics {
    private FuelType fuel;
    private EngineType engineType;
    private Integer displacement;
    private int hpw;
}
