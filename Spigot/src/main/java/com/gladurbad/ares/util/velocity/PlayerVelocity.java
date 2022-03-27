package com.gladurbad.ares.util.velocity;

import lombok.Getter;

public class PlayerVelocity {

    @Getter
    private double x, y, z;

    public PlayerVelocity() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public PlayerVelocity(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }


}
