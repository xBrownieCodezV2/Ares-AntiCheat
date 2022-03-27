package com.gladurbad.ares.util.placeholder;

import com.gladurbad.ares.Ares;
import com.gladurbad.ares.check.AbstractCheck;
import lombok.Getter;

import java.util.function.Function;

@Getter
public enum AlertPlaceHolder {
    PLAYER_NAME("%PLAYER_NAME%", check -> {
        return check.getData().getPlayer().getName();
    }),
    UUID("%UUID%", check -> {
        return check.getData().getPlayer().getUniqueId().toString();
    }),
    CHECK_NAME("%CHECK_NAME%", AbstractCheck::getName),
    PING("%PING%", check -> {
        return Long.toString(check.getData().getConnectionTracker().getPing());
    }),
    LAG_TICK("%LAG_TICK%", check -> {
        return Integer.toString(check.getData().getConnectionTracker().getLagTick().getTicks());
    }),
    VL("%VL%", check -> {
        return Integer.toString((int) Math.floor(check.getAlertManager().getVl()));
    }),
    MAX_VL("%MAX_VL%", check -> {
        return Double.toString(check.getAlertManager().getThreshold());
    }),
    TPS("%TPS%", check -> {
        return String.valueOf(Ares.INSTANCE.getTaskManager().getTps());
    }),
    PLAYER_VERSION("%PLAYER_VERSION%", check -> check.getData().getVersionString());

    private final String placeholder;
    private final Function<AbstractCheck, String> function;


    AlertPlaceHolder(String placeholder, Function<AbstractCheck, String> function) {
        // This is the actual placeholder value that can be used in config files.
        this.placeholder = placeholder;

        // This is the function we run which will return a String.
        this.function = function;
    }

    public static String replacePlaceholders(AbstractCheck check, String input) {
        for (AlertPlaceHolder placeholder : AlertPlaceHolder.values()) {
            String output = placeholder.getFunction().apply(check);

            input = input.replace(placeholder.getPlaceholder(), output);
        }

        return input;
    }
}
