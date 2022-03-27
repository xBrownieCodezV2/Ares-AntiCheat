package com.gladurbad.ares.util.debug;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Pair<K, V> {
    private final K key;
    private final V val;
}
