package com.gladurbad.ares.check;

import com.gladurbad.ares.Ares;
import com.gladurbad.ares.data.PlayerData;
import lombok.Getter;
import org.atteo.classindex.ClassIndex;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

// Yes, this is from Nemesis, I liked the idea of indexing subclasses to prevent having to register the check manually.
// Also good for the GUI anyways.
// Credits: sim0n
@Getter
public class CheckManager {
    private final List<Class<? extends AbstractCheck>> checks = new ArrayList<>();
    private final List<Constructor<? extends AbstractCheck>> constructors = new ArrayList<>();

    public CheckManager() {
        ClassIndex.getSubclasses(AbstractCheck.class, AbstractCheck.class.getClassLoader())
                .forEach(klass -> {
                    // Check if the class is not abstract and does not have any subclasses.
                    if (!Modifier.isAbstract(klass.getModifiers())) {
                        checks.add(klass);
                    }
                });

        checks.forEach(klass -> {
            try {
                constructors.add(klass.getConstructor(PlayerData.class));
            } catch (NoSuchMethodException exception) {
                Ares.INSTANCE.getPlugin().getLogger().log(Level.SEVERE,
                        "Failed to initialize check class: " + klass.getSimpleName());
            }
        });
    }
}
