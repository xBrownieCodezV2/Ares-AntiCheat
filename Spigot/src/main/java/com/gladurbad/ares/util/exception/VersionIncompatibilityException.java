package com.gladurbad.ares.util.exception;

import ac.artemis.packet.spigot.utils.ServerUtil;

public class VersionIncompatibilityException extends Exception {

    public VersionIncompatibilityException() {
        super(ServerUtil.getGameVersion().getServerVersion() + " is not supported.");
    }
}
