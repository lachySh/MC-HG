package com.au.lachysh.mchg.gamemap;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Coordinates {
    private Double x;
    private Double y;
    private Double z;
    private Float yaw;
    private Float pitch;
}
