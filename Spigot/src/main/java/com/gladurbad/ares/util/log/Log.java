package com.gladurbad.ares.util.log;

import lombok.Getter;

@Getter
public class Log {

    private final long creationTime = System.currentTimeMillis();
    private final double vl;

    public Log(double vl) {
        // You can store whatever you please in here, for now we just need the VL.
        this.vl = vl;
    }
}
