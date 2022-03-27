package com.gladurbad.ares.data.check;

import com.gladurbad.ares.Ares;
import com.gladurbad.ares.check.AbstractCheck;
import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.util.container.HeterogeneousContainer;
import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.logging.Level;

@Getter
public class CheckData {
    private final Map<Class<? extends AbstractCheck>, AbstractCheck> checksMap;
    private final HeterogeneousContainer categorizedChecks;

    public CheckData(PlayerData data) {
        this.checksMap = new HashMap<>();

        Ares.INSTANCE.getCheckManager().getConstructors()
                .stream()
                .map(klass -> {
                    try {
                        return klass.newInstance(data);
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        Ares.INSTANCE.getPlugin().getLogger().log(Level.SEVERE,
                                "Failed to create data check!");
                        e.printStackTrace();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .forEach(check -> checksMap.put(check.getClass(), check));

        this.categorizedChecks = new HeterogeneousContainer();

        checksMap.forEach((klass, check) -> categorizedChecks.put(check, klass.getSuperclass()));
    }

    public AbstractCheck getCheck(Class<? extends AbstractCheck> clazz) {
        return checksMap.get(clazz);
    }

    public List<AbstractCheck> getChecksList() {
        return new ArrayList<>(checksMap.values());
    }

    public double getTotalVl() {
        return getChecksList().stream().mapToDouble(check -> check.getAlertManager().getVl()).sum();
    }
}
