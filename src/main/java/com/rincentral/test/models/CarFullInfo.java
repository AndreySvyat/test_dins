package com.rincentral.test.models;

import com.rincentral.test.models.enums.GearboxType;
import com.rincentral.test.models.enums.WheelDriveType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CarFullInfo {
    private Integer id;

    private String segment;

    private String brand;

    private String model;

    private String country;

    private String generation;

    private String modification;

    private Integer masSpeed;

    private Double acceleration;

    private GearboxType gearboxType;

    private WheelDriveType wheelDrive;

    private int yearPresented;

    private Integer yearExpired;

    private Integer body;

    private Integer engine;
}
