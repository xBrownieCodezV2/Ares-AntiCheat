package com.gladurbad.ares.util.string;

import com.gladurbad.ares.util.debug.Debug;
import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class StringUtil {
    public String color(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public String[] color(String[] input) {
        String[] strings = new String[input.length];

        for (int i = 0; i < strings.length; i++) {
            strings[i] = ChatColor.translateAlternateColorCodes('&', input[i]);
        }

        return strings;
    }

    public List<String> color(List<String> strings) {
        List<String> colored = new ArrayList<>();
        for (String s : strings) {
            colored.add(color(s));
        }
        return colored;
    }

    // Ugly, don't care, 2 tired.
    public String chainDebugs(Debug<?>... debugs) {
        StringBuilder result = new StringBuilder();

        boolean sex = false;

        for (Debug<?> debug : debugs) {
            if (debug.getName().isEmpty() || String.valueOf(debug.getInfo()).isEmpty()) continue;

            sex = true;
        }

        if (sex) {
            result.append("\n\n&4Info:\n");

            for (Debug<?> debug : debugs) {
                if (debug.getName().isEmpty() || String.valueOf(debug.getInfo()).isEmpty()) continue;

                result.append("&7").append(debug.getName()).append(": &f").append(debug.getInfo()).append("\n");
            }
        }

        return color(result.toString());
    }
}
