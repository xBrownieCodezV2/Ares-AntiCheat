package com.gladurbad.ares.util.container;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class HeterogeneousContainer {

    private final Map<Class<?>, List<Object>> containerMap = new HashMap<>();

    public <T> HeterogeneousContainer put(T object, Class<?> type) {
        containerMap.putIfAbsent(type, new ArrayList<>());
        containerMap.get(type).add(object);

        return this;
    }

    public <T> List<T> get(Class<T> type) {
        // Ignore the warning, unchecked cast is perfectly safe here.
        containerMap.computeIfAbsent(type, k -> new ArrayList<>());

        return (List<T>) containerMap.get(type);
    }

    public <T> T getSpecific(Class<T> specificType) {
        for (List<Object> objectList : containerMap.values()) {
            for (Object o : objectList) {
                if (o.getClass() == specificType) {
                    return (T) o;
                }
            }
        }

        return null;
    }

    public int size() {
        int count = 0;

        for (List<Object> list : containerMap.values()) {
            count += list.size();
        }

        return count;
    }
}
