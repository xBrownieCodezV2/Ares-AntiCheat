package com.gladurbad.ares.util.debug;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Debug<T> {
    private final String name;
    private final T info;
}
